package com.example.mvvmarchitecture.message

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mvvmarchitecture.base.PermissionUtils
import com.example.mvvmarchitecture.data.local.Contact
import com.example.mvvmarchitecture.data.local.Message
import com.example.mvvmarchitecture.ui.theme.MVVMArchitectureTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SmsActivity : ComponentActivity() {

    private val vmContact: CommonViewModel by viewModels()

    @Inject
    lateinit var permissionUtils: PermissionUtils

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionUtils.result.observe(this) { result ->
            when (result) {
                is PermissionUtils.Result.SMS -> {
                    if (result.isGranted) {
                        vmContact.getSmsConversation()
                        permissionUtils.checkContactPermission()
                    } else {
                        Toast.makeText(this, "Permissions are denied for sms", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is PermissionUtils.Result.Contact -> {
                    if (result.isGranted) {
                        vmContact.getContacts()
                    } else {
                        Toast.makeText(
                            this,
                            "Permissions are denied for contact",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
//        permissionUtils.checkContactPermission()
        permissionUtils.checkSmsPermissions()
        setContent {
            MVVMArchitectureTheme {
                val sms by vmContact.sms.collectAsState(listOf())
                val contacts by vmContact.contact.collectAsState(listOf())
                Row() {
                    Box(
                        modifier = Modifier
                            .width(0.dp)
                            .weight(1.0f)
                    ) {
                        ContactList(contacts)
                    }
                    Box(
                        modifier = Modifier
                            .width(0.dp)
                            .weight(1.0f)
                    ) {
                        SmsList(sms)
                    }
                }
            }
        }
    }

    @Composable
    private fun SmsList(contacts: List<Message>) {
        LazyColumn {
            itemsIndexed(contacts) { _, msg ->
                Column {
                    Text(text = msg.id)
                    Text(text = msg.number)
                    Text(text = msg.body)
                    Text(text = msg.time)
                    Divider()
                }
            }
        }
    }

    @Composable
    private fun ContactList(contacts: List<Contact>) {
        LazyColumn {
            itemsIndexed(contacts) { _, contact ->
                Column {
                    Text(text = contact.id.toString())
                    Text(text = contact.displayName)
                    Text(text = contact.phoneNumber)
                    Text(text = contact.timeStamp)
                    Divider()
                }
            }
        }
    }

    fun isDeviceLocked(context: Context): Boolean {
        return (context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager).isDeviceLocked
    }
}
