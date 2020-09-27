package tang.song.edu.yugiohcollectiontracker.ui_database

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tang.song.edu.yugiohcollectiontracker.BaseApplication
import tang.song.edu.yugiohcollectiontracker.databinding.FragmentCardSetListBinding
import tang.song.edu.yugiohcollectiontracker.ui_database.adapters.CardSetListAdapter
import tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels.BaseSearchViewModel
import tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels.CardSetListViewModel
import tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels.CardSetListViewModelFactory
import tang.song.edu.yugiohcollectiontracker.viewBinding
import javax.inject.Inject

class CardSetListFragment : BaseSearchListFragment(), CardSetListAdapter.OnItemClickListener {
    @Inject
    lateinit var mViewModelFactory: CardSetListViewModelFactory

    private val binding by viewBinding(FragmentCardSetListBinding::inflate)

    private lateinit var mViewModel: CardSetListViewModel
    private lateinit var mAdapter: CardSetListAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity?.application as BaseApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel = ViewModelProvider(this, mViewModelFactory).get(CardSetListViewModel::class.java)
    }

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
