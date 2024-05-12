CREATE TABLE auction
(
   id                 BIGSERIAL                                                             PRIMARY KEY,
   item               TEXT                                                                     NOT NULL,
   step               DECIMAL(13, 2) CHECK(step > 0),
   start              DECIMAL(13, 2) CHECK(start > 0),
   time_from          TIMESTAMP WITHOUT TIME ZONE                                              NOT NULL,
   time_to            TIMESTAMP WITHOUT TIME ZONE                                              NOT NULL,
   status             TEXT                                                                     NOT NULL,
   og_id              BIGINT                                                                   NOT NULL
);

CREATE TABLE participant
(
   id                 BIGSERIAL                                                             PRIMARY KEY,
   username           TEXT                                                              NOT NULL UNIQUE,
   money              DECIMAL(13, 2) CHECK(money >= 0)
);

CREATE TABLE bid
(
    id                BIGSERIAL                                                             PRIMARY KEY,
    bid_size          DECIMAL(13, 2) CHECK(bid_size >= 0),
    auction_id        BIGINT          REFERENCES auction (id)  ON DELETE CASCADE                NOT NULL,
    participant_id    BIGINT          REFERENCES participant (id)  ON DELETE CASCADE            NOT NULL
);

CREATE TABLE outbox
(
    id BIGSERIAL PRIMARY KEY,
    data TEXT NOT NULL
);