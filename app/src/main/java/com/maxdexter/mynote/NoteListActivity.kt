package com.maxdexter.mynote

import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.maxdexter.mynote.data.Note
import com.maxdexter.mynote.data.NotePad
import com.maxdexter.mynote.databinding.ActivityMainBinding
import com.maxdexter.mynote.ui.fragments.DetailFragment
import com.maxdexter.mynote.ui.fragments.NoteListFragment
import java.util.*

class NoteListActivity : AppCompatActivity(), NoteListFragment.Callbacks {
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
        setContentView(R.layout.activity_main)
        sharedPref = SharedPref(this)
        //initFragmentList(type);
        //initBottomNav();

        val mNavHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment?
        val navController = mNavHostFragment?.navController
        bottomNavigationView = findViewById(R.id.bottom_nav)
        if (navController != null) {
            bottomNavigationView.setupWithNavController(navController)
        }
        initFloatingActionButton()
//        initBottomSheet()
        initTheme()
        //initSwitch()
       // initSpinner()
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
            setTheme(R.style.Theme_AppCompat_DayNight_NoActionBar)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            setTheme(R.style.MyTheme)
        }
    }

    private fun initFloatingActionButton() {
        mFloatingActionButton = findViewById(R.id.add_button)
        mFloatingActionButton.setOnClickListener(View.OnClickListener {
            val note = Note()
            NotePad.get(this@NoteListActivity).addNote(note)
            val intent = NotePagerActivity.newIntent(this@NoteListActivity, note.uuid)
            startActivity(intent)
        })
    }

    //    private void initFragmentList(int type) {
    //        FragmentManager fm = getSupportFragmentManager();
    //        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
    //        if(fragment == null){
    //            fragment = NoteListFragment.newInstance(type);
    //            fm.beginTransaction().add(R.id.fragment_container,fragment).commit();
    //        }else{
    //            fragment = NoteListFragment.newInstance(type);
    //            fm.beginTransaction().replace(R.id.fragment_container,fragment).commit();
    //        }
    //    }
    //    private void initBottomNav() {
    //        mNavigationView = findViewById(R.id.bottom_nav);
    //        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
    //            @Override
    //            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
    //                switch (menuItem.getItemId()){
    //                    case R.id.simple_note:
    //                        type = DetailFragment.NOTE_TYPE_SIMPLE;
    //                        initFragmentList(type);
    //                        return true;
    //                    case R.id.important_note:
    //                        type = DetailFragment.NOTE_TYPE_IMPORTANT;
    //                        initFragmentList(type);
    //                        return true;
    //                    case R.id.password_note:
    //                        type = DetailFragment.NOTE_TYPE_PASSWORD;
    //                        initFragmentList(type);
    //                        return true;
    //                    case R.id.settingsFragment:
    //                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, SettingsFragment.Companion.newInstance()).commit();
    ////                        if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
    ////                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    ////                        }else bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    //                        return true;
    //                }
    //                return false;
    //            }
    //        });
    //
    //    }
//    private fun initBottomSheet() {
//        val bottomSheet = findViewById<LinearLayout>(R.id.bottom_sheet)
//        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
//        val fab = findViewById<FloatingActionButton>(R.id.add_button)
//        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetCallback() {
//            override fun onStateChanged(view: View, i: Int) {
//                if (BottomSheetBehavior.STATE_EXPANDED == i) {
//                    fab.isEnabled = false
//                } else fab.isEnabled = true
//            }
//
//            override fun onSlide(view: View, v: Float) {
//                fab.animate().scaleX(1 - v).scaleY(1 - v).setDuration(0).start()
//            }
//        })
//    }

    override fun onNoteSelected(note: Note) { //Если ориентация портретная и R.id.fragment_container_detail нет в мкете то при клике на элемент списка запуститься NotePagerActivity
        if (findViewById<View?>(R.id.fragment_container_detail) == null) {
            val intent = NotePagerActivity.newIntent(this, note.uuid)
            startActivity(intent)
        } else { // если ориентация альбомная и R.id.fragment_container_detail есть в макете то произайдет транзакция DetailFragment в контейнер в макете
            val newDetail: Fragment = DetailFragment.newInstance(note.uuid)
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container_detail, newDetail).commit()
        }
    }
}