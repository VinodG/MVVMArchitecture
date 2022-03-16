package com.example.mvvmarchitecture

import com.example.mvvmarchitecture.base.BaseVM
import com.example.mvvmarchitecture.data.remote.ChatRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(var repo: ChatRepo) : BaseVM() {
    fun listen() {
        repo.socket
    }
}