# Containers

## Layered Dockerfile
The image is built in two stages and runs as a non-root user.

## First build
```
Step 1/14 : FROM eclipse-temurin:21-jre AS builder
Step 2/14 : WORKDIR /builder
Step 3/14 : ARG JAR_FILE=build/libs/catalog-service-0.0.1-SNAPSHOT.jar
Step 4/14 : COPY ${JAR_FILE} application.jar
Step 5/14 : RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted
Step 6/14 : FROM eclipse-temurin:21-jre
Step 7/14 : RUN useradd spring
Step 8/14 : USER spring
Step 9/14 : WORKDIR /application
Step 10/14 : COPY --from=builder /builder/extracted/dependencies/ ./
Step 11/14 : COPY --from=builder /builder/extracted/spring-boot-loader/ ./
Step 12/14 : COPY --from=builder /builder/extracted/snapshot-dependencies/ ./
Step 13/14 : COPY --from=builder /builder/extracted/application/ ./
Step 14/14 : ENTRYPOINT ["java", "-jar", "application.jar"]
Successfully built f0697dd0e854
Successfully tagged catalog-service:dockerfile
```
Every step ran.

## Second build after changing one line in BookController
```
Step 1/14 : FROM eclipse-temurin:21-jre AS builder
Step 2/14 : WORKDIR /builder                 ---> Using cache
Step 3/14 : ARG JAR_FILE=...                 ---> Using cache
Step 4/14 : COPY ${JAR_FILE} application.jar ---> rebuilt (new JAR)
Step 5/14 : RUN java -Djarmode=tools ...     ---> rebuilt
Step 6/14 : FROM eclipse-temurin:21-jre
Step 7/14 : RUN useradd spring               ---> Using cache
Step 8/14 : USER spring                      ---> Using cache
Step 9/14 : WORKDIR /application             ---> Using cache
Step 10/14 : COPY ... dependencies/          ---> Using cache
Step 11/14 : COPY ... spring-boot-loader/    ---> Using cache
Step 12/14 : COPY ... snapshot-dependencies/ ---> Using cache
Step 13/14 : COPY ... application/           ---> rebuilt (changed code)
Step 14/14 : ENTRYPOINT [...]                ---> rebuilt
Successfully built e3c1d14720f8
Successfully tagged catalog-service:dockerfile
```
Steps 7-12 were reused from cache. Only the application layer was rebuilt, because that is the only layer whose content changed.


## Non-root check
```
$ docker run --rm --entrypoint id catalog-service:dockerfile
uid=1001(spring) gid=1001(spring) groups=1001(spring)
```
\