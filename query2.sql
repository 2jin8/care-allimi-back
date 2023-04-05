use allimi;

drop table if exists facility, notice, notice_content, user, nhresident;

create table facility (
	facility_id bigint not null auto_increment,
	address varchar(255),
	name varchar(255),
	tel varchar(255),
	primary key (facility_id)
) engine=InnoDB default CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;  
   
create table user (
   user_id bigint not null auto_increment,
	id varchar(255) not null,
	password varchar(255) not null,
	name varchar(255),
	tel varchar(255),
	user_role varchar(255),
	primary key (user_id)
) engine=InnoDB default CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

create table notice_content (
	nc_id bigint not null auto_increment,
	content TEXT CHARACTER SET UTF8,
	create_date datetime(6),
	sub_content TEXT CHARACTER SET UTF8,
	primary key (nc_id)
) engine=InnoDB default  CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

create table notice (
	notice_id bigint not null auto_increment,
	facility_id bigint,
    user_id bigint,
    target_id bigint,
    nc_id bigint,
	primary key (notice_id), 
	foreign key (facility_id) references facility(facility_id),
    foreign key (user_id) references user(user_id),
    foreign key (target_id) references user(user_id),
    foreign key (nc_id) references notice_content(nc_id)
) engine=InnoDB default  CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

create table nhresident (
	resident_id bigint not null auto_increment,
	facility_id bigint,
    user_id bigint,
	name varchar(255),
    health_info varchar(255),
    birth varchar(255),
    primary key (resident_id),
	foreign key (facility_id) references facility(facility_id),
    foreign key (user_id) references user(user_id)
) engine=InnoDB default CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
                  
-- select * from information_schema.table_constraints; 

insert into user (id, password, name, tel, user_role) values ("zxcv", "zxcv", "구현진", "010-0000-0000", "PROTECTOR");
insert into user (id, password, name, tel, user_role) values ("3456", "3456", "주효림", "010-1234-5678", "WORKER");
insert into user (id, password, name, tel, user_role) values ("1234", "1234", "권태연", "010-8888-8888", "MANAGER");
insert into user (id, password, name, tel, user_role) values ("abcd", "abcd", "정혜지", "010-2222-3333", "PROTECTOR");


insert into facility (address, name, tel) values ("구미 형곡동", "구미요양원", "054-000-0000");

insert into notice_content (content, sub_content) values ("본 내용", "서브 내용");
insert into notice (facility_id, user_id, target_id, nc_id) values (1, 2, 1, 1);

insert into nhresident (facility_id, user_id, name) values (1, 1, "할머니");
insert into nhresident (facility_id, user_id, name) values (1, 2, "할머니");

update notice_content set create_date="2023-04-03", sub_content="aaaa" where nc_id=1;

-- desc user;
-- desc facility;
-- desc nhresident;
-- desc notice;
-- desc notice_content;

select * from user;
select * from facility;
select * from notice_content;
select * from notice;
select * from nhresident;