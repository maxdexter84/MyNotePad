package com.maxdexter.mynote.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.maxdexter.mynote.model.Note

@Dao
interface NoteDao {
    @Query("SELECT * FROM note")
    fun getAll():LiveData<List<Note>>

    @Query("SELECT * FROM note WHERE uuid = :uuid")
    fun getById(uuid: String): LiveData<Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insert(note: Note)

    @Update
   suspend fun update(note: Note)

    @Delete
   suspend fun delete(note: Note)
}