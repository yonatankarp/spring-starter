CREATE TABLE greeting (
    id       SERIAL PRIMARY KEY,
    language VARCHAR(10) NOT NULL,
    message  TEXT        NOT NULL,
    UNIQUE (language, message)
);

INSERT INTO greeting (language, message) VALUES
    ('en', 'Hello, World!'),
    ('es', 'Hola, Mundo!'),
    ('fr', 'Bonjour le monde!'),
    ('de', 'Hallo, Welt!'),
    ('ja', 'こんにちは世界');
