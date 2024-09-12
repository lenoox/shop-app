package com.lenoox.sendemail.model

data class OrderRequest(
    val id: Long,
    val orderNumber: String,
    val price: Number?,
    val user: UserRequest
)