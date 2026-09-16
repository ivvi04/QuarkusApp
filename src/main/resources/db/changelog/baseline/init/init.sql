-- liquibase formatted sql

-- changeset Denis:init-1
DROP SCHEMA IF EXISTS quarkus_app CASCADE;
DROP USER IF EXISTS quarkus;
CREATE USER quarkus WITH PASSWORD 'quarkus';
CREATE SCHEMA quarkus_app;
GRANT USAGE ON SCHEMA quarkus_app TO quarkus;
ALTER DEFAULT PRIVILEGES IN SCHEMA quarkus_app GRANT ALL ON TABLES TO quarkus;
ALTER DEFAULT PRIVILEGES IN SCHEMA quarkus_app GRANT ALL ON SEQUENCES TO quarkus;