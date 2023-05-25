package sam.g.trackuriboh.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.databinding.FragmentCardSetListBinding
import sam.g.trackuriboh.ui.search.adapters.CardSetListAdapter
import sam.g.trackuriboh.ui.search.viewmodels.CardSetListViewModel
import sam.g.trackuriboh.utils.addDividerItemDecoration
import sam.g.trackuriboh.utils.viewBinding

@AndroidEntryPoint
class CardSetListFragment : Fragment(), CardSetListAdapter.OnItemClickListener {
    private val binding by viewBinding(FragmentCardSetListBinding::inflate)

    private val viewModel: CardSetListViewModel by viewModels()
    private lateinit var cardSetListAdapter: CardSetListAdapter

    companion object {
        const val FRAGMENT_RESULT_REQUEST_KEY = "CardSetListFragment_requestResultKey"
        const val SET_ID_DATA_KEY = "CardSetListFragment_setId"

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

        viewModel.searchResult.observe(viewLifecycleOwner) {
            cardSetListAdapter.submitData(lifecycle, it)

            binding.cardSetList.invalidateItemDecorations()
        }
    }

    override fun onItemClick(setId: Long) {
        setFragmentResult(FRAGMENT_RESULT_REQUEST_KEY, bundleOf(SET_ID_DATA_KEY to setId))
    }

    private fun initRecyclerView() {
        cardSetListAdapter = CardSetListAdapter().apply {
            setOnItemClickListener(this@CardSetListFragment)
        }

        binding.cardSetList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cardSetListAdapter
            addDividerItemDecoration()
        }
    }
}
