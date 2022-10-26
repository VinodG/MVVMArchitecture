package com.example.mvvmarchitecture.message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmarchitecture.data.CommonRepo
import com.example.mvvmarchitecture.data.local.Contact
import com.example.mvvmarchitecture.data.local.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommonViewModel @Inject constructor(val repo: CommonRepo) : ViewModel() {

    private val _sms: MutableStateFlow<List<Message>> = MutableStateFlow(listOf())
    val sms: StateFlow<List<Message>> = _sms
    private val _contact: MutableStateFlow<List<Contact>> = MutableStateFlow(listOf())
    val contact: StateFlow<List<Contact>> = _contact

    init {
        insertSms()
        insertContacts()
    }

    fun getSmsConversation() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getSms().collect { _sms.emit(it) }
        }
    }

    fun getContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getContacts().collect { _contact.emit(it) }
        }
    }

    fun insertSms() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.insertSmsIntoLocal().collect {
                    println("inserted sms: ${it.size}")
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    fun insertContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.insertContactIntoLocal().collect {
                    println("inserted contacts: ${it.size}")
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
}