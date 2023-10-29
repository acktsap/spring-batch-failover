package acktsap.spring.batch.failover.model

data class Customer(
    val id: Long,
    val name: String,
    val active: Boolean,
)
