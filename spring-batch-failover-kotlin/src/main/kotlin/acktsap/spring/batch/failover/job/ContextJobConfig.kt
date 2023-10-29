package acktsap.spring.batch.failover.job

import acktsap.spring.batch.failover.model.Customer
import acktsap.spring.batch.failover.model.TargetCustomer
import com.navercorp.spring.batch.plus.kotlin.configuration.BatchDsl
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.batch.core.Job
import org.springframework.batch.item.ExecutionContext
import org.springframework.batch.item.ItemStreamReader
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class ContextJobConfig(
    private val batch: BatchDsl,
    private val transactionManager: PlatformTransactionManager,
    private val jdbcOperations: JdbcOperations,
) {

    private val logger = KotlinLogging.logger {}

    // flag to simulate first try error
    private var isFirst = true

    @Bean
    fun contextJob(): Job = batch {
        job("contextJob") {
            step("insertToTargetStep") {
                chunk<Customer, TargetCustomer>(3, transactionManager) {
                    reader(
                        object : ItemStreamReader<Customer> {
                            private var iteratorOrNot: Iterator<Customer>? = null
                            private var lastId: Long? = null

                            override fun open(executionContext: ExecutionContext) {
                                this.lastId = executionContext.getLong("LAST_ID", 0L)
                                logger.info { "Existing LAST_ID is '${this.lastId}'" }
                            }

                            override fun read(): Customer? {
                                val iterator = getIterator()
                                return if (iterator.hasNext()) {
                                    val next = iterator.next()
                                    lastId = next.id
                                    next
                                } else {
                                    null
                                }
                            }

                            override fun update(executionContext: ExecutionContext) {
                                executionContext.putLong("LAST_ID", checkNotNull(this.lastId))
                                logger.info { "Success to update LAST_ID to '${this.lastId}'" }
                            }

                            override fun close() {
                                iteratorOrNot = null
                                lastId = null
                            }

                            private fun getIterator(): Iterator<Customer> {
                                if (iteratorOrNot == null) {
                                    val query =
                                        "SELECT * FROM CUSTOMER WHERE IS_ACTIVE=TRUE AND ID > ${checkNotNull(lastId)}"
                                    val list: List<Customer> = jdbcOperations.query(query) { rs, _ ->
                                        Customer(
                                            id = rs.getLong("ID"),
                                            name = rs.getString("NAME"),
                                            active = rs.getBoolean("IS_ACTIVE"),
                                        )
                                    }
                                    iteratorOrNot = list.iterator()
                                }
                                return checkNotNull(iteratorOrNot)
                            }
                        }
                    )
                    processor { TargetCustomer(it.id) }
                    writer { chunk ->
                        for (targetCustomer in chunk) {
                            if (targetCustomer.customerId == 8L && isFirst) {
                                isFirst = false
                                throw IllegalStateException(
                                    "Custom error occurred in writing (customer id: '${targetCustomer.customerId})"
                                )
                            }

                            jdbcOperations.batchUpdate("INSERT INTO TARGET_CUSTOMER VALUES (${targetCustomer.customerId})")
                        }
                        logger.info { "Success to insert items (ids: ${chunk.items.map { it.customerId }})" }
                    }
                }
            }
        }
    }
}
