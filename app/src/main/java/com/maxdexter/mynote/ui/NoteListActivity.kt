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
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }


//    private fun initSpinner() {
//        mSpinner = findViewById(R.id.text_size_spinner)
//        val size = ArrayList<String>()
//        size.add("small")
//        size.add("medium")
//        size.add("large")
//        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, size)
//        mSpinner.setAdapter(adapter)
//        mSpinner.setSelection(sharedPref!!.textSize)
//        mSpinner.setOnItemSelectedListener(object : OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
//                when (position) {
//                    0 -> {
//                        sharedPref!!.textSize = 0
//                        NotePad.get(applicationContext).setLiveData(0)
//                        return
//                    }
//                    1 -> {
//                        sharedPref!!.textSize = 1
//                        NotePad.get(applicationContext).setLiveData(1)
//                        return
//                    }
//                    2 -> {
//                        sharedPref!!.textSize = 2
//                        NotePad.get(applicationContext).setLiveData(2)
//                    }
//                }
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {}
//        })
//    }




}