package sam.g.trackuriboh.ui.transaction.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.material.composethemeadapter.MdcTheme
import sam.g.trackuriboh.R
import sam.g.trackuriboh.ui.common.QuantitySelector
import sam.g.trackuriboh.ui.common.SelectableLabelValueRow
import sam.g.trackuriboh.ui.transaction.viewmodels.AddTransactionFormViewModel
import sam.g.trackuriboh.utils.formatDate
import sam.g.trackuriboh.utils.joinStringsWithInterpunct
import java.text.DateFormat


@Composable
fun AddTransactionFrom(
    state: AddTransactionFormViewModel.AddTransactionFormState,
    onTransactionTypeClick: () -> Unit,
    onDateClicked: () -> Unit,
    onProductClicked: () -> Unit,
    onSkuClicked: () -> Unit,
    onPriceChanged: (String) -> Unit,
    onQuantityChanged: (Int) -> Unit,
    onAddTransactionClick: () -> Unit,
) {
    Column {
        SelectableLabelValueRow(
            label = stringResource(id = R.string.lbl_transaction),
            value = state.formData.type.getDisplayStringResId(LocalContext.current)
        ) {
            onTransactionTypeClick()
        }

        SelectableLabelValueRow(
            label = stringResource(id = R.string.lbl_date),
            value = state.formData.date.formatDate(DateFormat.SHORT)
                ?: stringResource(id = R.string.add_transaction_select_date_hint)
        ) {
            onDateClicked()
        }

        val product = state.formData.productWithCardSetAndSkuIds?.product
        SelectableLabelValueRow(
            label = stringResource(id = R.string.lbl_product),
            value = if (product == null) {
                stringResource(id = R.string.add_transaction_select_card_hint)
            } else {
                joinStringsWithInterpunct(
                    product.name,
                    product.number ?: ""
                )
            }
        ) {
            onProductClicked()
        }

        val printing = state.formData.skuWithMetadata?.printing
        val condition = state.formData.skuWithMetadata?.condition
        AnimatedVisibility(visible = product != null) {
            SelectableLabelValueRow(
                label = stringResource(id = R.string.lbl_sku),
                value = if (condition != null && printing != null) {
                    joinStringsWithInterpunct(
                        printing.name ?: "",
                        condition.name ?: "",
                    )
                } else {
                    stringResource(id = R.string.add_transaction_select_sku_hint)
                },
            ) {
                onSkuClicked()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.material_border_padding)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.form_row_spacing))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(0.4f),
                    value = state.formData.price ?: "",
                    onValueChange = { onPriceChanged(it) },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    label = { Text(text = stringResource(id = R.string.lbl_price)) }
                )

                QuantitySelector(
                    modifier = Modifier.align(Alignment.Bottom),
                    quantity = state.formData.quantity,
                    onQuantityChanged = onQuantityChanged,
                )
            }

            Button(
                onClick = onAddTransactionClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = state.canSave
            ) {
                Text(
                    text = stringResource(id = R.string.add_transaction),
                    style = MaterialTheme.typography.button,
                )
            }
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
            { },
            { },
            { },
            { },
            { },
            { },
            { },
        )
    }
}
