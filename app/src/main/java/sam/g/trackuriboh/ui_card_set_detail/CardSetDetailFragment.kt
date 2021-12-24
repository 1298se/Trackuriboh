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

@AndroidEntryPoint
class CardSetDetailFragment :
    BaseSearchListFragment<ProductWithCardSetAndSkuIds>(),
    CardListAdapter.OnItemClickListener {
    private lateinit var mAdapter: CardListAdapter

    private val binding by viewBinding(FragmentCardSetDetailBinding::inflate)
    private val viewModel: CardSetDetailViewModel by viewModels()

    private lateinit var searchView: SearchView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initToolbar()
        initSearchSuggestions()

        viewModel.getSearchResult().observe(viewLifecycleOwner) {
            mAdapter.submitData(lifecycle, it)
        }

        viewModel.cardSet.observe(viewLifecycleOwner) {
            binding.cardSetDetailToolbar.title = it.name
        }
    }

    override fun getViewModel(): BaseSearchViewModel<ProductWithCardSetAndSkuIds> = viewModel

    override fun getListView(): RecyclerView = binding.cardSetDetailProductList

    override fun getAdapter(): PagingDataAdapter<ProductWithCardSetAndSkuIds, out RecyclerView.ViewHolder> = mAdapter

    override fun onCardItemClick(cardId: Long) {
        findNavController().safeNavigate(CardSetDetailFragmentDirections.actionCardSetDetailFragmentToCardDetailActivity(cardId))
    }

    private fun initRecyclerView() {
        mAdapter = CardListAdapter().apply {
            setOnItemClickListener(this@CardSetDetailFragment)
        }

        binding.cardSetDetailProductList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
            addItemDecoration(VerticalSpaceItemDecoration(resources.getDimension(R.dimen.list_item_large_row_spacing)))
        }
    }

    private fun initToolbar() {
        binding.cardSetDetailToolbar.setupWithNavController(
            findNavController(),
            AppBarConfiguration(
                topLevelDestinationIds = setOf(),
                fallbackOnNavigateUpListener = {
                    activity?.finish()
                    true
                }
            )
        )

        createOptionsMenu()
    }

    private fun createOptionsMenu() {
        binding.cardSetDetailToolbar.apply {
            inflateMenu(R.menu.card_set_detail_toolbar_menu)

            menu.findItem(R.id.action_search).apply {
                searchView = setIconifiedSearchViewBehaviour(object : SearchViewQueryHandler {
                    override fun handleQueryTextSubmit(query: String?) {
                        search(query)
                    }

                    override fun handleQueryTextChanged(newText: String?) {
                        viewModel.setSearchSuggestion(newText)
                    }

                    override fun handleSearchViewCollapse() {
                        search(null)
                    }
                })
            }
        }
    }

    private fun initSearchSuggestions() {

        searchView.initSearchSuggestions()

        viewModel.searchSuggestionsCursor.observe(viewLifecycleOwner) {
            searchView.setSuggestionsCursor(it)
        }
    }
}
