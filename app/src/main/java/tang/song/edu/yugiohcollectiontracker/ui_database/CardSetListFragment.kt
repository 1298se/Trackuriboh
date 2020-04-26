package tang.song.edu.yugiohcollectiontracker.ui_database

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tang.song.edu.yugiohcollectiontracker.BaseApplication
import tang.song.edu.yugiohcollectiontracker.databinding.FragmentCardSetListBinding
import tang.song.edu.yugiohcollectiontracker.ui_database.adapters.CardSetListAdapter
import tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels.CardSetViewModel
import tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels.CardSetViewModelFactory
import javax.inject.Inject

class CardSetListFragment : BaseSearchListFragment() {
    @Inject
    lateinit var mViewModelFactory: CardSetViewModelFactory

    private var _binding: FragmentCardSetListBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var mViewModel: CardSetViewModel
    private lateinit var mAdapter: CardSetListAdapter

    companion object {
        private const val ARGS_QUERY_STRING = "ARGS_QUERY_STRING"

        fun newInstance(queryString: String?): CardSetListFragment {
            val args = Bundle().apply {
                putString(ARGS_QUERY_STRING, queryString)
            }

            return CardSetListFragment().apply {
                arguments = args
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity?.application as BaseApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            mQueryString = it.getString(ARGS_QUERY_STRING)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCardSetListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        mViewModel = ViewModelProvider(this, mViewModelFactory).get(CardSetViewModel::class.java)

        mQueryString?.let {
            mViewModel.search(mQueryString)
        }

        mViewModel.cardSetList.observe(viewLifecycleOwner) {
            mAdapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun getListView(): RecyclerView {
        return binding.cardSetList
    }

    override fun search(queryText: String?) {
        mViewModel.search(queryText)
    }

    override fun clearList() {
        mAdapter.submitList(null)
    }

    private fun initRecyclerView() {
        mAdapter = CardSetListAdapter()
        val layoutManager = LinearLayoutManager(requireContext())

        binding.cardSetList.apply {
            this.layoutManager = layoutManager
            this.adapter = mAdapter
        }
    }
}
