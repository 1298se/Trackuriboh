package sam.g.trackuriboh.ui.transaction.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.material.composethemeadapter.MdcTheme
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.entities.TransactionType
import sam.g.trackuriboh.ui.common.AppThemeDenseOutlinedTextField
import sam.g.trackuriboh.ui.common.AppThemeOutlinedTextButton
import sam.g.trackuriboh.ui.common.AppThemeSegmentedButton
import sam.g.trackuriboh.ui.common.QuantitySelector
import sam.g.trackuriboh.ui.common.SimpleDialogForm
import sam.g.trackuriboh.ui.transaction.viewmodels.AddTransactionFormViewModel
import sam.g.trackuriboh.utils.formatDate
import java.text.DateFormat
import java.util.Date


@Composable
fun AddTransactionFrom(
    state: AddTransactionFormViewModel.AddTransactionFormState?,
    onTypeChanged: (TransactionType) -> Unit,
    onPriceChanged: (Double) -> Unit,
    onSelectDateClicked: (Date?) -> Unit,
    onQuantityChanged: (Int) -> Unit,
    onAddClick: () -> Unit,
    onCancelClick: () -> Unit,
) {
    // Since the text buttons already have some margin at the top and bottom (???), we use paddingSmall at the bottom
    SimpleDialogForm(
        title = stringResource(id = R.string.add_to_user_list_action_title),
        onPositiveButtonClick = onAddClick,
        onNegativeButtonClick = onCancelClick,
        positiveButtonEnabled = state?.canSave ?: false,
        positiveButtonText = stringResource(R.string.lbl_save).uppercase(),
        negativeButtonText = stringResource(id = R.string.lbl_cancel).uppercase(),
    ) {

        AppThemeSegmentedButton(
            option1Text = "Purchase",
            option2Text = "Sale",
            onOptionSelected = {
                val type = when(it) {
                    1 -> TransactionType.SALE
                    else -> TransactionType.PURCHASE
                }
                onTypeChanged(type)
            }
        )

        val date = state?.formData?.date ?: Date()
        val dateString = date.formatDate(DateFormat.MEDIUM) ?: ""

        AppThemeOutlinedTextButton(
            text = dateString,
            onButtonClick = { onSelectDateClicked(date) },
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {

            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = "Price",
                style = MaterialTheme.typography.body1
            )

            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.text_spacing_large)))
            AppThemeDenseOutlinedTextField(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = state?.formData?.price?.toString(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                hintText = "0.00",
                onValueChange = { onPriceChanged(it.toDoubleOrNull() ?: 0.0) }
            )
        }

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
}

@Preview
@Composable
private fun AddTransactionFormPreview() {
    val formState = AddTransactionFormViewModel.AddTransactionFormState(
        canSave = true,
        formData = AddTransactionFormViewModel.AddTransactionFormData(),
    )

    MdcTheme {
        AddTransactionFrom(
            state = formState,
            onTypeChanged = { },
            onPriceChanged = { },
            onSelectDateClicked = { },
            onQuantityChanged = { },
            onAddClick = { },
            onCancelClick = { },
        )
    }
}
