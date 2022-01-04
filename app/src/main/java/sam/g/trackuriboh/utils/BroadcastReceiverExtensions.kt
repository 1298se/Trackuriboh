package sam.g.trackuriboh.utils

import android.content.BroadcastReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// Taken from https://stackoverflow.com/questions/22741202/how-to-use-goasync-for-broadcastreceiver
fun BroadcastReceiver.goAsync(
    coroutineScope: CoroutineScope = GlobalScope,
    block: suspend () -> Unit
) {
    val result = goAsync()
    coroutineScope.launch {
        try {
            block()
        } finally {
            result.finish()
        }
    }
}
