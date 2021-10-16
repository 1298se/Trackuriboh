package tang.song.edu.trackuriboh.ui_transaction_form.views

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import tang.song.edu.trackuriboh.data.types.StringResourceEnum

private const val SELECTED_ITEM_KEY = "Selected_Item"
private const val SUPER_STATE_KEY = "Super_State"

class EnumAutoCompleteTextView(context: Context, attributeSet: AttributeSet) : MaterialAutoCompleteTextView(context, attributeSet) {
    var selectedItem: StringResourceEnum?  = null

    override fun onRestoreInstanceState(state: Parcelable?) {
        var superState = state
        if (state is Bundle) {
            selectedItem = state.getParcelable(SELECTED_ITEM_KEY)
            superState = state.getParcelable(SUPER_STATE_KEY)
        }

        super.onRestoreInstanceState(superState)
    }

    override fun onSaveInstanceState(): Parcelable = Bundle().apply {
            putParcelable(SUPER_STATE_KEY, super.onSaveInstanceState())
            putParcelable(SELECTED_ITEM_KEY, selectedItem)
    }
}

