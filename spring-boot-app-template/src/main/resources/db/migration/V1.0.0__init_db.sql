CREATE TABLE hello_world (
    id SERIAL PRIMARY KEY,
    message VARCHAR(255) NOT NULL
);

INSERT INTO hello_world (message) VALUES ('Hello World!');
