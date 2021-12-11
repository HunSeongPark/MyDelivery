package com.hunseong.delivery.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.hunseong.delivery.R
import com.hunseong.delivery.databinding.ActivityMainBinding
import com.hunseong.delivery.extension.gone
import com.hunseong.delivery.extension.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavigation()
    }

    private fun initNavigation() {
        val navController = (supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment).navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.home_fragment || destination.id == R.id.my_fragment) {
                binding.bottomNav.visible()
            } else {
                binding.bottomNav.gone()
            }
        }

        binding.bottomNav.setupWithNavController(navController)

    }
}