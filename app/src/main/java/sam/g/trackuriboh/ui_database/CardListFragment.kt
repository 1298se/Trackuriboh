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
import sam.g.trackuriboh.utils.openTCGPlayer
import sam.g.trackuriboh.utils.viewBinding
import javax.inject.Inject

@AndroidEntryPoint
class CardListFragment : BaseSearchListFragment<ProductWithCardSetAndSkuIds>(), CardListAdapter.OnItemClickListener {
    @Inject
    lateinit var mAdapter: CardListAdapter

    private val binding by viewBinding(FragmentCardListBinding::inflate)

    private val mViewModel: CardListViewModel by viewModels()

    companion object {
        const val CARD_ITEM_CLICK_REQUEST_KEY = "CardListFragment_onCardItemClick"
        const val CARD_ITEM_CARD_ID = "CardListFragment_cardId"
        const val VIEW_PRICE_CLICK_REQUEST_KEY = "CardListFragment_onViewPriceItemClick"
        const val VIEW_PRICE_SKU_IDS = "CardListFragment_skuIds"
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

    override fun getItemDecorator() = VerticalSpaceItemDecoration(resources.getDimension(R.dimen.list_item_large_row_spacing))

    /**
     * Some new whack way to communicate between fragments, but we do it this way because [CardListFragment] needs to perform
     * different actions based on where it's being used
     */
    override fun onCardItemClick(cardId: Long) {
        setFragmentResult(CARD_ITEM_CLICK_REQUEST_KEY, bundleOf(CARD_ITEM_CARD_ID to cardId))
    }

    override fun onViewPricesItemClick(skuIds: List<Long>) {
       setFragmentResult(VIEW_PRICE_CLICK_REQUEST_KEY, bundleOf(VIEW_PRICE_SKU_IDS to skuIds.toLongArray()))
    }

    override fun onOpenTCGPlayerClick(cardId: Long) {
        openTCGPlayer(cardId)
    }

    private fun initRecyclerView() {
        mAdapter.setOnItemClickListener(this)
        binding.cardList.apply {
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = mAdapter
        }
    }
}
