use allimi;

show tables;

-- drop table if exists all_notice, facility, image, letter, invitation, nhresident, notice,users, visit, schedules;

create table all_notice (
   allnotice_id bigint not null auto_increment,
	contents mediumtext,
	create_date datetime,
	important bit,
	title text,
	facility_id bigint not null,
	user_id bigint,
	primary key (allnotice_id)
) engine=InnoDB;

create table facility (
   facility_id bigint not null auto_increment,
	address varchar(255) not null,
	fm_name varchar(255),
	facility_name varchar(255) not null,
	tel varchar(255) not null,
	primary key (facility_id)
) engine=InnoDB;

create table image (
   image_id bigint not null auto_increment,
	image_url mediumtext,
	allnotice_id bigint,
	notice_id bigint,
	primary key (image_id)
) engine=InnoDB;

create table invitation (
   invit_id bigint not null auto_increment,
	dates date,
	user_role smallint not null,
	facility_id bigint not null,
    create_date date,
	user_id bigint not null,
	primary key (invit_id)
) engine=InnoDB;

create table letter (
   letter_id bigint not null auto_increment,
	contents text,
	create_date datetime,
	is_read bit,
	facility_id bigint not null,
	nhr_id bigint not null,
	user_id bigint,
	primary key (letter_id)
) engine=InnoDB;

create table nhresident (
   nhr_id bigint not null auto_increment,
	birth varchar(255),
	health_info text,
	resident_name varchar(255),
	user_role varchar(255),
	facility_id bigint,
	user_id bigint,
	primary key (nhr_id)
) engine=InnoDB;

create table notice (
   notice_id bigint not null auto_increment,
	contents mediumtext,
	create_date datetime,
	sub_contents mediumtext,
	facility_id bigint not null,
	nhr_id bigint not null,
	user_id bigint,
	primary key (notice_id)
) engine=InnoDB;

create table users (
   user_id bigint not null auto_increment,
	current_nhresident integer,
	login_id varchar(255) not null,
	user_name varchar(255),
	passwords varchar(255) not null,
	phone_num varchar(255),
	primary key (user_id)
) engine=InnoDB;

create table visit (
   visit_id bigint not null auto_increment,
	create_date datetime,
	phone_num varchar(255),
	rej_reason varchar(255),
	state varchar(255),
	texts varchar(1024),
	visitor_name varchar(255) not null,
	want_date datetime not null,
	facility_id bigint not null,
	nhr_id bigint not null,
	user_id bigint,
	primary key (visit_id)
) engine=InnoDB;

create table schedules (
   schedule_id bigint not null auto_increment,
	dates date not null,
	texts varchar(255) not null,
	facility_id bigint not null,
	user_id bigint not null,
	primary key (schedule_id)
) engine=InnoDB;

alter table all_notice 
   add constraint FKtch021huph6qocdd4nrftbh5 
   foreign key (facility_id) 
   references facility (facility_id) ON DELETE CASCADE;
   
alter table all_notice 
   add constraint FKobxalty2mvdlowcv0dut7fbnt 
   foreign key (user_id) 
   references users (user_id) ON DELETE cascade;
   
alter table image 
   add constraint FK2upt0ce363186b049swfeec7r 
   foreign key (allnotice_id) 
   references all_notice (allnotice_id) ON DELETE CASCADE;
   
alter table image 
   add constraint FKmxefv2q9erynjpr2u4tftmn1r 
   foreign key (notice_id) 
   references notice (notice_id) ON DELETE CASCADE;
   
alter table invitation 
   add constraint FK3moafqpfup73xfmvh4bnprniq 
   foreign key (facility_id) 
   references facility (facility_id) ON DELETE CASCADE;
   
alter table invitation 
   add constraint FKs81mf5o97gfb55r5vbov80r5m 
   foreign key (user_id) 
   references users (user_id) ON DELETE CASCADE;
   
alter table letter 
   add constraint FKpwouv9ouekkmml4799v2apjdo 
   foreign key (facility_id) 
   references facility (facility_id) ON DELETE CASCADE;
   
alter table letter 
   add constraint FKsxxggv7fnchcip0a3tnvv6lx 
   foreign key (nhr_id) 
   references nhresident (nhr_id) ON DELETE CASCADE;
   
alter table letter 
   add constraint FK10amltainvpyo3f174ytomgal 
   foreign key (user_id) 
   references users (user_id) ON DELETE cascade;
   
alter table nhresident 
   add constraint FKnc3cjtrturjr63jfahcvirmtr 
   foreign key (facility_id) 
   references facility (facility_id) ON DELETE CASCADE;
   
alter table nhresident 
   add constraint FKdnmpm247w9y2f20hfs4ep3494 
   foreign key (user_id) 
   references users (user_id) ON DELETE CASCADE;
   
alter table notice 
   add constraint FK1hh8ndxgu1rocdd32uqp7d8wx 
   foreign key (facility_id) 
   references facility (facility_id) ON DELETE CASCADE;
   
alter table notice 
   add constraint FKmanjqcnmkea4ldktmopxql3l 
   foreign key (nhr_id) 
   references nhresident (nhr_id) ON DELETE CASCADE;
   
alter table notice 
   add constraint FK6hu3mfrsmpbqjk44w2fq5t5us 
   foreign key (user_id) 
   references users (user_id) ON DELETE CASCADE;
   
alter table visit 
   add constraint FKd0jrm155ko61rtlcoq27rb2lv 
   foreign key (facility_id) 
   references facility (facility_id) ON DELETE CASCADE;
   
alter table visit 
   add constraint FKrwyfqhab2ioowat871omb8knf 
   foreign key (nhr_id) 
   references nhresident (nhr_id) ON DELETE CASCADE;
   
alter table visit 
   add constraint FKcl61si34n0gd12cn9scyu5rdq 
   foreign key (user_id) 
   references users (user_id) ON DELETE CASCADE;
   
alter table schedules 
   add constraint FK9lfqaxkg1ek6cw7e12ouqpk7f 
   foreign key (facility_id) 
   references facility (facility_id) ON DELETE cascade;
       
alter table schedules 
   add constraint FKd4y4xekwahv9boo6lc8gfl3jv 
   foreign key (user_id) 
   references users (user_id) on delete cascade;

ALTER TABLE schedules CHANGE COLUMN date dates date not null;
-- use allimi;
-- show tables;

-- select * from users;
-- select * from facility;
-- select * from nhresident;
-- select * from notice;
-- select * from image;
-- select * from all_notice;
-- select * from visit;
-- select * from letter;
-- select * from schedules;