create table universities (
    id                  serial,
    name                varchar(100) not null,
    city                varchar(30) not null,
    ---------------------------------------------------
    constraint universities_id_pk primary key (id),
    constraint universities_name_city_un unique (name, city)
);

comment on table universities is 'Таблица университетов';
comment on column universities.id is 'id университета';
comment on column universities.name is 'Название университета';
comment on column universities.city is 'Город, в котором находится университет';

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

comment on table dormitories is 'Таблица общежитий';
comment on column dormitories.id is 'id общежития';
comment on column dormitories.university_id is 'id университета';
comment on column dormitories.name is 'Название общежития';
comment on column dormitories.address is 'Адрес общежития';


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

comment on table users is 'Таблица пользователей';
comment on column users.id is 'id пользователя';
comment on column users.login is 'Логин пользователя';
comment on column users.firstname is 'Имя';
comment on column users.lastname is 'Фамилия';
comment on column users.email is 'Электронная почта';
comment on column users.hash_password is 'Хеш пароля';
comment on column users.gender is 'Пол: MALE или FEMALE';
comment on column users.contacts is 'Контакты, видны после взаимной заявки';
comment on column users.role is 'Роль: USER или ADMIN';
comment on column users.course is 'Курс обучения';
comment on column users.birth_year is 'Год рождения';
comment on column users.created_at is 'Дата регистрации';
comment on column users.is_blocked is 'Заблокирован ли администратором';
comment on column users.is_confirmed is 'Подтверждён ли аккаунт через Telegram';
comment on column users.dormitory_id is 'id общежития (NULL у администратора)';

create table confirmation_codes (
    id              bigserial,
    user_id         bigint not null,
    code            varchar(6) not null,
    send_at         timestamp not null default now(),
    ---------------------------------------------------
    constraint confirmation_codes_id_pk primary key (id),
    constraint confirmation_codes_code_un unique (code),
    constraint confirmation_codes_user_id_fk foreign key (user_id) references users(id) on delete cascade
);

comment on table confirmation_codes is 'Таблица кодов подтверждения через тг';
comment on column confirmation_codes.id is 'id кода';
comment on column confirmation_codes.user_id is 'id пользователя';
comment on column confirmation_codes.code is 'Код подтверждения';
comment on column confirmation_codes.send_at is 'Время выдачи кода';

create table questions (
    id              serial,
    text            varchar(255) not null,
    is_active       boolean not null default true,
    created_at      timestamp not null default now(),
    ---------------------------------------------------
    constraint questions_id_pk primary key (id),
    constraint questions_text_un unique (text)
);

comment on table questions is 'Таблица вопросов анкеты';
comment on column questions.id is 'id вопроса';
comment on column questions.text is 'Текст вопроса';
comment on column questions.is_active is 'Показывается ли вопрос в анкете';
comment on column questions.created_at is 'Дата создания вопроса';

create table answer_options (
    id              serial,
    question_id     int not null,
    value           int not null,
    label           varchar(100) not null,
    ---------------------------------------------------
    constraint answer_options_id_pk primary key (id),
    constraint answer_options_question_id_fk foreign key (question_id) references questions(id) on delete cascade,
    constraint answer_options_question_id_value_un unique (question_id, value),
    constraint answer_options_value_ch check (value between 1 and 5)
);

comment on table answer_options is 'Таблица вариантов ответа на вопросы';
comment on column answer_options.id is 'id варианта';
comment on column answer_options.question_id is 'id вопроса';
comment on column answer_options.value is 'Числовое значение ответа (1-5)';
comment on column answer_options.label is 'Текст варианта ответа';

create table user_answers (
    user_id         bigint not null,
    question_id     int not null,
    value           int not null,
    importance      int not null,
    is_dealbreaker  boolean not null default false,
    ---------------------------------------------------
    constraint user_answers_pk primary key (user_id, question_id),
    constraint user_answers_user_id_fk foreign key (user_id) references users(id) on delete cascade,
    constraint user_answers_question_id_fk foreign key (question_id) references questions(id) on delete cascade,
    constraint user_answers_value_ch check (value between 1 and 5),
    constraint user_answers_importance_ch check (importance between 0 and 10)
);

comment on table user_answers is 'Таблица ответов пользователей на вопросы анкеты';
comment on column user_answers.user_id is 'id пользователя';
comment on column user_answers.question_id is 'id вопроса';
comment on column user_answers.value is 'Ответ пользователя (1-5)';
comment on column user_answers.importance is 'Важность совпадения с соседом (0-10)';
comment on column user_answers.is_dealbreaker is 'Принципиален ли вопрос';

create table roommate_requests (
    id              bigserial,
    sender_id       bigint not null,
    receiver_id     bigint not null,
    status          varchar(10) not null default 'PENDING',
    created_at      timestamp not null default now(),
    ---------------------------------------------------
    constraint roommate_requests_id_pk primary key (id),
    constraint roommate_requests_sender_id_fk foreign key (sender_id) references users(id) on delete cascade ,
    constraint roommate_requests_receiver_id_fk foreign key (receiver_id) references users(id) on delete cascade ,
    constraint roommate_requests_status_ch check (status in ('PENDING', 'ACCEPTED', 'REJECTED')),
    constraint roommate_requests_sender_id_ch check (sender_id != receiver_id),
    constraint roommate_requests_sender_receiver_un unique (sender_id, receiver_id)
);

comment on table roommate_requests is 'Таблица заявок';
comment on column roommate_requests.id is 'id заявки';
comment on column roommate_requests.sender_id is 'id пользователя, который отправляет заявку';
comment on column roommate_requests.receiver_id is 'id пользователя, который получает заявку';
comment on column roommate_requests.status is 'Статус заявки';
comment on column roommate_requests.created_at is 'Время создания заявки';

create table rooms (
    id              bigserial,
    dormitory_id    int not null,
    owner_id        bigint not null,
    capacity        int not null,
    room_number     varchar(20) not null,
    description     varchar(255),
    created_at      timestamp not null default now(),
    ---------------------------------------------------
    constraint rooms_id_pk primary key (id),
    constraint rooms_dormitory_id_fk foreign key (dormitory_id) references dormitories(id),
    constraint rooms_owner_id_fk foreign key (owner_id) references users(id) on delete cascade,
    constraint rooms_capacity_ch check (capacity between 2 and 4)
);

comment on table rooms is 'Таблица комнат(не физических)';
comment on column rooms.id is 'id комнаты';
comment on column rooms.dormitory_id is 'id общежития';
comment on column rooms.owner_id is 'id пользователя, который создал комнату';
comment on column rooms.capacity is 'Вместимость комнаты(от 2 до 4)';
comment on column rooms.room_number is 'Номер комнаты';
comment on column rooms.description is 'Комментарий к комнате';
comment on column rooms.created_at is 'Время создания комнаты';

create table room_members (
    room_id         bigint not null,
    user_id         bigint not null,
    status          varchar(10) not null default 'PENDING',
    ---------------------------------------------------
    constraint room_members_pk primary key (room_id, user_id),
    constraint room_members_room_id_fk foreign key (room_id) references rooms(id) on delete cascade,
    constraint room_members_user_id_fk foreign key (user_id) references users(id) on delete cascade,
    constraint room_members_status_ch check (status in('PENDING', 'ACCEPTED', 'REJECTED'))
);
comment on table room_members is 'Таблица участников комнаты';
comment on column room_members.room_id is 'id комнаты';
comment on column room_members.user_id is 'id пользователей';
comment on column room_members.status is 'статус пользователя относительно комнаты';

create table dorm_reviews (
    id              bigserial,
    user_id         bigint not null,
    dormitory_id    int not null,
    text            varchar(1000) not null,
    rating          int not null,
    created_at      timestamp not null default now(),
    ---------------------------------------------------
    constraint dorm_reviews_id_pk primary key (id),
    constraint dorm_reviews_user_dormitory_un unique (user_id, dormitory_id),
    constraint dorm_reviews_user_id_fk foreign key (user_id) references users(id) on delete cascade,
    constraint dorm_reviews_dormitory_id_fk foreign key (dormitory_id) references dormitories(id),
    constraint dorm_reviews_rating_ch check (rating between 1 and 5)
);

comment on table dorm_reviews is 'Таблица отзывов об общежитиях';
comment on column dorm_reviews.id is 'id отзыва';
comment on column dorm_reviews.user_id is 'id автора отзыва';
comment on column dorm_reviews.dormitory_id is 'id общежития';
comment on column dorm_reviews.text is 'Текст отзыва';
comment on column dorm_reviews.rating is 'Оценка (1-5)';
comment on column dorm_reviews.created_at is 'Время создания отзыва';

create table complaints (
    id              bigserial,
    sender_id       bigint not null,
    receiver_id     bigint not null,
    text            varchar(1000) not null,
    status          varchar(10) not null default 'NEW',
    created_at      timestamp not null default now(),
    ---------------------------------------------------
    constraint complaints_id_pk primary key (id),
    constraint complaints_sender_id_fk foreign key (sender_id) references users(id) on delete cascade,
    constraint complaints_receiver_id_fk foreign key (receiver_id) references users(id) on delete cascade,
    constraint complaints_status_ch check (status in ('NEW', 'RESOLVED', 'REJECTED')),
    constraint complaints_sender_id_ch check (sender_id != receiver_id)
);

comment on table complaints is 'Таблица жалоб на пользователей';
comment on column complaints.id is 'id жалобы';
comment on column complaints.sender_id is 'id пользователя, который жалуется';
comment on column complaints.receiver_id is 'id пользователя, на которого жалуются';
comment on column complaints.text is 'Причина жалобы';
comment on column complaints.status is 'Статус: NEW, RESOLVED или REJECTED';
comment on column complaints.created_at is 'Время создания жалобы';
