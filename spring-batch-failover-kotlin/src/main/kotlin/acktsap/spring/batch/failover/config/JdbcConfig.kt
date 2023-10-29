package acktsap.spring.batch.failover.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

/*
    localhost:8080/h2-console

    - jdbc url : "jdbc:h2:mem:testdb"
    - user     : "sa"
    - pw       : ""
 */
@Configuration
class JdbcConfig {

    @Bean
    fun h2DataSource(): DataSource {
        return EmbeddedDatabaseBuilder()
            .setName("testdb")
            .setType(EmbeddedDatabaseType.H2)
            .addScript("classpath:org/springframework/batch/core/schema-h2.sql")
            .addScript("classpath:sql/test.sql")
            .build()
    }

    @Bean
    fun transactionManager(): PlatformTransactionManager = DataSourceTransactionManager(h2DataSource())

    @Bean
    fun jdbcOperations(): JdbcOperations = JdbcTemplate(h2DataSource())

    // use same datasource for test
    @Bean
    fun jobDataSource(): DataSource = h2DataSource()

    @Bean
    fun jobTransactionManager(): PlatformTransactionManager = DataSourceTransactionManager(jobDataSource())
}
