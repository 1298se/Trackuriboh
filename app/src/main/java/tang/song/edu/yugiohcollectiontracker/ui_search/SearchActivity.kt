package tang.song.edu.yugiohcollectiontracker.ui_search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import tang.song.edu.yugiohcollectiontracker.R

class SearchActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search)

        setupActionBarAndNavigation()
    }

    private fun setupActionBarAndNavigation() {
        val navController = findNavController(R.id.search_nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)

        setSupportActionBar(findViewById(R.id.search_toolbar))
        setupActionBarWithNavController(navController, appBarConfiguration)
    }
}
