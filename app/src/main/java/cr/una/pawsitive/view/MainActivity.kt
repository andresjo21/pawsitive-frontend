package cr.una.pawsitive.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import cr.una.pawsitive.R
import cr.una.pawsitive.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        //log the onCreate method
        Log.i("MainActivityDebug", "onCreate")
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_container
        ) as NavHostFragment
        navController = navHostFragment.navController
        setupBottomNav()
    }

    override fun onResume() {
        super.onResume()
        setupBottomNav()
    }

    private fun setupBottomNav() {
        // Get the global isAdmin variable
        val isAdmin = (application as cr.una.pawsitive.utils.MyApplication).isAdmin
        Log.i("MainActivityDebug", "isAdmin: $isAdmin")

        //Set the navigation graph and bottom navigation menu based on the isAdmin variable
        if (isAdmin) {
            navController.setGraph(R.navigation.nav_graph_admin)
            //log current graph
            Log.i("MainActivityDebug", "Current graph: ${navController.graph.toString()}")
            binding.bottomNav.menu.clear() // Clear the existing menu
            binding.bottomNav.inflateMenu(R.menu.bottom_nav_admin) // Use the admin menu
        } else {
            navController.setGraph(R.navigation.nav_graph)
            //log current graph
            Log.i("MainActivityDebug", "Current graph: ${navController.graph.toString()}")
            binding.bottomNav.menu.clear() // Clear the existing menu
            binding.bottomNav.inflateMenu(R.menu.bottom_nav) // Use the user menu
        }

        binding.bottomNav.setupWithNavController(navController)
    }
}