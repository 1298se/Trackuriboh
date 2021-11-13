package sam.g.trackuriboh

import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import sam.g.trackuriboh.ui_common.ErrorDialogFragment

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

fun Fragment.showErrorDialog(title: String, message: String) {
    ErrorDialogFragment.newInstance(title, message).show(parentFragmentManager, null)
}

fun Fragment.handleNavigationAction(action: NavDirections) {
    findNavController().navigate(action)
}
