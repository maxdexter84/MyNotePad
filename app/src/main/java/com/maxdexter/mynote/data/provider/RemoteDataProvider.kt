package com.maxdexter.mynote.data.provider

import androidx.lifecycle.LiveData
import com.maxdexter.mynote.model.Note
import com.maxdexter.mynote.model.User

/**
 * Теперь создадим классы доступа к данным.
 * Чтобы отвязать реализацию логики хранения данных от остальной логики приложения, создадим  интерфейс RemoteDataProvider:*/
interface RemoteDataProvider {
    fun subscribeToAllNotes(): LiveData<MutableList<Note>>
    fun getNoteById(uuid: String): LiveData<Note>
    fun saveNote(note: Note) : LiveData<Note>
    fun getCurrentUser(): LiveData<User?>
    suspend fun deleteNote(note: Note): Boolean

}
/**
 * Так как запросы к базе данных асинхронные, нужен механизм отложенного получения результата.
 * Будем использовать для этого LiveData. В качестве возвращаемого значения применим NoteResult.
 * В последнем методе осмысленного значения возвращаться не будет,
 * но при успешном сохранении заметки будем возвращать ее — чтобы понять, что операция успешно завершилась.*/