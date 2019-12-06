package com.example.booksappandroid.core

import okhttp3.Response
import okhttp3.WebSocket
import okio.ByteString
import okhttp3.WebSocketListener


class EchoWebSocketListener : WebSocketListener() {

    override fun onOpen(webSocket: WebSocket, response: Response) {
        webSocket.send("something")
    }

    override fun onMessage(webSocket: WebSocket?, text: String?) {
        println("Receiving : " + text!!)
    }
}