CREATE TABLE author (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    birth_date DATE NOT NULL
);

CREATE TABLE book (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    price NUMERIC(10,2),
    publish_status BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE book_author_link (
    book_id BIGINT REFERENCES book(id),
    author_id BIGINT REFERENCES author(id),
    PRIMARY KEY (book_id, author_id),
    FOREIGN KEY (book_id) REFERENCES book(id),
    FOREIGN KEY (author_id) REFERENCES author(id)
);