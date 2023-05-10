create table user_files
(
    user_id       bigint not null,
    original_name varchar(255),
    saved_name    varchar(255),
    size          bigint
);

alter table user_files
    add constraint FK58770a6ppqd0j8k5lkskpxxos foreign key (user_id) references user (id);