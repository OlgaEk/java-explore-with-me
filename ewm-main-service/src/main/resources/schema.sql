DROP TABLE IF EXISTS categories, events_compilations, participation, compilations, events, users;

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR                                 NOT NULL,
    email VARCHAR                                 NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT uq_user_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR                                 NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (id),
    CONSTRAINT uq_category_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title              VARCHAR                                 NOT NULL,
    annotation         VARCHAR                                 NOT NULL,
    description        VARCHAR,
    category_id        BIGINT REFERENCES categories (id),
    confirmed_requests INT     DEFAULT 0,
    created_on         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    initiator_id       BIGINT REFERENCES users (id),
    lat                FLOAT                                   NOT NULL,
    lon                FLOAT                                   NOT NULL,
    paid               BOOLEAN                                 NOT NULL,
    participant_limit  INT     DEFAULT 0,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN DEFAULT TRUE,
    state              VARCHAR                                 NOT NULL,
    CONSTRAINT pk_event PRIMARY KEY (id),
    CONSTRAINT uq_event_title UNIQUE (title)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title  VARCHAR                                 NOT NULL,
    pinned BOOLEAN                                 NOT NULL,
    CONSTRAINT pk_compilation PRIMARY KEY (id),
    CONSTRAINT uq_compilation_title UNIQUE (title)
);

CREATE TABLE IF NOT EXISTS events_compilations
(
    event_id       BIGINT REFERENCES events (id),
    compilation_id BIGINT REFERENCES compilations (id),
    CONSTRAINT pk_event_compilation PRIMARY KEY (event_id, compilation_id)

);

CREATE TABLE IF NOT EXISTS participation
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    event_id     BIGINT REFERENCES events (id),
    requester_id BIGINT REFERENCES users (id),
    created      TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    status       VARCHAR                                 NOT NULL,
    CONSTRAINT pk_participation PRIMARY KEY (id)
);
