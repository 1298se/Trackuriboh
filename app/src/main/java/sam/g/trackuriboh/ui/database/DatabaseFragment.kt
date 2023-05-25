package sam.g.trackuriboh.ui.database

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.R
import sam.g.trackuriboh.databinding.FragmentDatabaseBinding
import sam.g.trackuriboh.ui.database.viewmodels.DatabaseViewModel
import sam.g.trackuriboh.ui.search.SearchResultFragment
import sam.g.trackuriboh.utils.viewBinding

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
@AndroidEntryPoint
class DatabaseFragment : Fragment() {
    private val binding by viewBinding(FragmentDatabaseBinding::inflate)
    private val viewModel: DatabaseViewModel by viewModels()

    companion object {
        private val DATABASE_FRAGMENT_TAG: String = DatabaseExploreFragment::class.java.name
        private val SEARCH_RESULT_FRAGMENT_TAG: String = SearchResultFragment::class.java.name
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (childFragmentManager.findFragmentByTag(DATABASE_FRAGMENT_TAG) == null &&
                childFragmentManager.findFragmentByTag(SEARCH_RESULT_FRAGMENT_TAG) == null) {
            showDatabaseExplorePage()
        }

        initSearchView()
    }

    private fun showDatabaseExplorePage() {
        childFragmentManager.commit {
            replace<DatabaseExploreFragment>(binding.searchResultContainer.id, DATABASE_FRAGMENT_TAG)
        }
    }

    private fun initSearchView() {
        with (binding.databaseToolbar) {
            showBackButton(viewModel.hasUserInitiatedSearch)
        }

        with (binding.databaseSearchview) {
            searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    viewModel.hasUserInitiatedSearch = true

                    binding.databaseToolbar.showBackButton(true)
                }
            }

            searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    val searchResultFragment = SearchResultFragment.newInstance(query = query)

                    childFragmentManager.commit {
                        replace(binding.searchResultContainer.id, searchResultFragment, SEARCH_RESULT_FRAGMENT_TAG)
                    }

                    clearFocus()

                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
    }

    private fun MaterialToolbar.showBackButton(visible: Boolean) {
        if (visible) {
            setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        } else {
            navigationIcon = null
        }

        binding.databaseToolbar.setNavigationOnClickListener {
            viewModel.hasUserInitiatedSearch = false

            showBackButton(false)

            binding.databaseSearchview.searchView.apply {
                setQuery("", false)
                clearFocus()
            }

            if (childFragmentManager.findFragmentByTag(SEARCH_RESULT_FRAGMENT_TAG) != null) {
                showDatabaseExplorePage()
            }
        }
    }
}