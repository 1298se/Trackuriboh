package sam.g.trackuriboh.ui.user_list.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.material.composethemeadapter.MdcTheme
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
    OutlinedTextField(value = state., onValueChange = )
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun AddToUserListFormPreview() {
    val formState = AddToUserListFormViewModel.AddToUserListFormState(
        canSave = true,
        formData = AddToUserListFormViewModel.AddToUserListFormData(
            null,
            null,
            1
        ),
    )

    MdcTheme {
        AddToUserListForm(
            state = formState,
            onSelectSkuButtonClick = { },
            onSelectListButtonClick = { },
            onQuantityChanged = { },
            onSaveClick = { },
            onCancelClick = { },
        )
    }
}
