package com.maxdexter.mynote;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.maxdexter.mynote.data.Note;
import com.maxdexter.mynote.data.NotePad;
import com.maxdexter.mynote.ui.fragments.DetailFragment;
import com.maxdexter.mynote.ui.fragments.NoteListFragment;

import java.util.ArrayList;

public class NoteListActivity extends AppCompatActivity implements NoteListFragment.Callbacks {
    BottomSheetBehavior bottomSheetBehavior;
    private int type = 0;
    BottomNavigationView mNavigationView;
    FloatingActionButton mFloatingActionButton;
    SwitchCompat mSwitchCompat;
    SharedPref sharedPref;
    Spinner mSpinner;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref = new SharedPref(this);
        initFragmentList(type);
        initBottomNav();
        initFloatingActionButton();
        initBottomSheet();
        initTheme();
        initSwitch();
        initSpinner();
    }

    private void initSpinner() {
        ArrayList<String>size = new ArrayList<>();
        size.add("small");
        size.add("medium");
        size.add("large");
        mSpinner = findViewById(R.id.text_size_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,size);
        mSpinner.setAdapter(adapter);
        mSpinner.setSelection(sharedPref.getTextSize());
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        sharedPref.setTextSize(0);
                        NotePad.get(getApplicationContext()).setLiveData(0);
                        return;
                    case 1:
                        sharedPref.setTextSize(1);
                        NotePad.get(getApplicationContext()).setLiveData(1);
                        return;
                    case 2:
                        sharedPref.setTextSize(2);
                        NotePad.get(getApplicationContext()).setLiveData(2);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void initSwitch() {
        mSwitchCompat = findViewById(R.id.switch_theme);
        mSwitchCompat.setChecked(sharedPref.isDarkTheme());
        mSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPref.setDarkTheme(isChecked);
                recreate();
            }
        });
    }

    private void initTheme() {
        if(sharedPref.isDarkTheme()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            setTheme(R.style.Theme_AppCompat_DayNight_NoActionBar);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            setTheme(R.style.MyTheme);

        }
    }

    private void initFloatingActionButton() {
        mFloatingActionButton = findViewById(R.id.add_button);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note note = new Note();
                NotePad.get(NoteListActivity.this).addNote(note);

                Intent intent = NotePagerActivity.newIntent(NoteListActivity.this,note.getUUID());
                startActivity(intent);
            }
        });
    }

    private void initFragmentList(int type) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if(fragment == null){
            fragment = NoteListFragment.newInstance(type);
            fm.beginTransaction().add(R.id.fragment_container,fragment).commit();
        }else{
            fragment = NoteListFragment.newInstance(type);
            fm.beginTransaction().replace(R.id.fragment_container,fragment).commit();
        }
    }

    private void initBottomNav() {
        mNavigationView = findViewById(R.id.bottom_nav);
        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.simple_note:
                        type = DetailFragment.NOTE_TYPE_SIMPLE;
                        initFragmentList(type);
                        return true;
                    case R.id.important_note:
                        type = DetailFragment.NOTE_TYPE_IMPORTANT;
                        initFragmentList(type);
                        return true;
                    case R.id.password_note:
                        type = DetailFragment.NOTE_TYPE_PASSWORD;
                        initFragmentList(type);
                        return true;
                    case R.id.tools:
                        if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        }else bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        return false;
                }
                return false;
            }
        });

    }
private void initBottomSheet(){
    LinearLayout bottomSheet = findViewById(R.id.bottom_sheet);
    bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
    final FloatingActionButton fab = findViewById(R.id.add_button);
    bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View view, int i) {
            if(BottomSheetBehavior.STATE_EXPANDED == i){
                fab.setEnabled(false);
            }else fab.setEnabled(true);
        }

        @Override
        public void onSlide(@NonNull View view, float v) {
            fab.animate().scaleX(1 - v).scaleY(1 - v).setDuration(0).start();

        }
    });
}

    @Override
    public void onNoteSelected(Note note) { //Если ориентация портретная и R.id.fragment_container_detail нет в мкете то при клике на элемент списка запуститься NotePagerActivity
        if(findViewById(R.id.fragment_container_detail)== null){
            Intent intent = NotePagerActivity.newIntent(this,note.getUUID());
            startActivity(intent);
        }else{ // если ориентация альбомная и R.id.fragment_container_detail есть в макете то произайдет транзакция DetailFragment в контейнер в макете
            Fragment newDetail = DetailFragment.newInstance(note.getUUID());
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_detail,newDetail).commit();
        }
    }

}
