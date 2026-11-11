# Configuration demo

The service was built once with `./gradlew bootJar`. 
The default value inside the JAR (`application.yml`) is `Welcome to the local book catalog!`.

## 1. Command-line argument

```
$ java -jar build/libs/catalog-service-0.0.1-SNAPSHOT.jar --polar.greeting="Welcome to the catalog from CLI"
$ curl localhost:9001/
Welcome to the catalog from CLI
```

## 2. JVM system property

```
$ java -Dpolar.greeting="Welcome to the catalog from JVM" -jar build/libs/catalog-service-0.0.1-SNAPSHOT.jar
$ curl localhost:9001/
Welcome to the catalog from JVM
```

## 3. Environment variable

```
$ POLAR_GREETING="Welcome to the catalog from ENV" java -jar build/libs/catalog-service-0.0.1-SNAPSHOT.jar
$ curl localhost:9001/
Welcome to the catalog from ENV
```

replaced by undriscore. like `polar.greeting` becomes `POLAR_GREETING`.

## 4. JVM property and command-line argument intogether

```
$ java -Dpolar.greeting="Welcome from JVM" -jar build/libs/catalog-service-0.0.1-SNAPSHOT.jar --polar.greeting="Welcome from CLI"
$ curl localhost:9001/
Welcome from CLI
```

the command-line argument (`Welcome from CLI`). Spring Boot's precedence order is command-line arguments, then JVM system, then environment variable, then profile files , then `application.yml` in the JAR, so a command-line argument overrides a JVM property.

## Bonus: the `testdata` profile

With `./gradlew bootRun`, the `testdata` profile is active, so `BookDataLoader` loads two sample books:

```
$ curl localhost:9001/books
[{"isbn":"1234567892","title":"Polar Journey","author":"Iorek Polarson","price":12.9},{"isbn":"1234567891","title":"Northern Lights","author":"Lyra Silverstar","price":9.9}]
```