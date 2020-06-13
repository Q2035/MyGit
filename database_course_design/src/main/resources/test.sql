create table j_user(
    id int primary key auto_increment,
    username varchar(20) not null unique ,
    password varchar(33),
    nickname varchar(10),
    avatar varchar(20),
    email varchar(30),
    account_status int,
    descriptor varchar(20)
);

create table j_role(
    id int primary key auto_increment,
    role_name varchar(20),
    role_description varchar(50)
);

create table j_user_role(
    id int primary key auto_increment,
    user_id int,
    role_id int,
    foreign key(user_id) references j_user(id),
    foreign key(role_id) references j_user_role(id)
);

create table j_black_list(
    id int primary key auto_increment,
    user_id int,
    happen_time datetime,
    ip_address varchar(23),
    reason varchar(30),
    foreign key(user_id) references j_user(id)
);

create table j_job(
    id int primary key auto_increment,
    job_description varchar(50),
    originator int,
    start_time datetime,
    deadline datetime,
    submit_count int,
    total_count int
);

create table j_submit_person(
    id int primary key auto_increment,
    user_id int,
    job_id int,
    if_submit int,
    submit_time datetime,
    foreign key(user_id) references j_user(id),
    foreign key(job_id) references j_job(id)
);

create table j_file_name(
    id int primary key auto_increment,
    job_id int,
    style varchar(20),
    foreign key(job_id) references j_job(id)
);