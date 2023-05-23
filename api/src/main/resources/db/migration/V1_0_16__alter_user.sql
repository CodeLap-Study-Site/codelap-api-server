create table user_files
(
    user_id       bigint not null,
    s3imageurl    varchar(255),
    original_name varchar(255)
);

alter table user_files
    add constraint FK58770a6ppqd0j8k5lkskpxxos foreign key (user_id) references user (id);