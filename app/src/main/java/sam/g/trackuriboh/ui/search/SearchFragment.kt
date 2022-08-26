package sam.g.trackuriboh.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.data.db.entities.CardSet
import sam.g.trackuriboh.data.db.entities.Product
import sam.g.trackuriboh.databinding.FragmentSearchBinding
import sam.g.trackuriboh.utils.viewBinding


@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val binding by viewBinding(FragmentSearchBinding::inflate)
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.databaseStatusInfo.observe(viewLifecycleOwner) {
            binding.databaseStatusView.setupWith(it)
        }

        viewModel.recentCardSetsWithProducts.observe(viewLifecycleOwner) {
            setupCardSetExploreList(it)
        }
    }

    private fun setupCardSetExploreList(data: Map<CardSet, Map<Product, Double?>>) {
        for (entry in data.entries) {
            context?.let {
                val view = CardSetExploreRowView(it).apply {
                    setupWith(entry.key, entry.value)
                }
                binding.cardSetExploreContainer.addView(view)
            }
        }
    }

}