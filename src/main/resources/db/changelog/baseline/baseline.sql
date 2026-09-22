-- liquibase formatted sql

-- changeset Lakeev-DA:baseline-1
CREATE TABLE IF NOT EXISTS quarkus_app.orders
(
    id     bigserial PRIMARY KEY NOT NULL,
    name   text                  NOT NULL,
    status varchar               NOT NULL
);