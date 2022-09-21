package sam.g.trackuriboh.utils

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.activity.addCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.crashlytics.FirebaseCrashlytics
import sam.g.trackuriboh.di.NetworkModule

const val SNACKBAR_SHOW_REQUEST_KEY = "Snackbar_Show"
const val SNACKBAR_TYPE = "Snackbar_Type"
const val SNACKBAR_MESSAGE = "Snackbar_Message"

fun Fragment.setViewPagerBackPressBehaviour(viewPager: ViewPager2) {
    activity?.onBackPressedDispatcher?.addCallback(this) {
        if (viewPager.currentItem == 0) {
            if (!findNavController().navigateUp(getAppBarConfiguration())) {
                isEnabled = false
                activity?.onBackPressed()
            }
        } else {
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }
}

interface SearchViewQueryHandler {
    fun handleQueryTextSubmit(query: String?) {
        return
    }

    fun handleSearchViewExpanded() {
        return
    }

    fun handleSearchViewCollapse() {
        return
    }

    fun handleQueryTextChanged(newText: String?) {
        return
    }
}


fun MenuItem.setIconifiedSearchViewBehaviour(
    searchView: SearchView,
    handler: SearchViewQueryHandler,
) {
    searchView.apply {
        setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                handler.handleQueryTextSubmit(query)

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                handler.handleQueryTextChanged(newText)

                return true
            }
        })

        setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                handler.handleSearchViewExpanded()
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                handler.handleSearchViewCollapse()
                return true
            }
        })
    }
}

/**
 * The Snackbar will try to find a suitable parent view (either a CoordinatorLayout or the window decor's content view)
 * by walking up the view tree. So if the fragment will display a snackbar, it's probably better to make the root view
 * a CoordinatorLayout since it also enables features like swipe to dismiss and automatically moving of widgets
 */
fun Fragment.showSnackbar(message: String, type: SnackbarType = SnackbarType.INFO, anchorView: View? = null) {
    view?.showSnackbar(message, type, anchorView)
}

/**
 * Should use this to handle navigation so that triggering the same action twice doesn't crash...
 */
fun NavController.safeNavigate(action: NavDirections) {
        currentDestination?.getAction(action.actionId)?.let { navigate(action) }
}

fun Fragment.openTCGPlayer(productId: Long) {
    val browserIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(NetworkModule.TCGPLAYER_PRODUCT_URL + productId)
    )

    try {
        startActivity(browserIntent)
    } catch (e: ActivityNotFoundException) {
        FirebaseCrashlytics.getInstance().recordException(e)
        showSnackbar("Please install a browser to continue")
    }
}

fun BottomSheetDialogFragment.setDefaultExpanded(skipCollapsed: Boolean = true) {
    getBottomSheet()?.let {
        BottomSheetBehavior.from(it).apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            this.skipCollapsed = skipCollapsed
        }
    }
}

fun BottomSheetDialogFragment.setFullScreen() {
    setDefaultExpanded()
    getBottomSheet()?.layoutParams?.height = WindowManager.LayoutParams.MATCH_PARENT
}

fun BottomSheetDialogFragment.getBottomSheet(): View? {
    return dialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet)
}

fun FragmentManager.findViewPagerFragment(fragmentId: Long): Fragment? {
    return findFragmentByTag("f$fragmentId")
}

