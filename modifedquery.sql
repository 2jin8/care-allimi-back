
use allimi;
show tables;
-- drop table if exists all_notice, facility, invitation, nhresident, functions, notice, letter, users, visit;


	create table all_notice (
        contents mediumtext CHARACTER SET UTF8,
        image_url varchar(1024),
        id bigint not null,
        primary key (id)
    ) engine=InnoDB;
    
    create table facility (
       facility_id bigint not null auto_increment,
        address varchar(255) not null,
        fm_name varchar(255),
        facility_name varchar(255) not null,
        tel varchar(255) not null,
        primary key (facility_id)
    ) engine=InnoDB;
    
	create table functions (
       id bigint not null auto_increment,
        create_date datetime(6),
        nhr_id bigint,
        primary key (id)
    ) engine=InnoDB;
    
	create table invitation (
       invit_id bigint not null auto_increment,
        dates date,
        tel varchar(255) not null,
        user_role smallint not null,
        primary key (invit_id)
    ) engine=InnoDB;

    create table nhresident (
       resident_id bigint not null auto_increment,
        birth varchar(255),
        health_info varchar(255),
        resident_name varchar(255),
        user_role varchar(255),
        is_approved bit,
        primary key (resident_id)
    ) engine=InnoDB;

    create table notice (
       contents mediumtext CHARACTER SET UTF8,
        image_url varchar(1024),
        sub_content mediumtext CHARACTER SET UTF8,
        id bigint not null,
        primary key (id)
    ) engine=InnoDB;
    
	create table letter (
        id bigint not null,
        content mediumtext,
        is_read bit,
        primary key (id)
    ) engine=InnoDB;

    create table users (
       user_id bigint not null auto_increment,
        id varchar(255) not null,
        user_name varchar(255),
        passwords varchar(255) not null,
        tel varchar(255),
        current_nhresident integer default 0,
        primary key (user_id)
    ) engine=InnoDB;
    
	create table visit (
       is_read bit, 
       rej_reason varchar(255), 
       state varchar(255), 
       tel varchar(255), 
       texts varchar(1024), 
       visitor_name varchar(255) not null, 
       want_date datetime(6) not null, 
       id bigint not null,
       primary key (id)
	)engine=InnoDB;
    
     alter table all_notice 
       add constraint FKkidtx5jxy994r10emxwsfq3we 
       foreign key (id) 
       references functions (id) ON DELETE CASCADE;
    
    alter table functions 
       add constraint FKs0uoijc64v1igvrs8eio5hs4o 
       foreign key (nhr_id) 
       references nhresident (resident_id) ON DELETE CASCADE;
    
    alter table letter 
       add constraint FKbiu9pf5up7n2jve9n2ci20779 
       foreign key (id) 
       references functions(id) ON DELETE CASCADE;
    
    alter table notice 
       add constraint FK3dfb7xyfpjesgtipgsg1xrovk 
       foreign key (id) 
       references functions (id) ON DELETE CASCADE;           
    
    alter table visit 
       add constraint FKaywpj1b02t6axd4loxdxl2159 
       foreign key (id) 
       references functions (id) ON DELETE CASCADE;
    
    alter table functions 
       add column facility_id bigint not null;
    
    alter table invitation 
       add column facility_id bigint not null;
    
    alter table nhresident 
       add column facility_id bigint;
    
    alter table nhresident 
       add column user_id bigint;
       
	alter table functions 
       add column user_id bigint;
    
    alter table functions 
       add constraint FK6wiok54n6ra8pogbixi7hs8dx 
       foreign key (facility_id) 
       references facility (facility_id) ON DELETE CASCADE;

    alter table functions 
       add constraint FK5dwdmss73y84j8fqr3h3tlfe1 
       foreign key (user_id) 
       references users (user_id) ON DELETE set null;
    
    alter table invitation 
       add constraint FK3moafqpfup73xfmvh4bnprniq 
       foreign key (facility_id) 
       references facility (facility_id) ON DELETE CASCADE;
    
    alter table nhresident 
       add constraint FKnc3cjtrturjr63jfahcvirmtr 
       foreign key (facility_id) 
       references facility (facility_id) ON DELETE CASCADE;
    
    alter table nhresident 
       add constraint FKdnmpm247w9y2f20hfs4ep3494 
       foreign key (user_id) 
       references users (user_id) ON DELETE SET NULL;
    
    
