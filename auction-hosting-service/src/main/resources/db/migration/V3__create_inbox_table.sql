CREATE TABLE inbox (
    response_id        TEXT                                             PRIMARY KEY,
    auction_id         BIGINT                                           NOT NULL,
    money              DECIMAL(13, 2) CHECK(money >= 0),
    p_type             TEXT                                             NOT NULL
);