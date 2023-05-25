package sam.g.trackuriboh.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.composethemeadapter.MdcTheme
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import sam.g.trackuriboh.MainGraphDirections
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.entities.TransactionType
import sam.g.trackuriboh.databinding.FragmentAddTransactionBinding
import sam.g.trackuriboh.ui.card_selection.CardSelectionFragment
import sam.g.trackuriboh.ui.common.SimpleListBottomSheetDialogFragment
import sam.g.trackuriboh.ui.common.SingleChoiceDialog
import sam.g.trackuriboh.ui.transaction.components.AddTransactionFrom
import sam.g.trackuriboh.ui.transaction.viewmodels.AddTransactionFormViewModel
import sam.g.trackuriboh.utils.fromLocalToUTC
import sam.g.trackuriboh.utils.fromUTCToLocal
import sam.g.trackuriboh.utils.joinStringsWithInterpunct
import sam.g.trackuriboh.utils.viewBinding
import java.util.Date

@AndroidEntryPoint
class AddTransactionFragment : Fragment() {
    private val binding: FragmentAddTransactionBinding by viewBinding(FragmentAddTransactionBinding::inflate)

    private val viewModel: AddTransactionFormViewModel by viewModels()

    companion object {
        private const val SELECTED_SKU_INDEX_RESULT_REQUEST_KEY =
            "AddTransactionFragment_selectedSkuIndexResultRequestKey"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.addTransactionToolbar.setupWithNavController(findNavController())

        binding.addTransactionComposeContainer.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MdcTheme {
                    val formState by viewModel.formState.observeAsState()

                    formState?.let {
                        AddTransactionFrom(
                            state = it,
                            onTransactionTypeClick = ::showTransactionTypeSelectionDialog,
                            onDateClicked = ::showDateSelectionDialog,
                            onProductClicked = ::showCardSelectionFragment,
                            onSkuClicked = ::showSkuSelectionDialog,
                            onPriceChanged = viewModel::setPrice,
                            onQuantityChanged = viewModel::setQuantity,
                            onAddTransactionClick = ::addTransaction,
                        )
                    }
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFragmentResultListeners()
    }

    private fun initFragmentResultListeners() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Long>(
            CardSelectionFragment.CARD_ID_DATA_KEY
        )?.observe(viewLifecycleOwner) {
            viewModel.setProduct(it)
        }

        childFragmentManager.setFragmentResultListener(
            SELECTED_SKU_INDEX_RESULT_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val skusWithMetadata =
                viewModel.formState.value?.formData?.productWithCardSetAndSkuIds?.skusWithMetadata
            val index = bundle.getLong(SimpleListBottomSheetDialogFragment.SELECTED_INDEX_DATA_KEY)


            viewModel.setSku(skusWithMetadata?.get(index.toInt()))
        }

    }

    private fun showTransactionTypeSelectionDialog() {
        val transactionTypes = TransactionType.values()

        SingleChoiceDialog(
            requireContext(),
            getString(R.string.lbl_transaction),
            transactionTypes.map { it.getDisplayStringRes(requireContext()) },
            transactionTypes.indexOf(viewModel.formState.value?.formData?.type)
        ) { dialog, i ->
            viewModel.setTransactionType(TransactionType.values()[i])

            dialog.dismiss()
        }.show()
    }

    private fun showSkuSelectionDialog() {
        val skusWithMetadata =
            viewModel.formState.value?.formData?.productWithCardSetAndSkuIds?.skusWithMetadata

        SimpleListBottomSheetDialogFragment.newInstance(
            getString(R.string.add_to_user_list_select_sku_hint),
            skusWithMetadata?.map {
                joinStringsWithInterpunct(it.printing?.name, it.condition?.name)
            } ?: emptyList(),
            SELECTED_SKU_INDEX_RESULT_REQUEST_KEY
        ).show(childFragmentManager, null)
    }

    private fun showCardSelectionFragment() {
        findNavController().navigate(MainGraphDirections.actionGlobalCardSelectionFragment(true))
    }

    private fun showDateSelectionDialog() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(viewModel.formState.value?.formData?.date?.fromLocalToUTC()?.time)
            .build()

        datePicker.addOnPositiveButtonClickListener {
            datePicker.selection?.let {
                // The given date is in UTC, we want to change it to the same date but in local time.
                viewModel.setDate(Date(it).fromUTCToLocal())
            }
        }
        datePicker.show(childFragmentManager, null)
    }

    private fun addTransaction() {
        lifecycleScope.launch {
            viewModel.addTransaction()

            findNavController().navigateUp()
        }
    }
}
