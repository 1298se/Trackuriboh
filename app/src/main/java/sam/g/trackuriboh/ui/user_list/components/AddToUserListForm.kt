package sam.g.trackuriboh.ui.user_list.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import sam.g.trackuriboh.R
import sam.g.trackuriboh.ui.common.AppThemeDialogButtons
import sam.g.trackuriboh.ui.common.AppThemeOutlinedTextButton
import sam.g.trackuriboh.ui.common.QuantitySelector
import sam.g.trackuriboh.ui.user_list.viewmodels.AddToUserListFormViewModel


@ExperimentalMaterialApi
@Composable
fun AddToUserListForm(
    state: AddToUserListFormViewModel.AddToUserListFormState?,
    onSelectSkuButtonClick: () -> Unit,
    onSelectListButtonClick: () -> Unit,
    onQuantityChanged: (Int) -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
) {
    // Since the text buttons already have some margin at the top and bottom (???), we use paddingSmall at the bottom
    Column(
        modifier = Modifier.padding(
            start = dimensionResource(id = R.dimen.material_border_padding_small),
            end = dimensionResource(id = R.dimen.material_border_padding_small),
            top = dimensionResource(id = R.dimen.material_border_padding),
            bottom = dimensionResource(id = R.dimen.material_border_padding_small)
        )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.form_row_spacing))
        ) {

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(id = R.string.add_to_user_list_action_title),
                style = MaterialTheme.typography.h6
            )

            val userList = state?.formData?.userList

            AppThemeOutlinedTextButton(
                text = userList?.name ?: stringResource(id = R.string.add_to_user_list_select_list_hint),
                onButtonClick = onSelectListButtonClick,
            )

            val condition = state?.formData?.skuWithConditionAndPrinting?.condition
            val printing = state?.formData?.skuWithConditionAndPrinting?.printing

            AppThemeOutlinedTextButton(
                text = if (condition != null && printing != null) {
                    stringResource(
                        id = R.string.edition_condition_oneline,
                        printing.name ?: "",
                        condition.name ?: "",
                    )
                } else {
                    stringResource(id = R.string.add_to_user_list_select_sku_hint)
                },
                onButtonClick = onSelectSkuButtonClick,
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
                    quantity = state?.formData?.quantity ?: -1,
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onQuantityChanged = onQuantityChanged,
                )
            }
        }

        AppThemeDialogButtons(
            positiveButtonEnabled = state?.canSave ?: false,
            negativeButtonEnabled = true,
            onPositiveButtonClick = onSaveClick,
            onNegativeButtonClick = onCancelClick,
            positiveButtonText = stringResource(R.string.lbl_save).uppercase(),
            negativeButtonText = stringResource(id = R.string.lbl_cancel).uppercase(),
        )
    }
}

