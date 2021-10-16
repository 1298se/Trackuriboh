package tang.song.edu.trackuriboh.ui_database

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import tang.song.edu.trackuriboh.data.db.entities.Card
import tang.song.edu.trackuriboh.databinding.FragmentCardListBinding
import tang.song.edu.trackuriboh.ui_database.adapters.CardListAdapter
import tang.song.edu.trackuriboh.ui_database.viewmodels.BaseSearchViewModel
import tang.song.edu.trackuriboh.ui_database.viewmodels.CardListViewModel
import tang.song.edu.trackuriboh.viewBinding
import javax.inject.Inject

@AndroidEntryPoint
class CardListFragment : BaseSearchListFragment<Card>(), CardListAdapter.OnItemClickListener {
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

        search(mViewModel.currentQueryValue())
    }

    override fun onItemClick(cardId: Long) {
        hideSoftKeyboard()

        val action = DatabaseFragmentDirections.actionDatabaseFragmentToCardDetailFragment(cardId)
        findNavController().navigate(action)
    }

    override fun getViewModel(): BaseSearchViewModel<Card> {
        return mViewModel
    }

    override fun getListView(): RecyclerView {
        return binding.cardList
    }

    override fun getAdapter(): PagingDataAdapter<Card, out RecyclerView.ViewHolder> {
        return mAdapter
    }

    override suspend fun submitData(pagingData: PagingData<Card>) {
        mAdapter.submitData(pagingData)
    }

    private fun initRecyclerView() {
        mAdapter.setOnItemClickListener(this)
        binding.cardList.apply {
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = mAdapter
        }
    }
}
