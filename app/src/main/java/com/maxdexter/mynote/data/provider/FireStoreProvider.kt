package com.maxdexter.mynote.data.provider

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.maxdexter.mynote.model.Note
import com.maxdexter.mynote.data.errors.NoAuthException
import com.maxdexter.mynote.model.User

private const val USERS_COLLECTION = "users"
private const val NOTES_COLLECTION = "notes"
class FireStoreProvider : RemoteDataProvider {
    //Создадим свойство, представляющее пользователя, авторизованного в данный момент:
    private val currentUser
        get() = FirebaseAuth.getInstance().currentUser

    private val tag = "${FireStoreProvider::class.java.simpleName} :"
    /**
     * Корневым элементом структуры данных будет коллекция notes.
     * Вынесем это имя в константу верхнего уровня.
     * Чтобы создать новую коллекцию, достаточно получить экземпляр базы данных для приложения:*/
    private val db = FirebaseFirestore.getInstance()

    //Добавим метод для получения ссылки на коллекцию заметок пользователя:
    private fun getUserNotesCollection() = currentUser?.let {
        db.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
    } ?: throw NoAuthException()


    override fun getCurrentUser(): LiveData<User?> =
            MutableLiveData<User?>().apply {
                value = currentUser?.let { User(it.displayName ?: "",
                        it.email ?: "") }
            }

    @Suppress("unchecked_cast")
    override fun subscribeToAllNotes(): LiveData<MutableList<Note>> =
            MutableLiveData<MutableList<Note>>().apply {
                try {
                    getUserNotesCollection().addSnapshotListener { snapshot, e ->

                        if(snapshot != null && !snapshot.isEmpty) {
                            value = snapshot.documents.map { it.toObject(Note::class.java) } as MutableList<Note>?
                            e?.stackTraceToString()?.let { Log.e("TAG", it) }
                        }


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
                                Log.d(tag, "Note $note is saved")
                                value = note
                            }.addOnFailureListener {
                                Log.d(tag, "Error saving note $note, message: ${it.message}")
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
        var result = false
        getUserNotesCollection().document(note.uuid).delete()
                .addOnSuccessListener {
                    Log.d(tag, "DocumentSnapshot successfully deleted!")
                    result = true
                }.addOnFailureListener {
                    e -> Log.w(tag, "Error deleting document", e)
                }
        return result
    }




}