package com.example.tourrent

import android.content.Context
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        setup()
    }

    private fun setup(){
        val prefs = getSharedPreferences("INFO", Context.MODE_PRIVATE)
        val mode = prefs.getString("Mode","")
        if(mode == "" || mode == "T") {
            nav_view.visibility = View.VISIBLE
            nav_view2.visibility = View.GONE

            val navView: BottomNavigationView = findViewById(R.id.nav_view)
            val navController = findNavController(R.id.nav_host_fragment)
            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.tourist_home,
                    R.id.chatroom_list,
                    R.id.booking_list,
                    R.id.tourist_profile
                )
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)

            visibilityPNavElements(navController)
        }
        else if (mode == "G"){
            nav_view.visibility = View.GONE
            nav_view2.visibility = View.VISIBLE

            val navView2: BottomNavigationView = findViewById(R.id.nav_view2)
            val navController2 = findNavController(R.id.nav_host_fragment)
            val appBarConfiguration2 = AppBarConfiguration(
                setOf(
                    R.id.booking_list,
                    R.id.chatroom_list,
                    R.id.guide_profile
                )
            )
            setupActionBarWithNavController(navController2, appBarConfiguration2)
            navView2.setupWithNavController(navController2)

            visibilityGNavElements(navController2)
        }
    }

    private fun visibilityPNavElements(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.tourist_home,
                R.id.chatroom_list,
                R.id.booking_list,
                R.id.tourist_profile -> nav_view?.visibility = View.VISIBLE
                else -> nav_view?.visibility = View.GONE
            }
        }
    }

    private fun visibilityGNavElements(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.tourist_home,
                R.id.chatroom_list,
                R.id.booking_list,
                R.id.guide_profile -> nav_view2?.visibility = View.VISIBLE
                else -> nav_view2?.visibility = View.GONE
            }
        }
    }
}