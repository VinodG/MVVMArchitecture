package com.example.mvvmarchitecture.di

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    fun provideFirebaseDatabase(@ApplicationContext context: Context): DatabaseReference {
        FirebaseApp.initializeApp(context)
        return FirebaseDatabase.getInstance(ROOT).reference
    }

    @Provides
    fun provideFireStore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    const val ROOT = "https://ai-app-1cf09-default-rtdb.firebaseio.com/"


}