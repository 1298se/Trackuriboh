package sam.g.trackuriboh.ui_database

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.relations.ProductWithCardSetAndSkuIds
import sam.g.trackuriboh.databinding.FragmentCardListBinding
import sam.g.trackuriboh.ui_common.VerticalSpaceItemDecoration
import sam.g.trackuriboh.ui_database.adapters.CardListAdapter
import sam.g.trackuriboh.ui_database.viewmodels.BaseSearchViewModel
import sam.g.trackuriboh.ui_database.viewmodels.CardListViewModel
import sam.g.trackuriboh.utils.viewBinding

@AndroidEntryPoint
class CardListFragment : BaseSearchListFragment<ProductWithCardSetAndSkuIds>(), CardListAdapter.OnItemClickListener {
    private lateinit var mAdapter: CardListAdapter

    private val binding by viewBinding(FragmentCardListBinding::inflate)

    private val mViewModel: CardListViewModel by viewModels()

    companion object {
        const val CARD_ITEM_CLICK_REQUEST_KEY = "CardListFragment_onCardItemClick"
        const val CARD_ITEM_CARD_ID_RESULT = "CardListFragment_cardId"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
    }

    override fun getViewModel(): BaseSearchViewModel<ProductWithCardSetAndSkuIds> = mViewModel

    override fun getListView(): RecyclerView = binding.cardList

    override fun getAdapter(): PagingDataAdapter<ProductWithCardSetAndSkuIds, out RecyclerView.ViewHolder> = mAdapter

    /**
     * Some new whack way to communicate between fragments, but we do it this way because [CardListFragment] needs to perform
     * different actions based on where it's being used
     */
    override fun onCardItemClick(cardId: Long) {
        setFragmentResult(CARD_ITEM_CLICK_REQUEST_KEY, bundleOf(CARD_ITEM_CARD_ID_RESULT to cardId))
    }

    private fun initRecyclerView() {
        mAdapter = CardListAdapter().apply {
            setOnItemClickListener(this@CardListFragment)
        }

        binding.cardList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
            addItemDecoration(VerticalSpaceItemDecoration(resources.getDimension(R.dimen.list_item_large_row_spacing)))
        }
    }
}
