package com.example.mvvmarchitecture.data

import android.content.Context
import com.example.mvvmarchitecture.data.local.Message
import com.example.mvvmarchitecture.data.remote.Api
import com.example.mvvmarchitecture.message.ContactDataSource
import com.example.mvvmarchitecture.message.SmsDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommonRepo @Inject constructor(
    private var api: Api,
    private var smsDataSource: SmsDataSource,
    private var contactDataSource: ContactDataSource,
    @ApplicationContext val context: Context
) {
    suspend fun get() = api.get()
    suspend fun getSms() = smsDataSource.getSms()
    suspend fun insertSmsIntoLocal(): Flow<List<Message>> = smsDataSource.insertSmsIntoLocal()

    suspend fun getContacts() =contactDataSource.getContacts()
    suspend fun insertContactIntoLocal() =contactDataSource.insertContacts()
}