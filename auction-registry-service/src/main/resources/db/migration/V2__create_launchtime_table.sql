CREATE TABLE launchtime (
    id                 BIGSERIAL                                                             PRIMARY KEY,
    auction_id         BIGINT          REFERENCES auction (id)  ON DELETE CASCADE            NOT NULL UNIQUE,
    time_from          TIMESTAMP WITHOUT TIME ZONE                                           NOT NULL,
    time_to            TIMESTAMP WITHOUT TIME ZONE                                           NOT NULL
);

