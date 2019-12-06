package com.example.booksappandroid.screens.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.booksappandroid.core.Result
import com.example.booksappandroid.screens.data.local.BookDAO
import com.example.booksappandroid.screens.data.remote.BookApi
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JSON
import kotlinx.serialization.stringify
import org.json.JSONObject


class BookRepository(private val bookDao: BookDAO) {

    val books = bookDao.getAll()

    suspend fun refresh(): Result<Boolean> {
        try {
            val books = BookApi.service.find()
            for (book in books) {
                bookDao.insert(book)
            }
            return Result.Success(true)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    fun getById(itemId: String): LiveData<Book> {
        return bookDao.getById(itemId)
    }

    suspend fun save(book: Book): Result<Book> {
        try {
            val bookJson = BookJson(book.title, book.author, book.price)
            val createdItem = BookApi.service.create(bookJson)
            bookDao.insert(createdItem)
            return Result.Success(createdItem)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    suspend fun delete(book: Book):Result<String> {
        try {
            BookApi.service.delete(book._id);
            bookDao.delete(book._id)
            return Result.Success("deleted");
        }
        catch (e:Exception) {
            return Result.Error(e)
        }
    }

    suspend fun update(book: Book): Result<Book> {
        try {
            val updatedItem = BookApi.service.update(book._id, book)
            bookDao.update(updatedItem)
            return Result.Success(updatedItem)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }


}

