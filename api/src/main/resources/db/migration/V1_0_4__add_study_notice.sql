create table study_notice
(
    id         bigint not null auto_increment,
    contents   varchar(255),
    created_at datetime(6),
    status     smallint,
    title      varchar(255),
    leader_id  bigint,
    study_id   bigint,
    primary key (id)
);

alter table study_notice
    add constraint FKppamo3aciacp5w2j3lt8ri8ut foreign key (leader_id) references user (id);

alter table study_notice
    add constraint FK2anjxf0utmjtnv7n0r6dwsybk foreign key (study_id) references study (id);

create table study_notice_files
(
    study_notice_id bigint not null,
    original_name   varchar(255),
    saved_name      varchar(255),
    size            bigint
);

alter table study_notice_files
    add constraint FKopnofxwh9o3daqunuevxr15ra foreign key (study_notice_id) references study_notice (id);
