-- show databases;

-- use allimi;

-- show tables;
--  drop table if exists facility, notice, notice_content, user;
-- alter table user convert to charset utf8;
-- alter table facility convert to charset utf8;
-- alter table notice convert to charset utf8;
-- alter table notice_content convert to charset utf8;


create table facility (
   facility_id bigint not null auto_increment,
	address varchar(255),
	name varchar(255),
	tel varchar(255),
	primary key (facility_id)
) engine=InnoDB default CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
    
create table notice (
   notice_id bigint not null auto_increment,
	primary key (notice_id)
) engine=InnoDB default  CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

    create table notice_content (
       nc_id bigint not null auto_increment,
        contents TEXT CHARACTER SET UTF8,
        create_date datetime(6),
        sub_content TEXT CHARACTER SET UTF8,
        primary key (nc_id)
    ) engine=InnoDB default  CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

    create table user (
       user_id bigint not null auto_increment,
        id varchar(255) not null,
        name varchar(255),
        password varchar(255) not null,
        protector_name varchar(255),
        tel varchar(255),
        user_role varchar(255),
        primary key (user_id)
    ) engine=InnoDB default CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


alter table facility 
       add column owner_id bigint not null;                   
    
alter table notice 
   add column nc_id bigint;                  
    
alter table notice 
   add column facility_id bigint;              
   
       alter table notice 
       add column target bigint;

alter table notice 
   add column user_id bigint;                    

alter table user 
   add column facility_id bigint;                      

alter table facility 
   add constraint FKj4bbqvvhwk0fy0kqih94i361f 
   foreign key (owner_id) 
   references user (user_id);                   

alter table notice 
   add constraint FK6mwldfvbmdh6mms0gv6vc51sw 
   foreign key (nc_id) 
   references notice_content (nc_id);                  

alter table notice 
   add constraint FK1hh8ndxgu1rocdd32uqp7d8wx 
   foreign key (facility_id) 
   references facility (facility_id);                      

    alter table notice 
       add constraint FKaetwm8r59vhr3cefe0kcly7rc 
       foreign key (target) 
       references user (user_id);
       
           alter table notice 
       add constraint FKcvf4mh5se36inrxn7xlh2brfv 
       foreign key (user_id) 
       references user (user_id);

alter table user 
   add constraint FK20vgu3dfrvnsn5996kp95nws0 
   foreign key (facility_id) 
   references facility (facility_id);

insert into user (id, password, user_role) values ("zxcv", "zxcv", "PROTECTOR");
insert into user (id, password, user_role) values ("3456", "3456", "WORKER");
insert into user (id, password, user_role) values ("1234", "1234", "MANAGER");
insert into facility (owner_id) values (3);

-- select * from user;
-- desc notice_content;
-- desc notice;
-- desc user;
select * from notice_content;
select * from notice;
-- select * from user;
-- show full columns from user;
-- show variables where Variable_Name LIKE "%dir";
