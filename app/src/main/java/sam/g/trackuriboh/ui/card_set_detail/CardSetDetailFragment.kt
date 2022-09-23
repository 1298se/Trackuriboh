package sam.g.trackuriboh.ui.card_set_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.ExperimentalMaterialApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.MainGraphDirections
import sam.g.trackuriboh.R
import sam.g.trackuriboh.databinding.FragmentCardSetDetailBinding
import sam.g.trackuriboh.ui.card_set_detail.viewmodels.CardSetDetailViewModel
import sam.g.trackuriboh.ui.common.ToolbarSearchView
import sam.g.trackuriboh.ui.search.CardListFragment
import sam.g.trackuriboh.ui.search_suggestions.CardSearchSuggestionsViewModel
import sam.g.trackuriboh.utils.SearchViewQueryHandler
import sam.g.trackuriboh.utils.safeNavigate
import sam.g.trackuriboh.utils.setIconifiedSearchViewBehaviour
import sam.g.trackuriboh.utils.viewBinding

/**
 * Fragment containing the list of cards in a particular set
 */
@ExperimentalMaterialApi
@AndroidEntryPoint
class CardSetDetailFragment : Fragment() {
    private val binding by viewBinding(FragmentCardSetDetailBinding::inflate)
    private val args: CardSetDetailFragmentArgs by navArgs()

    private val viewModel: CardSetDetailViewModel by viewModels()
    private val searchSuggestionsViewModel: CardSearchSuggestionsViewModel by viewModels()

    private lateinit var toolbarSearchView: ToolbarSearchView

    companion object {
        private val CARD_LIST_FRAGMENT_TAG = CardListFragment::class.java.name
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
            showSearchResults(null)
        }

        initToolbar()
        initFragmentResultListeners()
        initObservers()
    }

    private fun showSearchResults(query: String?) {
        childFragmentManager.commit {
            replace(
                binding.cardSetDetailFragmentContainer.id,
                CardListFragment.newInstance(query = query, setId = args.setId),
                CARD_LIST_FRAGMENT_TAG
            )
        }
    }

    private fun initToolbar() {
        binding.cardSetDetailToolbar.setupWithNavController(
            findNavController(),
            AppBarConfiguration.Builder().setFallbackOnNavigateUpListener {
                activity?.finish()
                true
            }.build()
        )

        createOptionsMenu()
    }

    private fun initFragmentResultListeners() {
        childFragmentManager.setFragmentResultListener(
            CardListFragment.FRAGMENT_RESULT_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val cardId = bundle.getLong(CardListFragment.CARD_ID_DATA_KEY)

            findNavController().safeNavigate(
                MainGraphDirections.actionGlobalCardDetailFragment(cardId)
            )
        }
    }

    private fun initObservers() {
        viewModel.cardSet.observe(viewLifecycleOwner) {
            binding.cardSetDetailToolbar.title = it?.name
        }
    }

    private fun createOptionsMenu() {
        binding.cardSetDetailToolbar.apply {
            inflateMenu(R.menu.card_set_detail_toolbar)

            menu.findItem(R.id.action_search).apply {
                toolbarSearchView = (actionView as ToolbarSearchView)

                setIconifiedSearchViewBehaviour(toolbarSearchView, object : SearchViewQueryHandler {
                    override fun handleQueryTextSubmit(query: String?) {
                        showSearchResults(query)

                        viewModel.query = query
                    }

                    override fun handleQueryTextChanged(newText: String?) {
                        searchSuggestionsViewModel.getSuggestions(newText)
                    }

                    override fun handleSearchViewCollapse() {
                        if (!viewModel.query.isNullOrEmpty()) {
                            showSearchResults(null)
                        }
                    }
                })
            }
        }
    }
}
