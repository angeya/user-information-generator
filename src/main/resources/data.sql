create table city
(
    id   int unsigned auto_increment comment 'id'
        primary key,
    name varchar(30) null comment '城市名称'
);

create table university
(
    id   int unsigned auto_increment comment 'id'
        primary key,
    name varchar(30) null comment '大学名称'
);

create table user
(
    id            int auto_increment
        primary key,
    constraint user_id_uindex unique (id),
    name          varchar(30) null comment '名字',
    age           smallint    not null comment '年龄',
    gender        tinyint     not null comment '性别',
    university_id int         null comment '大学id',
    city_id       int         null comment '城市id',
    new_column    int         null
);
