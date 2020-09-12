package com.maxdexter.mynote;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.maxdexter.mynote.ui.fragments.FullscreenFragment;

import java.util.Objects;

public class DetailActivity extends AppCompatActivity {
private static final String EXTRA_NOTE_ID = "EXTRA_NOTE_ID";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        String noteId = Objects.requireNonNull(getIntent().getExtras()).getString(EXTRA_NOTE_ID);
        initDetailFragment(noteId);

    }

    private void initDetailFragment(String noteId) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container_detail);
        if(fragment == null){
            fragment = FullscreenFragment.newInstance(noteId);// Передаем идинтификатор заметки в FullscreenFragment через метод newInstance
            fm.beginTransaction().add(R.id.fragment_container_detail,fragment).commit();
        }
    }

    public static Intent newIntent(Context context, String noteId){
    Intent intent = new Intent(context,DetailActivity.class);
    intent.putExtra(EXTRA_NOTE_ID,noteId);
    return intent;
}





}
