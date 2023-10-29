package acktsap.spring.batch.failover.config

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.context.annotation.Configuration

@Configuration
@EnableBatchProcessing(
    dataSourceRef = "jobDataSource",
    transactionManagerRef = "jobTransactionManager"
)
class BatchConfig
