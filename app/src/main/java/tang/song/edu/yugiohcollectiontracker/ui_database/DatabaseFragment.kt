package tang.song.edu.yugiohcollectiontracker.ui_database

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import androidx.work.WorkInfo
import com.google.android.material.tabs.TabLayoutMediator
import tang.song.edu.yugiohcollectiontracker.BaseApplication
import tang.song.edu.yugiohcollectiontracker.BaseFragment
import tang.song.edu.yugiohcollectiontracker.R
import tang.song.edu.yugiohcollectiontracker.databinding.FragmentDatabaseBinding
import tang.song.edu.yugiohcollectiontracker.ui_database.adapters.DatabaseViewPagerAdapter
import tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels.DatabaseViewModel

class DatabaseFragment : BaseFragment(), SearchView.OnQueryTextListener {
    private var _binding: FragmentDatabaseBinding? = null
    private val binding get() = _binding!!

    private var mSearchView: SearchView? = null

    private lateinit var mViewModel: DatabaseViewModel
    private lateinit var mAdapter: DatabaseViewPagerAdapter
    private lateinit var mViewPager: ViewPager2

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
        _binding = FragmentDatabaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = ViewModelProvider(requireActivity()).get(DatabaseViewModel::class.java)

        initTabLayoutWithViewPager()
        initObservers()
    }

    override fun onDestroyView() {
        _binding = null

        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate the options menu from XML
        inflater.inflate(R.menu.database_actionbar_menu, menu)

        menu.findItem(R.id.action_open_search).apply {
            mSearchView = (this.actionView.findViewById(R.id.search_view) as SearchView).apply {
                setIconifiedByDefault(true)

                setOnQueryTextListener(this@DatabaseFragment)
            }

            setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                    menu.findItem(R.id.action_database_sync).isVisible = false
                    return true
                }

                override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                    activity?.invalidateOptionsMenu()
                    return true
                }
            })
        }

        mViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                performSearch(mSearchView?.query.toString())
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_open_filter -> {
                findNavController().navigate(R.id.action_databaseFragment_to_filterBottomSheetDialogFragment)
                true
            }
            R.id.action_database_sync -> {
                mViewModel.syncDatabase()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        performSearch(newText)
        return true
    }

    private fun initObservers() {
        mViewModel.syncWorkInfo.observe(viewLifecycleOwner) { listOfWorkInfo ->
            if (listOfWorkInfo.isNullOrEmpty()) {
                return@observe
            }

            val workInfo = listOfWorkInfo[0]

            if (workInfo.state.isFinished && workInfo.state == WorkInfo.State.FAILED) {
                showError(
                    R.string.database_sync_error_title,
                    R.string.database_sync_error_message
                )
            }
        }
    }

    private fun initTabLayoutWithViewPager() {
        mViewPager = binding.databaseViewPager.apply {
            adapter = DatabaseViewPagerAdapter(this@DatabaseFragment).also {
                mAdapter = it
            }

            TabLayoutMediator(binding.databaseTabLayout, this) { tab, position ->
                tab.text = when (position) {
                    0 -> getString(R.string.tab_card_title)
                    1 -> getString(R.string.tab_set_title)
                    else -> null
                }

            }.attach()
        }
    }

    private fun performSearch(newText: String?) {
        val currentFragment = childFragmentManager.findFragmentByTag("f" + mAdapter.getItemId(mViewPager.currentItem))

        if (currentFragment is BaseSearchListFragment<*>) {
            currentFragment.onQueryTextChange(newText)
        }
    }

    fun getQueryString(): String? {
        return mSearchView?.query?.toString()
    }
}
