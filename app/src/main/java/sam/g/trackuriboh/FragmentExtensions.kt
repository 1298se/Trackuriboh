package sam.g.trackuriboh

import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar

fun Fragment.setViewPagerBackPressBehaviour(viewPager: ViewPager2) {
    activity?.onBackPressedDispatcher?.addCallback(this) {
        if (viewPager.currentItem == 0) {
            if (!findNavController().popBackStack()) {
                activity?.finish()
            }
        } else {
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }
}

fun Fragment.showSnackbar(message: String) {
    view?.let {
        Snackbar.make(it, message, Snackbar.LENGTH_LONG)
        .show()
    }
}

fun Fragment.handleNavigationAction(action: NavDirections) {
    findNavController().navigate(action)
}
