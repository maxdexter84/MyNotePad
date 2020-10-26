package com.maxdexter.mynote.data.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.maxdexter.mynote.R
import com.maxdexter.mynote.databinding.ListImageItemBinding
import com.maxdexter.mynote.databinding.ListItemNoteBinding
import com.maxdexter.mynote.extensions.setImage
import com.maxdexter.mynote.extensions.setImagePrev
import com.maxdexter.mynote.model.Note

class ImageAdapter(private val list: List<String>) : RecyclerView.Adapter<ImageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {

       return ImageViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = list.get(position)
        holder.bind(image)
    }

    override fun getItemCount(): Int {
        return list.size ?: 0
    }


}

class ImageViewHolder(val binding: ListImageItemBinding, val context: Context): RecyclerView.ViewHolder(binding.root) {

    fun bind(image: String){
        binding.noteImage.setImagePrev(context, image.toUri())
    }
    companion object {
        fun from (parent: ViewGroup): ImageViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListImageItemBinding.inflate(layoutInflater,parent, false)
            return ImageViewHolder(binding, parent.context)
        }
    }

}
