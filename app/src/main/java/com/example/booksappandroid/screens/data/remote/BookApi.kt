package com.example.booksappandroid.screens.data.remote

import com.example.booksappandroid.core.Api
import com.example.booksappandroid.screens.data.Book
import com.example.booksappandroid.screens.data.BookJson
import org.json.JSONObject
import retrofit2.http.*
import retrofit2.Response


object BookApi {
    interface Service {
        @GET("/api/books")
        suspend fun find(): List<Book>

        @GET("/api/books/{id}")
        suspend fun read(@Path("id") itemId: String): Book

        @Headers("Content-Type: application/json")
        @POST("/api/books")
        suspend fun create(@Body book: BookJson): Book

        @Headers("Content-Type: application/json")
        @PUT("/api/books/{id}")
        suspend fun update(@Path("id") bookId: String, @Body book: Book): Book

        @Headers("Content-Type: application/json")
        @DELETE("/api/books/{id}")
        suspend fun delete(@Path("id") bookId: String): Response<Unit>
    }

    val service: Service = Api.retrofit.create(Service::class.java)
}