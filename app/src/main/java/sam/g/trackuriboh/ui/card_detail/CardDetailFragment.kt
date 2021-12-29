package sam.g.trackuriboh.ui.card_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import sam.g.trackuriboh.*
import sam.g.trackuriboh.data.db.entities.UserList
import sam.g.trackuriboh.data.db.relations.ProductWithCardSetAndSkuIds
import sam.g.trackuriboh.databinding.FragmentCardDetailBinding
import sam.g.trackuriboh.ui.card_detail.adapters.CardDetailStateAdapter
import sam.g.trackuriboh.ui.card_detail.viewmodels.CardDetailViewModel
import sam.g.trackuriboh.ui.common.CollapseToolbarStateChangeListener
import sam.g.trackuriboh.ui.database.adapters.ImagePagerAdapter
import sam.g.trackuriboh.ui.user_list.UserListSelectionBottomSheetDialogFragment
import sam.g.trackuriboh.ui.user_list.viewmodels.UserListsViewModel
import sam.g.trackuriboh.utils.*

@AndroidEntryPoint
class CardDetailFragment : Fragment(), Toolbar.OnMenuItemClickListener {
    private val binding by viewBinding(FragmentCardDetailBinding::inflate)

    private val cardDetailViewModel: CardDetailViewModel by viewModels()

    private val userListsViewModel: UserListsViewModel by viewModels()

    private val args: CardDetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewPagerBackPressBehaviour(binding.cardDetailViewPager)

        initToolbar()
        initFab()

        cardDetailViewModel.cardWithCardSetAndSkuIds.observe(viewLifecycleOwner) {

            populateLayout(it)
            /*
             * FOR PROPER VIEWSTATE RESTORATION TO OCCUR, THE ADAPTERS MUST BE REATTACHED
             * ONCHANGE INSTEAD OF EXPOSIING A setItems METHOD AND CALLING NOTIFYCHANGE.
             * In this case, since the data is a one-shot operation, we just initialize the adapters
             * here.
             * See ViewPager2.setAdapter
             */
            initImageViewPager(listOf(it?.product?.imageUrl))
            initCardDetailViewPager(it)
            initFragmentResultListeners(it?.skuIds?.get(0))
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_add_to_watchlist -> {
                findNavController().safeNavigate(
                    CardDetailFragmentDirections.actionCardDetailFragmentToUserListSelectionBottomSheetDialogFragment()
                )
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

    private fun initCardDetailViewPager(productWithCardSetAndSkuIds: ProductWithCardSetAndSkuIds?) {
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

    private fun initFab() {
        binding.cardDetailTcgplayerFab.setOnClickListener {
            openTCGPlayer(args.cardId)
        }
    }

    private fun initFragmentResultListeners(skuId: Long?) {
        // This observers the [CardPricesBottomSheetDialog] in the ViewPager
        childFragmentManager.setFragmentResultListener(SNACKBAR_SHOW_REQUEST_KEY, viewLifecycleOwner) { _, bundle ->
            val type = SnackbarType.valueOf(bundle.getString(SNACKBAR_TYPE) ?: SnackbarType.INFO.name)
            val message = bundle.getString(SNACKBAR_MESSAGE) ?: getString(R.string.error_message_generic)

            showSnackbar(message, type)
        }

        parentFragmentManager.setFragmentResultListener(UserListSelectionBottomSheetDialogFragment.FRAGMENT_RESULT_REQUEST_KEY, viewLifecycleOwner) { _, bundle ->
            val selectedList = bundle.getParcelable<UserList>(UserListSelectionBottomSheetDialogFragment.SELECTED_USER_LIST_DATA_KEY)

            if (selectedList != null && skuId != null) {
                lifecycleScope.launch {
                    val job = userListsViewModel.insertToUserList(
                        listId = selectedList.id,
                        skuId = skuId
                    )

                    job.join()

                    showSnackbar(getString(R.string.add_to_user_list_success_message, selectedList.name))
                }

            }

        }
    }
}