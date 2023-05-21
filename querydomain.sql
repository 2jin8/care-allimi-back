	
-- CREATE DATABASE 이름 DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

use allimi;

-- drop table if exists all_notice, facility, image, letter, invitation, nhresident, notice,users, visit, schedules;

create table all_notice (
   allnotice_id bigint not null auto_increment,
	contents mediumtext,
	created_date datetime,
	important bit not null,
	title text,
	writer_id bigint,
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
	user_role varchar(255) not null,
	facility_id bigint not null,
    created_date date,
	user_id bigint not null,
	primary key (invit_id)
) engine=InnoDB;

create table letter (
   letter_id bigint not null auto_increment,
	contents text,
	created_date datetime,
	is_read bit not null,
	protector_id bigint,
	primary key (letter_id)
) engine=InnoDB;

create table nhresident (
   nhr_id bigint not null auto_increment,
	birth varchar(255),
	health_info text,
	resident_name varchar(255),
	user_role varchar(255),
	worker_id bigint,
	facility_id bigint,
	user_id bigint,
	primary key (nhr_id)
) engine=InnoDB;

create table notice (
   notice_id bigint not null auto_increment,
	contents mediumtext,
	create_date datetime,
	sub_contents mediumtext,
	target_id bigint,
    writer_id bigint,
	primary key (notice_id)
) engine=InnoDB;

create table users (
   user_id bigint not null auto_increment,
	current_nhresident bigint,
	login_id varchar(255) not null,
	user_name varchar(255),
	passwords varchar(255) not null,
	phone_num varchar(255),
	primary key (user_id)
) engine=InnoDB;

create table visit (
   visit_id bigint not null auto_increment,
	created_date datetime,
	rej_reason varchar(255),
	state varchar(255),
	texts varchar(1024),
	want_date datetime not null,
	protector_id bigint,
	primary key (visit_id)
) engine=InnoDB;

create table schedules (
   schedule_id bigint not null auto_increment,
	dates date not null,
	texts varchar(255) not null,
	writer_id bigint,
	primary key (schedule_id)
) engine=InnoDB;

   
alter table all_notice 
   add constraint FKge6esoux37e6a90557s723i5y 
   foreign key (writer_id) 
   references nhresident (nhr_id) ON DELETE SET NULL;     
   
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
   add constraint FK69g53cnxa60sfxronrwllq515 
   foreign key (protector_id) 
   references nhresident (nhr_id) ON DELETE SET NULL;
   
alter table nhresident 
   add constraint FKnc3cjtrturjr63jfahcvirmtr 
   foreign key (facility_id) 
   references facility (facility_id) ON DELETE CASCADE;
   
alter table nhresident 
   add constraint FKdnmpm247w9y2f20hfs4ep3494 
   foreign key (user_id) 
   references users (user_id) ON DELETE CASCADE;
	
alter table nhresident 
   add constraint FKh3pydpi7f75txsru86a113r8g 
   foreign key (worker_id) 
   references nhresident (nhr_id) ON DELETE SET NULL;

alter table notice 
   add constraint FKfwux2apiq36fhwv1mi34u6q1k 
   foreign key (target_id) 
   references nhresident (nhr_id) ON DELETE CASCADE;
   
alter table notice 
   add constraint FKijkabcs9ia42lafwb53l41yp5 
   foreign key (writer_id) 
   references nhresident (nhr_id) ON DELETE SET NULL;
   
alter table visit 
	add constraint FKc62fw5r2mt132b4lkggavq2h0 
	foreign key (protector_id) 
	references nhresident (nhr_id) ON DELETE SET NULL;
   
alter table users 
   add constraint FKhstp1gcy9o5knt2ot34e2jylj 
   foreign key (current_nhresident) 
   references nhresident (nhr_id) ON DELETE SET NULL;
   
alter table schedules 
   add constraint FKqi3fe7dsxx6rn4w0p41ctqtu2 
   foreign key (writer_id) 
   references nhresident (nhr_id) on delete set null;













