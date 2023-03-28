create table study_notice_comment
(
    id              bigint not null,
    content         varchar(255),
    create_at       datetime(6),
    status          varchar(255),
    study_notice_id bigint,
    user_id         bigint,
    primary key (id)
);

alter table study_notice_comment
    add constraint FKm41mm0asxh70w7dweqix17q15 foreign key (study_notice_id) references study_notice (id);

alter table study_notice_comment
    add constraint FKmdhjsk7noqaf3negppex5387b foreign key (user_id) references user (id);