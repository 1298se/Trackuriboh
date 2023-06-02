package sam.g.trackuriboh.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import sam.g.trackuriboh.MainGraphDirections
import sam.g.trackuriboh.R
import sam.g.trackuriboh.databinding.FragmentSearchResultBinding
import sam.g.trackuriboh.ui.search.adapters.SearchResultStateAdapter
import sam.g.trackuriboh.utils.safeNavigate
import sam.g.trackuriboh.utils.viewBinding

class SearchResultFragment : Fragment() {
    private val binding by viewBinding(FragmentSearchResultBinding::inflate)

    private lateinit var searchResultAdapter: SearchResultStateAdapter

    companion object {
        private const val ARG_QUERY = "SearchResultFragment_argQuery"

        fun newInstance(query: String? = null) =
            SearchResultFragment().apply {
                arguments = bundleOf(ARG_QUERY to query)
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initTabLayoutWithViewPager()
        initFragmentResultListeners()
    }

    private fun initTabLayoutWithViewPager() {
        binding.searchResultViewPager.apply {
            adapter = SearchResultStateAdapter(
                this@SearchResultFragment,
                arguments?.getString(ARG_QUERY)
            ).also {
                searchResultAdapter = it
            }
        }

        TabLayoutMediator(binding.searchResultTabLayout, binding.searchResultViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.tab_card_title)
                1 -> getString(R.string.tab_set_title)
                else -> null
            }
        }.attach()
    }

    private fun initFragmentResultListeners() {
        childFragmentManager.apply {
            setFragmentResultListener(CardListFragment.FRAGMENT_RESULT_REQUEST_KEY, viewLifecycleOwner) { _, bundle ->
                val cardId = bundle.getLong(CardListFragment.CARD_ID_DATA_KEY)

                parentFragment?.findNavController()?.safeNavigate(
                    MainGraphDirections.actionGlobalCardDetailFragment(cardId)
                )
            }

            setFragmentResultListener(CardSetListFragment.FRAGMENT_RESULT_REQUEST_KEY, viewLifecycleOwner) { _, bundle ->
                val setId = bundle.getLong(CardSetListFragment.SET_ID_DATA_KEY)

                parentFragment?.findNavController()?.safeNavigate(
                    MainGraphDirections.actionGlobalCardSetDetailFragment(setId)
                )
            }
        }
    }
}