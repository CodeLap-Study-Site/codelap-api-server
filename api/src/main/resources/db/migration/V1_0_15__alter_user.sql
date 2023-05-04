create table user_tech_stacks
(
    user_id    bigint not null,
    tech_stack varchar(255)
);

alter table user_tech_stacks
    add constraint FKlvvl2som3ci13m9rd9etnpvcb foreign key (user_id) references user (id);

alter table user drop column age;

alter table user drop column password;

alter table user drop column status;

alter table user drop key uq_email;

alter table user drop column email;

alter table user
    add column status varchar(255);