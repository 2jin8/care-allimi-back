
    create table facility (
       facility_id bigint not null auto_increment,
        address varchar(255) not null,
        name varchar(255) not null,
        tel varchar(255) not null,
        primary key (facility_id)
    ) engine=InnoDB;

    create table nhresident (
       resident_id bigint not null auto_increment,
        birth varchar(255),
        health_info varchar(255),
        name varchar(255),
        user_role varchar(255),
        primary key (resident_id)
    ) engine=InnoDB;

    create table notice (
       notice_id bigint not null auto_increment,
        primary key (notice_id)
    ) engine=InnoDB;

    create table notice_content (
       nc_id bigint not null auto_increment,
        contents TEXT CHARACTER SET UTF8,
        create_date datetime(6),
        sub_contents TEXT CHARACTER SET UTF8,
        primary key (nc_id)
    ) engine=InnoDB;

    create table users (
       user_id bigint not null auto_increment,
        id varchar(255) not null,
        name varchar(255),
        password varchar(255) not null,
        tel varchar(255),
        primary key (user_id)
    ) engine=InnoDB;
