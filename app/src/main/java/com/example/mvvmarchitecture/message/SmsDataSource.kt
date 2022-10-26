package com.example.mvvmarchitecture.message

import android.content.Context
import android.provider.ContactsContract
import android.provider.Telephony
import com.example.mvvmarchitecture.data.local.AppDatabase
import com.example.mvvmarchitecture.data.remote.Api
import com.example.mvvmarchitecture.data.local.Contact
import com.example.mvvmarchitecture.data.local.Message
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject

class SmsDataSource @Inject constructor(
    private val database: AppDatabase,
    @ApplicationContext val context: Context
) {
    suspend fun getSms() = database.messageDao().getAll()
    suspend fun insertSmsIntoLocal(): Flow<List<Message>> = flow {
        val cursor =
            context.contentResolver.query(Telephony.Sms.CONTENT_URI, null, null, null, null)
        val messages = ArrayList<Message>()
        while (cursor != null && cursor.moveToNext()) {
            val id = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms._ID))
            val smsDate = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE))
            val number = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
            val body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY))
            messages.add(
                Message(
                    id = id,
                    number = number,
                    body = body,
                    time = Date(smsDate.toLong()).time.toString()
                )
            )
            database.messageDao().insertAll(messages.last())
        }

        cursor?.close()
        emit(messages)
    }

}