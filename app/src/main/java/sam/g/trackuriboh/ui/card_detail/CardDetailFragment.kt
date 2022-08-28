package sam.g.trackuriboh.ui.card_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.compose.material.ExperimentalMaterialApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.relations.ProductWithCardSetAndSkuIds
import sam.g.trackuriboh.databinding.FragmentCardDetailBinding
import sam.g.trackuriboh.ui.card_detail.adapters.CardDetailStateAdapter
import sam.g.trackuriboh.ui.card_detail.viewmodels.CardDetailViewModel
import sam.g.trackuriboh.ui.common.CollapseToolbarStateChangeListener
import sam.g.trackuriboh.ui.database.adapters.ImagePagerAdapter
import sam.g.trackuriboh.ui.user_list.AddToUserListDialogFragment
import sam.g.trackuriboh.utils.*

@ExperimentalMaterialApi
@AndroidEntryPoint
class CardDetailFragment : Fragment(), Toolbar.OnMenuItemClickListener {
    private val binding by viewBinding(FragmentCardDetailBinding::inflate)

    private val cardDetailViewModel: CardDetailViewModel by viewModels()

    private val args: CardDetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewPagerBackPressBehaviour(binding.cardDetailViewPager)

        initToolbar()
        initFab()

        initFragmentResultListeners()

        cardDetailViewModel.cardWithCardSetAndSkuIds.observe(viewLifecycleOwner) {

            populateLayout(it)
            /*
             * !IMPORTANT: FOR PROPER VIEWSTATE RESTORATION TO OCCUR, THE ADAPTERS MUST BE REATTACHED
             * ONCHANGE INSTEAD OF EXPOSIING A setItems METHOD AND CALLING NOTIFYCHANGE.
             * In this case, since the data is a one-shot operation, we just initialize the adapters
             * here.
             * See ViewPager2.setAdapter
             */
            initImageViewPager(listOf(it.product.imageUrl))
            initCardDetailViewPager(it)
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_add_to_user_list -> {
                AddToUserListDialogFragment.newInstance(args.cardId).show(childFragmentManager, null)
            }
        }
        return false
    }

    private fun initToolbar() {
        binding.cardDetailToolbar.apply {
            inflateMenu(R.menu.card_detail_toolbar)

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
        binding.cardImageViewPager.adapter = ImagePagerAdapter(imageList)

        TabLayoutMediator(binding.cardImageTabLayout, binding.cardImageViewPager) { _, _ -> }.attach()
    }

    private fun initCardDetailViewPager(productWithCardSetAndSkuIds: ProductWithCardSetAndSkuIds) {
        binding.cardDetailViewPager.adapter = CardDetailStateAdapter(
            this@CardDetailFragment,
            productWithCardSetAndSkuIds
        )

        TabLayoutMediator(binding.cardDetailTabLayout, binding.cardDetailViewPager) { tab, position ->
            tab.text = when (position) {
                CardDetailViewModel.Page.PRICES.position -> getString(R.string.lbl_prices)
                CardDetailViewModel.Page.OVERVIEW.position -> getString(R.string.lbl_details)
                else -> null
            }
        }.attach()
    }

    private fun populateLayout(productWithCardSetAndSkuIds: ProductWithCardSetAndSkuIds?) {
        binding.cardDetailNameTextview.apply {
            text = productWithCardSetAndSkuIds?.product?.name
        }

        binding.cardDetailSetNameTextview.apply {
            text = productWithCardSetAndSkuIds?.cardSet?.name
            setOnClickListener { _ ->
                productWithCardSetAndSkuIds?.cardSet?.id?.let { it ->
                    /*findNavController().safeNavigate(
                        CardDetailFragmentDirections.actionCardDetailFragmentToCardSetDetailFragment(it)
                    )*/
                }
            }
        }
    }

    private fun initFab() {
        binding.cardDetailTcgplayerFab.setOnClickListener {
            openTCGPlayer(args.cardId)
        }
    }

    private fun initFragmentResultListeners() {
        // This observers the [CardPricesBottomSheetDialog] in the ViewPager
        childFragmentManager.setFragmentResultListener(SNACKBAR_SHOW_REQUEST_KEY, viewLifecycleOwner) { _, bundle ->
            val type = SnackbarType.valueOf(bundle.getString(SNACKBAR_TYPE) ?: SnackbarType.INFO.name)
            val message = bundle.getString(SNACKBAR_MESSAGE) ?: getString(R.string.error_message_generic)

            showSnackbar(message, type)
        }

        // Observe [AddToUserListDialogFragment]
        childFragmentManager.setFragmentResultListener(
            AddToUserListDialogFragment.FRAGMENT_RESULT_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val userListName = bundle.getString(AddToUserListDialogFragment.ADDED_USER_LIST_NAME_DATA_KEY)

            showSnackbar(getString(R.string.add_to_user_list_success_message, userListName))
        }
    }
}
