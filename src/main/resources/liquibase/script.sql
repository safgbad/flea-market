-- liquibase formatted sql

-- changeset lolipis:1
CREATE TABLE users
(
    id            INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    email         VARCHAR NOT NULL,
    password      VARCHAR NOT NULL,
    first_name    VARCHAR NOT NULL,
    last_name     VARCHAR,
    phone         VARCHAR NOT NULL,
    user_role     VARCHAR NOT NULL DEFAULT 'USER',
    image         VARCHAR
);

CREATE TABLE ads
(
    id            INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    title         VARCHAR NOT NULL,
    description   VARCHAR,
    price         INTEGER NOT NULL,
    image         VARCHAR NOT NULL,
    author_id     INTEGER REFERENCES users (id)
);

CREATE TABLE comments
(
    id            INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    text          VARCHAR NOT NULL,
    public_date   TIMESTAMP NOT NULL,
    ads_id        INTEGER REFERENCES ads (id),
    author_id     INTEGER REFERENCES users (id)
);


-- changeset safgbad:2
CREATE TABLE roles
(
    id   INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR NOT NULL
);

INSERT INTO roles (name) VALUES
    ('USER'),
    ('ADMIN');

ALTER TABLE users
    DROP COLUMN user_role,
    ADD COLUMN role_id INTEGER NOT NULL REFERENCES roles (id) DEFAULT 1;

-- changeset safgbad:3
ALTER TABLE users
    RENAME COLUMN email TO username;

ALTER TABLE users
    ADD COLUMN enabled BOOLEAN NOT NULL DEFAULT true;

-- changeset safgbad:4
ALTER TABLE users
    ADD CONSTRAINT username_unique UNIQUE (username);

-- changeset safgbad:5
ALTER TABLE users
    ALTER COLUMN first_name DROP NOT NULL;

ALTER TABLE users
    ALTER COLUMN phone DROP NOT NULL;

-- changeset safgbad:6
CREATE TABLE authorities
(
    username  VARCHAR NOT NULL REFERENCES USERS (username),
    authority VARCHAR NOT NULL
);

ALTER TABLE users
    DROP COLUMN role_id;

DROP TABLE roles;

-- changeset safgbad:7
ALTER TABLE authorities
    ADD PRIMARY KEY (username, authority);
