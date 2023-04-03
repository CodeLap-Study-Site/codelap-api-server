create table study_view
(
    id         bigint not null auto_increment,
    ip_address varchar(255),
    study_id   bigint,
    primary key (id)
);

alter table study_view
    add constraint uq_ipAddress unique (ip_address);

alter table study_view
    add constraint FK6r86ddequ8k2wyr77cac4tpga foreign key (study_id) references study (id);