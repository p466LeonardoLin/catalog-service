# Catalog Service

Catalog Service is one service of Polar Bookshop. It manages books through REST API.

## How to run

Start the service: `./gradlew bootRun` (port 9001).
Build and run all tests: `./gradlew build`.

## Exercises

### E1: PUT returns 201 when it creates a book

`PUT /books/{isbn}` now returns `ResponseEntity<Book>`. The controller asks whether the book exists before saving 200 OK if it existed, 201 Created if it was new.

### E2: 
Without a rule, PUT /books/1111111111 with a body containing "isbn": "2222222222" would silently create a book under the body's ISBN, and the URL the client used would point to nothing. 

### E3:

### E4: DELETE on a missing book

DELETE returns 204 whether or not the book existed. HTTP defines DELETE as idempotent so it sends the same request several times. Later calls change nothing, so the state is the same. Idempotency is about server state, not about getting the same status code, so a 404 on a repeat would also be idempotent. I think 204 is correct here because the client's goal, that the book does not exist, is met either way, and clients can retry safely.

### E5: Full lifecycle test

`CatalogServiceApplicationTests.whenBookLifecycleThenAllStepsWork` does POST,
GET, PUT, DELETE, then GET (expecting 404) on one book.

