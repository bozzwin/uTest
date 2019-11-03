DROP TABLE bugs IF EXISTS;

CREATE TABLE bugs  (
    bugId BIGINT NOT NULL,
    deviceId BIGINT NOT NULL,
    testerId BIGINT NOT NULL
);

DROP TABLE devices IF EXISTS;

CREATE TABLE devices  (
    id BIGINT NOT NULL,
    description VARCHAR(20)
);

DROP TABLE tester_device IF EXISTS;

CREATE TABLE tester_device  (
    testerId BIGINT NOT NULL,
    deviceId BIGINT NOT NULL
);

DROP TABLE testers IF EXISTS;

CREATE TABLE testers  (
    id BIGINT NOT NULL,
    first_name VARCHAR(20),
    last_name VARCHAR(20),
    country VARCHAR(20),
    last_login TIMESTAMP
);

