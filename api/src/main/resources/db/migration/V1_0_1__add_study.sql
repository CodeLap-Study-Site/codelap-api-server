create table study
(
    id               bigint  not null auto_increment,
    created_at       datetime(6),
    difficulty       varchar(255),
    info             varchar(255),
    max_members_size integer not null,
    name             varchar(255),
    occupation       varchar(255),
    year             integer not null,
    end_at           datetime(6),
    start_at         datetime(6),
    status           varchar(255),
    leader_id        bigint,
    primary key (id)
);

alter table study
    add constraint FKtkq6hdq643u696hau0jjt3dke foreign key (leader_id) references user (id);

create table study_members
(
    study_id   bigint not null,
    members_id bigint not null
);

alter table study_members
    add constraint FK4v53fte9tjo5rk10rnie2vbd6 foreign key (members_id) references user (id);

alter table study_members
    add constraint FK91e2fjvhd8t9u8m71jtj5jx0g foreign key (study_id) references study (id);
