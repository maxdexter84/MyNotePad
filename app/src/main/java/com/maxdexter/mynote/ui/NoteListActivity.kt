package com.maxdexter.mynote.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.maxdexter.mynote.R
import com.maxdexter.mynote.SharedPref
import com.maxdexter.mynote.databinding.ActivityMainBinding


class NoteListActivity : AppCompatActivity() {
    private lateinit var viewModel: NoteListActivityViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPref: SharedPref
    private lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.MyTheme)
        setDarkTheme()
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(NoteListActivityViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        sharedPref = SharedPref(this)


        val mNavHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment?
        val navController = mNavHostFragment?.navController
        bottomNavigationView = findViewById(R.id.bottom_nav)
        if (navController != null) {
            bottomNavigationView.setupWithNavController(navController)
        }



    }

    private fun setDarkTheme() {
        val isDarkTheme = SharedPref(this).isDarkTheme
        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }







}