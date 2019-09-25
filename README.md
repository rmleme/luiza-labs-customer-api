# luiza-labs-customer-api

## Instructions

**Requirements:** docker, docker-compose, JDK 8, Maven.

1. Download the source code to your local box. Linux is recommended. 
2. Build the application with maven: at base directory (where `pom.xml` is located), run `mvn package`.
3. Go to directory `src/main/docker/` and run `build.sh`. A new Docker image will be created.
4. Run `docker-compose up -d`
5. Two containers will be started: one for MySQL and the other for this application (*note: it is assumed the DB schema will be created through some external process.* DDL script can be found at `src/main/sql/schema.sql`).
6. Once all containers are ready, the API may be accessed through the base URL `http://<IP>:8080/customer-api/customer`:
```bash
curl http://localhost:8080/customer-api/customer
```
