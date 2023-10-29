package acktsap.spring.batch.failover.job

import acktsap.spring.batch.failover.model.Customer
import acktsap.spring.batch.failover.model.TargetCustomer
import com.navercorp.spring.batch.plus.kotlin.configuration.BatchDsl
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.batch.core.ChunkListener
import org.springframework.batch.core.Job
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.item.ItemStreamReader
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class ChunkListenerJobConfig(
    private val batch: BatchDsl,
    private val transactionManager: PlatformTransactionManager,
    private val jdbcOperations: JdbcOperations,
) {

    private val logger = KotlinLogging.logger {}

    // flag to simulate first try error
    private var isFirst = true

    @Bean
    fun chunkListenerJob(): Job = batch {
        job("chunkListenerJob") {
            step("insertToTargetStep") {
                chunk<Customer, TargetCustomer>(3, transactionManager) {
                    reader(
                        object : ItemStreamReader<Customer> {
                            private var iteratorOrNot: Iterator<Customer>? = null

                            override fun read(): Customer? {
                                val iterator = getIterator()
                                return if (iterator.hasNext()) {
                                    iterator.next()
                                } else {
                                    null
                                }
                            }

                            override fun close() {
                                iteratorOrNot = null
                            }

                            private fun getIterator(): Iterator<Customer> {
                                if (iteratorOrNot == null) {
                                    val sql = "SELECT * FROM CUSTOMER WHERE IS_ACTIVE=TRUE"
                                    val list: List<Customer> =
                                        jdbcOperations.query(sql) { rs, _ ->
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
                    listener(
                        object : ChunkListener {
                            override fun afterChunkError(context: ChunkContext) {
                                logger.info { "Error occurred in chunk. Cleanup data" }
                                jdbcOperations.execute("DELETE FROM TARGET_CUSTOMER")
                            }
                        }
                    )
                }
            }
        }
    }
}
