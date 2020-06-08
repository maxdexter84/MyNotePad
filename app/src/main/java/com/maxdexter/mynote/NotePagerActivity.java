package com.maxdexter.mynote;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.maxdexter.mynote.data.Note;
import com.maxdexter.mynote.data.NotePad;
import com.maxdexter.mynote.ui.fragments.DetailFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NotePagerActivity extends AppCompatActivity {

    private FloatingActionButton mFloatingActionButton;
    private String noteId;
   private List<Note>currentList;
    private static final String EXTRA_NOTE = "note_id";
    private ViewPager mViewPager; //Создаем экземпляр ViewPager
private List<Note> mNoteList;//Создаем список
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_pager);
        noteId = getIntent().getStringExtra(EXTRA_NOTE);

        currentList = newList(noteId);
        initViewPager(currentList);

        initFloatingAB();
    }

    private void initFloatingAB() {
        mFloatingActionButton = findViewById(R.id.floatingActionButton_save);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViewPager(List<Note>list) {
        mViewPager = findViewById(R.id.note_view_pager); //Инициализируем ViewPager
        mNoteList = list;
        final List<Note>currentList = new ArrayList<>();
        FragmentManager fragmentManager = getSupportFragmentManager(); //Создаем FragmentManager
        mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {//Адаптером назначается безымянный экземпляр FragmentStatePagerAdapter
            @Override
            public Fragment getItem(int i) {
                Note note = mNoteList.get(i);

                return DetailFragment.newInstance(note.getUUID());
            }

            @Override
            public int getCount() {
                return mNoteList.size();
            }
        });
        for (int i = 0; i < mNoteList.size(); i++) {
            if(mNoteList.get(i).getUUID().equals(noteId)){
                mViewPager.setCurrentItem(i);
            }
        }
    }

    public static Intent newIntent(Context context, String noteId){
        Intent intent = new Intent(context,NotePagerActivity.class);
        intent.putExtra(EXTRA_NOTE,noteId);
        return intent;
    }
    private List<Note> newList(String id){
        List<Note> currentList = new ArrayList<>();
        List<Note>list = NotePad.get(this).getDatabase().mNoteDao().getAll();
        int type = NotePad.get(this).getNote(id).getTypeNote();
        for(Note n: NotePad.get(this).getNotes()){
            if(n.getTypeNote() == type){
                currentList.add(n);
            }
        }
        return currentList;
    }
}