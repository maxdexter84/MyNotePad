package com.maxdexter.mynote.data.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.maxdexter.mynote.databinding.ListImageItemBinding
import com.maxdexter.mynote.extensions.setImagePrev

class ImageAdapter(val listener :(Int)->Unit): RecyclerView.Adapter<ImageViewHolder>() {

    private var list = listOf<String>()

    fun updateData(data: List<String>) {
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
        return list.size ?: 0
    }


}

class ImageViewHolder(val binding: ListImageItemBinding, val context: Context): RecyclerView.ViewHolder(binding.root) {

    fun bind(image: String, listener: (Int) -> Unit) {
        binding.noteImage.setImagePrev(context, image.toUri())
        binding.noteImage.setOnClickListener {
            listener.invoke(adapterPosition)
            Log.i("CLICK","$adapterPosition")
        }
    }
    companion object {
        fun from (parent: ViewGroup): ImageViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListImageItemBinding.inflate(layoutInflater,parent, false)
            return ImageViewHolder(binding, parent.context)
        }
    }
}


