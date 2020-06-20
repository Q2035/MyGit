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
    user_id int unique,
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

# create table j_job_participants(
#     id int primary key auto_increment,
#     user_id int,
#     job_id int,
#     if_submit bool,
#     submit_time datetime,
#     foreign key(user_id) references j_user(id),
#     foreign key(job_id) references j_job(id)
# );

# 这里不该这样
# create table j_file_name(
#     id int primary key auto_increment,
#     job_id int,
#     style varchar(20),
#     foreign key(job_id) references j_job(id)
# );

# 列名不能为separator，Reserved keywords
create table j_file_name(
  id int primary key auto_increment,
  part_count int,
  job_id int,
  separat varchar(10),
  part1 varchar(20),
  part2 varchar(20),
  part3 varchar(20),
  part4 varchar(20),
  part5 varchar(20),
  part6 varchar(20),
  foreign key(job_id) references j_job(id)
);

create table j_clazz(
    id int primary key auto_increment,
    clazz_name varchar(20)
);

create table j_user_clazz(
    id int primary key auto_increment,
    user_id int,
    clazz_id int,
    foreign key (user_id) references j_user(id),
    foreign key (clazz_id) references j_clazz(id)
)