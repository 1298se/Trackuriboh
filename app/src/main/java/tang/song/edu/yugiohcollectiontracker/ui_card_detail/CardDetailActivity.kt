package tang.song.edu.yugiohcollectiontracker.ui_card_detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import tang.song.edu.yugiohcollectiontracker.R

class CardDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_detail)

        findNavController(R.id.card_detail_nav_host_fragment).setGraph(R.navigation.card_detail_nav_graph, intent.extras)
    }
}
