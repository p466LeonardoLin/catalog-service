# API demo

Catalog Service running locally on port 9001.

## 201 Created: POST a new book

```bash
$ curl -i -X POST localhost:9001/books -H 'Content-Type: application/json' -d '{"isbn":"1234567891","title":"Northern Lights","author":"Lyra Silverstar","price":9.90}'
HTTP/1.1 201
Content-Type: application/json
Transfer-Encoding: chunked
Date: Wed, 04 Nov 2026 04:34:41 GMT

{"isbn":"1234567891","title":"Northern Lights","author":"Lyra Silverstar","price":9.9}
```

## 422 Unprocessable Entity when the POST for the book whose isbn already exists 

here, isbm already exist
```bash
$ curl -i -X POST localhost:9001/books -H 'Content-Type: application/json' -d '{"isbn":"1234567891","title":"Northern Lights","author":"Lyra Silverstar","price":9.90}'
HTTP/1.1 422
Content-Type: text/plain;charset=UTF-8
Content-Length: 43
Date: Wed, 04 Nov 2026 04:34:57 GMT

A book with ISBN 1234567891 already exists.
```

## 400 Bad Request: when ill POST an invalid book

The ISBN has letters and the title is empty.

```bash
$ curl -i -X POST localhost:9001/books -H 'Content-Type: application/json' -d '{"isbn":"123ABC456Z","title":"","author":"Jon Snow","price":9.90}'
HTTP/1.1 400
Content-Type: application/json
Transfer-Encoding: chunked
Date: Wed, 04 Nov 2026 04:35:15 GMT
Connection: close

{"isbn":"The ISBN format must be valid.","title":"The book title must be defined."}
```

## 200 OK when i GET a book by ISBN 

```bash
$ curl -i localhost:9001/books/1234567891
HTTP/1.1 200
Content-Type: application/json
Transfer-Encoding: chunked
Date: Wed, 04 Nov 2026 04:35:30 GMT

{"isbn":"1234567891","title":"Northern Lights","author":"Lyra Silverstar","price":9.9}
```

## 404 Not Found. when i GET a book that does not exist return 404

```bash
$ curl -i localhost:9001/books/9999999999
HTTP/1.1 404
Content-Type: text/plain;charset=UTF-8
Content-Length: 44
Date: Wed, 04 Nov 2026 04:35:40 GMT

The book with ISBN 9999999999 was not found.
```

## 200 OK i will GET the list of all books

```bash
$ curl -i localhost:9001/books
HTTP/1.1 200
Content-Type: application/json
Transfer-Encoding: chunked
Date: Wed, 04 Nov 2026 04:35:47 GMT

[{"isbn":"1234567891","title":"Northern Lights","author":"Lyra Silverstar","price":9.9}]
```

## 200 OK when PUT to update an existing book to other info

```bash
$ curl -i -X PUT localhost:9001/books/1234567891 -H 'Content-Type: application/json' -d '{"isbn":"1234567891","title":"Northern Lights 2","author":"Lyra Silverstar","price":12.50}'
HTTP/1.1 200
Content-Type: application/json
Transfer-Encoding: chunked
Date: Wed, 04 Nov 2026 04:36:38 GMT

{"isbn":"1234567891","title":"Northern Lights 2","author":"Lyra Silverstar","price":12.5}
```

## 204 No Content when i DELETE a book

```bash
$ curl -i -X DELETE localhost:9001/books/1234567891
HTTP/1.1 204
Date: Wed, 04 Nov 2026 04:36:44 GMT
```

## Not shown: 201 on PUT

Table 1 also promises 201 when PUT creates a book that does not exist. As the handout says, that case is covered by Exercise E1, so it is not demonstrated here.