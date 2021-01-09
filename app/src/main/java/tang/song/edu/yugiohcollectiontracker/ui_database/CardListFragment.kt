package tang.song.edu.yugiohcollectiontracker.ui_database

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Card
import tang.song.edu.yugiohcollectiontracker.databinding.FragmentCardListBinding
import tang.song.edu.yugiohcollectiontracker.ui_database.adapters.CardListAdapter
import tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels.BaseSearchViewModel
import tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels.CardListViewModel
import tang.song.edu.yugiohcollectiontracker.viewBinding
import javax.inject.Inject

@AndroidEntryPoint
class CardListFragment : BaseSearchListFragment<Card>(), CardListAdapter.OnItemClickListener {
    @Inject
    lateinit var mRequestManager: RequestManager

    private val binding by viewBinding(FragmentCardListBinding::inflate)

    private val mViewModel: CardListViewModel by viewModels()
    private lateinit var mAdapter: CardListAdapter

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

    override suspend fun submitData(pagingData: PagingData<Card>) {
        mAdapter.submitData(pagingData)
    }

    private fun initRecyclerView() {
        mAdapter = CardListAdapter(this, mRequestManager)
        val layoutManager = LinearLayoutManager(requireContext())

        binding.cardList.apply {
            this.layoutManager = layoutManager
            this.adapter = mAdapter
        }
    }
}
