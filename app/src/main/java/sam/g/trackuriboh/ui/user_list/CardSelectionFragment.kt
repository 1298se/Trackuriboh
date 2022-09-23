package sam.g.trackuriboh.ui.user_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.compose.material.ExperimentalMaterialApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.R
import sam.g.trackuriboh.databinding.FragmentCardSelectionBinding
import sam.g.trackuriboh.ui.search.CardListFragment
import sam.g.trackuriboh.ui.search_suggestions.CardSearchSuggestionsViewModel
import sam.g.trackuriboh.utils.*

@ExperimentalMaterialApi
@AndroidEntryPoint
class CardSelectionFragment : Fragment() {

    private val binding: FragmentCardSelectionBinding by viewBinding(FragmentCardSelectionBinding::inflate)

    private val searchSuggestionsViewModel: CardSearchSuggestionsViewModel by viewModels()

    private val args: CardSelectionFragmentArgs by navArgs()

    private lateinit var searchView: SearchView

    private lateinit var cardListFragment: CardListFragment

    companion object {
        const val FRAGMENT_RESULT_REQUEST_KEY = "CardSelectionFragment_fragmentResultRequestKey"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardListFragment = CardListFragment.newInstance()

        if (childFragmentManager.findFragmentByTag(CardListFragment::class.java.name) == null) {
            childFragmentManager.beginTransaction().replace(
                binding.newCardSelectionFragmentContainer.id,
                cardListFragment
            ).commit()
        }

        initToolbar()
        initSearchSuggestions()
        initFragmentResultListeners()
    }

    private fun initFragmentResultListeners() {
        childFragmentManager.setFragmentResultListener(
            CardListFragment.FRAGMENT_RESULT_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val cardId = bundle.getLong(CardListFragment.CARD_ID_DATA_KEY)

            AddToUserListDialogFragment.newInstance(cardId, args.userList).show(childFragmentManager, null)
        }
        
        childFragmentManager.setFragmentResultListener(
            AddToUserListDialogFragment.FRAGMENT_RESULT_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val userListName = bundle.getString(AddToUserListDialogFragment.ADDED_USER_LIST_NAME_DATA_KEY)

            showSnackbar(getString(R.string.add_to_user_list_success_message, userListName))
        }
    }

    private fun initToolbar() {
        binding.cardSelectionToolbar.setupWithNavController(
            findNavController(),
            AppBarConfiguration(
                topLevelDestinationIds = setOf(),
                fallbackOnNavigateUpListener = {
                    activity?.finish()
                    true
                }
            )
        )

        createOptionsMenu()
    }

    private fun initSearchSuggestions() {
        searchView.initSearchSuggestions()

        searchView.initSearchSuggestions()

        searchSuggestionsViewModel.suggestionsCursor.observe(viewLifecycleOwner) {
            searchView.setSuggestionsCursor(it)
        }
    }


    private fun createOptionsMenu() {
        binding.cardSelectionToolbar.apply {
            inflateMenu(R.menu.card_set_detail_toolbar)

            menu.findItem(R.id.action_search).apply {
                searchView = (actionView?.findViewById(R.id.search_view) as SearchView)

                setIconifiedSearchViewBehaviour(searchView, object : SearchViewQueryHandler {
                    override fun handleQueryTextSubmit(query: String?) {
                        // cardListFragment.search(query)

                        searchView.clearFocus()
                        binding.focusDummyView.requestFocus()
                    }

                    override fun handleQueryTextChanged(newText: String?) {
                        searchSuggestionsViewModel.getSuggestions(newText)
                    }

                    override fun handleSearchViewCollapse() {
                        // cardListFragment.search(null)
                    }
                })
            }
        }
    }
}