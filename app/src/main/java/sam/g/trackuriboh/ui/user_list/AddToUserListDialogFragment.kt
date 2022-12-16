package sam.g.trackuriboh.ui.user_list

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
import sam.g.trackuriboh.data.db.entities.UserList
import sam.g.trackuriboh.data.db.relations.SkuWithConditionAndPrinting
import sam.g.trackuriboh.databinding.DialogAddToUserListBinding
import sam.g.trackuriboh.ui.card_detail.SkuSelectionBottomSheetFragment
import sam.g.trackuriboh.ui.user_list.components.AddTransactionForm
import sam.g.trackuriboh.ui.user_list.viewmodels.AddToUserListFormViewModel
import sam.g.trackuriboh.utils.viewBinding
import kotlin.properties.Delegates

@ExperimentalMaterialApi
@AndroidEntryPoint
class AddToUserListDialogFragment : DialogFragment() {
    private val binding: DialogAddToUserListBinding by viewBinding(DialogAddToUserListBinding::inflate)

    private val viewModel: AddToUserListFormViewModel by viewModels()

    private var cardId by Delegates.notNull<Long>()

    private var userList: UserList? = null

    companion object {
        const val FRAGMENT_RESULT_REQUEST_KEY = "AddToUserListDialogFragment_fragmentResultRequestKey"
        const val ADDED_USER_LIST_NAME_DATA_KEY = "AddToUserListDialogFragment_addedUserListName"

        const val ARG_USER_LIST = "AddToUserListDialogFragment_argUserList"
        private const val ARG_CARD_ID = "AddToUserListDialogFragment_argCardId"

        fun newInstance(cardId: Long, userList: UserList? = null) =
            AddToUserListDialogFragment().apply {
                arguments = bundleOf(ARG_CARD_ID to cardId, ARG_USER_LIST to userList)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            cardId = it.getLong(ARG_CARD_ID)
            userList = it.getParcelable(ARG_USER_LIST)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding.addToUserListComposeContainer.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                MdcTheme {

                    val formState by viewModel.formState.observeAsState()

                    AddTransactionForm(
                        state = formState,
                        onSelectSkuButtonClick = ::showSkuSelectionBottomSheet,
                        onSelectListButtonClick = ::showListSelectionBottomSheet,
                        onQuantityChanged = viewModel::onQuantityChanged,
                        onSaveClick = ::saveEntry,
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

    private fun saveEntry() {
        viewModel.addEntryToUserList().observe(viewLifecycleOwner) {
            setFragmentResult(FRAGMENT_RESULT_REQUEST_KEY, it)

            dismiss()
        }
    }

    private fun showListSelectionBottomSheet() {
        UserListSelectionBottomSheetFragment().show(childFragmentManager, null)
    }

    private fun showSkuSelectionBottomSheet() {
        SkuSelectionBottomSheetFragment.newInstance(cardId).show(childFragmentManager, null)
    }

    private fun initFragmentResultListeners() {
        // Here we can use viewLifecycleOwner because we are not using onCreateDialog
        childFragmentManager.setFragmentResultListener(
            SkuSelectionBottomSheetFragment.FRAGMENT_RESULT_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val selectedSku = bundle.getParcelable<SkuWithConditionAndPrinting>(
                SkuSelectionBottomSheetFragment.SELECTED_SKU_WITH_CONDITION_AND_PRICING_DATA_KEY
            )

            viewModel.onSkuChanged(selectedSku)

        }

        childFragmentManager.setFragmentResultListener(
            UserListSelectionBottomSheetFragment.FRAGMENT_RESULT_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val selectedList = bundle.getParcelable<UserList>(UserListSelectionBottomSheetFragment.SELECTED_USER_LIST_DATA_KEY)

            viewModel.onUserListChanged(selectedList)
        }
    }
}