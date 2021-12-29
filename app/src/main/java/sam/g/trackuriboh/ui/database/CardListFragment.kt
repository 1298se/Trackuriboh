package sam.g.trackuriboh.ui.database

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
import sam.g.trackuriboh.ui.common.VerticalSpaceItemDecoration
import sam.g.trackuriboh.ui.database.adapters.CardListAdapter
import sam.g.trackuriboh.ui.database.viewmodels.BaseSearchViewModel
import sam.g.trackuriboh.ui.database.viewmodels.CardListViewModel
import sam.g.trackuriboh.utils.viewBinding

@AndroidEntryPoint
class CardListFragment : BaseSearchListFragment<ProductWithCardSetAndSkuIds>(), CardListAdapter.OnItemClickListener {
    private lateinit var mAdapter: CardListAdapter

    private val binding by viewBinding(FragmentCardListBinding::inflate)

    private val mViewModel: CardListViewModel by viewModels()

    companion object {
        const val FRAGMENT_RESULT_REQUEST_KEY = "CardListFragment_fragmentResultRequestKey"
        const val CARD_ID_DATA_KEY = "CardListFragment_cardId"
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

    override fun onCardItemClick(cardId: Long) {
        setFragmentResult(FRAGMENT_RESULT_REQUEST_KEY, bundleOf(CARD_ID_DATA_KEY to cardId))
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
