package com.maxdexter.mynote.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.maxdexter.mynote.R
import com.maxdexter.mynote.R.*
import com.maxdexter.mynote.SharedPref
import com.maxdexter.mynote.databinding.ActivityMainBinding
import com.maxdexter.mynote.ui.fragments.bottomsheet.BottomSheetDrawerFragment

class NoteListActivity : AppCompatActivity() {
    lateinit var viewModel: NoteListActivityViewModel
    lateinit var binding: ActivityMainBinding
    lateinit var bottomSheetBehavior:BottomSheetBehavior<View>
    private val type = 0
    lateinit var mNavigationView: BottomNavigationView
    lateinit var mFloatingActionButton: FloatingActionButton
    lateinit var mSwitchCompat: SwitchCompat
    lateinit var sharedPref: SharedPref
    lateinit var mSpinner: Spinner
    lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, layout.activity_main)
        viewModel = ViewModelProvider(this).get(NoteListActivityViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        sharedPref = SharedPref(this)
        //initFragmentList(type);
        //initBottomNav();

        val mNavHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment?
        val navController = mNavHostFragment?.navController
        bottomNavigationView = findViewById(R.id.bottom_nav)
        if (navController != null) {
            bottomNavigationView.setupWithNavController(navController)
        }



        initTheme()
        //initSwitch()
       // initSpinner()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.bottomappbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            android.R.id.home -> {
               val bottomSheetDrawerFragment = BottomSheetDrawerFragment()
                bottomSheetDrawerFragment.show(supportFragmentManager,bottomSheetDrawerFragment.tag)

            }

        }
        return super.onOptionsItemSelected(item)
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

//    private fun initSwitch() {
//        mSwitchCompat = findViewById(R.id.switch_theme)
//        mSwitchCompat.setChecked(sharedPref!!.isDarkTheme)
//        mSwitchCompat.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
//            sharedPref!!.isDarkTheme = isChecked
//            recreate()
//        })
//    }

    private fun initTheme() {
        if (sharedPref.isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(style.Theme_AppCompat_DayNight_NoActionBar)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            setTheme(style.MyTheme)
        }
    }





//    override fun onNoteSelected(note: Note) { //Если ориентация портретная и R.id.fragment_container_detail нет в мкете то при клике на элемент списка запуститься NotePagerActivity
//        if (findViewById<View?>(id.fragment_container_detail) == null) {
//            val intent = NotePagerActivity.newIntent(this, note.uuid)
//            startActivity(intent)
//        } else { // если ориентация альбомная и R.id.fragment_container_detail есть в макете то произайдет транзакция DetailFragment в контейнер в макете
//            val newDetail: Fragment = DetailFragment.newInstance(note.uuid)
//            supportFragmentManager.beginTransaction().replace(id.fragment_container_detail, newDetail).commit()
//        }
//    }
}