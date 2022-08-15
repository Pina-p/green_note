package com.techniquesmyanmar.greennotes.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey (autoGenerate = true)
    val id : Int ?= null,
    val user_name: String,
    val user_profile_img : String,
    val user_pwd: String,
    val user_email:String
)
