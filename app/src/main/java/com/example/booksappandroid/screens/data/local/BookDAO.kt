package com.example.booksappandroid.screens.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.*

import com.example.booksappandroid.screens.data.Book;

@Dao
interface BookDAO {
    @Query("SELECT * from books ORDER BY title ASC")
    fun getAll(): LiveData<List<Book>>

    @Query("SELECT * FROM books WHERE _id=:id ")
    fun getById(id: String): LiveData<Book>

    @Query("DELETE FROM books WHERE _id=:id")
    suspend fun delete(id: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: Book)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(book: Book)

    @Query("DELETE FROM books")
    suspend fun deleteAll()
}
