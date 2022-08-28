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
import com.google.android.material.divider.MaterialDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.data.db.entities.CardSet
import sam.g.trackuriboh.databinding.FragmentCardSetListBinding
import sam.g.trackuriboh.ui.database.adapters.CardSetListAdapter
import sam.g.trackuriboh.ui.database.viewmodels.BaseSearchViewModel
import sam.g.trackuriboh.ui.database.viewmodels.CardSetListViewModel
import sam.g.trackuriboh.utils.viewBinding

@AndroidEntryPoint
class CardSetListFragment : BaseSearchListFragment<CardSet>(), CardSetListAdapter.OnItemClickListener {
    private val binding by viewBinding(FragmentCardSetListBinding::inflate)

    private val mViewModel: CardSetListViewModel by viewModels()
    private lateinit var mAdapter: CardSetListAdapter

    companion object {
        const val FRAGMENT_RESULT_REQUEST_KEY = "CardSetListFragment_requestResultKey"
        const val SET_ID_DATA_KEY = "CardSetListFragment_setId"

        // This is the same value as the navArg name so that the SavedStateHandle can access from either
        const val ARG_QUERY = "CardSetListFragment_argQuery"

        fun newInstance(query: String? = null) =
            CardSetListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_QUERY, query)
                }
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        search(arguments?.getString(ARG_QUERY))
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

    override fun onItemClick(setId: Long) {
        setFragmentResult(FRAGMENT_RESULT_REQUEST_KEY, bundleOf(SET_ID_DATA_KEY to setId))
    }

    private fun initRecyclerView() {
        mAdapter = CardSetListAdapter().apply {
            setOnItemClickListener(this@CardSetListFragment)
        }

        binding.cardSetList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
            addItemDecoration(MaterialDividerItemDecoration(context, (layoutManager as LinearLayoutManager).orientation))
        }
    }
}
