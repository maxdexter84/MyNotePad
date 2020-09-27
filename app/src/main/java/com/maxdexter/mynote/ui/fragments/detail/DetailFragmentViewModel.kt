package com.maxdexter.mynote.ui.fragments.detail

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxdexter.mynote.data.NotePad
import com.maxdexter.mynote.extensions.currentDate
import com.maxdexter.mynote.model.Note
import com.maxdexter.mynote.utils.DetailEvent
import kotlinx.coroutines.*
import java.io.File
import java.util.*


class DetailFragmentViewModel(uuid: String, private val context: Context) : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    lateinit var note: Note
    private val _newNote = MutableLiveData<Note>()
            val newNote: LiveData<Note>
            get() = _newNote

    private var _eventType = MutableLiveData<Pair<DetailEvent,Intent>?>()
    val eventType: LiveData<Pair<DetailEvent,Intent>?>
            get() = _eventType

    init {
        _eventType.value = null
        selectNote(uuid)
    }

    private fun selectNote(uuid: String) {
        if (uuid == com.maxdexter.mynote.utils.NEW_NOTE) {
            note = Note()
            _newNote.value = note
        } else {
            NotePad.get(context)?.getNote(uuid)?.observeForever{
                if(it != null) {
                    note = it
                    _newNote.value = note
                }

            }

        }
    }




    fun changeTitle(title: String) {
        note.title = title
    }

    fun changeDescription(text: String) {
        note.description = text
    }

    fun changeType(type: Int) {
        note.typeNote = type
    }

    private fun changeDate() {
        note.date = Date().currentDate()
    }

    fun saveNote() {
        changeDate()
        if (note.title != "" || note.description != ""){
        uiScope.launch {
            NotePad.get(context)?.addNote(note)
            }
        }
        _eventType.value = Pair(DetailEvent.SAVE, Intent())
    }
    fun getPhotoFile(): File {
        val filesDir: File = context.filesDir
        return File(filesDir, note.photoFilename)
    }

    fun photoIntent() {
        val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val uri = getPhotoFile().let { FileProvider.getUriForFile(context, "com.maxdexter.mynote.fileprovider", it) }
        captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        val cameraActivity = context.packageManager.queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY)
        for (activity in cameraActivity) {
            context.grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
        _eventType.value = Pair(DetailEvent.PHOTO, captureImage)
    }

   fun galleryIntent(){
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        val uri = getPhotoFile()?.let { FileProvider.getUriForFile(context, "com.maxdexter.mynote.fileprovider", it) }
        galleryIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        val cameraActivity = context.packageManager.queryIntentActivities(galleryIntent, PackageManager.MATCH_DEFAULT_ONLY)
        for (activity in cameraActivity) {
            context.grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
       _eventType.value = Pair(DetailEvent.GALLERY, galleryIntent)
    }

    fun shareNote() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plane"
        shareIntent.putExtra(Intent.EXTRA_TITLE, note.title)
        shareIntent.putExtra(Intent.EXTRA_TEXT, note.description)
        _eventType.value = Pair(DetailEvent.SHARE, shareIntent)

    }

    fun deleteEvent() {
        _eventType.value = Pair(DetailEvent.DELETE, Intent())

    }
    fun deleteNote() {
        uiScope.launch {
            NotePad.get(context)?.deleteNote(note)
        }
    }



    override fun onCleared() {
        super.onCleared()

    }
}