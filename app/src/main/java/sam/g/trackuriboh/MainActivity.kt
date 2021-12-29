package sam.g.trackuriboh

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.databinding.ActivityMainBinding
import sam.g.trackuriboh.utils.getAppBarConfiguration
import sam.g.trackuriboh.utils.setEnabled

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    companion object {
        const val FRAGMENT_RESULT_REQUEST_KEY = "MainActivity_fragmentResultRequestKey"
        const val ACTION_SET_BOTTOM_NAV_ENABLED = "MainActivity_setBottomNavEnabled"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        navHostFragment = supportFragmentManager.findFragmentById(binding.mainNavHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController

        initBottomNavigation()
        initFragmentResultListeners()
    }

    private fun initBottomNavigation() {
        binding.bottomNavView.setupWithNavController(navController)
    }

    private fun initFragmentResultListeners() {
        navController.getBackStackEntry(R.id.main_nav).savedStateHandle.getLiveData<Boolean>(ACTION_SET_BOTTOM_NAV_ENABLED).observe(
            this
        ) {
            binding.bottomNavView.menu.setEnabled(it)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(getAppBarConfiguration())
    }
}
