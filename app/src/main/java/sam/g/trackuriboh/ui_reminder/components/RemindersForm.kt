package sam.g.trackuriboh.ui_reminder.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LiveData
import sam.g.trackuriboh.R
import sam.g.trackuriboh.ui_common.AppThemeDenseOutlinedEnumAutoCompleteTextField
import sam.g.trackuriboh.ui_common.AppThemeDenseOutlinedTextField
import sam.g.trackuriboh.ui_common.AppThemeDialogButtons
import sam.g.trackuriboh.ui_common.AppThemeOutlinedTextButton
import sam.g.trackuriboh.ui_reminder.ReminderFormViewModel
import java.text.DateFormat
import java.util.*

@ExperimentalMaterialApi
@Composable
fun RemindersForm(
    formState: LiveData<ReminderFormViewModel.ReminderFormState>,
    onReminderTypeSelected: (Int) -> Unit,
    onHostChanged: (String) -> Unit,
    onLinkChanged: (String) -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    showDateTimePicker: () -> Unit,
) {
    val state by formState.observeAsState()

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
                text = stringResource(id = if (state?.mode == ReminderFormViewModel.Mode.EDIT) {
                    R.string.lbl_edit_reminder
                } else {
                    R.string.lbl_create_reminder
                }),
                style = MaterialTheme.typography.h6
            )

            AppThemeDenseOutlinedEnumAutoCompleteTextField(
                options = state?.reminderTypeOptions ?: emptyList(),
                selectedOption = state?.formData?.reminderType,
                onOptionSelected = onReminderTypeSelected
            )

            AppThemeDenseOutlinedTextField(
                text = state?.formData?.host,
                onValueChange = onHostChanged,
                hintText = "Host",
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_person_outline_24),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSurface,
                    )
                })

            AppThemeDenseOutlinedTextField(
                text = state?.formData?.link,
                onValueChange = onLinkChanged,
                hintText = "Link",
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_link_24),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSurface,
                    )
                })

            AppThemeOutlinedTextButton(
                text = state?.formData?.date?.let {
                    DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault()).format(it)
                } ?: "Date & Time",
                onButtonClick = showDateTimePicker,
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_event_note_24),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSurface,
                    )
                })
        }

        AppThemeDialogButtons(
            positiveButtonEnabled = state?.canSave ?: false,
            negativeButtonEnabled = true,
            positiveButtonText = stringResource(id = if (state?.mode == ReminderFormViewModel.Mode.EDIT) {
                R.string.lbl_update
            } else {
                R.string.lbl_save
            }).uppercase(),
            negativeButtonText = stringResource(id = R.string.lbl_cancel).uppercase(),
            onPositiveButtonClick = onSaveClick,
            onNegativeButtonClick = onCancelClick
        )
    }
}




