package sam.g.trackuriboh.ui_card_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.*
import sam.g.trackuriboh.data.db.relations.ProductWithCardSetAndSkuIds
import sam.g.trackuriboh.databinding.FragmentCardDetailBinding
import sam.g.trackuriboh.ui_card_detail.adapters.CardDetailPagerAdapter
import sam.g.trackuriboh.ui_card_detail.viewmodels.CardDetailViewModel
import sam.g.trackuriboh.ui_common.CollapseToolbarStateChangeListener
import sam.g.trackuriboh.ui_database.adapters.ImagePagerAdapter
import sam.g.trackuriboh.utils.*

@AndroidEntryPoint
class CardDetailFragment : Fragment(), Toolbar.OnMenuItemClickListener {
    private val binding by viewBinding(FragmentCardDetailBinding::inflate)

    private val mViewModel: CardDetailViewModel by viewModels()

    private val args: CardDetailFragmentArgs by navArgs()

    private lateinit var mImagePagerAdapter: ImagePagerAdapter

    private lateinit var mCardDetailPagerAdapter: CardDetailPagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewPagerBackPressBehaviour(binding.cardDetailViewPager)

        initToolbar()
        initTCGPlayerFab()
        initFragmentResultListeners()

        mViewModel.cardWithCardSetAndSkuIds.observe(viewLifecycleOwner) {

            initLayout(it)
            /**
             * FOR PROPER VIEWSTATE RESTORATION TO OCCUR, THE ADAPTERS MUST BE REATTACHED
             * ONCHANGE INSTEAD OF EXPOSIING A setItems METHOD AND CALLING NOTIFYCHANGE
             */
            initImageViewPager(listOf(it?.product?.imageUrl))
            initCardDetailViewPager(it)
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return true
    }

    private fun initToolbar() {
        binding.cardDetailToolbar.apply {
            inflateMenu(R.menu.card_detail_toolbar_menu)

            setOnMenuItemClickListener(this@CardDetailFragment)
        }

       binding.cardDetailAppBarLayout.addOnOffsetChangedListener(object : CollapseToolbarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State) {
                if (state == State.COLLAPSED) {
                    binding.cardDetailToolbar.title = getString(R.string.lbl_card_details)
                } else {
                    binding.cardDetailToolbar.title = null
                }
            }
        })

        binding.cardDetailCollapsingToolbarLayout.setupWithNavController(
            binding.cardDetailToolbar,
            findNavController(),
            AppBarConfiguration.Builder().setFallbackOnNavigateUpListener {
                activity?.finish()
                true
            }.build()
        )
    }

    private fun initImageViewPager(imageList: List<String?>) {
        binding.cardImageViewPager.adapter = ImagePagerAdapter(imageList).also { mImagePagerAdapter = it }

        TabLayoutMediator(binding.cardImageTabLayout, binding.cardImageViewPager) { _, _ -> }.attach()
    }

    private fun initCardDetailViewPager(productWithCardSetAndSkuIds: ProductWithCardSetAndSkuIds?) {
        binding.cardDetailViewPager.adapter = CardDetailPagerAdapter(
            this@CardDetailFragment,
            productWithCardSetAndSkuIds
        ).also {
            mCardDetailPagerAdapter = it
        }

        TabLayoutMediator(binding.cardDetailTabLayout, binding.cardDetailViewPager) { tab, position ->
            tab.text = when (position) {
                CardDetailViewModel.Page.PRICES.position -> getString(R.string.lbl_prices)
                CardDetailViewModel.Page.OVERVIEW.position -> getString(R.string.lbl_details)
                else -> null
            }
        }.attach()
    }

    private fun initLayout(productWithCardSetAndSkuIds: ProductWithCardSetAndSkuIds?) {
        binding.cardDetailNameTextview.apply {
            text = productWithCardSetAndSkuIds?.product?.name
            setOnClickListener {
                productWithCardSetAndSkuIds?.product?.name?.let {
                    findNavController().safeNavigate(
                        CardDetailFragmentDirections.actionCardDetailFragmentToCardPrintingsActivity(it)
                    )
                }
            }
        }

        binding.cardDetailSetNameTextview.apply {
            text = productWithCardSetAndSkuIds?.cardSet?.name
            setOnClickListener { _ ->
                productWithCardSetAndSkuIds?.cardSet?.id?.let { it ->
                    findNavController().safeNavigate(
                        CardDetailFragmentDirections.actionCardDetailFragmentToCardSetDetailActivity(it)
                    )
                }
            }
        }
    }

    private fun initTCGPlayerFab() {
        binding.cardDetailTcgplayerExtendedFab.setOnClickListener {
            openTCGPlayer(args.cardId)
        }
    }

    private fun initFragmentResultListeners() {
        childFragmentManager.setFragmentResultListener(SNACKBAR_SHOW_REQUEST_KEY, this) { _, bundle ->
            val type = SnackbarType.valueOf(bundle.getString(SNACKBAR_TYPE) ?: SnackbarType.INFO.name)
            val message = bundle.getString(SNACKBAR_MESSAGE) ?: getString(R.string.error_message_generic)

            showSnackbar(message, type, binding.cardDetailTcgplayerExtendedFab)
        }
    }
}
