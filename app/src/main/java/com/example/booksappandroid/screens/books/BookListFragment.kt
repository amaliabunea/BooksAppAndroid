package com.example.booksappandroid.screens.books

import android.content.Context
import android.net.ConnectivityManager
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
import com.example.booksappandroid.auth.data.AuthRepository
import com.example.booksappandroid.core.TAG
import kotlinx.android.synthetic.main.book_list_fragment.*

class BookListFragment : Fragment() {


    private lateinit var bookListAdapter: BookListAdapter
    private lateinit var bookListModel: BookListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        Log.v(TAG, "onCreateView")
        return inflater.inflate(R.layout.book_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "onActivityCreated")
        if (!AuthRepository.isLoggedIn) {
            findNavController().navigate(R.id.login_fragment)
            return;
        }
        setupItemList()
        fab.setOnClickListener {
            Log.v(TAG, "add new book")
            findNavController().navigate(R.id.book_edit_fragment)
        }
        btnChart.setOnClickListener{
            Log.v(TAG, "display chart")
            val bks = bookListAdapter.books
            findNavController().navigate(R.id.chart_fragment, Bundle().apply {
                putInt("size", bks.size)
                for (i in bks.indices) {
                    putFloat("book$i", bks[i].price.toFloat())
                    putString("booktitle$i", bks[i].title)
                }
            })
        }

        btnCoordinates.setOnClickListener{
            Log.v(TAG, "display coordinates")
            findNavController().navigate(R.id.map_fragment)
        }

        btnLogout.setOnClickListener{
            Log.v(TAG, "logout")
            AuthRepository.logout()
            findNavController().navigate(R.id.login_fragment)
        }

    }


    private fun setupItemList() {
        bookListAdapter = BookListAdapter(this)
        book_list.adapter = bookListAdapter
        bookListModel = ViewModelProviders.of(this).get(BookListViewModel::class.java)
        bookListModel.books.observe(this, Observer { books ->
            Log.v(TAG, "update books")
            bookListAdapter.books = books
        })
        bookListModel.loading.observe(this, Observer { loading ->
            Log.v(TAG, "update loading")
            progress.visibility = if (loading) View.VISIBLE else View.GONE
        })
        bookListModel.loadingError.observe(this, Observer { exception ->
            if (exception != null) {
                Log.v(TAG, "update loading error")
                val message = "Loading exception ${exception.message}"
                val parentActivity = activity?.parent
                if (parentActivity != null) {
                    Toast.makeText(parentActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        })
        bookListModel.refresh()
    }
}