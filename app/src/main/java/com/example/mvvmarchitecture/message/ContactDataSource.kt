package com.example.mvvmarchitecture.message

import android.content.Context
import android.provider.ContactsContract
import android.provider.Telephony
import com.example.mvvmarchitecture.data.local.AppDatabase
import com.example.mvvmarchitecture.data.remote.Api
import com.example.mvvmarchitecture.data.local.Contact
import com.example.mvvmarchitecture.data.local.Message
import com.example.mvvmarchitecture.message.SmsDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject

class ContactDataSource @Inject constructor(
    private val database: AppDatabase,
    @ApplicationContext val context: Context
) {
    suspend fun getContacts() = database.contactDao().getAll()

    suspend fun insertContacts(): Flow<List<Contact>> = flow {
        val cursor =
            context.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null
            );
        val contacts = ArrayList<Contact>()
        while (cursor != null && cursor.moveToNext()) {
            cursor.run {
//                var str = ""
//                (0..columnCount - 1).forEach {
//                    val col = getColumnName(it)
//                    str = str + "\n" + "$col : " + getString(getColumnIndexOrThrow(col))
//                }
                val id =
                    getString(getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone._ID))
                val timeStamp =
                    getString(getColumnIndexOrThrow(ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP))
                val name =
                    getString(getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val number =
                    getString(getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val contact = Contact(
                    id = id.toIntOrNull() ?: 0,
                    displayName = name,
                    timeStamp = timeStamp,
                    phoneNumber = number
                )
                database.contactDao().insertAll(contact)
                contacts.add(contact)
            }
        }
        cursor?.close()
        emit(contacts)
    }
}