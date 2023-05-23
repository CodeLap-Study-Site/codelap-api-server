create table study_confirmation
(
    id               bigint not null auto_increment,
    confirmed_at     datetime(6),
    content          varchar(255),
    created_at       datetime(6),
    rejected_message varchar(255),
    status           smallint,
    title            varchar(255),
    study_id         bigint,
    user_id          bigint,
    primary key (id)
);

alter table study_confirmation
    add constraint FK8a82tng81isl7kookleftkugt foreign key (study_id) references study (id);

alter table study_confirmation
    add constraint FKoeonm5og7rxirkbh86sriqkeb foreign key (user_id) references user (id);

create table study_confirmation_files
(
    study_confirmation_id bigint not null,
    s3imageurl            varchar(255),
    original_name         varchar(255)
);

alter table study_confirmation_files
    add constraint FK5j6dphd6qsg4l9t1j40ygwfiu foreign key (study_confirmation_id) references study_confirmation (id);
