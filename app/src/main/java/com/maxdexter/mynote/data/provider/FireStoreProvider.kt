package com.maxdexter.mynote.data.provider

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.maxdexter.mynote.data.Note
import com.maxdexter.mynote.data.errors.NoAuthException
import com.maxdexter.mynote.model.NoteResult
import com.maxdexter.mynote.model.User

private const val USERS_COLLECTION = "users"
private const val NOTES_COLLECTION = "notes"
class FireStoreProvider : RemoteDataProvider {
    //Создадим свойство, представляющее пользователя, авторизованного в данный момент:
    private val currentUser
        get() = FirebaseAuth.getInstance().currentUser

    private val TAG = "${FireStoreProvider::class.java.simpleName} :"
    /**
     * Корневым элементом структуры данных будет коллекция notes.
     * Вынесем это имя в константу верхнего уровня.
     * Чтобы создать новую коллекцию, достаточно получить экземпляр базы данных для приложения:*/
    private val db = FirebaseFirestore.getInstance()


    /**
     * И вызвать у него метод collection() с именем нужной коллекции:*/
    private val notesReference = db.collection(NOTES_COLLECTION )

            //Добавим метод для получения ссылки на коллекцию заметок пользователя:
    private fun getUserNotesCollection() = currentUser?.let {
        db.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
    } ?: throw NoAuthException()


    override fun getCurrentUser(): LiveData<User?> =
            MutableLiveData<User?>().apply {
                value = currentUser?.let { User(it.displayName ?: "",
                        it.email ?: "") }
            }


    override fun subscribeToAllNotes(): LiveData<MutableList<Note>> =
            MutableLiveData<MutableList<Note>>().apply {
                try {
                    getUserNotesCollection().addSnapshotListener { snapshot, e ->
                        value = snapshot?.documents?.map { it.toObject(Note::class.java)!! } as MutableList<Note>?
                    }
                }catch (e: Throwable) {
                    Log.e("TAG", e.stackTraceToString())
                }
            }

    override fun saveNote(note: Note): LiveData<Note> =
            MutableLiveData<Note>().apply {
                try {
                    getUserNotesCollection().document(note.id.toString())
                            .set(note).addOnSuccessListener {
                                Log.d(TAG, "Note $note is saved")
                                value = note
                            }.addOnFailureListener {
                                Log.d(TAG, "Error saving note $note, message: ${it.message}")
                                throw it
                            }
                } catch (e: Throwable) {
                    Log.e("TAG", e.stackTraceToString())
                }
            }

    override fun getNoteById(uuid: String): LiveData<Note> =
            MutableLiveData<Note>().apply {
                try {
                    getUserNotesCollection().document(uuid).get()
                            .addOnSuccessListener {
                                value = it.toObject(Note::class.java)
                            }.addOnFailureListener {
                                throw it
                            }
                } catch (e: Throwable) {
                    Log.e("TAG", e.stackTraceToString())
                }
            }

    override suspend fun deleteNote(note: Note): Boolean {
        var result: Boolean = false
        getUserNotesCollection().document(note.uuid).delete()
                .addOnSuccessListener {result = true
                    Log.d(TAG, "DocumentSnapshot successfully deleted!")
                }.addOnFailureListener {
                    e -> Log.w(TAG, "Error deleting document", e)
                }
        return result
    }
}