create table study_tech_stack_list
(
    study_id        bigint not null,
    tech_stack_list varchar(255)
);

alter table study_tech_stack_list
    add constraint FK9f7m49a8kckvtr1s1xxk79ldp foreign key (study_id) references study (id);