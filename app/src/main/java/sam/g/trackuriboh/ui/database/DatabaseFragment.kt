package sam.g.trackuriboh.ui.database

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.work.WorkInfo
import sam.g.trackuriboh.MainActivity
import sam.g.trackuriboh.R
import sam.g.trackuriboh.databinding.FragmentDatabaseV2Binding
import sam.g.trackuriboh.ui.common.utils.UiState
import sam.g.trackuriboh.ui.search.DatabaseExploreFragment
import sam.g.trackuriboh.ui.search.SearchResultFragment
import sam.g.trackuriboh.utils.*

/**
 * !IMPORTANT
 *
 * Update: After converting to a persistent toolbar, this issue no longer occurs.
 *
 * The SearchView is very buggy and the behaviour is sometimes hard to manage. When updating Hilt,
 * the keyboard starting flickering when returning back from another fragment (this is because
 * Navigation uses replace instead of add for fragment transactions, so the fragment is recreated
 * when returning). To workaround this
 * we'll use activities to contain destinations that come from here.
 */
class DatabaseFragment : Fragment() {

    private val viewModel: DatabaseViewModel by viewModels()

    companion object {
        private const val DATABASE_EXPLORE_FRAGMENT_TAG = "Database_Explore_Fragment"
        private const val SEARCH_RESULT_FRAGMENT_TAG = "Search_Result_Fragment"
    }

    private val binding by viewBinding(FragmentDatabaseV2Binding::inflate)
    private var hasUserInitiatedSearch = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (childFragmentManager.findFragmentByTag(DATABASE_EXPLORE_FRAGMENT_TAG) == null &&
                childFragmentManager.findFragmentByTag(SEARCH_RESULT_FRAGMENT_TAG) == null) {
            showDatabaseExplorePage()
        }

        initSearchView()
    }

    private fun showDatabaseExplorePage() {
        childFragmentManager.commit {
            replace(R.id.search_result_container, DatabaseExploreFragment(), DATABASE_EXPLORE_FRAGMENT_TAG)
        }
    }

    private fun initSearchView() {
        binding.searchBackButton.show(hasUserInitiatedSearch)

        binding.searchBackButton.setOnClickListener {
            hasUserInitiatedSearch = false

            it.visibility = View.GONE

            binding.searchView.apply {
                setQuery("", false)
                clearFocus()
            }

            showDatabaseExplorePage()
        }

        binding.searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                hasUserInitiatedSearch = true

                binding.searchBackButton.visibility = View.VISIBLE
            }
        }

        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val searchResultFragment = SearchResultFragment.newInstance(query = query)

                childFragmentManager.commit {
                    replace(R.id.search_result_container, searchResultFragment, SEARCH_RESULT_FRAGMENT_TAG)
                }

                binding.searchView.clearFocus()
                binding.focusDummyView.requestFocus()

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun initDatabaseSyncObservers() {
        /*viewModel.databaseUpdateState.observe(viewLifecycleOwner) { event ->
            handleWorkUpdated(
                workEvent = event,
                onLoading = { uiState ->
                    setProgress(indeterminate = true, progress = -1, message = uiState.message)
                },
                onSuccess = { uiState ->
                    uiState.message?.let { showSnackbar(it, SnackbarType.SUCCESS) }
                },
                onFailure = { uiState ->
                    uiState.message?.let { showSnackbar(it, SnackbarType.ERROR) }
                },
                onCancelled = { uiState ->
                    uiState.message?.let { showSnackbar(it, SnackbarType.INFO) }
                }
            )
        }*/
    }

    private fun handleWorkUpdated(
        workEvent: SingleEvent<UiState<WorkInfo>>,
        onLoading: (uiState: UiState<WorkInfo>) -> Unit,
        onSuccess: (uiState: UiState<WorkInfo>) -> Unit,
        onFailure: (uiState: UiState<WorkInfo>) -> Unit,
        onCancelled: (uiState: UiState<WorkInfo>) -> Unit,
    ) {
        if (workEvent.hasBeenHandled) {
            return
        }

        val savedStateHandle = findNavController().getBackStackEntry(R.id.mainGraph).savedStateHandle

        when (val uiState = workEvent.getContent()) {
            is UiState.Loading -> {
                showLoading()
                savedStateHandle.set(MainActivity.ACTION_SET_BOTTOM_NAV_ENABLED, false)
                onLoading(uiState)
            }
            else -> {
                workEvent.handleEvent()
                savedStateHandle.set(MainActivity.ACTION_SET_BOTTOM_NAV_ENABLED, true)

                when (uiState) {
                    is UiState.Failure -> {
                        if (uiState.data?.state == WorkInfo.State.CANCELLED) {
                            onCancelled(uiState)
                        } else {
                            onFailure(uiState)
                        }
                    }
                    is UiState.Success -> onSuccess(uiState)
                    else -> return
                }
            }
        }
    }

    private fun showLoading() {
    }

}