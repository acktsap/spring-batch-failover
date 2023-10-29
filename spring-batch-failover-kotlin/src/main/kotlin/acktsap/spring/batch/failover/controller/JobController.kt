package acktsap.spring.batch.failover.controller

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.getBean
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class JobController(
    private val jobLauncher: JobLauncher,
    private val beanFactory: BeanFactory,
) {

    @RequestMapping("/clear-job")
    fun runClearJob(@RequestParam("param") paramValue: String) {
        val job = beanFactory.getBean<Job>("clearJob")
        val jobParameter = JobParametersBuilder()
            .addString("param", paramValue)
            .toJobParameters()
        jobLauncher.run(job, jobParameter)
    }

    @RequestMapping("/chunk-listener-job")
    fun runChunkListenerJob(@RequestParam("param") paramValue: String) {
        val job = beanFactory.getBean<Job>("chunkListenerJob")
        val jobParameter = JobParametersBuilder()
            .addString("param", paramValue)
            .toJobParameters()
        jobLauncher.run(job, jobParameter)
    }

    @RequestMapping("/write-listener-job")
    fun runWriteListenerJob(@RequestParam("param") paramValue: String) {
        val job = beanFactory.getBean<Job>("writeListenerJob")
        val jobParameter = JobParametersBuilder()
            .addString("param", paramValue)
            .toJobParameters()
        jobLauncher.run(job, jobParameter)
    }

    @RequestMapping("/exception-handler-job")
    fun runExceptionHandlerJob(@RequestParam("param") paramValue: String) {
        val job = beanFactory.getBean<Job>("exceptionHandlerJob")
        val jobParameter = JobParametersBuilder()
            .addString("param", paramValue)
            .toJobParameters()
        jobLauncher.run(job, jobParameter)
    }

    @RequestMapping("/context-job")
    fun runContextJob(@RequestParam("param") paramValue: String) {
        val job = beanFactory.getBean<Job>("contextJob")
        val jobParameter = JobParametersBuilder()
            .addString("param", paramValue)
            .toJobParameters()
        jobLauncher.run(job, jobParameter)
    }

    @RequestMapping("/retry-job")
    fun runRetryJob(@RequestParam("param") paramValue: String) {
        val job = beanFactory.getBean<Job>("retryJob")
        val jobParameter = JobParametersBuilder()
            .addString("param", paramValue)
            .toJobParameters()
        jobLauncher.run(job, jobParameter)
    }

    @RequestMapping("/skip-job")
    fun runSkipJob(@RequestParam("param") paramValue: String) {
        val job = beanFactory.getBean<Job>("skipJob")
        val jobParameter = JobParametersBuilder()
            .addString("param", paramValue)
            .toJobParameters()
        jobLauncher.run(job, jobParameter)
    }
}
