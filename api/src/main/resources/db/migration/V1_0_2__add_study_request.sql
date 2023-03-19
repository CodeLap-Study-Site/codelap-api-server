create table study_request
(
    id             bigint not null auto_increment,
    created_at     datetime(6),
    message        varchar(255),
    reject_message varchar(255),
    status         varchar(255),
    study_id       bigint,
    user_id        bigint,
    primary key (id)
);

alter table study_request
    add constraint FKja4totw5xl4w10cqn6yelq306 foreign key (study_id) references study (id);

alter table study_request
    add constraint FKjftiq9y8a3q7brxpvn0a51019 foreign key (user_id) references user (id);
