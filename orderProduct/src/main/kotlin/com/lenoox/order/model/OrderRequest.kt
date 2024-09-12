package com.lenoox.order.model

data class OrderRequest(
    val id: Long,
    val orderNumber: String,
    val price: Number?,
    val user: UserRequest
)