package com.maxdexter.mynote.data.provider

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.*
import com.maxdexter.mynote.data.Note
import com.maxdexter.mynote.model.NoteResult

private const val NOTES_COLLECTION = "notes"
class FireStoreProvider : RemoteDataProvider {
    private val TAG = "${FireStoreProvider::class.java.simpleName} :"
    /**
     * Корневым элементом структуры данных будет коллекция notes.
     * Вынесем это имя в константу верхнего уровня.
     * Чтобы создать новую коллекцию, достаточно получить экземпляр базы данных для приложения:*/
    private val db = FirebaseFirestore.getInstance()

    /**
     * И вызвать у него метод collection() с именем нужной коллекции:*/
    private val notesReference = db.collection(NOTES_COLLECTION )


    override fun subscribeToAllNotes(): LiveData<NoteResult> =
        MutableLiveData<NoteResult>().apply {
        notesReference.addSnapshotListener { snapshot, e ->
            value = e?.let { NoteResult.Error(it) }
                    ?: snapshot.let {
                        val notes = it?.documents?.map { it.toObject(Note::class.java) }
                        NoteResult.Success(notes)
                    }
            }
    }



    override fun getNoteById(id: String): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.document(id).get()
                .addOnSuccessListener(object : OnSuccessListener<DocumentSnapshot> {
                    override fun onSuccess(snapshot: DocumentSnapshot) {
                        result.value =
                                NoteResult.Success(snapshot.toObject(Note::class.java))
                    }
                }).addOnFailureListener { result.value = NoteResult.Error(it) }

        return result
    }

    override fun saveNote(note: Note): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()

        notesReference.document(note.uuid)
                .set(note).addOnSuccessListener(
                        object : OnSuccessListener<Void> {

                            override fun onSuccess(p0: Void?) {
                                Log.d(TAG, "Note $note is saved")
                                result.value = NoteResult.Success(note)
                            }

                        }).addOnFailureListener {
                    object : OnFailureListener {

                        override fun onFailure(p0: Exception) {
                            Log.d(TAG, "Error saving note $note, message: ${p0.message}")
                            result.value = NoteResult.Error(p0)
                        }
                    }
                }

        return result
    }
}