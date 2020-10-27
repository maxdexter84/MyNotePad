package com.maxdexter.mynote.ui.fragments.image_bottom

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ImageBottomViewModel(val path: String) : ViewModel() {

    private val _imageUriList = MutableLiveData<List<String>>()
            val imageUriList: LiveData<List<String>>
            get() = _imageUriList

    init {
        _imageUriList.value = getImageList()
    }

    private fun getImageList(): List<String>{
        var list = listOf<String>()
        if (path != ""){
            list = path.split(",") ?: listOf<String>()
            Log.i("CLICK", "$list")
        }
        return list
    }

}