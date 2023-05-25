package sam.g.trackuriboh.ui.user_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.material.composethemeadapter.MdcTheme
import sam.g.trackuriboh.R
import sam.g.trackuriboh.ui.common.AppThemeOutlinedTextButton
import sam.g.trackuriboh.ui.common.QuantitySelector
import sam.g.trackuriboh.ui.common.SimpleDialogForm
import sam.g.trackuriboh.ui.user_list.viewmodels.AddToUserListFormViewModel
import sam.g.trackuriboh.utils.joinStringsWithInterpunct


@Composable
fun AddToUserListForm(
    state: AddToUserListFormViewModel.AddToUserListFormState,
    onSelectSkuButtonClick: () -> Unit,
    onSelectListButtonClick: () -> Unit,
    onQuantityChanged: (Int) -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
) {
    // Since the text buttons already have some margin at the top and bottom (???), we use paddingSmall at the bottom
    SimpleDialogForm(
        title = stringResource(id = R.string.add_to_user_list_action_title),
        onPositiveButtonClick = onSaveClick,
        onNegativeButtonClick = onCancelClick,
        positiveButtonEnabled = state.canSave,
        positiveButtonText = stringResource(R.string.lbl_save).uppercase(),
        negativeButtonText = stringResource(id = R.string.lbl_cancel).uppercase(),
    ) {
        val condition = state.formData.skuWithMetadata?.condition
        val printing = state.formData.skuWithMetadata?.printing

        AppThemeOutlinedTextButton(
            text = if (condition != null && printing != null) {
                joinStringsWithInterpunct(
                    printing.name,
                    condition.name,
                )
            } else {
                stringResource(id = R.string.add_to_user_list_select_sku_hint)
            },
            onButtonClick = {
                if (state.isInitialized) {
                    onSelectSkuButtonClick()
                }
            },
        )

        val userList = state.formData.userList

        AppThemeOutlinedTextButton(
            text = userList?.name
                ?: stringResource(id = R.string.add_to_user_list_select_list_hint),
            onButtonClick = {
                if (state.isInitialized) {
                    onSelectListButtonClick()
                }
            },
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {

            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = "Quantity",
                style = MaterialTheme.typography.body1
            )

            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.text_spacing_large)))
            QuantitySelector(
                quantity = state.formData.quantity,
                modifier = Modifier.align(Alignment.CenterVertically),
                onQuantityChanged = onQuantityChanged,
                dense = true,
            )
        }
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun AddToUserListFormPreview() {
    val formState = AddToUserListFormViewModel.AddToUserListFormState(
        canSave = true,
        formData = AddToUserListFormViewModel.AddToUserListFormData(),
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
