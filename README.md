# Rate exchanger

### Documentation
In order to implement rate exchanger, we need to

1) Provide environment (postgres database) via docker compose
2) Create database and tables
3) Create rest endpoint
4) Create Cache Manager
5) Use circuit breaker for exchange rates web service
6) Write unit tests
7) Write integration test
8) Wrap up whole application with Docker and docker compose to be able to run app in local

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



