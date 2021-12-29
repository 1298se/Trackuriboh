package sam.g.trackuriboh.ui.card_detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.R
import sam.g.trackuriboh.databinding.ActivityCardDetailBinding

/**
 * We use an activity here because it comes from DatabaseFragment, and the SearchView has a bug when returning back
 * from the CardDetailFragment where the keyboard flickers
 */
@AndroidEntryPoint
class CardDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityCardDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val navController = (supportFragmentManager.findFragmentById(binding.cardDetailNavHost.id) as NavHostFragment).navController

        navController.setGraph(R.navigation.card_detail, intent.extras)
    }
}
