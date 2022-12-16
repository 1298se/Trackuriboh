package sam.g.trackuriboh.ui.user_list.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.material.composethemeadapter.MdcTheme
import sam.g.trackuriboh.ui.common.SelectableLabelValueRow
import sam.g.trackuriboh.ui.user_list.viewmodels.AddToUserListFormViewModel
import sam.g.trackuriboh.utils.formatDate
import java.text.DateFormat
import java.util.*
import sam.g.trackuriboh.R
import sam.g.trackuriboh.ui.common.QuantitySelector


@ExperimentalMaterialApi
@Composable
fun AddTransactionForm(
    state: AddToUserListFormViewModel.AddToUserListFormState?,
    onSelectSkuButtonClick: () -> Unit,
    onSelectListButtonClick: () -> Unit,
    onQuantityChanged: (Int) -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
) {
    Column {
        OutlinedTextField(
            value = state?.formData?.name ?: "",
            onValueChange = { },
            label = { Text(text = "Name") }
        )

        SelectableLabelValueRow(
            label = "Transaction" ,
            value = state?.formData?.type?.toString()
        ) {

        }
        SelectableLabelValueRow(
            label = "Date",
            value = state?.formData?.date.formatDate(DateFormat.SHORT) ?: "Select a date"
        ) {

        }
        SelectableLabelValueRow(
            label = "List",
            value = state?.formData?.userList?.name
        ) {

        }

        val printing = state?.formData?.skuWithConditionAndPrinting?.printing
        val condition = state?.formData?.skuWithConditionAndPrinting?.condition

        SelectableLabelValueRow(
            label = "SKU",
            value = if (condition != null && printing != null) {
                stringResource(
                    id = R.string.edition_condition_oneline,
                    printing.name ?: "",
                    condition.name ?: "",
                )
            } else {
                stringResource(id = R.string.add_to_user_list_select_sku_hint)
            },
        ) {

        }

        OutlinedTextField(
            value = state?.formData?.price?.toString() ?: "",
            onValueChange = { },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            label = { Text(text = "Price") }
        )

        QuantitySelector(
            quantity = state?.formData?.quantity ?: -1,
            onQuantityChanged = onQuantityChanged,
        )
    }

}

@ExperimentalMaterialApi
@Preview
@Composable
private fun AddTransactionFormPreview() {
    val formState = AddToUserListFormViewModel.AddToUserListFormState(
        canSave = true,
        formData = AddToUserListFormViewModel.AddToUserListFormData(
            name = "Billy Bob",
            date = Calendar.getInstance().time,
            userList = null,
            skuWithConditionAndPrinting = null,

        ),
    )

    MdcTheme {
        AddTransactionForm(
            state = formState,
            onSelectSkuButtonClick = { },
            onSelectListButtonClick = { },
            onQuantityChanged = { },
            onSaveClick = { },
            onCancelClick = { },
        )
    }
}
