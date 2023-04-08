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
	contents TEXT CHARACTER SET UTF8,
	create_date timestamp not null default current_timestamp,
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

insert into user (id, password, name, tel, user_role) values ("111", "111", "구현진", "010-1111-1111", "PROTECTOR");
insert into user (id, password, name, tel, user_role) values ("222", "222", "주효림", "010-2222-2222", "WORKER");
insert into user (id, password, name, tel, user_role) values ("333", "333", "권태연", "010-3333-3333", "MANAGER");
insert into user (id, password, name, tel, user_role) values ("444", "444", "정혜지", "010-4444-4444", "PROTECTOR");
insert into user (id, password, name, tel, user_role) values ("555", "555", "삼족오", "010-5555-5555", "WORKER");
insert into user (id, password, name, tel, user_role) values ("666", "666", "삼족육", "010-6666-6666", "PROTECTOR");
insert into user (id, password, name, tel, user_role) values ("777", "777", "사족오", "010-7777-7777", "PROTECTOR");

insert into facility (address, name, tel) values ("구미 형곡동", "구미요양원", "054-000-0000");
insert into facility (address, name, tel) values ("구미 거의동", "금오요양원", "054-111-0000");

insert into notice_content (contents, sub_content) values ("(시설1) 1번 ㅎㅇ 본내용", "1번 ㅎㅇ 서브 내용");
insert into notice (facility_id, user_id, target_id, nc_id) values (1, 2, 1, 1);

insert into notice_content (contents, sub_content) values ("(시설1) 1번 ㅎㅇㅎㅇ 본내용", "1번 ㅎㅇㅎㅇ 서브 내용");
insert into notice (facility_id, user_id, target_id, nc_id) values (1, 2, 1, 2);

insert into notice_content (contents, sub_content) values ("(시설1) 4번 ㅎㅇ 본내용", "4번 ㅎㅇ 서브 내용");
insert into notice (facility_id, user_id, target_id, nc_id) values (1, 2, 4, 3);

insert into notice_content (contents, sub_content) values ("(시설2) 6번 ㅎㅇ 본내용", "6번 ㅎㅇ 서브 내용");
insert into notice (facility_id, user_id, target_id, nc_id) values (2, 5, 6, 4);

insert into notice_content (contents, sub_content) values ("(시설2) 6번 ㅎㅇㅎㅇ 본내용", "6번 ㅎㅇㅎㅇ 서브 내용");
insert into notice (facility_id, user_id, target_id, nc_id) values (2, 5, 6, 5);

insert into notice_content (contents, sub_content) values ("(시설2) 7번 ㅎㅇㅎㅇ 본내용", "7번 ㅎㅇㅎㅇ 서브 내용");
insert into notice (facility_id, user_id, target_id, nc_id) values (2, 5, 7, 6);


insert into nhresident (facility_id, user_id, name) values (1, 1, "할머니");
insert into nhresident (facility_id, user_id, name) values (1, 2, "할머니");
insert into nhresident (facility_id, user_id, name) values (1, 3, "할머니");
insert into nhresident (facility_id, user_id, name) values (1, 4, "할머니");
insert into nhresident (facility_id, user_id, name) values (2, 5, "할아버지");
insert into nhresident (facility_id, user_id, name) values (2, 6, "할아버지");
insert into nhresident (facility_id, user_id, name) values (2, 7, "할아버지");

-- update notice_content set sub_content="aaaa" where nc_id=1;

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