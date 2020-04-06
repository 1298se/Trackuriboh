package tang.song.edu.yugiohcollectiontracker.ui_search

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import tang.song.edu.yugiohcollectiontracker.BaseApplication
import tang.song.edu.yugiohcollectiontracker.R
import tang.song.edu.yugiohcollectiontracker.ui_search.viewmodels.SearchViewModel
import tang.song.edu.yugiohcollectiontracker.ui_search.viewmodels.SearchViewModelFactory
import javax.inject.Inject

class SearchActivity : AppCompatActivity() {
    @Inject
    lateinit var mViewModelFactory: SearchViewModelFactory

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var mViewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as BaseApplication).appComponent.inject(this)

        super.onCreate(savedInstanceState)

        mViewModel = ViewModelProvider(this, mViewModelFactory).get(SearchViewModel::class.java)

        setContentView(R.layout.activity_search)
        setupActionBarAndNavigation()

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        intent?.let {
            handleIntent(it)
        }
    }

    private fun setupActionBarAndNavigation() {
        val navController = findNavController(R.id.search_nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)

        setSupportActionBar(findViewById(R.id.search_toolbar))
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            handleSearch(query)
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                SearchRecentSuggestions(this, SearchSuggestionProvider.AUTHORITY, SearchSuggestionProvider.MODE)
                    .saveRecentQuery(query, null)
            }
        }
    }

    private fun handleSearch(queryString: String?) {
        mViewModel.search(queryString)
    }
}
