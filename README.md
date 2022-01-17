# Rate exchanger

### Documentation
In order to implement rate exchanger, we need to

1) Provide environment (postgres database) via docker compose
2) Create database and tables
3) Create rest endpoint
4) Decide Cache Provider
5) Use circuit breaker for exchange rates web service
6) Write tests
7) Wrap up whole application with Docker and docker compose to be able to run app in local

## Prerequisites
Install Docker Desktop on local machine

## 1. Provide environment (postgres database) via docker compose
To be able to run the project with an initialised database, run in terminal inside root project: 
```bash
docker-compose -f docker-compose.yml up
```

## 2. Create database and tables
Database and tables will be created via an initial sql script, placed inside application directory, under init folder.
Migration schema can be made using flyway (out of scope).

## 3. Create REST endpoint
Created mock endpoint to test if everything works fine. 

## 4. Decide Cache Provider
In the project, Ehcache 3 is used. Under resources directory, ehcache.xml file can be found where cache settings can be found

```xml
<cache-template name="default">
    <expiry>
        <ttl unit="seconds">10</ttl>
    </expiry>

    <resources>
        <heap>1000</heap>
        <offheap unit="MB">10</offheap>
        <disk persistent="true" unit="MB">20</disk>
    </resources>
</cache-template>
```
For testing purpose, cache time to live is set to 10 seconds. For objects that are rarely updated, ttl is set to half hour.

## 5. Use circuit breaker for exchange rates web service
In the project, Resilience4j is used with a sliding window count based approach. 
```java
@Bean
public CircuitBreakerConfigCustomizer externalRateServiceCircuitBreakerConfig() {
    return CircuitBreakerConfigCustomizer
            .of("externalRateService",
                    builder -> builder.slidingWindowSize(10)
                            .slidingWindowType(COUNT_BASED)
                            .waitDurationInOpenState(Duration.ofSeconds(3))
                            .minimumNumberOfCalls(5)
                            .failureRateThreshold(50.0f));
}
```
## 6. Tests
Tests are created to be able to check app behaviour. An H2 in memory db is used for tests.


