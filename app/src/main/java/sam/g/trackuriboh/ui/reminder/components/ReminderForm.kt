package sam.g.trackuriboh.ui.reminder.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import sam.g.trackuriboh.R
import sam.g.trackuriboh.ui.common.AppThemeDenseOutlinedEnumAutoCompleteTextField
import sam.g.trackuriboh.ui.common.AppThemeDenseOutlinedTextField
import sam.g.trackuriboh.ui.common.AppThemeDialogButtons
import sam.g.trackuriboh.ui.common.AppThemeOutlinedTextButton
import sam.g.trackuriboh.ui.reminder.ReminderFormViewModel
import java.text.DateFormat
import java.util.*

@ExperimentalMaterialApi
@Composable
fun ReminderForm(
    state: ReminderFormViewModel.ReminderFormState?,
    onReminderTypeSelected: (Int) -> Unit,
    onHostChanged: (String) -> Unit,
    onLinkChanged: (String) -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    onDateTimeButtonClick: () -> Unit,
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
                text = stringResource(id = if (state?.mode == ReminderFormViewModel.Mode.EDIT) {
                    R.string.edit_reminder_form_title
                } else {
                    R.string.create_reminder_form_title
                }),
                style = MaterialTheme.typography.h6
            )

            AppThemeDenseOutlinedEnumAutoCompleteTextField(
                modifier = Modifier.fillMaxWidth(),
                options = state?.reminderTypeOptions ?: emptyList(),
                hintText = stringResource(id = R.string.reminder_form_reminder_type_hint),
                selectedOption = state?.formData?.reminderType,
                onOptionSelected = onReminderTypeSelected
            )

            AppThemeDenseOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                text = state?.formData?.host,
                onValueChange = onHostChanged,
                hintText = stringResource(id = R.string.reminder_form_host_hint),
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_person_outline_24),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSurface,
                    )
                })

            AppThemeDenseOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                text = state?.formData?.link,
                onValueChange = onLinkChanged,
                hintText = stringResource(id = R.string.reminder_form_link_hint),
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_link_24),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSurface,
                    )
                })

            AppThemeOutlinedTextButton(
                modifier = Modifier.fillMaxWidth(),
                text = state?.formData?.date?.let {
                    DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault()).format(it)
                } ?: stringResource(id = R.string.reminder_form_date_time_hint),
                onButtonClick = onDateTimeButtonClick,
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




