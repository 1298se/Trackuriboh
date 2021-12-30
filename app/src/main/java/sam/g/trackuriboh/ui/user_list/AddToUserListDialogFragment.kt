package sam.g.trackuriboh.ui.user_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.composethemeadapter.MdcTheme
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.data.db.entities.UserList
import sam.g.trackuriboh.data.db.relations.SkuWithConditionAndPrinting
import sam.g.trackuriboh.databinding.DialogAddToUserListBinding
import sam.g.trackuriboh.ui.card_detail.SkuSelectionBottomSheetFragment
import sam.g.trackuriboh.ui.user_list.components.AddToUserListForm
import sam.g.trackuriboh.ui.user_list.viewmodels.AddToUserListFormViewModel
import sam.g.trackuriboh.utils.safeNavigate
import sam.g.trackuriboh.utils.viewBinding

@ExperimentalMaterialApi
@AndroidEntryPoint
class AddToUserListDialogFragment : DialogFragment() {
    private val binding: DialogAddToUserListBinding by viewBinding(DialogAddToUserListBinding::inflate)

    private val viewModel: AddToUserListFormViewModel by viewModels()

    private val args: AddToUserListDialogFragmentArgs by navArgs()

    companion object {
        const val FRAGMENT_RESULT_REQUEST_KEY = "AddToUserListDialogFragment_fragmentResultRequestKey"
        const val ADDED_USER_LIST_NAME_DATA_KEY = "AddToUserListDialogFragment_addedUserListName"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding.addToUserListComposeContainer.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MdcTheme {

                    val formState by viewModel.formState.observeAsState()

                    AddToUserListForm(
                        state = formState,
                        onSelectSkuButtonClick = ::showSkuSelectionBottomSheet,
                        onSelectListButtonClick = ::showListSelectionBottomSheet,
                        onQuantityChanged = viewModel::onQuantityChanged,
                        onSaveClick = viewModel::addEntryToUserList,
                        onCancelClick = findNavController()::popBackStack,
                    )
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initFragmentResultListeners()
    }

    private fun showListSelectionBottomSheet() {
        findNavController().safeNavigate(
            AddToUserListDialogFragmentDirections
                .actionAddToUserListBottomSheetFragmentToUserListSelectionBottomSheetFragment()
        )
    }

    private fun showSkuSelectionBottomSheet() {
        findNavController().safeNavigate(
            AddToUserListDialogFragmentDirections
                .actionAddToUserListBottomSheetFragmentToSkuSelectionBottomSheetFragment(args.cardId)
        )
    }

    private fun initObservers() {
        viewModel.formState.observe(viewLifecycleOwner) {
            if (it.entryAddCompleted) {
                setFragmentResult(
                    FRAGMENT_RESULT_REQUEST_KEY,
                    bundleOf(ADDED_USER_LIST_NAME_DATA_KEY to it.formData.userList?.name)
                )
                findNavController().popBackStack()
            }
        }
    }

    private fun initFragmentResultListeners() {

        // Here we can use viewLifecycleOwner because we are not using onCreateDialog
        parentFragmentManager.setFragmentResultListener(
            SkuSelectionBottomSheetFragment.FRAGMENT_RESULT_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val selectedSku = bundle.getParcelable<SkuWithConditionAndPrinting>(
                SkuSelectionBottomSheetFragment.SELECTED_SKU_WITH_CONDITION_AND_PRICING_DATA_KEY
            )

            viewModel.onSkuChanged(selectedSku)

        }

        parentFragmentManager.setFragmentResultListener(
            UserListSelectionBottomSheetFragment.FRAGMENT_RESULT_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val selectedList = bundle.getParcelable<UserList>(UserListSelectionBottomSheetFragment.SELECTED_USER_LIST_DATA_KEY)

            viewModel.onUserListChanged(selectedList)
        }
    }

    @Preview
    @Composable
    private fun Preview() {
        val formState = AddToUserListFormViewModel.AddToUserListFormState(
            canSave = true,
            formData = AddToUserListFormViewModel.AddToUserListFormData(
                null,
                null,
                1
            ),
        )

        MdcTheme {
            AddToUserListForm(
                state = formState,
                onSelectSkuButtonClick = { },
                onSelectListButtonClick = { },
                onQuantityChanged = { },
                onSaveClick = { },
                onCancelClick = { },
            )
        }
    }
}