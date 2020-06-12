package com.maxdexter.mynote.ui.fragments;
import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.maxdexter.mynote.DetailActivity;
import com.maxdexter.mynote.R;
import com.maxdexter.mynote.SharedPref;
import com.maxdexter.mynote.data.Note;
import com.maxdexter.mynote.data.NotePad;
import com.maxdexter.mynote.data.PictureUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {
    private static final String ARG_NOTE_ID = "note_id";
    public static final int NOTE_TYPE_SIMPLE = 0;
    public static final int NOTE_TYPE_IMPORTANT = 1;
    public static final int NOTE_TYPE_PASSWORD = 2;
    private static final int REQUEST_PHOTO = 2;
    private static final int REQUEST_GALLERY =3 ;
    private static float text_size = 0;
    private ImageButton share;
    private ImageButton delete;
    private ImageButton voice;
    private ImageButton image;
    private ImageButton gallery;
    private Note mNote;
    private EditText mDescriptionField;
    private EditText mTitle;
    private FloatingActionButton mButton;
    private RadioGroup mRadioGroup;
    private File mPhotoFile;
    private ImageView photo;
    SharedPref mSharedPref;
    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        mSharedPref = new SharedPref(getActivity());
        String noteId = getArguments().getString(ARG_NOTE_ID);// Получение идентификатора заметки из аргументов
        assert noteId != null;
        mNote = NotePad.get(getActivity()).getNote(noteId);
        mPhotoFile = NotePad.get(getActivity()).getPhotoFile(mNote);
        text_size = mSharedPref.getTextSize();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        setRadioButton(view);
        getTextDescript(view);
        getTextTitle(view);
        initRadioGroup(view);
       // initFAB();
        initButtonGroup(view);
        initImageButton(view);
        updatePhotoView();

        return view;
    }

    private void initImageButton(View view) {
        photo = view.findViewById(R.id.image_view_fragment_detail);
        registerForContextMenu(photo);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Click!!", Toast.LENGTH_SHORT).show();
                Intent intent = DetailActivity.newIntent(getActivity(),mNote.getUUID());
                startActivity(intent);
            }
        });
    }

    private void updatePhotoView(){
        if(mPhotoFile == null || !mPhotoFile.exists()){
            photo.setVisibility(View.INVISIBLE);
        }else{
            photo.setVisibility(View.VISIBLE);
            Bitmap bitmap = PictureUtils.getScaleBitmap(mPhotoFile.getPath(), Objects.requireNonNull(getActivity()));
            photo.setImageBitmap(bitmap);
        }
    }


    private void initButtonGroup(View view) {
        share = view.findViewById(R.id.share_button);
        delete = view.findViewById(R.id.delete_button);
        image = view.findViewById(R.id.add_image_button);
        gallery = view.findViewById(R.id.add_gallery_button);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plane");
                intent.putExtra(Intent.EXTRA_TITLE,mNote.getTitle());
                intent.putExtra(Intent.EXTRA_TEXT,mNote.getDescription());
                startActivity(intent);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"Delete note?",Snackbar.LENGTH_LONG).setAction("Yes", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NotePad.get(getContext()).deleteNote(mNote);
                        Objects.requireNonNull(getActivity()).finish();
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
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryIntent();
            }
        });
    }

    private void photoIntent() {
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Uri uri = FileProvider.getUriForFile(Objects.requireNonNull(getActivity()),"com.maxdexter.mynote.fileprovider",mPhotoFile);
        captureImage.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        List<ResolveInfo> cameraActivity = getActivity().getPackageManager().queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);
        for(ResolveInfo activity: cameraActivity){
            getActivity().grantUriPermission(activity.activityInfo.packageName,uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        startActivityForResult(captureImage,REQUEST_PHOTO);
    }
    private void galleryIntent() {
        final Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        Uri uri = FileProvider.getUriForFile(Objects.requireNonNull(getActivity()),"com.maxdexter.mynote.fileprovider",mPhotoFile);
        galleryIntent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        List<ResolveInfo> cameraActivity = getActivity().getPackageManager().queryIntentActivities(galleryIntent, PackageManager.MATCH_DEFAULT_ONLY);
        for(ResolveInfo activity: cameraActivity){
            getActivity().grantUriPermission(activity.activityInfo.packageName,uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        startActivityForResult(galleryIntent,REQUEST_GALLERY);
    }

//    private void initFAB() {
//        mButton = getActivity().findViewById(R.id.floatingActionButton_save);
//        mButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NotePad.get(getContext()).addNote(mNote);
//                getActivity().finish();
//            }
//        });
//    }

    //этот метод создает экземпляр фрагмента , упаковывает и задает его аргументы(этот метод вызывается в активносте хосте)
    public static DetailFragment newInstance(String noteId){//Присоединение аргументов к фрагменту
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTE_ID,noteId);
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(args);
        return detailFragment;
    }
   private void setRadioButton(View view){
       mRadioGroup = view.findViewById(R.id.radioGroup);
       RadioButton radioButton;
       if(mNote.getTypeNote() == DetailFragment.NOTE_TYPE_SIMPLE){
           radioButton = mRadioGroup.findViewById(R.id.simple_radio_btn);
           radioButton.setChecked(true);
       }else if(mNote.getTypeNote() == DetailFragment.NOTE_TYPE_IMPORTANT){
           radioButton = mRadioGroup.findViewById(R.id.important_radio_btn);
           radioButton.setChecked(true);
       }else if(mNote.getTypeNote() == DetailFragment.NOTE_TYPE_PASSWORD){
           radioButton = mRadioGroup.findViewById(R.id.password_radio_btn);
           radioButton.setChecked(true);
       }

   }
    private void initRadioGroup(View view) {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.simple_radio_btn:
                        mNote.setTypeNote(NOTE_TYPE_SIMPLE);
                        NotePad.get(getContext()).getDatabase().mNoteDao().update(mNote);
                       return ;
                    case R.id.important_radio_btn:
                        mNote.setTypeNote(NOTE_TYPE_IMPORTANT);
                        NotePad.get(getContext()).getDatabase().mNoteDao().update(mNote);
                        return;
                    case R.id.password_radio_btn:
                        mNote.setTypeNote(NOTE_TYPE_PASSWORD);
                        NotePad.get(getContext()).getDatabase().mNoteDao().update(mNote);
                }
            }
        });
    }



    private void getTextTitle(View view) {
        mTitle = view.findViewById(R.id.title_id);
        mTitle.setTextSize(text_size);
        mTitle.setText(mNote.getTitle());
        mTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
             mNote.setTitle(s.toString());
            NotePad.get(getContext()).addNote(mNote);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getTextDescript(View view) {
        mDescriptionField = view.findViewById(R.id.descript_id);
        mDescriptionField.setTextSize(text_size);
        mDescriptionField.setText(mNote.getDescription());
        mDescriptionField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNote.setDescription(s.toString());
                NotePad.get(getContext()).addNote(mNote);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_PHOTO){
            Uri uri = FileProvider.getUriForFile(Objects.requireNonNull(getActivity()),"com.maxdexter.mynote.fileprovider",mPhotoFile);
            getActivity().revokeUriPermission(uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updatePhotoView();
        }
        if (requestCode == REQUEST_GALLERY && data != null) {
            // Получаем URI изображения
            Uri imageUri = data.getData();
            if (imageUri != null) {
                try {
                    // Получаем InputStream, из которого будем декодировать Bitmap
                    InputStream inputStream = Objects.requireNonNull(getContext()).getContentResolver().openInputStream(imageUri);
                    FileOutputStream fos = Objects.requireNonNull(getActivity()).openFileOutput(mPhotoFile.getName(), Context.MODE_PRIVATE);
                    assert inputStream != null;
                    byte[]image = new byte[inputStream.available()];
                    inputStream.read(image);
                    fos.write(image);
                    updatePhotoView();
                    inputStream.close();
                } catch (NullPointerException|IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = Objects.requireNonNull(getActivity()).getMenuInflater();
        inflater.inflate(R.menu.context_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.delete){
            mPhotoFile.delete();
            Objects.requireNonNull(getActivity()).recreate();
        }
        return super.onContextItemSelected(item);

    }
}
