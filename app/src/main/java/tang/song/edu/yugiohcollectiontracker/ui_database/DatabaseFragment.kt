package tang.song.edu.yugiohcollectiontracker.ui_database

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.viewpager2.widget.ViewPager2
import androidx.work.WorkInfo
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import tang.song.edu.yugiohcollectiontracker.BaseApplication
import tang.song.edu.yugiohcollectiontracker.BaseFragment
import tang.song.edu.yugiohcollectiontracker.R
import tang.song.edu.yugiohcollectiontracker.databinding.FragmentDatabaseBinding
import tang.song.edu.yugiohcollectiontracker.ui_database.adapters.DatabaseViewPagerAdapter
import tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels.DatabaseViewModel
import tang.song.edu.yugiohcollectiontracker.ui_search.SearchActivity

class DatabaseFragment : BaseFragment() {
    private var _binding: FragmentDatabaseBinding? = null
    private val binding get() = _binding!!

    private lateinit var mViewPager: ViewPager2
    private lateinit var mTabLayout: TabLayout
    private lateinit var mViewModel: DatabaseViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity?.application as BaseApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel = ViewModelProvider(this).get(DatabaseViewModel::class.java)

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

        initTabLayoutWithViewPager(view)
        initObservers()
    }

    override fun onDestroyView() {
        _binding = null

        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate the options menu from XML
        inflater.inflate(R.menu.database_actionbar_menu, menu)
        // Get the SearchView and set the searchable configuration
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        menu.findItem(R.id.action_open_search).apply {
            (this.actionView.findViewById(R.id.search_view) as SearchView).apply {
                setIconifiedByDefault(true)

                val componentName = ComponentName(context, SearchActivity::class.java)
                setSearchableInfo(searchManager.getSearchableInfo(componentName))
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

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_database_sync -> {
                mViewModel.syncDatabase()
                true
            }
            else -> false
        }
    }

    private fun initObservers() {
        mViewModel.syncWorkInfo.observe(viewLifecycleOwner) { listOfWorkInfo ->
            if (listOfWorkInfo.isNullOrEmpty()) {
                return@observe
            }

            val workInfo = listOfWorkInfo[0]

            if (workInfo.state.isFinished && workInfo.state == WorkInfo.State.FAILED) {
                showErrorDialog(
                    R.string.database_sync_error_title,
                    R.string.database_sync_error_message
                )
            }
        }
    }

    private fun initTabLayoutWithViewPager(view: View) {
        mViewPager = binding.databaseViewPager
        mViewPager.adapter = DatabaseViewPagerAdapter(requireActivity())
        mTabLayout = binding.databaseTabLayout

        TabLayoutMediator(mTabLayout, mViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.tab_card_title)
                1 -> getString(R.string.tab_set_title)
                else -> null
            }

        }.attach()
    }
}
