package sam.g.trackuriboh.ui_common

import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

abstract class CollapseToolbarStateChangeListener : AppBarLayout.OnOffsetChangedListener {
    enum class State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    private var mState: State = State.IDLE

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        when {
            verticalOffset == 0 -> {
                if (mState != State.EXPANDED) {
                    onStateChanged(appBarLayout, State.EXPANDED)
                }
                mState = State.EXPANDED
            }
            (appBarLayout?.totalScrollRange ?: 0) <= abs(verticalOffset) -> {
                if (mState != State.COLLAPSED) {
                    onStateChanged(appBarLayout, State.COLLAPSED)
                }
                mState = State.COLLAPSED
            }
            else -> {
                if (mState != State.IDLE) {
                    onStateChanged(appBarLayout, State.IDLE)
                }
                mState = State.IDLE
            }
        }
    }

    abstract fun onStateChanged(appBarLayout: AppBarLayout?, state: State)
}
