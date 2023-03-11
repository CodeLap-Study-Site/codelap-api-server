create table user
(
    id         bigint  not null auto_increment,
    age        integer not null,
    occupation varchar(255),
    year       integer not null,
    created_at datetime(6),
    email      varchar(255),
    name       varchar(255),
    password   varchar(255),
    status     smallint,
    primary key (id)
);

alter table user
    add constraint uq_email unique (email);
