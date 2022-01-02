package sam.g.trackuriboh.ui.card_detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.relations.SkuWithConditionAndPrinting
import sam.g.trackuriboh.databinding.BottomSheetSkuSelectionBinding
import sam.g.trackuriboh.databinding.ItemSimpleOneLineBinding
import sam.g.trackuriboh.ui.card_detail.viewmodels.SkuSelectionViewModel
import sam.g.trackuriboh.utils.viewBinding

@AndroidEntryPoint
class SkuSelectionBottomSheetFragment : BottomSheetDialogFragment() {
    private val binding: BottomSheetSkuSelectionBinding by viewBinding(BottomSheetSkuSelectionBinding::inflate)

    private val viewModel: SkuSelectionViewModel by viewModels()

    companion object {
        const val FRAGMENT_RESULT_REQUEST_KEY = "SkuSelectionBottomSheetFragment_fragmentResultRequestKey"
        const val SELECTED_SKU_WITH_CONDITION_AND_PRICING_DATA_KEY = "SkuSelectionBottomSheetFragment_selectedSkuWithConditionAndPricing"

        const val ARG_CARD_ID = "SkuSelectionBottomSheetFragment_argCardId"

        fun newInstance(cardId: Long) =
            SkuSelectionBottomSheetFragment().apply {
                arguments = bundleOf(ARG_CARD_ID to cardId)
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.skus.observe(viewLifecycleOwner) {
            initListView(it)
        }
    }

    private fun initListView(skuList: List<SkuWithConditionAndPrinting>) {
        binding.skuSelectionList.adapter = SkuAdapter(requireContext(), skuList)
    }

    inner class SkuAdapter(
        context: Context,
        userLists: List<SkuWithConditionAndPrinting>
    ) : ArrayAdapter<SkuWithConditionAndPrinting>(context, 0, userLists) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var curView = convertView

            if (curView == null) {
                val binding = ItemSimpleOneLineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                val skuWithConditionAndPrinting = getItem(position)

                with(binding) {
                    binding.simpleOneLineText.text = getString(R.string.edition_condition_oneline,
                        skuWithConditionAndPrinting?.printing?.name,
                        skuWithConditionAndPrinting?.condition?.name
                    )

                    root.setOnClickListener {
                        setFragmentResult(FRAGMENT_RESULT_REQUEST_KEY, bundleOf(SELECTED_SKU_WITH_CONDITION_AND_PRICING_DATA_KEY to skuWithConditionAndPrinting))

                        dismiss()
                    }
                }

                curView = binding.root
            }

            return curView
        }
    }
}
