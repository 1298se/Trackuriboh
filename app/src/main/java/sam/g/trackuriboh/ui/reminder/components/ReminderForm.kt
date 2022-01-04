package sam.g.trackuriboh.ui.reminder.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.material.composethemeadapter.MdcTheme
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.types.ReminderType
import sam.g.trackuriboh.ui.common.AppThemeDenseOutlinedEnumAutoCompleteTextField
import sam.g.trackuriboh.ui.common.AppThemeDenseOutlinedTextField
import sam.g.trackuriboh.ui.common.AppThemeOutlinedTextButton
import sam.g.trackuriboh.ui.common.SimpleDialogForm
import sam.g.trackuriboh.ui.reminder.ReminderFormViewModel
import sam.g.trackuriboh.utils.formatReminderDateTime
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
    SimpleDialogForm(
        title = stringResource(id = if (state?.mode == ReminderFormViewModel.Mode.EDIT) {
            R.string.edit_reminder_form_title
        } else {
            R.string.create_reminder_form_title
        }),
        onPositiveButtonClick = onSaveClick,
        onNegativeButtonClick = onCancelClick,
        positiveButtonEnabled = state?.canSave ?: false,
        positiveButtonText = stringResource(id = if (state?.mode == ReminderFormViewModel.Mode.EDIT) {
            R.string.lbl_update
        } else {
            R.string.lbl_save
        }).uppercase(),
        negativeButtonText = stringResource(id = R.string.lbl_cancel).uppercase()
    ) {

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
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Next),
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
                formatReminderDateTime(it)
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
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun Preview() {
    val formState = ReminderFormViewModel.ReminderFormState(
        reminderTypeOptions = emptyList(),
        canSave = true,
        formData = ReminderFormViewModel.ReminderFormData(
            ReminderType.AUCTION,
            "Billy",
            "https://facebook.com",
            Date()
        ),
        ReminderFormViewModel.Mode.CREATE
    )
    MdcTheme {
        ReminderForm(
            state = formState,
            onHostChanged = { },
            onLinkChanged = { },
            onReminderTypeSelected = { },
            onDateTimeButtonClick = { },
            onSaveClick = { },
            onCancelClick = { },
        )
    }
}





