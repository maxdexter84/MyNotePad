package com.maxdexter.mynote.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.maxdexter.mynote.NotePagerActivity;
import com.maxdexter.mynote.R;
import com.maxdexter.mynote.data.Note;
import com.maxdexter.mynote.data.NotePad;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteListFragment extends Fragment {
public static final String TYPE_ID = "type_id";
private RecyclerView mRecyclerView;
private NoteAdapter mNoteAdapter;
private int type;
private FloatingActionButton fab;
private List<Note> listNew;
private Callbacks mCallbacks;
//Обязательный интерфейс для активности хоста
public interface Callbacks{
    void onNoteSelected(Note note);
}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);
        mRecyclerView = view.findViewById(R.id.note_list_id);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        assert getArguments() != null;
        type = getArguments().getInt(TYPE_ID);
        updateUI(type);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        mRecyclerView.addItemDecoration(itemTouchHelper);

        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        return view;
    }


    private void updateUI(int type) {
        List<Note>list= NotePad.get(getContext()).getNotes();
            listNew = new ArrayList<>();
            for(Note note: list){
                if(note.getTypeNote() == type){
                    listNew.add(note);
                }
            }
            mNoteAdapter = new NoteAdapter(listNew);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setAdapter(mNoteAdapter);
            mNoteAdapter.notifyDataSetChanged();
    }


    private class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        Note mNote;
        TextView textTitle;
        TextView textDate;
        NoteHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textTitle = itemView.findViewById(R.id.text_item_id);
            textDate = itemView.findViewById(R.id.date_item_id);

        }

        private void bind(Note note){
            mNote = note;
            textTitle.setText(mNote.getTitle());
            textDate.setText(mNote.getDate());

        }

        @Override
        public void onClick(View v) {
            mCallbacks.onNoteSelected(mNote);
//            Intent intent = NotePagerActivity.newIntent(getContext(),mNote.getUUID());
//            startActivity(intent);
        }

    }
    private class NoteAdapter extends RecyclerView.Adapter<NoteHolder>{


        private List<Note> mNoteList;


        NoteAdapter(List<Note> noteList) {
            this.mNoteList = noteList;

        }

        @NonNull
        @Override
        public NoteHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_note,viewGroup,false);
            if(i == DetailFragment.NOTE_TYPE_IMPORTANT){
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_fav_note,viewGroup,false);

            }else if(i == DetailFragment.NOTE_TYPE_PASSWORD){
               view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_pass_note,viewGroup,false);
            }



            return new NoteHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NoteHolder noteHolder, int i) {
            Note note = mNoteList.get(i);
            noteHolder.bind(note);
        }

        @Override
        public int getItemCount() {
            return mNoteList.size();
        }

        @Override
        public int getItemViewType(int position) {
            Note note = mNoteList.get(position);
            return note.getTypeNote();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI(type);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUI(type);
    }

    public static NoteListFragment newInstance(int type){
        Bundle args = new Bundle();
        args.putInt(TYPE_ID,type);
        NoteListFragment listFragment = new NoteListFragment();
        listFragment.setArguments(args);
        return listFragment;
    }

    //Удаление по свайпу в лево
    private final ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {

        @Override
        public boolean onMove(RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            if (direction == ItemTouchHelper.LEFT) {
                Snackbar.make(Objects.requireNonNull(getView()), "Точно удалить?", Snackbar.LENGTH_LONG).setAction("Да", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delItem(true, position);
                    }
                }).show();
                mNoteAdapter.notifyItemChanged(position);
            }

        }

    };

    public void delItem(boolean delete,int position){
        final Note note = listNew.get(position);
        if(delete){
            NotePad.get(getContext()).deleteNote(note);
            listNew.remove(position);
            mNoteAdapter.notifyItemRemoved(position);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
}
