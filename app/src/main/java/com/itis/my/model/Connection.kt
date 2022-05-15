package com.itis.my.model

data class Connection(
    val id: String,
    val createdAt: Long,
    var friendId: String,
    var feedback: String,
    var name: String = ""
)