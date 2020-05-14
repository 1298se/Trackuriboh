package tang.song.edu.yugiohcollectiontracker.ui_database

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import androidx.work.WorkInfo
import com.google.android.material.tabs.TabLayoutMediator
import tang.song.edu.yugiohcollectiontracker.BaseApplication
import tang.song.edu.yugiohcollectiontracker.BaseFragment
import tang.song.edu.yugiohcollectiontracker.R
import tang.song.edu.yugiohcollectiontracker.databinding.FragmentDatabaseBinding
import tang.song.edu.yugiohcollectiontracker.ui_database.adapters.DatabasePagerAdapter
import tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels.DatabaseViewModel
import tang.song.edu.yugiohcollectiontracker.viewBinding

class DatabaseFragment : BaseFragment(R.layout.fragment_database), SearchView.OnQueryTextListener, Toolbar.OnMenuItemClickListener, MenuItem.OnActionExpandListener {
    private val binding by viewBinding(FragmentDatabaseBinding::bind)

    private lateinit var mSearchView: SearchView

    private lateinit var mViewModel: DatabaseViewModel
    private lateinit var mAdapter: DatabasePagerAdapter
    private lateinit var mViewPager: ViewPager2

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity?.application as BaseApplication).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = ViewModelProvider(requireActivity()).get(DatabaseViewModel::class.java)

        initToolbar()
        initTabLayoutWithViewPager()
        initObservers()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        performSearch(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_open_filter -> {
                findNavController().navigate(R.id.action_databaseFragment_to_filterBottomSheetDialogFragment)
                true
            }
            R.id.action_database_sync -> {
                mViewModel.syncDatabase()
                true
            }
            else -> false
        }
    }

    override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
        binding.databaseToolbar.menu.findItem(R.id.action_database_sync).isVisible = false
        return true
    }

    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        resetLists()

        binding.databaseToolbar.menu.clear()
        onCreateOptionsMenu()
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
            adapter = DatabasePagerAdapter(this@DatabaseFragment).also {
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

    private fun initToolbar() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        binding.databaseToolbar.setupWithNavController(navController, appBarConfiguration)

        onCreateOptionsMenu()
    }

    private fun onCreateOptionsMenu() {
        binding.databaseToolbar.apply {
            inflateMenu(R.menu.database_actionbar_menu)

            menu.findItem(R.id.action_open_search).apply {
                mSearchView = (this.actionView.findViewById(R.id.search_view) as SearchView).apply {
                    setIconifiedByDefault(true)
                    setOnQueryTextListener(this@DatabaseFragment)
                }

                setOnActionExpandListener(this@DatabaseFragment)
            }

            setOnMenuItemClickListener(this@DatabaseFragment)
        }
    }

    private fun performSearch(query: String?) {
        val currentFragment = childFragmentManager.findFragmentByTag("f" + mAdapter.getItemId(mViewPager.currentItem))

        if (currentFragment is BaseSearchListFragment) {
            currentFragment.onQueryTextChange(query)
        }
    }

    private fun resetLists() {
        repeat(mAdapter.itemCount) {
            val currentFragment = childFragmentManager.findFragmentByTag("f" + mAdapter.getItemId(it))

            if (currentFragment is BaseSearchListFragment && !currentFragment.lastQueryValue().isNullOrBlank()) {
                currentFragment.onQueryTextChange(null)
            }
        }
    }
}
