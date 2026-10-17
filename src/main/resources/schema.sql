create table universities (
    id                  serial,
    name                varchar(100) not null,
    city                varchar(30) not null,
    ---------------------------------------------------
    constraint universities_id_pk primary key (id),
    constraint universities_name_city_un unique (name, city)
);

create table dormitories (
    id                  serial,
    university_id       int not null,
    name                varchar(100) not null,
    address             varchar(100) not null,
    ---------------------------------------------------
    constraint dormitories_id_pk primary key (id),
    constraint dormitories_name_un unique (university_id, name),
    constraint dormitories_university_id_fk foreign key (university_id) references universities(id)
);

create table users (
                       id                bigserial,
                       login             varchar(12) not null,
                       firstname         varchar(50) not null,
                       lastname          varchar(50) not null,
                       email             varchar(50) not null,
                       hash_password     varchar(60) not null,
                       gender            varchar(10) not null,
                       contacts          varchar(100) not null,
                       role              varchar(10) not null default 'USER',
                       course            int not null,
                       birth_year        int not null,
                       created_at        timestamp not null default now(),
                       is_blocked        boolean not null default false,
                       is_confirmed      boolean not null default false,
                       dormitory_id      int,
                       ---------------------------------------------------
                       constraint users_id_pk primary key (id),
                       constraint users_login_un unique (login),
                       constraint users_email_un unique (email),
                       constraint users_gender_ch check (gender in ('MALE', 'FEMALE')),
                       constraint users_course_ch check (course between 1 and 6),
                       constraint users_birth_year_ch check (birth_year >= 1990),
                       constraint users_role_ch check (role in ('USER', 'ADMIN')),
                       constraint users_dormitory_id_fk foreign key (dormitory_id) references dormitories(id)
);



