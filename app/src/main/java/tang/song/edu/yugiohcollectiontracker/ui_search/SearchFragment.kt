package tang.song.edu.yugiohcollectiontracker.ui_search

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import tang.song.edu.yugiohcollectiontracker.BaseApplication
import tang.song.edu.yugiohcollectiontracker.BaseFragment
import tang.song.edu.yugiohcollectiontracker.R
import tang.song.edu.yugiohcollectiontracker.ui_database.adapters.CardListAdapter
import tang.song.edu.yugiohcollectiontracker.ui_search.viewmodels.SearchViewModel
import tang.song.edu.yugiohcollectiontracker.ui_search.viewmodels.SearchViewModelFactory
import javax.inject.Inject

class SearchFragment : BaseFragment(), MenuItem.OnActionExpandListener {
    @Inject
    lateinit var mRequestManager: RequestManager
    @Inject
    lateinit var mViewModelFactory: SearchViewModelFactory

    private lateinit var mAdapter: CardListAdapter
    private lateinit var mViewModel: SearchViewModel

    private lateinit var mSearchView: SearchView

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity?.application as BaseApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel =
            ViewModelProvider(requireActivity(), mViewModelFactory).get(SearchViewModel::class.java)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView(view)

        mViewModel.cardSearchResult.observe(this) { response ->
            if (response.isSuccessful) {
                mAdapter.submitList(response.body())
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_actionbar_menu, menu)

        menu.findItem(R.id.action_search).apply {
            mSearchView = (this.actionView.findViewById(R.id.search_view) as SearchView).apply {
                setIconifiedByDefault(false)
            }

            setOnActionExpandListener(this@SearchFragment)
            expandActionView()
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                activity?.findNavController(R.id.search_nav_host_fragment)
                    ?.navigate(R.id.action_searchFragment_to_filterBottomSheetDialogFragment).let {
                        mSearchView.clearFocus()
                        true
                    }
                false
            }
            else -> false
        }
    }

    override fun onMenuItemActionExpand(menuItem: MenuItem?): Boolean {
        return true
    }

    override fun onMenuItemActionCollapse(menuItem: MenuItem?): Boolean {
        activity?.finish()

        return false
    }

    private fun initRecyclerView(view: View) {
        mAdapter = CardListAdapter(mRequestManager)
        val layoutManager = LinearLayoutManager(requireContext())

        view.findViewById<RecyclerView>(R.id.card_search_result_list).apply {
            this.layoutManager = layoutManager
            this.adapter = mAdapter
        }
    }
}
