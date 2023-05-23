create table study_files
(
    study_id      bigint not null,
    s3imageurl    varchar(255),
    original_name varchar(255)
);

alter table study_files
    add constraint FKewqt6sb5t29289gq5k9xlb54m foreign key (study_id) references study (id);