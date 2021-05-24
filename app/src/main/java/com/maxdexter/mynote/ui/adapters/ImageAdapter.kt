package com.maxdexter.mynote.ui.adapters

import android.content.Context
import android.graphics.Canvas
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.maxdexter.mynote.R
import com.maxdexter.mynote.databinding.ListImageItemBinding
import com.maxdexter.mynote.extensions.setImagePrev
import com.maxdexter.mynote.ui.fragments.detail.DetailFragmentViewModel
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class ImageAdapter(val viewModel: DetailFragmentViewModel,val listener: (Int) -> Unit): RecyclerView.Adapter<ImageViewHolder>(){

    var list = mutableListOf<String>()

    fun updateData(data: MutableList<String>) {
        this.list = data

        val diffUtil = object : DiffUtil.Callback(){
            override fun getOldListSize(): Int {
                return list.size
            }

            override fun getNewListSize(): Int {
                return data.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return list[oldItemPosition] == data[newItemPosition]
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return list[oldItemPosition].hashCode() == data[newItemPosition].hashCode()
            }
        }
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        list = data
        diffResult.dispatchUpdatesTo(this)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {

       return ImageViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = list[position]
        holder.bind(image, listener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

     val simpleCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.UP) {
        //ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position: Int = viewHolder.adapterPosition // выясняем позицию элемента в адаптере
            val imageUri: String = list[position]
            when (direction) {
                ItemTouchHelper.UP -> {
                    viewModel.deletePhoto(imageUri)
//                mWeatherList.removeAt(position) // удаляем из списка
//                historyAdapter.notifyItemRemoved(position) //обновляем адаптер
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

class ImageViewHolder(val binding: ListImageItemBinding, val context: Context): RecyclerView.ViewHolder(binding.root) {

    fun bind(image: String, listener: (Int) -> Unit) {
        binding.noteImage.setImagePrev(context, image.toUri())
        binding.noteImage.setOnClickListener {
            listener.invoke(adapterPosition)
            Log.i("CLICK", "$adapterPosition")
        }
    }
    companion object {
        fun from(parent: ViewGroup): ImageViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListImageItemBinding.inflate(layoutInflater, parent, false)
            return ImageViewHolder(binding, parent.context)
        }
    }

}


