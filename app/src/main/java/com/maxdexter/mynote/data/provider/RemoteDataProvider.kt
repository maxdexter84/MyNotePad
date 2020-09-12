package com.maxdexter.mynote.data.provider

import androidx.lifecycle.LiveData
import com.maxdexter.mynote.data.Note
import com.maxdexter.mynote.model.NoteResult
/**
 * Теперь создадим классы доступа к данным.
 * Чтобы отвязать реализацию логики хранения данных от остальной логики приложения, создадим  интерфейс RemoteDataProvider:*/
interface RemoteDataProvider {
    fun subscribeToAllNotes(): LiveData<NoteResult>
    fun getNoteById(id: String): LiveData<NoteResult>
    fun saveNote(note: Note) : LiveData<NoteResult>
}
/**
 * Так как запросы к базе данных асинхронные, нужен механизм отложенного получения результата.
 * Будем использовать для этого LiveData. В качестве возвращаемого значения применим NoteResult.
 * В последнем методе осмысленного значения возвращаться не будет,
 * но при успешном сохранении заметки будем возвращать ее — чтобы понять, что операция успешно завершилась.*/