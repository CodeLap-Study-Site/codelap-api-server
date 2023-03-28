create table bookmark
(
    id         bigint not null,
    created_at datetime(6),
    study_id   bigint,
    user_id    bigint,
    primary key (id)
);

create table bookmark_seq
(
    next_val bigint
);

alter table bookmark
    add constraint FK3lgv1iky352nk1dl4amqklj4g foreign key (study_id) references study (id);

alter table bookmark
    add constraint FK3ogdxsxa4tx6vndyvpk1fk1am foreign key (user_id) references user (id);