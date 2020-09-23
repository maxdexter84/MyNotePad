package com.maxdexter.mynote.data.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.maxdexter.mynote.databinding.ListItemNoteBinding
import com.maxdexter.mynote.model.Note
import java.util.*


class NoteAdapter (val clickListener: NoteListener): ListAdapter<Note,NoteViewHolder>(NoteAdapterDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener,item)

    }
}


class NoteViewHolder private constructor(private val binding: ListItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(clickListener: NoteListener, note: Note) = with (note){
        binding.item = note
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }


    companion object {
        fun from (parent: ViewGroup): NoteViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemNoteBinding.inflate(layoutInflater,parent, false)
            return NoteViewHolder(binding)
        }
    }

}

/**
 * Обратный вызов для вычисления разницы между двумя ненулевыми элементами в списке.
 *
 * Используется ListAdapter для расчета минимального количества изменений между старым списком и новым
 * список, который был передан в " submitList`.
 */
class NoteAdapterDiffCallback : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }

}

class NoteListener(val clickListener: (uuid: String) -> Unit) {
    fun onClick(note: Note) = clickListener(note.uuid)
}