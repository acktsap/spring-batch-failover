# Spring Batch Failover Example

## Prerequisites

- Java 17 or higher

## Trigger

ClearJob

```shell
curl 'localhost:8080/clear-job?param=abc'
```

ChunkListenerJob

```shell
curl 'localhost:8080/chunk-listener-job?param=abc'
```

WriteListenerJob

```shell
curl 'localhost:8080/write-listener-job?param=abc'
```

ExceptionHandlerJob

```shell
curl 'localhost:8080/exception-handler-job?param=abc'
```

ContextJob

```shell
curl 'localhost:8080/context-job?param=abc'
```

RetryJob

```shell
curl 'localhost:8080/retry-job?param=abc'
```

SkipJob

```shell
curl 'localhost:8080/skip-job?param=abc'
```

## Show DB

http://localhost:8080/h2-console

- jdbc url : "jdbc:h2:mem:testdb"
- user : "sa"
- pw   : ""
