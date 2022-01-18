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

```postgresql
CREATE DATABASE rate_exchanger
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1;
GRANT ALL PRIVILEGES ON DATABASE rate_exchanger TO postgres;
\c rate_exchanger

CREATE TABLE bank_account (
  id bigint NOT NULL PRIMARY KEY,
  version bigint NOT NULL,
  iban VARCHAR(32) NOT NULL UNIQUE,
  currency VARCHAR(3) NOT NULL,
  balance numeric(19, 2) NOT NULL DEFAULT 0.00,
  last_updated timestamp without time zone
);

CREATE TABLE exchange_rate_config (
  id bigint NOT NULL PRIMARY KEY,
  version bigint NOT NULL,
  url VARCHAR(255) NOT NULL UNIQUE,
  token VARCHAR(255),
  enabled boolean default true
);

INSERT INTO bank_account values(1, 0, 'RO66RZBR6837312324226695', 'RON', 3500, current_date);
INSERT INTO bank_account values(2, 0, 'RO72RZBR2998497575697246', 'RON', 25000, current_date);
INSERT INTO bank_account values(3, 0, 'RO12PORL8235726762578739', 'RON', 75000, current_date);
INSERT INTO bank_account values(4, 0, 'RO65RZBR4965861368341393', 'RON', 120000, current_date);
INSERT INTO bank_account values(5, 0, 'RO62PORL8157642722117553', 'RON', 9000, current_date);

INSERT INTO exchange_rate_config values(1, 0, 'http://api.exchangeratesapi.io/latest?access_key=', 'your_access_key', true);
```

## 3. Create REST endpoint
Created mock endpoint to test if everything works fine. 
```java
@GetMapping(value = "/rate/account/{iban}")
public BigDecimal getExchangeRate(@PathVariable(value = "iban") String iban) {
    return accountsService.exchangeRate(iban);
}
```

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
Tests are created to be able to check app behaviour. An H2 in memory db is used for integration tests.

Unit tests will test small part of the application. Benchmark duration of unit tests vs integration tests: 70 ms vs 700 ms.

### 6.1 Unit tests
For unit tests, Mockito is used to mock dependencies via constructor injection.
```java
    private BankAccountRepository bankAccountRepository = Mockito.mock(BankAccountRepository.class);
    private ExchangeRateConfigRepository exchangeRateConfigRepository = Mockito.mock(ExchangeRateConfigRepository.class);
    private RestTemplate restTemplate = Mockito.mock(RestTemplate.class);

    private ExchangeRateConfigService exchangeRateConfigService;
    private RateExchangeService rateExchangeService;
    private AccountsService accountsService;

    @BeforeEach
    void initUseCase() {
        accountsService = new AccountsService(bankAccountRepository, rateExchangeService);
        exchangeRateConfigService = new ExchangeRateConfigService(exchangeRateConfigRepository);
        rateExchangeService = new RateExchangeService(restTemplate, exchangeRateConfigService);
    }
```

### 6.2 Integration tests
For integration purpose, field injection is used.
```java
@SpringBootTest
@ActiveProfiles("test")
class RateExchangerApplicationTests {

	@Autowired
	AccountsService accountsService;

	@Autowired
	RateExchangeService rateExchangeService;

	@Autowired
	private BankAccountRepository bankAccountRepository;

	@Autowired
	private ExchangeRateConfigRepository exchangeRateConfigRepository;

	@Test
	void testAccountNotFound() {
		BankAccount bankAccount = new BankAccount(1L, 0L, "RO33RZBR9238994926845252", "RON", new BigDecimal(2500), new Date());
		bankAccountRepository.save(bankAccount);

		Assertions.assertThrows(ObjectNotFoundException.class, () -> accountsService.exchangeRate("RO33RZBR9238994926845250"));
	}
}
```

## 7. Added Dockerfile to be able to run entire app with one command.

```dockerfile
FROM openjdk:8-jdk-alpine
ADD build/libs/exchanger-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar","/app.jar"]
```

The app will use postgres container as database, as follows:
```dockerfile
version: '2'

services:
  app:
    build:
      context: .
    container_name: app
    depends_on:
      - db
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/rate_exchanger
      - SPRING_DATASOURCE_USERNAME=postgres
      - SPRING_DATASOURCE_PASSWORD=postgres
      - SPRING_JPA_HIBERNATE_DDL_AUTO=update
    ports:
      - 8080:8080

  db:
    image: postgres:10.19
    container_name: db
    environment:
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=postgres
    volumes:
      - ./init:/docker-entrypoint-initdb.d/
    ports:
      - 5432:5432
```

