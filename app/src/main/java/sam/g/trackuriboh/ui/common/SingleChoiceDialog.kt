package sam.g.trackuriboh.ui.common

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

fun SingleChoiceDialog(
    context: Context,
    title: String,
    items: List<String>,
    checkedItemIndex: Int?,
    onItemCheckedListener: DialogInterface.OnClickListener
): Dialog {
    val builder = AlertDialog.Builder(context).apply {
        setTitle(title)
        setSingleChoiceItems(items.toTypedArray(), checkedItemIndex ?: -1, onItemCheckedListener)
    }

    return builder.create()
}

