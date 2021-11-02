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
    user_id bigint(20),
    title varchar(200),
    description text,
    type int,
    create_date datetime,
    due_date datetime,
    status int,
    father_task bigint(20),
    foreign key(user_id) references user(user_id),
    foreign key(father_task) references task(task_id)
);

create table if not exists tag(
    tag_id bigint(20) primary key,
    tag_name varchar(30),
    user_id bigint(20),
    unique(user_id, tag_name),
    foreign key(user_id) references user(user_id)
);

create table if not exists task_tag(
    tt_id bigint(20) primary key auto_increment,
    task_id bigint(20),
    tag_id bigint(20),
    unique(task_id, tag_id),
    foreign key(task_id) references task(task_id),
    foreign key(tag_id) references tag(tag_id)
);

create table if not exists user_task(
    ut_id bigint(20) primary key auto_increment,
    user_id bigint(20),
    task_id bigint(20),
    unique(task_id, user_id),
    foreign key(user_id) references user(user_id),
    foreign key(task_id) references task(task_id)
);

# creator
create table if not exists team(
    team_id bigint(20) primary key auto_increment,
    team_name varchar(30) not null,
    create_time datetime default current_timestamp,
    description text,
    creator bigint(20),
    unique(creator, team_name),
    foreign key(creator) references user(user_id)
);

# role in teams
create table if not exists identity(
     identity_id bigint(20) primary key auto_increment,
     identity_name varchar(20) unique
);

create table if not exists team_user(
    gu_id bigint(20) primary key auto_increment,
    team_id bigint(20),
    user_id bigint(20),
    team_role bigint(5),
    unique(team_id, user_id),
    foreign key(team_id) references team(team_id),
    foreign key(user_id) references user(user_id),
    foreign key(team_role)  references identity(identity_id)
);

create table if not exists contact(
    uu_id bigint(20) primary key auto_increment,
    person bigint(20) ,
    friend bigint(20) ,
    unique(person, friend),
    foreign key(person) references user(user_id),
    foreign key(friend) references user(user_id)
);

create table if not exists message(
    message_i bigint(20) primary key auto_increment,
    sender bigint(20),
    receiver bigint(20) ,
    content text,
    is_read boolean,
    foreign key(sender) references user(user_id),
    foreign key(receiver) references user(user_id)
);

insert into contact(person, friend) values (1, 2);








