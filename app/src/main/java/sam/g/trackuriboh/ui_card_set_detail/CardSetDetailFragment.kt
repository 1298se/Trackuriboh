package sam.g.trackuriboh.ui_card_set_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.*
import sam.g.trackuriboh.data.db.relations.ProductWithCardSetAndSkuIds
import sam.g.trackuriboh.databinding.FragmentCardSetDetailBinding
import sam.g.trackuriboh.ui_card_set_detail.viewmodels.CardSetDetailViewModel
import sam.g.trackuriboh.ui_common.VerticalSpaceItemDecoration
import sam.g.trackuriboh.ui_database.BaseSearchListFragment
import sam.g.trackuriboh.ui_database.adapters.CardListAdapter
import sam.g.trackuriboh.ui_database.viewmodels.BaseSearchViewModel
import sam.g.trackuriboh.utils.*
import javax.inject.Inject

@AndroidEntryPoint
class CardSetDetailFragment :
    BaseSearchListFragment<ProductWithCardSetAndSkuIds>(),
    CardListAdapter.OnItemClickListener {
    @Inject
    lateinit var mAdapter: CardListAdapter

    private val binding by viewBinding(FragmentCardSetDetailBinding::inflate)
    private val mViewModel: CardSetDetailViewModel by viewModels()

    private lateinit var mSearchView: SearchView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initToolbar()

        mViewModel.getSearchResult().observe(viewLifecycleOwner) {
            mAdapter.submitData(lifecycle, it)
        }

        mViewModel.cardSet.observe(viewLifecycleOwner) {
            binding.cardSetDetailToolbar.title = it.name
        }
    }

    override fun getViewModel(): BaseSearchViewModel<ProductWithCardSetAndSkuIds> = mViewModel

    override fun getListView(): RecyclerView = binding.cardSetDetailProductList

    override fun getAdapter(): PagingDataAdapter<ProductWithCardSetAndSkuIds, out RecyclerView.ViewHolder> = mAdapter

    override fun getItemDecorator() = VerticalSpaceItemDecoration(resources.getDimension(R.dimen.list_item_large_row_spacing))

    override fun onCardItemClick(cardId: Long) {
        handleNavigationAction(CardSetDetailFragmentDirections.actionCardSetDetailFragmentToCardDetailActivity(cardId))
    }

    override fun onViewPricesItemClick(skuIds: List<Long>) {
        handleNavigationAction(
            CardSetDetailFragmentDirections.actionCardSetDetailFragmentToCardPricesBottomSheetDialogFragment(skuIds.toLongArray())
        )
    }

    override fun onOpenTCGPlayerClick(cardId: Long) {
        openTCGPlayer(cardId)
    }

    private fun initRecyclerView() {
        mAdapter.setOnItemClickListener(this)

        binding.cardSetDetailProductList.apply {
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = mAdapter
        }
    }

    private fun initToolbar() {
        binding.cardSetDetailToolbar.setupWithNavController(
            findNavController(),
            AppBarConfiguration.Builder().setFallbackOnNavigateUpListener {
                activity?.finish()
                true
            }.build()
        )

        createOptionsMenu()
    }

    private fun createOptionsMenu() {
        binding.cardSetDetailToolbar.apply {
            inflateMenu(R.menu.card_set_detail_toolbar_menu)

            menu.findItem(R.id.action_search).apply {
                mSearchView = setIconifiedSearchViewBehaviour(this, object : SearchViewQueryHandler {
                    override fun handleQueryTextSubmit(query: String?) {
                        this@CardSetDetailFragment.search(query)
                    }

                    override fun handleSearchViewCollapse() {
                        this@CardSetDetailFragment.search(null)
                    }
                })
            }
        }
    }
}
