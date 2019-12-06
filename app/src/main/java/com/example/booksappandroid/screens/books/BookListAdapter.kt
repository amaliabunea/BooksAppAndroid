package com.example.booksappandroid.screens.books

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.booksappandroid.screens.book.BookEditFragment
import com.example.booksappandroid.R
import com.example.booksappandroid.screens.data.Book
import kotlinx.android.synthetic.main.book_view.view.*

class BookListAdapter(private val fragment: Fragment) : RecyclerView.Adapter<BookListAdapter.ViewHolder>() {

    var books = emptyList<Book>()
        set(value) {
            field = value
            notifyDataSetChanged();
        }

    private var onItemClick: View.OnClickListener;

    init {
        onItemClick = View.OnClickListener { view ->
            val book = view.tag as Book
            fragment.findNavController().navigate(R.id.book_edit_fragment, Bundle().apply {
                putString(BookEditFragment.BOOK_ID, book._id)
            })
    }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.book_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = books[position]
        holder.textView.textSize=24f
        holder.textView.text = book.title

        holder.itemView.tag = book
        holder.itemView.setOnClickListener(onItemClick)
    }

    override fun getItemCount() = books.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.title
    }
}