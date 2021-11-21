package sam.g.trackuriboh.ui_card_set_detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.R

/**
 * We use an activity here because it comes from DatabaseFragment, and the SearchView has a bug when returning back
 * from the CardDetailFragment where the keyboard flickers
 */
@AndroidEntryPoint
class CardSetDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_detail)
        findNavController(R.id.card_set_detail_nav_host_fragment).setGraph(R.navigation.card_set_detail_nav_graph, intent.extras)
    }
}
