# Rate exchanger

### Documentation
In order to implement rate exchanger, we need to

* Provide environment (postgres database) via docker compose
* Create database and tables
* Create rest endpoint
* Create Cache Manager
* Use circuit breaker for exchange rates web service
* write unit tests
* write integration test
* Wrap up whole application with Docker and docker compose to be able to run app in local

## Prerequisites
Install Docker Desktop on local machine

## Provide environment (postgres database) via docker compose
To be able to run the project with an initialised database, run in terminal inside root project: 
```bash
docker-compose -f docker-compose.yml up
```

## Create database and tables
Database and tables will be created via an initial sql script, placed inside application directory, under init folder.
Migration schema can be made using flyway (out of scope).

## Create REST endpoint
Created mock endpoint to test if everything works fine. 



