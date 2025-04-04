create table if not exists forex.users
(
    id       bigserial primary key,
    username varchar(255) not null unique,
    password varchar(255)
);

create table if not exists forex.users_roles
(
    user_id bigint       not null,
    role    varchar(255) not null,
    primary key (user_id, role),
    constraint fk_users_roles_users foreign key (user_id) references forex.users (id) on delete cascade on update no action
);

create table if not exists forex.profiles
(
    id          bigserial primary key,
    user_id     bigint not null,
    name        varchar(255) unique,
    age         int check (age >= 0),
    description text,
    sex         int,
    constraint fk_users_profiles foreign key (user_id) references forex.users (id) on delete cascade on update no action
);

create table if not exists forex.profile_photos
(
    id         bigserial primary key,
    profile_id bigint not null unique,
    photo      text,
    constraint fk_profile_photos_profiles foreign key (profile_id) references forex.profiles (id) on delete cascade on update no action
);

create table if not exists forex.chat
(
    id             bigserial primary key,
    first_user_id  bigint not null,
    second_user_id bigint not null,
    check (first_user_id < second_user_id), -- Убедимся, что пользователи всегда в правильном порядке
    unique (first_user_id, second_user_id), -- Уникальность чата между двумя пользователями
    constraint fk_chat_first_user foreign key (first_user_id) references forex.users (id),
    constraint fk_chat_second_user foreign key (second_user_id) references forex.users (id)
);

create table if not exists forex.message
(
    id                bigserial primary key,
    message_text      text   not null,
    chat_id           bigint not null,
    message_sender_id bigint not null,
    created_at        timestamp default current_timestamp, -- Время отправки сообщения
    constraint fk_message_chat foreign key (chat_id) references forex.chat (id),
    constraint fk_message_sender foreign key (message_sender_id) references forex.users (id)
);

CREATE INDEX idx_message_chat_id ON forex.message (chat_id);
CREATE INDEX idx_message_created_at ON forex.message (created_at);