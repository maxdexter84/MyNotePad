package com.maxdexter.mynote.ui.fragments.detail

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maxdexter.mynote.data.NoteRepository
import com.maxdexter.mynote.extensions.currentDate
import com.maxdexter.mynote.model.Note
import com.maxdexter.mynote.utils.DetailEvent
import kotlinx.coroutines.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class DetailFragmentViewModel(private val uuid: String, private val context: Context) : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    lateinit var note: Note
    lateinit var uri: Uri
    private val _newNote = MutableLiveData<Note>()
            val newNote: LiveData<Note>
            get() = _newNote

    private var _eventType = MutableLiveData<Pair<DetailEvent,Intent>?>()
    val eventType: LiveData<Pair<DetailEvent,Intent>?>
            get() = _eventType

    private val _imageList = MutableLiveData<List<String>>()
            val imageList: LiveData<List<String>>
            get() = _imageList

    init {
        _eventType.value = null
        selectNote(uuid)
    }

    private fun selectNote(uuid: String) {
        if (uuid == com.maxdexter.mynote.utils.NEW_NOTE) {
            note = Note()
            _newNote.value = note
        } else {
            noteChangeObserve()
        }
    }

   private fun noteChangeObserve() {
        NoteRepository.get()?.getNote(uuid)?.observeForever {
            if (it != null) {
                note = it
                _newNote.postValue(note)
                _imageList.value = getImageList(note)
            }

        }
    }
   private fun getImageList(note: Note): List<String>{
       var list = listOf<String>()
        if (note.photoFilename != ""){
            list = note.photoFilename.split(",") ?: listOf<String>()
        }
        return list
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

    fun addPhoto() {
        val allPhoto = StringBuffer()
        allPhoto.append("${note.photoFilename},")
        allPhoto.append("$uri")

        note.photoFilename = allPhoto.toString()
    }

    private fun changeDate() {
        note.date = Date().currentDate()
    }


    fun saveNote() {
        changeDate()
        if (note.title != "" || note.description != "" || note.photoFilename != ""){
        uiScope.launch {
            NoteRepository.get()?.addNote(note)
            }
        }
        _eventType.value = Pair(DetailEvent.SAVE, Intent())
    }
    fun getPhotoFile(): File {
        val filesDir: File = context.filesDir
        return File(filesDir, note.photoFilename)
    }

     fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = context.filesDir
        return File.createTempFile("JPEG_${timeStamp}_",".jpg",storageDir)

    }

    fun photoIntent() {
        val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        uri = createImageFile().let { FileProvider.getUriForFile(context, "com.maxdexter.mynote.fileprovider", it) }
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
        uri = createImageFile().let { FileProvider.getUriForFile(context, "com.maxdexter.mynote.fileprovider", it) }
        galleryIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        val cameraActivity = context.packageManager.queryIntentActivities(galleryIntent, PackageManager.MATCH_DEFAULT_ONLY)
        for (activity in cameraActivity) {
            context.grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
       _eventType.value = Pair(DetailEvent.GALLERY, galleryIntent)
    }


    fun shareNote() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        val uri = getPhotoFile().let { FileProvider.getUriForFile(context, "com.maxdexter.mynote.fileprovider", it) }
        shareIntent.putExtra(Intent.EXTRA_TITLE, note.title)
        shareIntent.putExtra(Intent.EXTRA_TEXT, note.description)
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareIntent.type = "*/*"
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        _eventType.value = Pair(DetailEvent.SHARE, shareIntent)
    }

    fun deleteEvent() {
        _eventType.value = Pair(DetailEvent.DELETE, Intent())
    }

    
    fun zoomImageEvent() {
        _eventType.value = Pair(DetailEvent.ZOOM_IMAGE, Intent())
    }
    fun deleteNote() {
        uiScope.launch {
            getPhotoFile().delete()
            NoteRepository.get()?.deleteNote(note)
        }
    }



    override fun onCleared() {
        super.onCleared()

    }
}