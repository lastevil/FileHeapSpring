CREATE TABLE IF NOT EXISTS users
(
    id         bigserial primary key,
    username   varchar(36) not null,
    password   varchar(80) not null,
    email      varchar(50) unique,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

CREATE TABLE IF NOT EXISTS roles
(
    id         bigserial primary key,
    name       varchar(50) not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

CREATE TABLE IF NOT EXISTS users_roles
(
    user_id bigint not null references users (id),
    role_id bigint not null references roles (id),
    primary key (user_id, role_id)
);

CREATE TABLE IF NOT EXISTS files
(
    id          bigserial,
    user_id     bigserial,
    file_name   varchar(100),
    type        VARCHAR(255),
    data        bytea,
    upload_time TIME,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);