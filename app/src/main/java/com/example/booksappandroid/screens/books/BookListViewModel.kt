package com.example.booksappandroid.screens.books

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

class BookListViewModel(application: Application) : AndroidViewModel(application) {
    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val books: LiveData<List<Book>>
    val loading: LiveData<Boolean> = mutableLoading
    val loadingError: LiveData<Exception> = mutableException

    val bookRepository: BookRepository

    init {
        val bookDao = TodoDatabase.getDatabase(application, viewModelScope).bookDao()
        bookRepository = BookRepository(bookDao)
        books = bookRepository.books
    }

    fun refresh() {
        viewModelScope.launch {
            Log.v(TAG, "refresh...");
            mutableLoading.value = true
            mutableException.value = null
            when (val result = bookRepository.refresh()) {
                is Result.Success -> {
                    Log.d(TAG, "refresh succeeded");
                }
                is Result.Error -> {
                    Log.w(TAG, "refresh failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableLoading.value = false
        }
    }

//    fun loadBooks() {
//        viewModelScope.launch {
//            Log.v(TAG, "loadBooks...");
//            mutableLoading.value = true
//            mutableException.value = null
//            when (val result = bookRepository.refresh()) {
//                is Result.Success<*> -> {
//                    Log.d(TAG, "refresh succeeded");
//                }
//                is Result.Error -> {
//                    Log.w(TAG, "refresh failed", result.exception);
//                    mutableException.value = result.exception
//                }
//            }
//            mutableLoading.value = false
//        }
//    }

}