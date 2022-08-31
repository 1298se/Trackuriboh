package sam.g.trackuriboh.ui.database

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.R
import sam.g.trackuriboh.databinding.FragmentDatabaseBinding
import sam.g.trackuriboh.ui.search.DatabaseExploreFragment
import sam.g.trackuriboh.ui.search.SearchResultFragment
import sam.g.trackuriboh.utils.show
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
    companion object {
        private const val DATABASE_EXPLORE_FRAGMENT_TAG = "Database_Explore_Fragment"
        private const val SEARCH_RESULT_FRAGMENT_TAG = "Search_Result_Fragment"
    }

    private val binding by viewBinding(FragmentDatabaseBinding::inflate)
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

}