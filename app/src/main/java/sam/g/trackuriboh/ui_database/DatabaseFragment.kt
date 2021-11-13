package sam.g.trackuriboh.ui_database

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.R
import sam.g.trackuriboh.databinding.FragmentDatabaseBinding
import sam.g.trackuriboh.setViewPagerBackPressBehaviour
import sam.g.trackuriboh.ui_database.adapters.DatabasePagerAdapter
import sam.g.trackuriboh.ui_database.viewmodels.DatabaseViewModel
import sam.g.trackuriboh.viewBinding

/**
 * The SearchView is very buggy and the behaviour is sometimes hard to manage. When updating Hilt,
 * the keyboard starting flickering when returning back from another fragment, so to workaround this
 * we'll use activities to contain destinations that come from here.
 */
@AndroidEntryPoint
class DatabaseFragment :
    Fragment(),
    SearchView.OnQueryTextListener,
    Toolbar.OnMenuItemClickListener,
    MenuItem.OnActionExpandListener
{
    private val binding by viewBinding(FragmentDatabaseBinding::inflate)

    private lateinit var mSearchView: SearchView

    private val mViewModel: DatabaseViewModel by activityViewModels()
    private lateinit var mAdapter: DatabasePagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewPagerBackPressBehaviour(binding.databaseViewPager)

        initToolbar()
        initTabLayoutWithViewPager()
        // TODO: Add Database Sync listener
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        performSearch(query)
        mSearchView.clearFocus()

        // Hack to stop the tablayout from getting focus after this is called
        binding.focusDummyView.requestFocus()

        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_database_sync -> {
                mViewModel.syncDatabase()
                true
            }
            else -> false
        }
    }

    override fun onMenuItemActionExpand(item: MenuItem?): Boolean { return true }

    /** When the searchview closes, **/
    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_search) {
            resetLists()
        }
        return true
    }

    private fun initTabLayoutWithViewPager() {
        binding.databaseViewPager.apply {
            adapter = DatabasePagerAdapter(this@DatabaseFragment).also {
                mAdapter = it
            }
        }

        TabLayoutMediator(binding.databaseTabLayout, binding.databaseViewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> getString(R.string.tab_card_title)
                    1 -> getString(R.string.tab_set_title)
                    else -> null
                }

        }.attach()
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
        val currentFragment = childFragmentManager.findFragmentByTag("f" + mAdapter.getItemId(binding.databaseViewPager.currentItem))

        if (currentFragment is BaseSearchListFragment<*>) {
            currentFragment.search(query)
        }
    }

    private fun resetLists() {
        repeat(mAdapter.itemCount) {
            val currentFragment = childFragmentManager.findFragmentByTag("f" + mAdapter.getItemId(it))

            if (currentFragment is BaseSearchListFragment<*> && currentFragment.lastQueryValue() != null) {
                currentFragment.search(null)
            }
        }
    }
}
