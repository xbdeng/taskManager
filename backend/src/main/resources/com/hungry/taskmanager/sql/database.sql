create table if not exists user(
    user_id bigint(20) primary key auto_increment,
    username varchar(50) unique,
    password varchar(300),  # modified
    salt varchar(300), # modified
    first_name varchar(30),
    last_name varchar(30),
    email varchar(50), #modified
    phone varchar(20)
);

create table if not exists task(
    task_id bigint(20) primary key,
    user_id bigint(20) references user(user_id),
    title varchar(200),
    description text,
    type int,
    create_date datetime,
    due_date datetime,
    status int,
    father_task bigint(20) references task(task_id)
);

create table if not exists tag(
    tag_id bigint(20) primary key,
    tag_name varchar(30),
    user_id bigint(20) references user(user_id),
    unique(user_id, tag_name)
);

create table if not exists task_tag(
    tt_id bigint(20) primary key auto_increment,
    task_id bigint(20) references task(task_id),
    tag_id bigint(20) references tag(tag_id),
    unique(task_id, tag_id)
);

create table if not exists user_task(
    ut_id bigint(20) primary key auto_increment,
    user_id bigint(20) references user(user_id),
    task_id bigint(20) references task(task_id),
    unique(task_id, user_id)
);

# creator
create table if not exists team(
    team_id bigint(20) primary key auto_increment,
    team_name varchar(30) not null,
    create_time datetime default current_timestamp,
    description text,
    creator bigint(20) references user(user_id),
    unique(creator, team_name)
);

# role in teams
create table if not exists identity(
     identity_id bigint(20) primary key auto_increment,
     identity_name varchar(20) unique
);

create table if not exists team_user(
    gu_id bigint(20) primary key auto_increment,
    team_id bigint(20) references team(team_id),
    user_id bigint(20) references user(user_id),
    team_role bigint(5) references identity(identity_id),
    unique(team_id, user_id)
);

create table if not exists contact(
    uu_id bigint(20) primary key,
    person bigint(20) references user(user_id),
    friend bigint(20) references user(user_id),
    unique(person, friend)
);

create table if not exists message(
    message_i bigint(20) primary key auto_increment,
    sender bigint(20) references user(user_id),
    receiver bigint(20) references user(user_id),
    content text,
    is_read boolean
);









