package com.maxdexter.mynote.ui.fragments;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.maxdexter.mynote.DetailActivity;
import com.maxdexter.mynote.R;
import com.maxdexter.mynote.data.Note;
import com.maxdexter.mynote.data.NotePad;

import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {
    private static final String ARG_NOTE_ID = "note_id";
    public static final int NOTE_TYPE_SIMPLE = 0;
    public static final int NOTE_TYPE_IMPORTANT = 1;
    public static final int NOTE_TYPE_PASSWORD = 2;
    private Note mNote;
    private EditText mDescriptionField;
    private EditText mTitle;
    private FloatingActionButton mButton;
    private RadioGroup mRadioGroup;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        String noteId = getArguments().getString(ARG_NOTE_ID);// Получение идентификатора заметки из аргументов
        assert noteId != null;
        mNote = NotePad.get(getActivity()).getNote(noteId);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        setRadioButton(view);
        getTextDescript(view);
        getTextTitle(view);
        initRadioGroup(view);
        initFAB();
        return view;
    }

    private void initFAB() {
        mButton = getActivity().findViewById(R.id.floatingActionButton_save);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotePad.get(getContext()).addNote(mNote);
                getActivity().finish();
            }
        });
    }

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
                        return;
                }
            }
        });
    }



    private void getTextTitle(View view) {
        mTitle = view.findViewById(R.id.title_id);
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
}
