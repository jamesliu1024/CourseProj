drop database if exists AndroidDB;
create database if not exists AndroidDB default charset = utf8 collate = utf8_general_ci;
use AndroidDB;

# 管理员表
create table if not exists admins
(
    admin_id       int primary key auto_increment comment '管理员id',
    admin_name     varchar(50) comment '管理员姓名',
    admin_password varchar(50) comment '管理员密码'
) engine = InnoDB
  auto_increment = 1000
  default charset = utf8;

# 教师表
create table if not exists teachers
(
    teacher_id       int primary key auto_increment comment '教师id',
    teacher_name     varchar(50) comment '教师姓名',
    teacher_password varchar(50) comment '教师密码',
    gender           int comment '性别',
    birthday         date comment '生日',
    start_time       date comment '入职时间'
) engine = InnoDB
  auto_increment = 1000
  default charset = utf8;

# 学生表
create table if not exists students
(
    student_id       int primary key auto_increment comment '学生id',
    student_name     varchar(50) comment '学生姓名',
    student_password varchar(50) comment '学生密码',
    gender           int comment '性别',
    birthday         date comment '生日',
    start_year       int comment '入学年份',
    years            int comment '学年制'
) engine = InnoDB
  auto_increment = 1000
  default charset = utf8;

# 课程信息表
create table if not exists courses
(
    course_id     int primary key auto_increment comment '课程id',
    course_name   varchar(50) comment '课程名称',
    course_credit int comment '学分',
    course_hour   int comment '学时',
    course_week   int comment '周数',
    course_type   int comment '课程类型 0:必修 1:选修'
) engine = InnoDB
  auto_increment = 1000
  default charset = utf8;

# 课程安排表
create table if not exists schedules
(
    schedule_id  int primary key auto_increment comment '课程表id',
    teacher_id   int comment '教师id',
    course_id    int comment '课程id',
    course_day   int comment '上课时间 1-7分别代表周一到周日',
    course_time  int comment '上课时间 1-8分别代表第一节到第八节课',
    course_place varchar(50) comment '上课地点',
    years        int comment '学年',
    terms        int comment '学期 0:上学期 1:下学期',
    foreign key (teacher_id) references teachers (teacher_id),
    foreign key (course_id) references courses (course_id)
) engine = InnoDB
  auto_increment = 1000
  default charset = utf8;

# 成绩表
create table if not exists scores
(
    score_id    int primary key auto_increment comment '成绩id',
    student_id  int comment '学生id',
    course_id   int comment '课程id',
    schedule_id int comment '课程表id',
    score       int comment '成绩',
    foreign key (student_id) references students (student_id),
    foreign key (course_id) references courses (course_id),
    foreign key (schedule_id) references schedules (schedule_id)
) engine = InnoDB
  auto_increment = 1000
  default charset = utf8;

# 测试数据
insert into admins (admin_name, admin_password) values ('admin', 'e10adc3949ba59abbe56e057f20f883eLJM');
insert into teachers (teacher_name, teacher_password,gender,birthday,start_time) values ('teacher', 'e10adc3949ba59abbe56e057f20f883eLJM',1,'1990-01-01','2010-08-01');
insert into students (student_name, student_password,gender,birthday,start_year,years) values ('student', 'e10adc3949ba59abbe56e057f20f883eLJM',1,'2000-10-29',2024,4);
insert into courses (course_name, course_credit, course_hour, course_week,course_type) values ('移动终端应用开发', 3, 32, 18,0);
insert into schedules (teacher_id, course_id, course_day, course_time, course_place, years, terms) values (1000, 1000, 4, 5, 'S104', 2023, 1);
insert into scores (student_id, course_id, schedule_id, score) values (1000, 1000, 1000, 90);