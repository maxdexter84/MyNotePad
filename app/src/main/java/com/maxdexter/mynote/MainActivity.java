package com.maxdexter.mynote;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SwitchCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import com.maxdexter.mynote.data.Note;
import com.maxdexter.mynote.data.NotePad;
import com.maxdexter.mynote.ui.fragments.DetailFragment;
import com.maxdexter.mynote.ui.fragments.NoteListFragment;

public class MainActivity extends AppCompatActivity {
    BottomSheetBehavior bottomSheetBehavior;
    private int type = 0;
    BottomNavigationView mNavigationView;
    FloatingActionButton mFloatingActionButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragmentList(type);
        initBottomNav();
        initFloatingActionButton();
        initBottomSheet();
    }

    private void initFloatingActionButton() {
        mFloatingActionButton = findViewById(R.id.add_button);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note note = new Note();
                NotePad.get(MainActivity.this).addNote(note);
                Intent intent = NotePagerActivity.newIntent(MainActivity.this,note.getUUID());
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

        }

        @Override
        public void onSlide(@NonNull View view, float v) {
            fab.animate().scaleX(1 - v).scaleY(1 - v).setDuration(0).start();
        }
    });
}

}
