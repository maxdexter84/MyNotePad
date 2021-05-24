package com.maxdexter.mynote.ui.fragments.detail

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maxdexter.mynote.data.NoteRepository
import com.maxdexter.mynote.extensions.currentDate
import com.maxdexter.mynote.extensions.deleteImageFromList
import com.maxdexter.mynote.extensions.getFile
import com.maxdexter.mynote.extensions.getImageList
import com.maxdexter.mynote.model.Note
import com.maxdexter.mynote.utils.DetailEvent
import kotlinx.coroutines.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class DetailFragmentViewModel(private val uuid: String, private val context: Context) : ViewModel() {
    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    lateinit var note: Note
    lateinit var uri: Uri
    lateinit var file: File
    private val _newNote = MutableLiveData<Note>()
            val newNote: LiveData<Note>
            get() = _newNote

    private var _eventType = MutableLiveData<Pair<DetailEvent,Intent>?>()
    val eventType: LiveData<Pair<DetailEvent,Intent>?>
            get() = _eventType

    private val _imageList = MutableLiveData<MutableList<String>>()
            val imageList: LiveData<MutableList<String>>
            get() = _imageList

    init {
        _eventType.value = null
        selectNote(uuid)
    }

    private fun selectNote(uuid: String) {
        if (uuid == com.maxdexter.mynote.utils.NEW_NOTE) {
            note = Note()
            saveEmptyNote()
            noteChangeObserve(note.uuid)
        } else {
            noteChangeObserve(uuid)
        }
    }

   private fun noteChangeObserve(uuid: String) {
        NoteRepository.get(context)?.getNote(uuid)?.observeForever {
            if (it != null) {
                note = it
                _newNote.postValue(note)
                _imageList.value = mutableListOf<String>().getImageList(note.photoFilename) //getImageList(note)
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

    fun addPhoto() {
        if (note.photoFilename == "")  note.photoFilename = "$uri"
        else{
            val oldImage = note.photoFilename
            val newImage = "$oldImage,$uri"
            note.photoFilename = newImage
        }
        saveEmptyNote()
    }

    fun deletePhoto(photo: String){
        val result = getPhotoFile(photo).delete()
        Log.i("DELETE", "$result")
        val photoUriList = mutableListOf<String>().getImageList(note.photoFilename)
        note.photoFilename = mutableListOf<String>().deleteImageFromList(photoUriList, photo)
        _imageList.value = mutableListOf<String>().getImageList(note.photoFilename)
        saveEmptyNote()
    }

    private fun changeDate() {
        note.date = Date().currentDate()
    }


    fun saveNote() {
        changeDate()
        if (note.title != "" || note.description != "" || note.photoFilename != ""){
        uiScope.launch {
            NoteRepository.get(context)?.addNote(note)
            }
        } else {
            uiScope.launch {
                NoteRepository.get(context)?.deleteNote(note)
            }
        }
        _eventType.value = Pair(DetailEvent.SAVE, Intent())
    }
   fun saveEmptyNote(){
        changeDate()
        uiScope.launch {
            NoteRepository.get(context)?.addNote(note)
        }
    }
    private fun getPhotoFile(uri:String): File {
        val storageDir: File? = context.filesDir
        return File(storageDir, uri)

    }

     private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = context.filesDir
        return File.createTempFile("JPEG_${timeStamp}_",".jpg",storageDir)

    }

    fun photoIntent() {
        val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        file = createImageFile()
        uri = file.let { FileProvider.getUriForFile(context, "com.maxdexter.mynote.fileprovider", it) }
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
        file = createImageFile()
        uri = file.let { FileProvider.getUriForFile(context, "com.maxdexter.mynote.fileprovider", it) }
        galleryIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        val cameraActivity = context.packageManager.queryIntentActivities(galleryIntent, PackageManager.MATCH_DEFAULT_ONLY)
        for (activity in cameraActivity) {
            context.grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
       _eventType.value = Pair(DetailEvent.GALLERY, galleryIntent)
    }


    fun shareNote() {
        val shareIntent = Intent(Intent.ACTION_SEND_MULTIPLE)
        val arrayUri = arrayListOf<Uri>()
        _imageList.value?.forEach { it ->
            arrayUri.add(it.toUri())
        }
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, arrayUri)
        shareIntent.putExtra(Intent.EXTRA_TITLE, note.title)
        shareIntent.putExtra(Intent.EXTRA_TEXT, note.description)

        shareIntent.type = "*/*"
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        _eventType.value = Pair(DetailEvent.SHARE, shareIntent)
    }

    fun deleteEvent() {
        _eventType.value = Pair(DetailEvent.DELETE, Intent())
    }

    fun deleteNote() {
        uiScope.launch {
            _imageList.value?.forEach { it -> it.substring(56,it.lastIndex + 1).let {
                val result = getPhotoFile(it).delete()
                Log.i("DELETE", "$result  file $it  not delete")
            }
            }
            NoteRepository.get(context)?.deleteNote(note)
        }
    }



    override fun onCleared() {
        super.onCleared()

    }
}