package sam.g.trackuriboh.ui.card_selection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.R
import sam.g.trackuriboh.databinding.FragmentCardSelectionBinding
import sam.g.trackuriboh.ui.common.ToolbarSearchView
import sam.g.trackuriboh.ui.search.CardListFragment
import sam.g.trackuriboh.ui.search_suggestions.CardSearchSuggestionsViewModel
import sam.g.trackuriboh.utils.SearchViewQueryHandler
import sam.g.trackuriboh.utils.setIconifiedSearchViewBehaviour
import sam.g.trackuriboh.utils.viewBinding

@AndroidEntryPoint
class CardSelectionFragment : Fragment() {

    private val binding: FragmentCardSelectionBinding by viewBinding(FragmentCardSelectionBinding::inflate)

    private val searchSuggestionsViewModel: CardSearchSuggestionsViewModel by viewModels()

    private lateinit var toolbarSearchView: ToolbarSearchView

    companion object {
        const val FRAGMENT_RESULT_REQUEST_KEY = "CardSelectionFragment_fragmentResultRequestKey"
        const val CARD_ID_DATA_KEY = "CardSelectionFragment_cardId"

        private val CARD_LIST_FRAGMENT_TAG: String = CardListFragment::class.java.name
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (childFragmentManager.findFragmentByTag(CARD_LIST_FRAGMENT_TAG) == null) {
            childFragmentManager.commit {
                replace(
                    binding.newCardSelectionFragmentContainer.id,
                    CardListFragment.newInstance(),
                    CARD_LIST_FRAGMENT_TAG
                )
            }
        }

        initToolbar()
        initFragmentResultListeners()
    }

    private fun initFragmentResultListeners() {
        childFragmentManager.setFragmentResultListener(
            CardListFragment.FRAGMENT_RESULT_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val cardId = bundle.getLong(CardListFragment.CARD_ID_DATA_KEY)

            parentFragmentManager.setFragmentResult(
                FRAGMENT_RESULT_REQUEST_KEY,
                bundleOf(CARD_ID_DATA_KEY to cardId)
            )

            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                CARD_ID_DATA_KEY,
                cardId
            )

            val navArgs = arguments?.let { CardSelectionFragmentArgs.fromBundle(it) }

            if (navArgs?.shouldCloseAfterClick == true) {
                findNavController().popBackStack()
            }
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

    private fun createOptionsMenu() {
        binding.cardSelectionToolbar.apply {
            inflateMenu(R.menu.card_set_detail_toolbar)

            menu.findItem(R.id.action_search).apply {
                toolbarSearchView = (actionView as ToolbarSearchView)

                setIconifiedSearchViewBehaviour(toolbarSearchView, object : SearchViewQueryHandler {
                    override fun handleQueryTextSubmit(query: String?) {
                        childFragmentManager.commit {
                            replace(
                                binding.newCardSelectionFragmentContainer.id,
                                CardListFragment.newInstance(query = query),
                                CARD_LIST_FRAGMENT_TAG
                            )
                        }
                    }

                    override fun handleQueryTextChanged(newText: String?) {
                        searchSuggestionsViewModel.getSuggestions(newText)
                    }

                    override fun handleSearchViewCollapse() {
                        findNavController().popBackStack()
                    }
                })
            }
        }
    }
}
