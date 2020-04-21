package tang.song.edu.yugiohcollectiontracker.ui_database

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import tang.song.edu.yugiohcollectiontracker.BaseApplication
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Card
import tang.song.edu.yugiohcollectiontracker.databinding.FragmentCardListBinding
import tang.song.edu.yugiohcollectiontracker.ui_database.adapters.CardListAdapter
import tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels.CardViewModel
import tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels.CardViewModelFactory
import javax.inject.Inject

class CardListFragment : BaseSearchListFragment<Card>(), CardListAdapter.OnItemClickListener {
    @Inject
    lateinit var mRequestManager: RequestManager

    @Inject
    lateinit var mViewModelFactory: CardViewModelFactory

    private var _binding: FragmentCardListBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var mViewModel: CardViewModel
    private lateinit var mAdapter: CardListAdapter

    companion object {
        fun newInstance(queryString: String?): CardListFragment {
            val args = Bundle().apply {
                putString(ARGS_QUERY_STRING, queryString)
            }

            return CardListFragment().apply {
                arguments = args
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity?.application as BaseApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCardListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        mViewModel = ViewModelProvider(this, mViewModelFactory).get(CardViewModel::class.java)

        mQueryString?.let {
            mViewModel.search(mQueryString)
        }

        mViewModel.cardList.observe(viewLifecycleOwner) {
            mAdapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onItemClick(cardId: Long) {
        val action = DatabaseFragmentDirections.actionDatabaseFragmentToCardDetailFragment(cardId)
        findNavController().navigate(action)
    }

    override fun getListView(): RecyclerView {
        return binding.cardList
    }

    override fun search(queryText: String?) {
        mViewModel.search(queryText)
    }

    override fun submitList(list: PagedList<Card>?) {
        mAdapter.submitList(list)
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
