# Catalog Service

Polar Bookshop: Catalog Service, version 1 (REST API).

![Commit Stage](https://github.com/p466LeonardoLin/catalog-service/actions/workflows/commit-stage.yml/badge.svg)

Run it with `./gradlew bootRun` (port 9001). Run the tests with `./gradlew build`.
Example requests and responses are in [docs/api-demo.md](docs/api-demo.md).

## Exercise notes

### E1: PUT returns 201 when it creates a book

`PUT /books/{isbn}` now returns `ResponseEntity<Book>`. The controller asks the
service whether the book exists (`bookExists`) before saving: 200 OK if it
existed and was updated, 201 Created if it was new. Both cases are covered in
`BookControllerMvcTests`. The exists-check and the save are two separate
steps, so they are not atomic; that is acceptable for this in-memory
practicum.

### E4: DELETE on a missing book

(Rewrite this in your own words before submitting.)
DELETE currently answers 204 whether or not the book existed. HTTP defines
DELETE as idempotent: sending the same request many times must leave the
server in the same final state, and here the book is gone after the first call
and stays gone after every later one. Idempotency is about server state, not
about getting an identical status code, so returning 404 on the second call
would also be idempotent. I think 204 is acceptable because the client's goal
("this book should not exist") is met either way, and it keeps retries simple.

### E5: Full lifecycle test

`CatalogServiceApplicationTests.whenBookLifecycleThenAllStepsWork` does POST,
GET, PUT, DELETE, then GET (expecting 404) on one book.

### E2 and E3 (optional for P466)

Not done.