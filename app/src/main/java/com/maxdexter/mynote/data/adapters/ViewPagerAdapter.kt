package com.maxdexter.mynote.data.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maxdexter.mynote.databinding.ListImageItemBinding
import com.maxdexter.mynote.databinding.ListImageViewpagerItemBinding
import com.maxdexter.mynote.extensions.setImage

class ViewPagerAdapter(val list:List<String>): RecyclerView.Adapter<ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val imageUri = list[position]
        holder.bind(imageUri)
    }

}

class ViewHolder(val binding: ListImageViewpagerItemBinding, val context: Context): RecyclerView.ViewHolder(binding.root) {
    fun bind(uri: String){
        binding.imageViewPager.setImage(context, uri)
    }
    companion object{
        fun from(parent: ViewGroup): ViewHolder{
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListImageViewpagerItemBinding.inflate(layoutInflater,parent, false)
            return ViewHolder(binding, parent.context)
        }
    }
}