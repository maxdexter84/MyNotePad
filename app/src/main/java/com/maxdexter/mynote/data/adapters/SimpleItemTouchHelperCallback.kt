package com.maxdexter.mynote.data.adapters

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


class SimpleItemTouchHelperCallback(private val adapter: ImageAdapter, private val swipeListener :(String) -> Unit) : ItemTouchHelper.Callback() {


    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END

        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        swipeListener.invoke(adapter.list[viewHolder.adapterPosition])
    }


    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE && viewHolder is ItemTouchViewHolder) {
            viewHolder.onItemSelected()
        }
       super.onSelectedChanged(viewHolder,actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        if (viewHolder is ItemTouchViewHolder){
            viewHolder.onItemCleared()
        }
        super.clearView(recyclerView, viewHolder)
    }


}
interface ItemTouchViewHolder{
    fun onItemSelected()
    fun onItemCleared()
}

//Код инициализации
//val itemTouchHelperCallback = SimpleItemTouchHelperCallback(adapter){
//    detailViewModel.deletePhoto(it)
//}
//val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
//itemTouchHelper.attachToRecyclerView(binding.recyclerView)