package tang.song.edu.yugiohcollectiontracker.ui_database

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import tang.song.edu.yugiohcollectiontracker.BaseApplication
import tang.song.edu.yugiohcollectiontracker.R
import tang.song.edu.yugiohcollectiontracker.databinding.FragmentCardListBinding
import tang.song.edu.yugiohcollectiontracker.ui_database.adapters.CardListAdapter
import tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels.BaseSearchViewModel
import tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels.CardListViewModel
import tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels.CardListViewModelFactory
import tang.song.edu.yugiohcollectiontracker.viewBinding
import javax.inject.Inject

class CardListFragment : BaseSearchListFragment(R.layout.fragment_card_list), CardListAdapter.OnItemClickListener {
    @Inject
    lateinit var mRequestManager: RequestManager

    @Inject
    lateinit var mViewModelFactory: CardListViewModelFactory

    private val binding by viewBinding(FragmentCardListBinding::bind)

    private lateinit var mViewModel: CardListViewModel
    private lateinit var mAdapter: CardListAdapter


    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity?.application as BaseApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel = ViewModelProvider(this, mViewModelFactory).get(CardListViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        mViewModel.cardList.observe(viewLifecycleOwner) {
            mAdapter.submitList(it)
        }
    }

    override fun onItemClick(cardId: Long) {
        hideSoftKeyboard()

        val action = DatabaseFragmentDirections.actionDatabaseFragmentToCardDetailFragment(cardId)
        findNavController().navigate(action)
    }

    override fun getViewModel(): BaseSearchViewModel<*> {
        return mViewModel
    }

    override fun getListView(): RecyclerView {
        return binding.cardList
    }

    override fun clearList() {
        mAdapter.submitList(null)
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
