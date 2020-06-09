package com.maxdexter.mynote;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

import com.maxdexter.mynote.data.Note;
import com.maxdexter.mynote.data.NotePad;
import com.maxdexter.mynote.ui.fragments.DetailFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NotePagerActivity extends AppCompatActivity {
    private File mPhotoFile;
    private ImageButton share;
    private ImageButton delete;
    private ImageButton voice;
    private ImageButton image;
    private Note currentNote;
    private FloatingActionButton mFloatingActionButton;
    private String noteId;
    private List<Note>currentList;
    private static final String EXTRA_NOTE = "note_id";
    private ViewPager mViewPager; //Создаем экземпляр ViewPager
    private List<Note> mNoteList;//Создаем список
    private static final int REQUEST_PHOTO =2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_pager);
        noteId = getIntent().getStringExtra(EXTRA_NOTE);

        currentList = newList(noteId);
        initViewPager(currentList);
        initFloatingAB();
        initButtonGroup();
        mPhotoFile = NotePad.get(this).getPhotoFile(currentNote);

    }

    private void photoIntent() {

        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(getPackageManager()) != null;
//            image.setEnabled(canTakePhoto);
        Uri uri = FileProvider.getUriForFile(this,"com.maxdexter.mynote.fileprovider",mPhotoFile);
        captureImage.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        List<ResolveInfo> cameraActivity = this.getPackageManager().queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);
        for(ResolveInfo activity: cameraActivity){
            this.grantUriPermission(activity.activityInfo.packageName,uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        startActivityForResult(captureImage,REQUEST_PHOTO);
    }

    private void initButtonGroup() {
        share = findViewById(R.id.share_button);
        delete = findViewById(R.id.delete_button);
        image = findViewById(R.id.add_image_button);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plane");
                intent.putExtra(Intent.EXTRA_TITLE,currentNote.getTitle());
                intent.putExtra(Intent.EXTRA_TEXT,currentNote.getDescription());
                startActivity(intent);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"Delete note?",Snackbar.LENGTH_LONG).setAction("Yes", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NotePad.get(getApplicationContext()).deleteNote(currentNote);
                        finish();
                    }
                }).show();
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoIntent();
            }
        });
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
                currentNote = mNoteList.get(i);
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
