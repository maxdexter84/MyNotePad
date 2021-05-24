package com.maxdexter.mynote.ui.adapters

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.maxdexter.mynote.R
import com.maxdexter.mynote.databinding.ListItemNoteBinding
import com.maxdexter.mynote.model.Note
import com.maxdexter.mynote.ui.fragments.allnote.NoteListFragmentViewModel
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


class NoteAdapter (val viewModel: NoteListFragmentViewModel,val clickListener: NoteListener): ListAdapter<Note,NoteViewHolder>(NoteAdapterDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener,item)
    }
    val simpleCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        //ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position: Int = viewHolder.adapterPosition // выясняем позицию элемента в адаптере
            val item: Note = getItem(position)
            when (direction) {
                ItemTouchHelper.LEFT -> {
                    viewModel.deleteNote(item, viewHolder.itemView)
                    notifyItemRemoved(position) //обновляем адаптер
                }
            }
        }

        override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
            RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(R.color.color_red)
                    .addSwipeLeftActionIcon(R.drawable.delete_dark_icone)
                    .create()
                    .decorate()
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
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