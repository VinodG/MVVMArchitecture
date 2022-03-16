package com.example.mvvmarchitecture.data.remote

import android.util.Log
import io.socket.client.Manager
import io.socket.client.Socket
import io.socket.emitter.Emitter
import javax.inject.Inject


class ChatRepo @Inject constructor(var socket: Socket?) {
    private val TAG = "ChatRepo"

    init {
        socket?.on(Manager.EVENT_TRANSPORT,Emitter.Listener {
            Log.e(TAG, ": tranport $it")

        })?.on(Socket.EVENT_CONNECT, Emitter.Listener {
                Log.e(TAG, ": OnConnected ")
            })?.on(Socket.EVENT_DISCONNECT, Emitter.Listener {
                Log.e(TAG, ": onDisconnected")
            })?.on(Socket.EVENT_CONNECT_ERROR, Emitter.Listener {
                Log.e(TAG, ": onError " + (it[0] as Throwable).message)
            })
        socket?.connect()
    }

}