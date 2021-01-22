package com.kingz.coroutines.data.model

import com.google.gson.annotations.SerializedName

/**
 * https://github.com/MindorksOpenSource/Kotlin-Coroutines-Android-Examples.git
 */
data class ApiUser(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("email")
    val email: String = "",
    @SerializedName("avatar")
    val avatar: String = ""
)