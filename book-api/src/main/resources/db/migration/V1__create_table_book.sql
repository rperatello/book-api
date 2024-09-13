
CREATE TABLE tb_book (
    id BIGINT NOT NULL AUTO_INCREMENT, 
    title VARCHAR(500) NOT NULL, 
    author VARCHAR(500), 
    main_genre VARCHAR(255), 
    sub_genre VARCHAR(255), 
    type VARCHAR(255), 
    currency VARCHAR(10), 
    price DECIMAL(10, 2),
    rating DECIMAL(3, 2), 
    people_rated INT, 
    url VARCHAR(500), 
    PRIMARY KEY (id)
);

CREATE INDEX IDX_book_author ON tb_book (author);
CREATE INDEX IDX_book_main_genre ON tb_book (main_genre);

