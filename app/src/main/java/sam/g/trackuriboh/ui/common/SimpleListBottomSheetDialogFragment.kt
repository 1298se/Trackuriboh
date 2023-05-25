package sam.g.trackuriboh.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.databinding.BottomSheetSimpleListBinding
import sam.g.trackuriboh.utils.setDefaultExpanded
import sam.g.trackuriboh.utils.viewBinding

@AndroidEntryPoint
class SimpleListBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private val binding: BottomSheetSimpleListBinding by viewBinding(BottomSheetSimpleListBinding::inflate)

    companion object {
        const val SELECTED_INDEX_DATA_KEY =
            "SimpleListBottomSheetDialogFragment_selectedSkuWithConditionAndPricing"

        const val ARG_TITLE = "SimpleListBottomSheetDialogFragment_title"
        const val ARG_ITEMS = "SimpleListBottomSheetDialogFragment_items"
        const val ARG_RESULT_REQUEST_KEY =
            "SimpleListBottomSheetDialogFragment_argFragmentResultRequestKey"

        fun newInstance(title: String?, items: List<String>, resultRequestKey: String) =
            SimpleListBottomSheetDialogFragment().apply {
                arguments = bundleOf(
                    ARG_TITLE to title,
                    ARG_ITEMS to items.toTypedArray(),
                    ARG_RESULT_REQUEST_KEY to resultRequestKey,
                )
            }
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

        setDefaultExpanded()

        binding.simpleListBottomSheetToolbar.title = arguments?.getString(ARG_TITLE)

        binding.simpleListBottomSheetList.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            arguments?.getStringArray(ARG_ITEMS) ?: emptyArray()
        )
        binding.simpleListBottomSheetList.setOnItemClickListener { _, _, _, id ->
            parentFragmentManager.setFragmentResult(
                arguments?.getString(ARG_RESULT_REQUEST_KEY) ?: "",
                bundleOf(SELECTED_INDEX_DATA_KEY to id)
            )

            dismiss()
        }
    }
}
