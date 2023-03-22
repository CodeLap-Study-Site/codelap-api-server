create table study_notice_read_members
(
    study_notice_id bigint not null,
    read_members_id bigint not null
);

alter table study_notice_read_members
    add constraint FK7gekvchhiydh3q9l2mx2xbr61 foreign key (read_members_id) references user (id);

alter table study_notice_read_members
    add constraint FKd2qlr7l38w4fldju74o0ta62u foreign key (study_notice_id) references study_notice (id);