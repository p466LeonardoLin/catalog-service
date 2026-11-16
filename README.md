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


## Practicum 2 exercise notes

### E1: 

I added `V2__Add_publisher_column.sql`, which adds a `publisher` column to `book` wchi is an optional value so it can be null, and a 5-argument `Book.of`. `BookService.editBookDetails` has been changed to match the chang of the `book.java`.

**Why the column must be nullable:** Existing book has already a null value for it. Therefore it must be nullable. Also, during a rolling deployment the old version of the service is still running against the already migrated database. Old code does not know about the new column and inserts books without a publisher.

**Two-release plan to make it mandatory.**
1. Release 1: add the nullable column, and deploy code that writes the publisher and still works when it is missing. So that it will fill in the publisher for existing books.
2. Release 2: once every instance runs the new code and every row has a publisher, add `V3` oublisher with not null with alter table.



### E2: Search by author

`GET /books?author=...` now returns the books by that author. without the parameter it returns all books. I changed `BookRepository.findByAuthor`, and `BookService.viewBooksByAuthor`. It is also tested in `BookRepositoryJdbcTests` and in `BookControllerMvcTests`.

### E3:

`BookDataLoader` is now guarded by `@ConditionalOnProperty(name = "polar.testdata.enabled", havingValue = "true")` instead of `@Profile("testdata")`, and `bootRun` sets `polar.testdata.enabled=true`.

`POLAR_TESTDATA_ENABLED=true java -jar build/libs/catalog-service-0.0.1-SNAPSHOT.jar`
It also works on the packaged JAR, with no rebuild.
With the variable set, the two sample books are loaded; without it nothing is loaded.


### E4: Optimistic locking in action

`BookRepositoryJdbcTests.staleUpdateThrowsOptimisticLockingFailure` loads the same book two times so that it can saves a update through the first copy, and then saves the second copythrowing `OptimisticLockingFailureException` since is out of date for the version. 

`BookControllerMvcTests.whenPutConflictsWithAnotherUpdateThenShouldReturn409` tests it.

