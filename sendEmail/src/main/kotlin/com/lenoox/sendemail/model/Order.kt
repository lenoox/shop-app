package com.lenoox.sendemail.model

data class Order(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val price: Number?
)