# Bonus answers (Practicum 2)

**1. Why is it a mistake to name feature profiles dev and prod?**
A profile should name a feature, so it is not an environment that uses dev and prod. Naming profiles after features lets any environment turn them on or off.

**2. Which value is used, and why is the environment variable still recommended?**
The JVM system property wins over the environment variable. 

The environment variable is recommended because every OS system and container platform can contribute it, so that the the command that starts the app never changes, being portable and runnbable everywhere.

**3. What makes Catalog Service stateless, and why can three instances run behind a load balancer?**
After Part B the service keeps no books in memory. All data is in PostgreSQL. So any instance can answer any request and give the same result, and a load balancer can send each request to any of the three.

**4. Why test against PostgreSQL in a container instead of H2?**
Production uses PostgreSQL, so testing on a different database can not show the error until the product is posted in the web for example. As last project, my problem of database qury error is only shown after i cahnged it to postgre from h2. This is environment parity, as we learned the one of the factors.

**5. Why must you never modify V1__Initial_schema.sql after it has been applied?**
If the file changes, the checksum no longer matches and Flyway refuses to start. Also, databases that already ran V1 would differ from new ones. So just make v2 then changed step bys step.

**6. Why does BookRepositoryJdbcTests insert test data with JdbcAggregateTemplate and not bookRepository.save?**
The test is checking the repository, so the setup should not depend on the code under test.
