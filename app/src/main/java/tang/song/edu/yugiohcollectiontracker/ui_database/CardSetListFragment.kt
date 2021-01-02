package tang.song.edu.yugiohcollectiontracker.ui_database

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import tang.song.edu.yugiohcollectiontracker.databinding.FragmentCardSetListBinding
import tang.song.edu.yugiohcollectiontracker.ui_database.adapters.CardSetListAdapter
import tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels.BaseSearchViewModel
import tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels.CardSetListViewModel
import tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels.CardSetListViewModelFactory
import tang.song.edu.yugiohcollectiontracker.viewBinding
import javax.inject.Inject

@AndroidEntryPoint
class CardSetListFragment : BaseSearchListFragment(), CardSetListAdapter.OnItemClickListener {
    @Inject
    lateinit var mViewModelFactory: CardSetListViewModelFactory

    private val binding by viewBinding(FragmentCardSetListBinding::inflate)

    private val mViewModel: CardSetListViewModel by viewModels()
    private lateinit var mAdapter: CardSetListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        mViewModel.cardSetList.observe(viewLifecycleOwner) {
            mAdapter.submitList(it)
        }
    }

    override fun getViewModel(): BaseSearchViewModel<*> {
        return mViewModel
    }

    override fun getListView(): RecyclerView {
        return binding.cardSetList
    }

    override fun clearList() {
        mAdapter.submitList(null)
    }

    override fun onItemClick(setCode: String) {
        val action = DatabaseFragmentDirections.actionDatabaseFragmentToCardSetDetailFragment(setCode)
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
