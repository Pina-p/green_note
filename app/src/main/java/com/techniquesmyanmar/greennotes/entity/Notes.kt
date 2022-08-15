package com.techniquesmyanmar.greennotes.entity

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URI

@Entity
data class Notes (
    @PrimaryKey(autoGenerate = true)
    val id: Int ?= null,
    val title: String,
    val noteText:String,
    val dateTime: String,
    val imagePath: String,
    val webLink:String,
    val color:String,
    val user_id: Int ?= null
)