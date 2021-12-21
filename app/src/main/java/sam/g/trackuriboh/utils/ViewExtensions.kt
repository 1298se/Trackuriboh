package sam.g.trackuriboh.utils

import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import sam.g.trackuriboh.R

/**
 * Use this method to add [RecyclerView.ItemDecoration] without stacking behaviour
 * We need to remove the [RecyclerView.ItemDecoration] and add it again
 * because otherwise the item decoration will stack every LiveData callback
 */
fun RecyclerView.addItemDecorationIfExists(decor: RecyclerView.ItemDecoration) {
    removeItemDecoration(decor)
    addItemDecoration(decor)
}

fun ViewGroup.showOnly(vararg views: View) {
    children.forEach {
        it.visibility = if (views.contains(it)) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}

fun ViewGroup.showAllExcept(vararg views: View) {
    children.forEach {
        it.visibility = if (views.contains(it)) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}

fun View?.show(visible: Boolean) {
    this?.visibility = if (visible) View.VISIBLE else View.GONE
}

enum class SnackbarType {
    SUCCESS,
    ERROR,
    INFO;
}
/**
 * The Snackbar will try to find a suitable parent view (either a CoordinatorLayout or the window decor's content view)
 * by walking up the view tree, so if a snackbar should be displayed, it's better to have a parent
 * that is a CoordinatorLayout since it also enables features like swipe to dismiss and automatically moving of widgets.
 */
fun View?.showSnackbar(message: String, type: SnackbarType = SnackbarType.INFO, anchorView: View? = null) {
    this?.let {
        val backgroundColor = when (type) {
            SnackbarType.SUCCESS -> ContextCompat.getColor(context, R.color.success_green)
            SnackbarType.ERROR -> ContextCompat.getColor(context, R.color.red)
            SnackbarType.INFO -> null
        }

        Snackbar.make(it, message, Snackbar.LENGTH_LONG).apply {
            setAnchorView(anchorView)

            if (backgroundColor != null) {
                setBackgroundTint(backgroundColor)
            }

            show()
        }
    }
}

fun Context.createAlertDialog(
    title: String? = null,
    message: String? = null,
    positiveButtonText: String? = this.getString(R.string.lbl_ok),
    negativeButtonText: String? = this.getString(R.string.lbl_cancel),
    onPositiveButtonClick: (dialog: DialogInterface, id: Int) -> Unit = { _, _ -> },
    onNegativeButtonClick: (dialog: DialogInterface, id: Int) -> Unit = { _, _ -> }
) : AlertDialog {

    with(AlertDialog.Builder(this)) {
        setTitle(title)
        setMessage(message)

        setPositiveButton(positiveButtonText) { dialog: DialogInterface, id: Int ->
            onPositiveButtonClick(dialog, id)
        }

        setNegativeButton(negativeButtonText) { dialog: DialogInterface, id: Int ->
            onNegativeButtonClick(dialog, id)
        }

        return create()
    }
}

fun getAppBarConfiguration() : AppBarConfiguration = AppBarConfiguration(setOf(R.id.databaseFragment, R.id.notificationsFragment))

fun MaterialToolbar.setupAsTopLevelDestinationToolbar() {
    setupWithNavController(findNavController(), getAppBarConfiguration())
}

fun MaterialToolbar.setMenuEnabled(enabled: Boolean) {
    menu.children.forEach {
        it.isEnabled = enabled
    }
}
