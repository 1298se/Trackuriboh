package sam.g.trackuriboh.ui.database

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.MainGraphDirections
import sam.g.trackuriboh.data.db.entities.CardSet
import sam.g.trackuriboh.data.db.entities.Product
import sam.g.trackuriboh.databinding.FragmentDatabaseExploreBinding
import sam.g.trackuriboh.ui.database.adapters.CardSetExploreCardsAdapter
import sam.g.trackuriboh.ui.database.viewmodels.DatabaseExploreViewModel
import sam.g.trackuriboh.ui.search.CardSetExploreRowView
import sam.g.trackuriboh.utils.safeNavigate


@AndroidEntryPoint
class DatabaseExploreFragment : Fragment(), CardSetExploreCardsAdapter.OnItemClickListener {

    private lateinit var binding: FragmentDatabaseExploreBinding
    private val viewModel: DatabaseExploreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!this::binding.isInitialized) {
            binding = FragmentDatabaseExploreBinding.inflate(inflater, container, false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
    }

    private fun initObservers() {
        viewModel.databaseInfoUiState.observe(viewLifecycleOwner) {
            binding.databaseStatusView.setupWith(it)
        }

        viewModel.databaseUpdateButtonState.observe(viewLifecycleOwner) {
            binding.databaseStatusView.setupUpdateButtonState(it)
        }

        viewModel.recentCardSetsWithProducts.observe(viewLifecycleOwner) {
            if (binding.cardSetExploreContainer.childCount == 0) {
                setupCardSetExploreList(it)
            }
        }
    }

    private fun setupCardSetExploreList(data: Map<CardSet, Map<Product, Double?>>) {
        with (binding.cardSetExploreContainer) {
            removeAllViews()

            for (entry in data.entries) {
                context?.let {
                    val view = CardSetExploreRowView(it).apply {
                        this.id = View.generateViewId()

                        setupWith(entry.key, entry.value, this@DatabaseExploreFragment)
                    }
                    addView(view)
                }
            }
        }
    }

    override fun onItemClick(cardId: Long) {
        findNavController().safeNavigate(
            MainGraphDirections.actionGlobalCardDetailFragment(cardId)
        )
    }
}