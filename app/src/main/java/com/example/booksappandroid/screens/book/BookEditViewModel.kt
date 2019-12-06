package com.example.booksappandroid.screens.book

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.booksappandroid.core.TAG
import com.example.booksappandroid.screens.data.Book
import com.example.booksappandroid.screens.data.BookRepository
import kotlinx.coroutines.launch

import com.example.booksappandroid.core.Result
import com.example.booksappandroid.screens.data.local.TodoDatabase

class BookEditViewModel(application: Application) : AndroidViewModel(application) {
    private val mutableFetching = MutableLiveData<Boolean>().apply { value = false }
    private val mutableCompleted = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val fetching: LiveData<Boolean> = mutableFetching
    val fetchingError: LiveData<Exception> = mutableException
    val completed: LiveData<Boolean> = mutableCompleted

    val bookRepository: BookRepository

    init {
        val bookDao = TodoDatabase.getDatabase(application, viewModelScope).bookDao()
        bookRepository = BookRepository(bookDao)
    }

    fun getBookById(bookId: String): LiveData<Book> {
        Log.v(TAG, "getBookById...")
        return bookRepository.getById(bookId)
    }

    fun saveOrUpdateItem(book: Book) {
        viewModelScope.launch {
            Log.v(TAG, "saveOrUpdateItem...")
            mutableFetching.value = true
            mutableException.value = null
            val result: Result<Book>
            if (book._id.isNotEmpty()) {
                result = bookRepository.update(book)
            } else {
                println(book)
                result = bookRepository.save(book)
            }
            when(result) {
                is Result.Success -> {
                    Log.d(TAG, "saveOrUpdateItem succeeded");
                }
                is Result.Error -> {
                    Log.w(TAG, "saveOrUpdateItem failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableCompleted.value = true
            mutableFetching.value = false
        }
    }

    fun deleteItem(book: Book) {
        viewModelScope.launch {
            Log.v(TAG, "deleteItem...");
            mutableFetching.value = true
            mutableException.value = null
            val result: Result<String> = bookRepository.delete(book)
            when(result) {
                is Result.Success -> {
                    Log.d(TAG, "deleteItem succeeded");
                }
                is Result.Error -> {
                    Log.w(TAG, "deleteItem failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableCompleted.value = true
            mutableFetching.value = false
        }
    }
}