package com.example.mvvmarchitecture.base

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import java.io.IOException
import javax.inject.Inject

class Preference @Inject constructor(@ApplicationContext var context: Context) {

    private companion object {
        @Volatile
        private var dataStore: DataStore<Preferences>? = null
        private val Context._dataStore: DataStore<Preferences> by preferencesDataStore("APP_PREFERENCES")

        private fun getInstance(context: Context): DataStore<Preferences> {
            if (dataStore == null) {
                dataStore = context._dataStore
            }
            return dataStore as DataStore<Preferences>
        }
    }

    suspend fun setToken(token: Int) = set(PreferenceKeys.USER_TOKEN, token)
    suspend fun getToken(function: (Int) -> Unit) =
        getInstance(context).get(PreferenceKeys.USER_TOKEN, function)


    suspend fun <V> set(key: Preferences.Key<V>, token: V) {
        getInstance(context).edit {
            it[key] = token
        }
    }

    suspend inline fun <reified T> DataStore<Preferences>.get(
        PreferencesKey: Preferences.Key<T>, crossinline func: (T) -> Unit
    ) {
        this.data.catch {
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map {
            it[PreferencesKey]
        }.collect {
            it?.let {
                func.invoke(it as T)
            }
        }
    }
}

object PreferenceKeys {
    val USER_TOKEN = intPreferencesKey("user_token")
}