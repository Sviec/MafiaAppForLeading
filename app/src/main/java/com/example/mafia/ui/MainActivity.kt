package com.example.mafia.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mafia.R
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationBar = findViewById<NavigationBarView>(R.id.bottom_navigation_view)
        val navController = findNavController(R.id.fragmentContainerView)
        bottomNavigationBar.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.introductionFragment) {
                bottomNavigationBar.visibility = View.GONE
            } else if (destination.id == R.id.mainFragment) {
                bottomNavigationBar.visibility = View.VISIBLE
            }
        }
    }
}