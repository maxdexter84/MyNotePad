package com.maxdexter.mynote.ui.fragments.allnote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.maxdexter.mynote.R
import com.maxdexter.mynote.data.NotePad
import com.maxdexter.mynote.data.adapters.NoteAdapter
import com.maxdexter.mynote.data.adapters.NoteListener
import com.maxdexter.mynote.databinding.FragmentNoteListBinding
import com.maxdexter.mynote.model.Note


class NoteListFragment : Fragment() {

    private var type = 0
    private var listNew: MutableList<Note>? = null

    private lateinit var binding: FragmentNoteListBinding
    private lateinit var adapter: NoteAdapter
    //Обязательный интерфейс для активности хоста
    private lateinit var viewModel: NoteListFragmentViewModel
    private lateinit var viewModelFactory: NoteListFragmentViewModelFactory



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_note_list, container, false)
        val context = context
        if (context != null) {viewModelFactory = NoteListFragmentViewModelFactory(context)}
        viewModel = ViewModelProvider(this, viewModelFactory).get(NoteListFragmentViewModel::class.java)


        binding.noteListId.layoutManager = LinearLayoutManager(activity)

        updateUI()
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        binding.noteListId.addItemDecoration(itemTouchHelper)
        itemTouchHelper.attachToRecyclerView(binding.noteListId)
        return binding.root
    }

    private fun updateUI() {
       // val list = NotePad.get(context).liveNotes
        adapter = NoteAdapter(NoteListener { it -> findNavController().navigate(NoteListFragmentDirections.actionNoteListFragmentToDetailFragment(it)) })
        viewModel.allNoteList.observe(viewLifecycleOwner, {adapter.submitList(it)})
        binding.noteListId.setHasFixedSize(true)
        binding.noteListId.adapter = adapter
        //            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    new Repository().loadToFireStore(list);
//                }
//            }).start();
    }

//    private inner class NoteHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
//        var mNote: Note? = null
//        var textTitle: TextView
//        var textDate: TextView
//        var mImageView: ImageView
//        fun bind(note: Note) {
//            mNote = note
//            textTitle.text = mNote!!.title
//            textDate.text = mNote!!.date
//            val file = NotePad.get(context).getPhotoFile(mNote)
//            if (file != null) {
//                mImageView.visibility = View.VISIBLE
//                Thread {
//                    val bitmap = PictureUtils.getScaleBitmap(file.path, activity)
//                    mImageView.post { mImageView.setImageBitmap(bitmap) }
//                }.start()
//            }
//        }
//
//        override fun onClick(v: View) {
//            mCallbacks!!.onNoteSelected(mNote)
//            //            Intent intent = NotePagerActivity.newIntent(getContext(),mNote.getUUID());
////            startActivity(intent);
//        }
//
//        init {
//            itemView.setOnClickListener(this)
//            textTitle = itemView.findViewById(R.id.text_item_id)
//            textDate = itemView.findViewById(R.id.date_item_id)
//            mImageView = itemView.findViewById(R.id.image)
//        }
//    }
//
//    private inner class NoteAdapter internal constructor(private val mNoteList: List<Note>) : RecyclerView.Adapter<NoteHolder>() {
//        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): NoteHolder {
//            val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_item_note, viewGroup, false)
//            return NoteHolder(view)
//        }
//
//        override fun onBindViewHolder(noteHolder: NoteHolder, i: Int) {
//            val note = mNoteList[i]
//            noteHolder.bind(note)
//        }
//
//        override fun getItemCount(): Int {
//            return mNoteList.size
//        }
//
//        override fun getItemViewType(position: Int): Int {
//            val note = mNoteList[position]
//            return note.typeNote
//        }
//    }



    //Удаление по свайпу в лево
    private val itemTouchHelperCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            if (direction == ItemTouchHelper.LEFT) {
                Snackbar.make(requireView(), "Точно удалить?", Snackbar.LENGTH_LONG).setAction("Да") { delItem(true, position) }.show()

            }
        }
    }

    fun delItem(delete: Boolean, position: Int) {
        val note = listNew!![position]
        if (delete) {
            NotePad.get(context).deleteNote(note)
            listNew!!.removeAt(position)

        }
    }



    companion object {
        const val TYPE_ID = "type_id"
        fun newInstance(type: Int): NoteListFragment {
            val args = Bundle()
            args.putInt(TYPE_ID, type)
            val listFragment = NoteListFragment()
            listFragment.arguments = args
            return listFragment
        }
    }
}