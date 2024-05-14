CREATE TABLE launch_history
(
   request_id         TEXT                                             PRIMARY KEY,
   success            BOOLEAN                                          NOT NULL,
   og_auction_id         BIGINT                                           NOT NULL,
   launch_time        TIMESTAMP WITHOUT TIME ZONE                      NOT NULL
);

CREATE TABLE stop_history
(
   request_id         TEXT                                             PRIMARY KEY,
   success            BOOLEAN                                          NOT NULL
);

