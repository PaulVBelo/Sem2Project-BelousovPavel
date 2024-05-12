CREATE TABLE auctioneer
(
   id                 BIGSERIAL                                                             PRIMARY KEY,
   username           TEXT                                                              NOT NULL UNIQUE,
   money              DECIMAL(13, 2) CHECK(money >= 0)
);

CREATE TABLE auction
(
   id                 BIGSERIAL                                                             PRIMARY KEY,
   item               TEXT                                                                     NOT NULL,
   auctioneer_id      BIGINT          REFERENCES auctioneer (id)  ON DELETE CASCADE            NOT NULL,
   step               DECIMAL(13, 2) CHECK(step > 0),
   start              DECIMAL(13, 2) CHECK(start > 0),
   duration           INTEGER                                                                  NOT NULL,
   status             TEXT                                                                     NOT NULL
);

CREATE TABLE outbox
(
    id BIGSERIAL PRIMARY KEY,
    data TEXT NOT NULL
);