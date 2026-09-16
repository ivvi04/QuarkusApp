-- liquibase formatted sql

-- changeset Lakeev-DA:baseline-1
CREATE TABLE IF NOT EXISTS quarkus.orders
(
    id     bigserial PRIMARY KEY NOT NULL,
    name   text                  NOT NULL,
    status varchar               NOT NULL
);

ALTER TABLE IF EXISTS quarkus.orders
    OWNER to quarkus_app;