package sam.g.trackuriboh.ui_price

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.*
import sam.g.trackuriboh.data.db.relations.SkuWithConditionAndPrinting
import sam.g.trackuriboh.databinding.BottomSheetCardPricesBinding
import sam.g.trackuriboh.ui_common.OneLineAttributeCardView
import sam.g.trackuriboh.ui_common.UiState
import sam.g.trackuriboh.ui_price.viewmodels.CardPricesViewModel
import sam.g.trackuriboh.utils.*

/**
 * This fragment can be used as a bottom sheet by itself and an embedded fragment
 */
@AndroidEntryPoint
class CardPricesBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private val mViewModel: CardPricesViewModel by viewModels()

    private val binding by viewBinding(BottomSheetCardPricesBinding::inflate)

    companion object {
        // This is the same value as the navArg name so that the SavedStateHandle can acess from either
        const val ARG_SKU_IDS = "skuIds"

        /*
         * Arg that sets [mShowsDialog] by force. Usually this value is handled depending on its use case
         * (i.e. normally it's true, if it's inflated into a container view it's false), however ViewPager2
         * doesn't inflate, so we have to manually set it if we're embedding it into a ViewPager2
         */
        const val SHOWS_DIALOG = "SHOULD_SHOW_DIALOG"

        fun newInstance(skus: List<Long>?, shouldShowDialog: Boolean? = null) =
            CardPricesBottomSheetDialogFragment().apply {
                arguments = Bundle().apply {
                    putLongArray(ARG_SKU_IDS, skus?.toLongArray())

                    shouldShowDialog?.let { putBoolean(SHOWS_DIALOG, shouldShowDialog) }
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.getBoolean(SHOWS_DIALOG, showsDialog)?.let {
            showsDialog = it
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDefaultExpanded()

        // According to docs, don't use viewLifeCycleOwner for observe because of the dialog lifecycle being different
        mViewModel.run {
            state.observe(if (showsDialog) this@CardPricesBottomSheetDialogFragment else viewLifecycleOwner) { uiState ->
                when (uiState) {
                    is UiState.Loading -> binding.contentContainer.showOnly(binding.progressIndicator)
                    is UiState.Failure, is UiState.Success -> {
                        binding.progressIndicator.hide()

                        binding.contentContainer.showAllExcept(binding.progressIndicator)

                        uiState.data?.let { buildSkuPriceViews(it) }

                        if (uiState is UiState.Failure) {
                            uiState.message?.let {
                                if (showsDialog) {
                                    showSnackbar(it)
                                } else {
                                    setFragmentResult(SNACKBAR_SHOW_REQUEST_KEY, bundleOf(
                                        SNACKBAR_TYPE to SnackbarType.ERROR.name,
                                        SNACKBAR_MESSAGE to it
                                    ))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun buildSkuPriceViews(data: Map<String?, List<SkuWithConditionAndPrinting>>) {
        data.toList().forEachIndexed { index, entry ->
            binding.contentContainer.addView(OneLineAttributeCardView(context).apply {
                layoutParams = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                ).apply {
                    if (index != data.size - 1) {
                        bottomMargin = context.resources.getDimension(R.dimen.list_item_large_row_spacing).toInt()
                    }
                }
                setHeader(entry.first, getString(R.string.lbl_price_lowest_listing_usd))
                setRowItems(entry.second.associate {
                    (it.condition?.name ?: getString(R.string.lbl_not_available)) to
                            (it.sku.lowestListingPrice?.toString() ?: getString(R.string.lbl_not_available))
                })
            })
        }
    }
}
