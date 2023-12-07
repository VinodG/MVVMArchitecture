package com.example.mvvmarchitecture.data.models

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Data(val data: HashMap<String,Base>)

@Polymorphic
@Serializable
open class Base(val id: String, val type: String)

@Serializable
@SerialName("item")
class Item(val x: String, val name: String) : Base(x, "item")

@Serializable
@SerialName("image")
class Image(val x: String, val url: String) : Base(x, "image")