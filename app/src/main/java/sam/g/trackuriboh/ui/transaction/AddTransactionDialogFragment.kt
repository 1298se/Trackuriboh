package sam.g.trackuriboh.ui.transaction

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.google.android.material.composethemeadapter.MdcTheme
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.data.db.entities.Product
import sam.g.trackuriboh.data.db.entities.UserList
import sam.g.trackuriboh.data.db.entities.UserListEntry
import sam.g.trackuriboh.data.db.relations.SkuWithConditionAndPrinting
import sam.g.trackuriboh.databinding.DialogAddToUserListBinding
import sam.g.trackuriboh.databinding.DialogAddTransactionBinding
import sam.g.trackuriboh.ui.card_detail.SkuSelectionBottomSheetFragment
import sam.g.trackuriboh.ui.transaction.components.AddTransactionFrom
import sam.g.trackuriboh.ui.transaction.viewmodels.AddTransactionFormViewModel
import sam.g.trackuriboh.ui.user_list.AddToUserListDialogFragment
import sam.g.trackuriboh.ui.user_list.UserListSelectionBottomSheetFragment
import sam.g.trackuriboh.ui.user_list.components.AddToUserListForm
import sam.g.trackuriboh.ui.user_list.viewmodels.AddToUserListFormViewModel
import sam.g.trackuriboh.utils.viewBinding
import java.util.Calendar
import java.util.Date
import kotlin.properties.Delegates

@ExperimentalMaterialApi
@AndroidEntryPoint
class AddTransactionDialogFragment : DialogFragment() {
    private val binding: DialogAddTransactionBinding by viewBinding(DialogAddTransactionBinding::inflate)

    private val viewModel: AddTransactionFormViewModel by viewModels()

    private lateinit var product: Product
    private lateinit var userListEntry: UserListEntry

    companion object {
        const val FRAGMENT_RESULT_REQUEST_KEY = "AddTransactionDialogFragment_fragmentResultRequestKey"
        const val ADDED_TRANSACTION_NAME_DATA_KEY = "AddTransactionDialogFragment_addedTransaction"

        const val ARG_USER_LIST_ENTRY = "AddTransactionDialogFragment_argUserListEntry"
        const val ARG_PRODUCT = "AddTransactionDialogFragment_argProduct"

        fun newInstance(product: Product, userListEntry: UserListEntry) =
            AddTransactionDialogFragment().apply {
                arguments = bundleOf(
                    ARG_USER_LIST_ENTRY to userListEntry,
                    ARG_PRODUCT to product,
                )
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            product = it.getParcelable(ARG_PRODUCT, Product::class.java)!!
            userListEntry = it.getParcelable(ARG_USER_LIST_ENTRY, UserListEntry::class.java)!!
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFragmentResultListeners()
    }

    private fun addTransaction() {
        viewModel.addEntryToTransactionList(product, userListEntry).observe(viewLifecycleOwner) {
            setFragmentResult(FRAGMENT_RESULT_REQUEST_KEY, it)
            dismiss()
        }
        dismiss()
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

    private fun initFragmentResultListeners() {
        // Here we can use viewLifecycleOwner because we are not using onCreateDialog
//        childFragmentManager.setFragmentResultListener(
//            SkuSelectionBottomSheetFragment.FRAGMENT_RESULT_REQUEST_KEY,
//            viewLifecycleOwner
//        ) { _, bundle ->
//            val selectedSku = bundle.getParcelable<SkuWithConditionAndPrinting>(
//                SkuSelectionBottomSheetFragment.SELECTED_SKU_WITH_CONDITION_AND_PRICING_DATA_KEY
//            )
//
//            viewModel.onSkuChanged(selectedSku)
//
//        }

//        childFragmentManager.setFragmentResultListener(
//            UserListSelectionBottomSheetFragment.FRAGMENT_RESULT_REQUEST_KEY,
//            viewLifecycleOwner
//        ) { _, bundle ->
//            val selectedList = bundle.getParcelable<UserList>(UserListSelectionBottomSheetFragment.SELECTED_USER_LIST_DATA_KEY)
//
//            viewModel.onUserListChanged(selectedList)
//        }
    }
}