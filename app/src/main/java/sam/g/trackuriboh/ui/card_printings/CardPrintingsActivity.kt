package sam.g.trackuriboh.ui.card_printings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.R
import sam.g.trackuriboh.databinding.ActivityCardPrintingsBinding
import sam.g.trackuriboh.ui.database.CardListFragment
import sam.g.trackuriboh.ui.database.CardListFragmentArgs
import sam.g.trackuriboh.ui.database.CardListFragmentDirections
import sam.g.trackuriboh.utils.safeNavigate

@AndroidEntryPoint
class CardPrintingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityCardPrintingsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(binding.cardPrintingsNavHost.id) as NavHostFragment

        val navController = navHostFragment.navController

        // TODO: Fix Magic String
        val cardListFragmentArgs = bundleOf("query" to intent.extras?.getString("cardName"))

        navController.setGraph(R.navigation.card_printings, CardListFragmentArgs.fromBundle(cardListFragmentArgs).toBundle())

        binding.cardPrintingsToolbar.setupWithNavController(
            navController = navController,
            configuration = AppBarConfiguration(
                topLevelDestinationIds = setOf(),
                fallbackOnNavigateUpListener = {
                    finish()
                    true
                }
            )
        )

        navHostFragment.childFragmentManager.setFragmentResultListener(
            CardListFragment.FRAGMENT_RESULT_REQUEST_KEY,
            this
        ) { _, bundle ->
            navController.safeNavigate(
                CardListFragmentDirections.actionCardListFragmentToCardDetailActivity(bundle.getLong(CardListFragment.CARD_ID_DATA_KEY))
            )
        }
    }
}
