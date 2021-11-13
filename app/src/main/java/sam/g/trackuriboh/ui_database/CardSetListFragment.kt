package sam.g.trackuriboh.ui_database

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.data.db.entities.CardSet
import sam.g.trackuriboh.databinding.FragmentCardSetListBinding
import sam.g.trackuriboh.handleNavigationAction
import sam.g.trackuriboh.ui_database.adapters.CardSetListAdapter
import sam.g.trackuriboh.ui_database.viewmodels.BaseSearchViewModel
import sam.g.trackuriboh.ui_database.viewmodels.CardSetListViewModel
import sam.g.trackuriboh.viewBinding

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
    }

    override fun getViewModel(): BaseSearchViewModel<CardSet> {
        return mViewModel
    }

    override fun getListView(): RecyclerView {
        return binding.cardSetList
    }

    override fun getAdapter(): PagingDataAdapter<CardSet, out RecyclerView.ViewHolder> {
        return mAdapter
    }

    override fun onItemClick(setName: String) {

        handleNavigationAction(DatabaseFragmentDirections.actionDatabaseFragmentToCardSetDetailFragment(setName))
    }

    private fun initRecyclerView() {
        mAdapter = CardSetListAdapter().apply {
            setOnItemClickListener(this@CardSetListFragment)
        }
        binding.cardSetList.apply {
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = mAdapter
        }
    }
}
