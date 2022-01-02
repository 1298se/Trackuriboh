package sam.g.trackuriboh.ui.card_detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navArgs
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.R
import sam.g.trackuriboh.databinding.ActivityCardDetailBinding

/**
 * !IMPORTANT
 * We use an activity here to host the CardDetailFragment flow because it comes from DatabaseFragment,
 * and the SearchView has a bug when returning back
 * from the CardDetailFragment where the keyboard flickers. This activity dynamically sets the start destination
 * of the cardDetailGraph depending on the args it receives. See [getStartDestination]
 */
@AndroidEntryPoint
class CardDetailActivity : AppCompatActivity() {

    private val args: CardDetailActivityArgs by navArgs()

    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityCardDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)

        navHostFragment = supportFragmentManager.findFragmentById(binding.cardDetailNavHost.id) as NavHostFragment

        val navGraph = navHostFragment.navController.navInflater.inflate(R.navigation.card_detail)

        navGraph.startDestination = getStartDestination()

        navHostFragment.navController.setGraph(navGraph, args.toBundle())
    }

    private fun getStartDestination(): Int {
        return when {
            args.cardId != -1L -> R.id.cardDetailFragment
            args.setId != -1L -> R.id.cardSetDetailFragment
            else -> throw IllegalArgumentException("Both cardId and setId are not set")
        }
    }
}
