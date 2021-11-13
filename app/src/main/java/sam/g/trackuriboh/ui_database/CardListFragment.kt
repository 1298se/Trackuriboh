package sam.g.trackuriboh.ui_database

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.relations.ProductWithSetAndSkuIds
import sam.g.trackuriboh.databinding.FragmentCardListBinding
import sam.g.trackuriboh.handleNavigationAction
import sam.g.trackuriboh.ui_common.VerticalSpaceItemDecoration
import sam.g.trackuriboh.ui_database.adapters.CardListAdapter
import sam.g.trackuriboh.ui_database.viewmodels.BaseSearchViewModel
import sam.g.trackuriboh.ui_database.viewmodels.CardListViewModel
import sam.g.trackuriboh.viewBinding
import javax.inject.Inject

@AndroidEntryPoint
class CardListFragment : BaseSearchListFragment<ProductWithSetAndSkuIds>(), CardListAdapter.OnItemClickListener {
    @Inject
    lateinit var mAdapter: CardListAdapter

    private val binding by viewBinding(FragmentCardListBinding::inflate)

    private val mViewModel: CardListViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
    }

    override fun onCardItemClick(cardId: Long) {

        val action = DatabaseFragmentDirections.actionDatabaseFragmentToCardDetailFragment(cardId)
        findNavController().navigate(action)
    }

    override fun onViewPricesItemClick(skuIds: List<Long>) {
        handleNavigationAction(
            DatabaseFragmentDirections.actionDatabaseFragmentToCardPricesBottomSheetDialogFragment(skuIds.toLongArray())
        )
    }

    override fun getViewModel(): BaseSearchViewModel<ProductWithSetAndSkuIds> {
        return mViewModel
    }

    override fun getListView(): RecyclerView {
        return binding.cardList
    }

    override fun getAdapter(): PagingDataAdapter<ProductWithSetAndSkuIds, out RecyclerView.ViewHolder> {
        return mAdapter
    }

    override fun getItemDecorator() = VerticalSpaceItemDecoration(resources.getDimension(R.dimen.list_item_large_row_spacing))

    private fun initRecyclerView() {
        mAdapter.setOnItemClickListener(this)
        binding.cardList.apply {
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = mAdapter
        }
    }
}
