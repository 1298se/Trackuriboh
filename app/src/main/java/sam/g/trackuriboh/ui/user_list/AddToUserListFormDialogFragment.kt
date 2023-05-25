package sam.g.trackuriboh.ui.user_list

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
import com.google.android.material.composethemeadapter.MdcTheme
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.entities.UserList
import sam.g.trackuriboh.databinding.DialogAddToUserListFormBinding
import sam.g.trackuriboh.ui.common.SimpleListBottomSheetDialogFragment
import sam.g.trackuriboh.ui.user_list.components.AddToUserListForm
import sam.g.trackuriboh.ui.user_list.viewmodels.AddToUserListFormViewModel
import sam.g.trackuriboh.utils.joinStringsWithInterpunct
import sam.g.trackuriboh.utils.viewBinding

@AndroidEntryPoint
class AddToUserListFormDialogFragment : DialogFragment() {
    private val binding: DialogAddToUserListFormBinding by viewBinding(
        DialogAddToUserListFormBinding::inflate
    )

    private val viewModel: AddToUserListFormViewModel by viewModels()

    companion object {
        const val FRAGMENT_RESULT_REQUEST_KEY =
            "AddToUserListDialogFragment_fragmentResultRequestKey"
        const val ADDED_USER_LIST_NAME_DATA_KEY = "AddToUserListDialogFragment_addedUserListName"

        const val ARG_PRODUCT_ID = "AddToUserListDialogFragment_argProductId"
        const val ARG_USER_LIST = "AddToUserListDialogFragment_argUserList"

        private const val SELECTED_SKU_INDEX_RESULT_REQUEST_KEY =
            "AddToUserListDialogFragment_selectedSkuIndexResultRequestKey"
        private const val SELECTED_USER_LIST_INDEX_RESULT_REQUEST_KEY =
            "AddToUserListDialogFragment_userListIndexResultRequestKey"

        fun newInstance(productId: Long, userList: UserList? = null) =
            AddToUserListFormDialogFragment().apply {
                arguments = bundleOf(ARG_PRODUCT_ID to productId, ARG_USER_LIST to userList)
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding.addToUserListComposeContainer.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                MdcTheme {
                    val formState by viewModel.formState.observeAsState()

                    formState?.let {
                        AddToUserListForm(
                            state = it,
                            onSelectSkuButtonClick = ::showSkuSelectionBottomSheet,
                            onSelectListButtonClick = ::showListSelectionBottomSheet,
                            onQuantityChanged = viewModel::setQuantity,
                            onSaveClick = ::saveEntry,
                            onCancelClick = ::dismiss,
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
        childFragmentManager.setFragmentResultListener(
            SELECTED_SKU_INDEX_RESULT_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val index = bundle.getLong(SimpleListBottomSheetDialogFragment.SELECTED_INDEX_DATA_KEY)

            viewModel.setSku(viewModel.productWithCardSetAndSkuIds.skusWithMetadata.getOrNull(index.toInt()))
        }

        childFragmentManager.setFragmentResultListener(
            SELECTED_USER_LIST_INDEX_RESULT_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val index = bundle.getLong(SimpleListBottomSheetDialogFragment.SELECTED_INDEX_DATA_KEY)

            viewModel.setUserList(viewModel.userLists.getOrNull(index.toInt()))
        }
    }

    private fun saveEntry() {
        viewModel.addEntryToUserList().observe(viewLifecycleOwner) {
            setFragmentResult(FRAGMENT_RESULT_REQUEST_KEY, it)

            dismiss()
        }
    }

    private fun showSkuSelectionBottomSheet() {
        SimpleListBottomSheetDialogFragment.newInstance(
            getString(R.string.add_to_user_list_select_sku_hint),
            viewModel.productWithCardSetAndSkuIds.skusWithMetadata.map {
                joinStringsWithInterpunct(it.printing?.name, it.condition?.name)
            },
            SELECTED_SKU_INDEX_RESULT_REQUEST_KEY
        ).show(childFragmentManager, null)
    }

    private fun showListSelectionBottomSheet() {
        SimpleListBottomSheetDialogFragment.newInstance(
            getString(R.string.add_to_user_list_select_list_hint),
            viewModel.userLists.map {
                it.name
            },
            SELECTED_USER_LIST_INDEX_RESULT_REQUEST_KEY
        ).show(childFragmentManager, null)
    }
}