package com.example.tripmate

data class Trip(
    var id: String = "",
    var userId: String = "",
    val name: String = "",
    val date: String = "",
    val description: String = "",
    val country: String = ""
)