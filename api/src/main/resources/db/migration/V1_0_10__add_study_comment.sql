create table study_comment
(
    id        bigint not null auto_increment,
    comment   varchar(255),
    create_at datetime(6),
    status    varchar(255),
    study_id  bigint,
    user_id   bigint,
    primary key (id)
);

alter table study_comment
    add constraint FKshei352323cig8trwjcnks1n9 foreign key (study_id) references study (id);

alter table study_comment
    add constraint FK41d5lrnls2duomvxh4ib6qjmf foreign key (user_id) references user (id);