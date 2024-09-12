package com.lenoox.shop

import com.lenoox.order.OrderApplication
import com.lenoox.sendemail.SendEmailApplication
import org.springframework.boot.builder.SpringApplicationBuilder

fun main(args: Array<String>) {
    val uws: SpringApplicationBuilder = SpringApplicationBuilder(OrderApplication::class.java)
        .properties(
            "server.port=8021",
            "server.contextPath=/UserService",
            "SOA.ControllerFactory.enforceProxyCreation=true"
        )
    uws.run()
    val pws: SpringApplicationBuilder = SpringApplicationBuilder(SendEmailApplication::class.java)
        .properties(
            "server.port=8022",
            "server.contextPath=/ProjectService",
            "SOA.ControllerFactory.enforceProxyCreation=true"
        )
    pws.run()
}
