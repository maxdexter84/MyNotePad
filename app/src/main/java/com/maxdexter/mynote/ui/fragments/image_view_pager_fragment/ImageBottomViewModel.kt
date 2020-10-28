package com.maxdexter.mynote.ui.fragments.image_view_pager_fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maxdexter.mynote.extensions.getImageList

class ImageBottomViewModel(val path: String) : ViewModel() {

    private val _imageUriList = MutableLiveData<List<String>>()
            val imageUriList: LiveData<List<String>>
            get() = _imageUriList

    init {
        _imageUriList.value = listOf<String>().getImageList(path)
    }


}