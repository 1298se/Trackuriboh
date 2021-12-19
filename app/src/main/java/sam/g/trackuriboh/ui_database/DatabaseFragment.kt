package sam.g.trackuriboh.ui_database

import android.app.SearchManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.*
import sam.g.trackuriboh.databinding.FragmentDatabaseBinding
import sam.g.trackuriboh.ui_database.CardListFragment.Companion.CARD_ITEM_CARD_ID
import sam.g.trackuriboh.ui_database.CardListFragment.Companion.CARD_ITEM_CLICK_REQUEST_KEY
import sam.g.trackuriboh.ui_database.CardListFragment.Companion.VIEW_PRICE_CLICK_REQUEST_KEY
import sam.g.trackuriboh.ui_database.CardListFragment.Companion.VIEW_PRICE_SKU_IDS
import sam.g.trackuriboh.ui_database.CardSetListFragment.Companion.SET_ITEM_CLICK_REQUEST_KEY
import sam.g.trackuriboh.ui_database.CardSetListFragment.Companion.SET_ITEM_ID
import sam.g.trackuriboh.ui_database.adapters.DatabaseStateAdapter
import sam.g.trackuriboh.ui_database.adapters.SearchSuggestionsAdapter
import sam.g.trackuriboh.ui_database.viewmodels.DatabaseViewModel
import sam.g.trackuriboh.utils.*

/**
 * The SearchView is very buggy and the behaviour is sometimes hard to manage. When updating Hilt,
 * the keyboard starting flickering when returning back from another fragment (this is because
 * Navigation uses replace instead of add for fragment transactions, so the fragment is recreated
 * when returning). To workaround this
 * we'll use activities to contain destinations that come from here.
 */
@AndroidEntryPoint
class DatabaseFragment :
    Fragment(),
    Toolbar.OnMenuItemClickListener {

    companion object {
        const val CARD_PAGE_POSITION = 0
        const val SET_PAGE_POSITION = 1
    }
    private val binding by viewBinding(FragmentDatabaseBinding::inflate)

    private lateinit var searchView: SearchView

    private val viewModel: DatabaseViewModel by activityViewModels()
    private lateinit var stateAdapter: DatabaseStateAdapter
    private lateinit var searchSuggestionsAdapter: SearchSuggestionsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewPagerBackPressBehaviour(binding.databaseViewPager)

        initToolbar()
        initTabLayoutWithViewPager()
        initActionObserver()
        initSearchSuggestionObserver()
        initFragmentResultListeners()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_database_update_check -> {
                viewModel.checkForDatabaseUpdates()

                true
            }
            R.id.action_database_download -> {
                viewModel.downloadDatabase()

                true
            }
            else -> false
        }
    }

    private fun initTabLayoutWithViewPager() {
        binding.databaseViewPager.apply {
            adapter = DatabaseStateAdapter(this@DatabaseFragment).also {
                stateAdapter = it
            }

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    when(position) {
                        CARD_PAGE_POSITION -> searchView.suggestionsAdapter = searchSuggestionsAdapter
                        SET_PAGE_POSITION -> searchView.suggestionsAdapter = null
                    }
                }
            })
        }

        TabLayoutMediator(binding.databaseTabLayout, binding.databaseViewPager) { tab, position ->
            tab.text = when (position) {
                CARD_PAGE_POSITION -> getString(R.string.tab_card_title)
                SET_PAGE_POSITION -> getString(R.string.tab_set_title)
                else -> null
            }
        }.attach()
    }

    private fun initToolbar() {
        binding.databaseToolbar.setupAsTopLevelDestinationToolbar()

        createOptionsMenu()
    }

    private fun initActionObserver() {
        viewModel.action.observe(viewLifecycleOwner) {
            if (it.hasBeenHandled) {
                return@observe
            }

            when (val action = it.getContent()) {
                is DatabaseViewModel.UiAction.Loading -> {
                    binding.databaseToolbar.setMenuEnabled(false)
                    showLoading(action.message)
                    setProgress(action.progress, action.indeterminate)
                }
                else -> {
                    it.handleEvent()
                    showContent()
                    binding.databaseToolbar.setMenuEnabled(true)

                    when (action) {
                        is DatabaseViewModel.UiAction.Snackbar -> showSnackbar(action.message, action.snackbarType)
                        is DatabaseViewModel.UiAction.UpdateAvailable -> {
                            context?.createAlertDialog(
                                title = getString(R.string.database_update_check_update_available_title),
                                message = getString(R.string.database_update_check_update_available_message),
                                positiveButtonText = getString(R.string.lbl_update),
                                negativeButtonText = getString(R.string.lbl_cancel),
                                onPositiveButtonClick = { _, _ -> viewModel.updateDatabase(action.cardSetIds) }
                            )?.show()
                        }
                    }
                }
            }
        }
    }

    private fun initSearchSuggestionObserver() {
        with(searchView) {
            suggestionsAdapter = SearchSuggestionsAdapter(
                context, null
            ).also {
                searchSuggestionsAdapter = it
            }

            setOnSuggestionListener(object : SearchView.OnSuggestionListener {
                override fun onSuggestionSelect(position: Int): Boolean = false

                override fun onSuggestionClick(position: Int): Boolean {
                    val cursor = (suggestionsAdapter.getItem(position) as Cursor)
                    val suggestion = cursor.getString(cursor.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_1))
                    setQuery(suggestion, true)

                    return true
                }
            })
        }

        viewModel.searchSuggestionsCursor.observe(viewLifecycleOwner) {
            with(searchView) {
                if (suggestionsAdapter != null) {
                    suggestionsAdapter.changeCursor(it)

                    findViewById<AutoCompleteTextView>(R.id.search_src_text).dropDownHeight =
                        minOf(5, it.count) * resources.getDimension(R.dimen.list_item_one_line_height).toInt()
                }
            }

        }
    }

    private fun setProgress(progress: Int, indeterminate: Boolean = false) {
        binding.databaseSyncProgressIndicator.apply {
            binding.databaseSyncProgressTextview.show(!indeterminate)
            isIndeterminate = indeterminate

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setProgress(progress, true)
            } else {
                this.progress = progress
            }
        }

        binding.databaseSyncProgressTextview.text = getString(R.string.progress_percent, progress)
    }

    private fun showLoading(message: String?) {
        binding.apply {
            databaseTabLayout.visibility = View.GONE
            databaseViewPager.visibility = View.GONE

            databaseSyncProgressIndicator.show()
            databaseSyncProgressTextview.visibility = View.VISIBLE

            with(databaseSyncInfoTextview) {
                visibility = View.VISIBLE
                text = message
            }

            databaseViewPager.adapter = null
        }
    }

    private fun showContent() {
        binding.apply {
            databaseTabLayout.visibility = View.VISIBLE
            databaseViewPager.visibility = View.VISIBLE

            databaseSyncProgressIndicator.hide()
            databaseSyncProgressTextview.visibility = View.GONE
            databaseSyncInfoTextview.visibility = View.GONE

            databaseViewPager.adapter = stateAdapter
        }

    }

    private fun createOptionsMenu() {
        binding.databaseToolbar.apply {
            inflateMenu(R.menu.database_toolbar_menu)

            menu.findItem(R.id.action_search).apply {
                searchView = setIconifiedSearchViewBehaviour(this, object : SearchViewQueryHandler {
                    override fun handleQueryTextSubmit(query: String?) {
                        performSearch(query)
                    }

                    override fun handleQueryTextChanged(newText: String?) {
                        viewModel.getSuggestions(newText)
                    }

                    override fun handleSearchViewExpanded() {
                        menu.children.forEach {
                            it.isVisible = it.itemId == itemId
                        }
                    }

                    override fun handleSearchViewCollapse() {
                        menu.children.forEach {
                            it.isVisible = true
                        }
                        resetLists()
                    }
                })
            }

            setOnMenuItemClickListener(this@DatabaseFragment)
        }
    }

    /** Queries on the current displayed fragment **/
    private fun performSearch(query: String?) {
        val currentFragment = childFragmentManager.findFragmentByTag("f" + stateAdapter.getItemId(binding.databaseViewPager.currentItem))

        if (currentFragment is BaseSearchListFragment<*>) {
            currentFragment.search(query)
        }

        searchView.clearFocus()
        binding.focusDummyView.requestFocus()
    }

    private fun resetLists() {
        repeat(stateAdapter.itemCount) {
            val currentFragment = childFragmentManager.findFragmentByTag("f" + stateAdapter.getItemId(it))

            if (currentFragment is BaseSearchListFragment<*> && currentFragment.lastQueryValue() != null) {
                currentFragment.search(null)
            }
        }
    }

    private fun initFragmentResultListeners() {
        childFragmentManager.apply {
            setFragmentResultListener(CARD_ITEM_CLICK_REQUEST_KEY, this@DatabaseFragment) { _, bundle ->
                handleNavigationAction(
                    DatabaseFragmentDirections.actionDatabaseFragmentToCardDetailActivity(bundle.getLong(CARD_ITEM_CARD_ID))
                )
            }

            setFragmentResultListener(VIEW_PRICE_CLICK_REQUEST_KEY, this@DatabaseFragment) { _, bundle ->
                handleNavigationAction(
                    DatabaseFragmentDirections.actionDatabaseFragmentToCardPricesBottomSheetDialogFragment(
                        bundle.getLongArray(VIEW_PRICE_SKU_IDS) ?: longArrayOf()
                    )
                )
            }

            setFragmentResultListener(SET_ITEM_CLICK_REQUEST_KEY, this@DatabaseFragment) { _, bundle ->
                handleNavigationAction(
                    DatabaseFragmentDirections.actionDatabaseFragmentToCardSetDetailActivity(bundle.getLong(SET_ITEM_ID))
                )
            }
        }
    }
}
