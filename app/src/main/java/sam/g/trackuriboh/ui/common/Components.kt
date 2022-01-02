package sam.g.trackuriboh.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.material.composethemeadapter.MdcTheme
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.types.StringResourceEnum

@Composable
fun SimpleDialogForm(
    title: String? = null,
    onPositiveButtonClick: () -> Unit,
    onNegativeButtonClick: () -> Unit,
    positiveButtonEnabled: Boolean = true,
    negativeButtonEnabled: Boolean = true,
    positiveButtonText: String = stringResource(R.string.lbl_ok).uppercase(),
    negativeButtonText: String = stringResource(id = R.string.lbl_cancel).uppercase(),
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier.padding(
            start = dimensionResource(id = R.dimen.material_border_padding),
            end = dimensionResource(id = R.dimen.material_border_padding),
            top = dimensionResource(id = R.dimen.material_border_padding),
            bottom = dimensionResource(id = R.dimen.material_border_padding_small)
        ),
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.form_row_spacing))
        ) {
            if (title != null) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h6
                )
            }

            content()
        }

        AppThemeDialogButtons(
            positiveButtonEnabled = positiveButtonEnabled,
            negativeButtonEnabled = negativeButtonEnabled,
            onPositiveButtonClick = onPositiveButtonClick,
            onNegativeButtonClick = onNegativeButtonClick,
            positiveButtonText = positiveButtonText,
            negativeButtonText = negativeButtonText,
        )
    }
}


/**
 * Why is it so hard to just add a trailing icon...
 */
@Composable
fun AppThemeDenseOutlinedTextField(
    modifier: Modifier = Modifier,
    text: String? = null,
    readOnly: Boolean = false,
    onValueChange: (String) -> Unit,
    borderColor: Color = Color.LightGray,
    hintText: String? = null,
    trailingIcon: @Composable () -> Unit = { },
) {
    var curHintText by rememberSaveable { mutableStateOf(hintText) }

    AppThemeOutlinedDenseRow(
        modifier = modifier,
        borderColor = borderColor
    ) {
        BasicTextField(
            value = if (!text.isNullOrEmpty()) text else curHintText ?: "",
            textStyle = MaterialTheme.typography.subtitle1.copy(color = MaterialTheme.colors.onSurface),
            modifier = Modifier
                .weight(1f)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        curHintText = null
                    } else if (text.isNullOrEmpty()) {
                        curHintText = hintText
                    }
                },
            onValueChange = onValueChange,
            readOnly = readOnly,
        )
        trailingIcon()
    }
}

@Composable
private fun AppThemeOutlinedDenseRow(
    modifier: Modifier = Modifier,
    borderColor: Color = Color.LightGray,
    onClick: (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit
) {

    Row(
        modifier = modifier
            .height(dimensionResource(id = R.dimen.text_field_dense_height))
            .clip(MaterialTheme.shapes.small)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(color = MaterialTheme.colors.primary),
                        onClick = onClick
                    )
                } else {
                    Modifier
                }
            )
            .border(BorderStroke(2.dp, borderColor))
            .padding(
                top = dimensionResource(id = R.dimen.text_field_dense_paddingTop),
                bottom = dimensionResource(id = R.dimen.text_field_dense_paddingBottom),
                start = dimensionResource(id = R.dimen.text_field_dense_paddingStart),
                end = dimensionResource(id = R.dimen.text_field_dense_paddingEnd)
            )
    ) {
        content()
    }
}

/**
 * For some reason the [TextButton] has some margins? Just make our own...
 */
@Composable
fun AppThemeOutlinedTextButton(
    modifier: Modifier = Modifier,
    text: String = "",
    borderColor: Color = Color.LightGray,
    onButtonClick: () -> Unit,
    trailingIcon: @Composable () -> Unit = { },
) {
    AppThemeOutlinedDenseRow(
        modifier = modifier,
        borderColor = borderColor,
        onClick = onButtonClick
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.subtitle1.copy(color = MaterialTheme.colors.onSurface),
            modifier = Modifier.weight(1f)
        )
        trailingIcon()
    }
}

@ExperimentalMaterialApi
@Composable
fun AppThemeDenseOutlinedAutoCompleteTextField(
    options: List<String>,
    modifier: Modifier = Modifier,
    selectedOption: String? = null,
    onOptionSelected: (Int) -> Unit = { },
    hintText: String? = null
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier.height(dimensionResource(id = R.dimen.text_field_dense_height)),
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        AppThemeOutlinedTextButton(
            onButtonClick = { },
            text = selectedOption ?: (hintText ?: ""),
            trailingIcon = {
                Icon(
                    Icons.Filled.ArrowDropDown,
                    "Trailing icon for exposed dropdown menu",
                    Modifier.rotate(if (expanded) 180f else 360f)
                )
            }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(
                    onClick = {
                        onOptionSelected(index)
                        expanded = false
                    }
                ) {
                    Text(text = option)
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun AppThemeDenseOutlinedEnumAutoCompleteTextField(
    options: List<StringResourceEnum>,
    modifier: Modifier = Modifier,
    hintText: String? = null,
    selectedOption: StringResourceEnum? = null,
    onOptionSelected: (Int) -> Unit = { },
) {
    AppThemeDenseOutlinedAutoCompleteTextField(
        modifier = modifier,
        options = options.map { stringResource(id = it.resourceId) },
        selectedOption = selectedOption?.resourceId?.let { stringResource(id = it) },
        onOptionSelected = onOptionSelected,
        hintText = hintText,
    )
}

@Composable
fun ColumnScope.AppThemeDialogButtons(
    positiveButtonText: String,
    negativeButtonText: String,
    onPositiveButtonClick: () -> Unit,
    onNegativeButtonClick: () -> Unit,
    positiveButtonEnabled: Boolean = true,
    negativeButtonEnabled: Boolean = true,
) {
    Row(
        modifier = Modifier.align(Alignment.End)
    ) {
        TextButton(
            onClick = onNegativeButtonClick,
            enabled = negativeButtonEnabled,
        ) {
            Text(
                text = negativeButtonText,
                style = MaterialTheme.typography.button
            )
        }
        TextButton(
            onClick = onPositiveButtonClick,
            enabled = positiveButtonEnabled,
        ) {
            Text(
                text = positiveButtonText,
                style = MaterialTheme.typography.button
            )
        }
    }
}

@Composable
fun QuantitySelector(
    onQuantityChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    quantity: Int = 0,
    borderColor: Color = Color.LightGray,
) {
    Row(
        modifier = modifier
            .height(dimensionResource(id = R.dimen.text_field_dense_height))
            .clip(MaterialTheme.shapes.small)
            .border(BorderStroke(2.dp, borderColor))
            .padding(
                top = dimensionResource(id = R.dimen.text_field_dense_paddingTop),
                bottom = dimensionResource(id = R.dimen.text_field_dense_paddingBottom),
            )
    ) {
        IconButton(onClick = { onQuantityChanged(quantity - 1) }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_remove_24),
                contentDescription = null
            )
        }
        BasicTextField(
            value = quantity.toString(),
            modifier = Modifier.width(24.dp),
            textStyle = MaterialTheme.typography.subtitle1.copy(
                color = MaterialTheme.colors.onSurface,
                textAlign = TextAlign.Center,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            onValueChange = { onQuantityChanged(it.toIntOrNull() ?: 0) },
        )
        IconButton(onClick = { onQuantityChanged(quantity + 1) }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_add_24dp),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
private fun QuantitySelectorPreview() {
    MdcTheme {
        QuantitySelector(
            onQuantityChanged = { },
            modifier = Modifier,
        )
    }
}

