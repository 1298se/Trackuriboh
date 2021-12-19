package sam.g.trackuriboh.ui_common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.types.StringResourceEnum

/**
 * TODO: Why is it so hard to just add a trailing icon...
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
            .height(dimensionResource(id = R.dimen.form_input_dense_height))
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
                start = dimensionResource(id = R.dimen.text_field_dense_paddingStart),
                end = dimensionResource(id = R.dimen.text_field_dense_paddingEnd)
            )
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
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
        onClick = onButtonClick) {
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
    selectedOption: String? = null,
    onOptionSelected: (Int) -> Unit = { },
    hintText: String? = null
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = Modifier.height(dimensionResource(id = R.dimen.form_input_dense_height)),
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
    selectedOption: StringResourceEnum? = null,
    onOptionSelected: (Int) -> Unit = { }
) {
    AppThemeDenseOutlinedAutoCompleteTextField(
        options = options.map { stringResource(id = it.resourceId) },
        selectedOption = selectedOption?.resourceId?.let { stringResource(id = it) },
        onOptionSelected = onOptionSelected,
        hintText = "Remind me of..."
    )
}

@Composable
fun ColumnScope.AppThemeDialogButtons(
    positiveButtonText: String?,
    negativeButtonText: String?,
    onPositiveButtonClick: () -> Unit = { },
    onNegativeButtonClick: () -> Unit = { },
) {
    Row(
        modifier = Modifier.align(Alignment.End)
    ) {
        negativeButtonText?.let {
            TextButton(
                onClick = onNegativeButtonClick,
            ) {
                Text(
                    text = it,
                    style = MaterialTheme.typography.button
                )
            }
        }
        positiveButtonText?.let {
            TextButton(onClick = onPositiveButtonClick) {
                Text(
                    text = positiveButtonText,
                    style = MaterialTheme.typography.button
                )
            }
        }

    }
}
