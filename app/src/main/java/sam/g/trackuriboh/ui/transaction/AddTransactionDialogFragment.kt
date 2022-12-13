package sam.g.trackuriboh.ui.transaction

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.composethemeadapter.MdcTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import sam.g.trackuriboh.databinding.DialogAddTransactionBinding
import sam.g.trackuriboh.ui.transaction.components.AddTransactionFrom
import sam.g.trackuriboh.ui.transaction.viewmodels.AddTransactionFormViewModel
import sam.g.trackuriboh.utils.viewBinding
import java.util.*

@AndroidEntryPoint
class AddTransactionDialogFragment : DialogFragment() {
    private val binding: DialogAddTransactionBinding by viewBinding(DialogAddTransactionBinding::inflate)

    private val viewModel: AddTransactionFormViewModel by viewModels()

    companion object {
        const val FRAGMENT_RESULT_REQUEST_KEY = "AddTransactionDialogFragment_fragmentResultRequestKey"

        const val ARG_LIST_ID = "AddTransactionDialogFragment_argListId"
        const val ARG_SKU_ID = "AddTransactionDialogFragment_argSkuId"

        fun newInstance(listId: Long, skuId: Long) =
            AddTransactionDialogFragment().apply {
                arguments = bundleOf(
                    ARG_LIST_ID to listId,
                    ARG_SKU_ID to skuId
                )
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding.addTransactionComposeContainer.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                MdcTheme {
                    val formState by viewModel.formState.observeAsState()

                    AddTransactionFrom(
                        state = formState,
                        onTypeChanged = viewModel::onTypeChanged,
                        onPriceChanged = viewModel::onPriceChanged,
                        onSelectDateClicked = ::showDateSelectionDialog,
                        onQuantityChanged = viewModel::onQuantityChanged,
                        onAddClick = ::addTransaction,
                        onCancelClick = ::dismiss,
                    )
                }
            }
        }

        return binding.root
    }

    private fun addTransaction() {
        lifecycleScope.launch {
            viewModel.addEntryToTransactionList()

            dismiss()
        }
    }

    private fun showDateSelectionDialog(date: Date?) {
        val cal = Calendar.getInstance()
        date?.let {
            cal.time = it
        }

        DatePickerDialog(requireContext(), 0, { view, year, month, dayOfMonth ->
            val newCal = Calendar.getInstance().apply { this.set(year, month, dayOfMonth) }
            viewModel.onDateChanged(newCal.time)
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
    }
}