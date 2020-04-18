package tang.song.edu.yugiohcollectiontracker.ui_database

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tang.song.edu.yugiohcollectiontracker.BaseApplication
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardSet
import tang.song.edu.yugiohcollectiontracker.databinding.FragmentCardSetListBinding
import tang.song.edu.yugiohcollectiontracker.ui_database.adapters.CardSetListAdapter
import tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels.CardSetViewModel
import tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels.CardSetViewModelFactory
import javax.inject.Inject

class CardSetListFragment : BaseSearchListFragment<CardSet>() {
    @Inject
    lateinit var mViewModelFactory: CardSetViewModelFactory

    private var _binding: FragmentCardSetListBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var mViewModel: CardSetViewModel
    private lateinit var mAdapter: CardSetListAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity?.application as BaseApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
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

        mViewModel = ViewModelProvider(requireActivity(), mViewModelFactory).get(CardSetViewModel::class.java)

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

    override fun submitList(list: PagedList<CardSet>?) {
        mAdapter.submitList(list)
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
