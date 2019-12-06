package com.example.booksappandroid.screens.book

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.booksappandroid.R
import com.example.booksappandroid.core.TAG
import com.example.booksappandroid.screens.data.Book
import kotlinx.android.synthetic.main.book_edit_fragment.*

class BookEditFragment : Fragment() {

    companion object {
        const val BOOK_ID = "BOOK_ID"
    }

    private lateinit var viewModel: BookEditViewModel
    private var bookId: String? = null
    private var book: Book? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(BOOK_ID)) {
                bookId = it.getString(BOOK_ID).toString()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v(TAG, "onCreateView")
        return inflater.inflate(R.layout.book_edit_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "onActivityCreated")
        setupViewModel()
        fab.setOnClickListener {
            Log.v(TAG, "save book")
//            viewModel.saveOrUpdateItem(
//                book_title.text.toString(),
//                book_author.text.toString(),
//                book_price.text.toString()
//            )
            val i = book
            if (i != null) {
                i.title = book_title.text.toString()
                i.author = book_author.text.toString()
                i.price = book_price.text.toString()
                viewModel.saveOrUpdateItem(i)
            }
        }
        remove_fab.setOnClickListener {
            Log.v(TAG, "remove book")
            val i = book
            if (i != null) {
                viewModel.deleteItem(i)
            }
        }

    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(BookEditViewModel::class.java)
//        viewModel.book.observe(this, Observer { book ->
//            Log.v(TAG, "update books")
//            book_title.setText(book.title)
//            book_author.setText(book.author)
//            book_price.setText(book.price)
//        })
        viewModel.fetching.observe(this, Observer { fetching ->
            Log.v(TAG, "update fetching")
            progress.visibility = if (fetching) View.VISIBLE else View.GONE
        })
        viewModel.fetchingError.observe(this, Observer { exception ->
            if (exception != null) {
                Log.v(TAG, "update fetching error")
                val message = "Fetching exception ${exception.message}"
                val parentActivity = activity?.parent
                if (parentActivity != null) {
                    Toast.makeText(parentActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewModel.completed.observe(this, Observer { completed ->
            if (completed) {
                Log.v(TAG, "completed, navigate back")
                findNavController().popBackStack()
            }
        })
        val id = bookId
        if (id == null) {
            book = Book("","", "", "")
        } else {
            viewModel.getBookById(id).observe(this, Observer {
                Log.v(TAG, "update books")
                if (it != null) {
                    book = it
                    book_title.setText(it.title)
                    book_author.setText(it.author)
                    book_price.setText(it.price)
                }
            })
        }
    }
//        } else {
//
//            viewModel.getBookById(id).observe(this, Observer {
//                Log.v(TAG, "update books")
//                if (it != null) {
//                    book = it
//                    book_title.setText(it.title)
//                    book_author.setText(it.author)
//                    book_price.setText(it.price)
//                }
//            })
//        }

}
