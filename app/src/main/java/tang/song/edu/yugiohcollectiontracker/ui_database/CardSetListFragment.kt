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
import dagger.hilt.android.AndroidEntryPoint
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardSet
import tang.song.edu.yugiohcollectiontracker.databinding.FragmentCardSetListBinding
import tang.song.edu.yugiohcollectiontracker.ui_database.adapters.CardSetListAdapter
import tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels.BaseSearchViewModel
import tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels.CardSetListViewModel
import tang.song.edu.yugiohcollectiontracker.viewBinding

@AndroidEntryPoint
class CardSetListFragment : BaseSearchListFragment<CardSet>(), CardSetListAdapter.OnItemClickListener {
    private val binding by viewBinding(FragmentCardSetListBinding::inflate)

    private val mViewModel: CardSetListViewModel by viewModels()
    private lateinit var mAdapter: CardSetListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        search(null)
    }

    override fun getViewModel(): BaseSearchViewModel<CardSet> {
        return mViewModel
    }

    override fun getListView(): RecyclerView {
        return binding.cardSetList
    }

    override suspend fun submitData(pagingData: PagingData<CardSet>) {
        mAdapter.submitData(pagingData)
    }

    override fun onItemClick(setName: String) {
        val action = DatabaseFragmentDirections.actionDatabaseFragmentToCardSetDetailFragment(setName)
        findNavController().navigate(action)
    }

    private fun initRecyclerView() {
        mAdapter = CardSetListAdapter(this)
        val layoutManager = LinearLayoutManager(requireContext())

        binding.cardSetList.apply {
            this.layoutManager = layoutManager
            this.adapter = mAdapter
        }
    }
}
