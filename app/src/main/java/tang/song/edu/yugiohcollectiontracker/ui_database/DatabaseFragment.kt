package tang.song.edu.yugiohcollectiontracker.ui_database

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import tang.song.edu.yugiohcollectiontracker.BaseFragment
import tang.song.edu.yugiohcollectiontracker.R
import tang.song.edu.yugiohcollectiontracker.databinding.FragmentDatabaseBinding
import tang.song.edu.yugiohcollectiontracker.services.DatabaseSyncService
import tang.song.edu.yugiohcollectiontracker.ui_database.adapters.DatabasePagerAdapter
import tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels.DatabaseViewModel
import tang.song.edu.yugiohcollectiontracker.viewBinding

@AndroidEntryPoint
class DatabaseFragment : BaseFragment(), SearchView.OnQueryTextListener, Toolbar.OnMenuItemClickListener, MenuItem.OnActionExpandListener {
    private val binding by viewBinding(FragmentDatabaseBinding::inflate)

    private lateinit var mSearchView: SearchView

    private val mViewModel: DatabaseViewModel by activityViewModels()
    private lateinit var mAdapter: DatabasePagerAdapter
    private lateinit var mViewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback {
            if (mViewPager.currentItem == 0) {
                if (!findNavController().popBackStack()) {
                    activity?.finish()
                }
            } else {
                mViewPager.currentItem = mViewPager.currentItem - 1
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.databaseSyncProgressIndicator.setVisibilityAfterHide(View.GONE)

        initToolbar()
        initTabLayoutWithViewPager()
        initObservers()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        performSearch(query)
        return true
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

    /** When the searchview closes, **/
    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_search) {
            resetLists()

            binding.databaseToolbar.menu.clear()
            onCreateOptionsMenu()
        }
        return true
    }

    private fun initObservers() {
        lifecycleScope.launchWhenStarted {
            mViewModel.databaseSyncState.collect {
                if (it is DatabaseSyncService.DatabaseSyncState.LOADING) {
                    binding.databaseViewPager.visibility = View.INVISIBLE
                    binding.databaseSyncProgressIndicator.show()
                } else {
                    binding.databaseViewPager.visibility = View.VISIBLE
                    binding.databaseSyncProgressIndicator.hide()

                    if (it is DatabaseSyncService.DatabaseSyncState.FAILURE) {
                        showError(R.string.database_sync_error_title, R.string.database_sync_error_message)
                    }
                }

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
            inflateMenu(R.menu.database_toolbar_menu)

            menu.findItem(R.id.action_search).apply {
                mSearchView = (this.actionView.findViewById(R.id.search_view) as SearchView).apply {
                    setIconifiedByDefault(true)
                    setOnQueryTextListener(this@DatabaseFragment)
                }

                setOnActionExpandListener(this@DatabaseFragment)
            }

            setOnMenuItemClickListener(this@DatabaseFragment)
        }
    }

    /** Queries on the current displayed fragment **/
    private fun performSearch(query: String?) {
        val currentFragment = childFragmentManager.findFragmentByTag("f" + mAdapter.getItemId(mViewPager.currentItem))

        if (currentFragment is BaseSearchListFragment<*>) {
            currentFragment.updateSearchList(query)
        }
    }

    private fun resetLists() {
        repeat(mAdapter.itemCount) {
            val currentFragment = childFragmentManager.findFragmentByTag("f" + mAdapter.getItemId(it))

            if (currentFragment is BaseSearchListFragment<*> && !currentFragment.lastQueryValue().isNullOrBlank()) {
                currentFragment.updateSearchList(null)
            }
        }
    }
}
