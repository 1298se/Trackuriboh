package sam.g.trackuriboh.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.databinding.FragmentCardFilterSelectionBinding
import sam.g.trackuriboh.ui.search.viewmodels.CardFilterViewModel
import sam.g.trackuriboh.utils.safeNavigate
import sam.g.trackuriboh.utils.viewBinding


@AndroidEntryPoint
class CardFilterSelectionFragment : Fragment() {
    private val viewModel: CardFilterViewModel by viewModels()
    private val binding by viewBinding(FragmentCardFilterSelectionBinding::inflate)

    private val args by navArgs<CardFilterSelectionFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.allFilters.observe(viewLifecycleOwner) { model ->
            val rarityNames = model.rarities.mapNotNull { it.name }
            val cardTypeNames = model.cardTypes.map { it.displayName }

            binding.rarityFilterChipFormCellview.setupWith(rarityNames)
            binding.cardTypeFilterChipFormCellview.setupWith(cardTypeNames)

            binding.rarityFilterChipFormCellview.setOnClickListener {
                findNavController().safeNavigate(
                    CardFilterSelectionFragmentDirections.actionCardFilterSelectionToListPickerFragment(rarityNames.toTypedArray())
                )
            }
        }
    }
}