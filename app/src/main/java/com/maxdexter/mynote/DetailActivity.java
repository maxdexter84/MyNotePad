package com.maxdexter.mynote;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.maxdexter.mynote.data.NotePad;
import com.maxdexter.mynote.ui.fragments.DetailFragment;

import java.util.Objects;
import java.util.UUID;

public class DetailActivity extends AppCompatActivity {
private static final String EXTRA_NOTE = "note_id";
private FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mFloatingActionButton = findViewById(R.id.floatingActionButton_save);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        initDetailFragment();

    }

    private void initDetailFragment() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container_detail);
        String noteId = Objects.requireNonNull(getIntent().getExtras()).getString(EXTRA_NOTE);
        if(fragment == null){
            fragment = DetailFragment.newInstance(noteId);// Передаем идинтификатор заметки в DetailFragment через метод newInstance
            fm.beginTransaction().add(R.id.fragment_container_detail,fragment).commit();
        }
    }

    public static Intent newIntent(Context context, String noteId){
    Intent intent = new Intent(context,DetailActivity.class);
    intent.putExtra(EXTRA_NOTE,noteId);
    return intent;
}





}
