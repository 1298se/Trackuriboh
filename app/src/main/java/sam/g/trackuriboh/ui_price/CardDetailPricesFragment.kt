package sam.g.trackuriboh.ui_price

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.R
import sam.g.trackuriboh.databinding.FragmentCardDetailPricesBinding
import sam.g.trackuriboh.ui_price.viewmodels.CardPricesViewModel
import sam.g.trackuriboh.viewBinding

@AndroidEntryPoint
class CardDetailPricesFragment : BottomSheetDialogFragment() {
    private val mViewModel: CardPricesViewModel by viewModels()

    private val binding by viewBinding(FragmentCardDetailPricesBinding::inflate)

    companion object {
        // This is the same value as the navArg name so that the SavedStateHandle can acess from either
        const val ARG_SKU_IDS = "skuIds"

        fun newInstance(skus: List<Long>?) =
            CardDetailPricesFragment().apply {
                arguments = Bundle().apply {
                    putLongArray(ARG_SKU_IDS, skus?.toLongArray())
                }
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel.printingToSkuMap.observe(this) { map ->
            map.toList().forEachIndexed { index, entry ->
                binding.skuPricesContainer.addView(SkuPricesCardView(requireContext()).apply {
                    layoutParams = ViewGroup.MarginLayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                    ).apply {
                        if (index != map.size - 1){
                            bottomMargin = context.resources.getDimension(R.dimen.list_item_large_row_spacing).toInt()
                        }
                    }
                    setHeader(entry.first)
                    setRowItems(entry.second)
                })
            }
        }
    }
}
