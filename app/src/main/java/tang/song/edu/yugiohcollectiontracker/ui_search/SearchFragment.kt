package tang.song.edu.yugiohcollectiontracker.ui_search

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import tang.song.edu.yugiohcollectiontracker.*
import tang.song.edu.yugiohcollectiontracker.databinding.FragmentSearchBinding
import tang.song.edu.yugiohcollectiontracker.ui_database.adapters.CardListAdapter
import tang.song.edu.yugiohcollectiontracker.ui_search.viewmodels.SearchViewModel
import tang.song.edu.yugiohcollectiontracker.ui_search.viewmodels.SearchViewModelFactory
import javax.inject.Inject

class SearchFragment : BaseFragment(), MenuItem.OnActionExpandListener {
    @Inject
    lateinit var mRequestManager: RequestManager
    @Inject
    lateinit var mViewModelFactory: SearchViewModelFactory

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAdapter: CardListAdapter
    private lateinit var mViewModel: SearchViewModel

    private lateinit var mSearchView: SearchView

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity?.application as BaseApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel = ViewModelProvider(requireActivity(), mViewModelFactory).get(SearchViewModel::class.java)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView(view)

        mViewModel.cardSearchResult.observe(viewLifecycleOwner) {
            mAdapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_actionbar_menu, menu)

        // Get the SearchView and set the searchable configuration
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        menu.findItem(R.id.action_search).apply {
            mSearchView = (this.actionView.findViewById(R.id.search_view) as SearchView).apply {
                setIconifiedByDefault(false)

                val componentName = ComponentName(context, SearchActivity::class.java)
                setSearchableInfo(searchManager.getSearchableInfo(componentName))

                expandActionView()
                setQuery(mViewModel.lastQueryValue(), false)
                clearFocus()
            }

            setOnActionExpandListener(this@SearchFragment)
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
        startActivity(Intent(context, MainActivity::class.java).apply {
            putExtra(TAG_SEARCH_COMPLETE, true)
        })

        activity?.finish()

        return false
    }

    private fun initRecyclerView(view: View) {
        mAdapter = CardListAdapter(mRequestManager)
        val layoutManager = LinearLayoutManager(requireContext())

        binding.searchResultList.apply {
            this.layoutManager = layoutManager
            this.adapter = mAdapter
        }
    }

    private fun updateRepoListFromInput() {
        mSearchView.query.trim().let {
            if (it.isNotEmpty()) {
                binding.searchResultList.scrollToPosition(0)
                mViewModel.search(it.toString())
                mAdapter.submitList(null)
            }
        }
    }
}
