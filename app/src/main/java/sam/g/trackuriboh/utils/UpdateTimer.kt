package sam.g.trackuriboh.utils

import javax.inject.Inject

class UpdateTimer @Inject constructor() {
    private var lastUpdateTime: Long? = null

    fun hasTimeElapsed(time: Long): Boolean {
        return lastUpdateTime.let {
            it == null || System.currentTimeMillis() - it > time
        }
    }

    fun refreshLastUpdateTime() {
        lastUpdateTime = System.currentTimeMillis()
    }
}
