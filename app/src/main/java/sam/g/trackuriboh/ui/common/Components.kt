package sam.g.trackuriboh.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults.textButtonColors
import androidx.compose.material.ContentAlpha
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.google.android.material.composethemeadapter.MdcTheme
import sam.g.trackuriboh.R
import sam.g.trackuriboh.databinding.ComponentSegmentedTwoButtonBinding

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
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
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
            keyboardOptions = keyboardOptions,
            singleLine = singleLine,
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

        AppThemeTextButton(
            onClick = onNegativeButtonClick,
            enabled = negativeButtonEnabled,
            text = negativeButtonText,
        )

        AppThemeTextButton(
            onClick = onPositiveButtonClick,
            enabled = positiveButtonEnabled,
            text = positiveButtonText,
        )
    }
}

@Composable
fun AppThemeTextButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    text: String,
) {
    TextButton(
        onClick = onClick,
        enabled = enabled,
        colors = textButtonColors(
            backgroundColor = Color.Transparent,
            contentColor = MaterialTheme.colors.onPrimary,
            disabledContentColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.button
        )
    }
}

@Composable
fun AppThemeSegmentedButton(
    option1Text: String,
    option2Text: String,
    onOptionSelected: (Int) -> Unit,
) {
    AndroidViewBinding(ComponentSegmentedTwoButtonBinding::inflate) {
        option1.text = option1Text
        option2.text = option2Text
        toggleButton.addOnButtonCheckedListener { _, checkedId, isChecked ->
            val index = when (checkedId) {
                option2.id -> 1
                else -> 0
            }
            onOptionSelected(index)
        }
    }
}

@Composable
fun QuantitySelector(
    onQuantityChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    quantity: Int = 0,
    borderColor: Color = Color.Gray,
    dense: Boolean = false,
) {
    Row(
        modifier = modifier
            .then(
                if (dense) {
                    Modifier.height(dimensionResource(id = R.dimen.text_field_dense_height))
                } else {
                    Modifier.height(dimensionResource(id = R.dimen.text_field_height))
                }
            )
            .border(BorderStroke(1.dp, borderColor))
            .clip(MaterialTheme.shapes.small)
    ) {
        IconButton(
            onClick = { onQuantityChanged(quantity - 1) },
            modifier = Modifier.align(Alignment.CenterVertically),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_remove_24),
                contentDescription = null
            )
        }

        BasicTextField(
            value = quantity.toString(),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .width(24.dp),
            textStyle = MaterialTheme.typography.subtitle1.copy(
                color = MaterialTheme.colors.onSurface,
                textAlign = TextAlign.Center,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            readOnly = true,
            onValueChange = { },
        )
        IconButton(
            onClick = { onQuantityChanged(quantity + 1) },
            modifier = Modifier.align(Alignment.CenterVertically),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_add_24dp),
                contentDescription = null
            )
        }
    }
}

@Composable
fun SelectableLabelValueRow(
    modifier: Modifier = Modifier,
    label: String?,
    value: String?,
    onClick: () -> Unit
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(dimensionResource(id = R.dimen.material_border_padding)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = label ?: "",
            style = MaterialTheme.typography.subtitle1
        )

        Text(
            text = value ?: "",
            style = MaterialTheme.typography.body2,
            textAlign = TextAlign.End
        )
    }
}

@Composable
fun TwoLineTextRow(
    headerText: String?,
    valueText: String?,
) {
    Column(
        modifier = Modifier.padding(
            horizontal = dimensionResource(id = R.dimen.list_item_padding_horizontal),
            vertical = dimensionResource(id = R.dimen.list_item_two_line_padding_vertical)
        )
    ) {
        Text(
            text = headerText ?: "",
            style = MaterialTheme.typography.overline,
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.text_spacing)))

        Text(
            text = valueText ?: "",
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
fun getProfitTextColor(profit: Double?) =
    if (profit != null) {
        if (profit < 0) {
            MaterialTheme.colors.onError
        } else {
            MaterialTheme.colors.primary
        }
    } else {
        Color.Unspecified
    }

@Composable
fun SwipeToDismissDeleteBackground() {
    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.error, MaterialTheme.shapes.medium)
            .padding(
                horizontal = dimensionResource(id = R.dimen.list_item_padding_horizontal)
            )
    ) {
        Icon(
            painter = painterResource(
                id = R.drawable.ic_baseline_delete_forever_24
            ),
            contentDescription = "",
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}

@Preview
@Composable
private fun ComponentPreviews() {
    MdcTheme {
        Column {
            SelectableLabelValueRow(label = "Bruh", value = "Bruh") {

            }

            QuantitySelector(
                onQuantityChanged = { },
                modifier = Modifier,
            )

            SimpleDialogForm(
                onPositiveButtonClick = { },
                onNegativeButtonClick = { }) {
                AppThemeDialogButtons(
                    positiveButtonText = "OK",
                    negativeButtonText = "Cancel",
                    onPositiveButtonClick = { },
                    onNegativeButtonClick = { })
            }
        }
    }
}
