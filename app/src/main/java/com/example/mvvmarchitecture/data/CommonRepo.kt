package com.example.mvvmarchitecture.data

import com.example.mvvmarchitecture.data.local.Contact
import com.example.mvvmarchitecture.data.local.Message
import com.example.mvvmarchitecture.data.remote.Api
import com.example.mvvmarchitecture.firebase.datasources.ContactApiDataSource
import com.example.mvvmarchitecture.message.ContactDataSource
import com.example.mvvmarchitecture.message.SmsDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CommonRepo @Inject constructor(
    private var api: Api,
    private var smsDataSource: SmsDataSource,
    private var contactDataSource: ContactDataSource,
    private var contactApi: ContactApiDataSource
) {
    suspend fun get() = api.get()
    suspend fun getSms() = smsDataSource.getSms()
    suspend fun insertSmsIntoLocal(): Flow<List<Message>> = smsDataSource.insertSmsIntoLocal()

    suspend fun getContacts() = contactDataSource.getContacts()
    suspend fun insertContactIntoLocal() = contactDataSource.insertContacts()

    suspend fun addToBackend(contacts: List<Contact>) {
        contacts.forEach { contact ->
            val x = contactApi.add(contact).first()
            println("${contact} is inserted ${x}")
        }
    }

}