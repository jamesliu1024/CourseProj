drop database if exists AndroidDB;
create database if not exists AndroidDB default charset = utf8 collate = utf8_general_ci;
use AndroidDB;

# 教师表
create table if not exists teachers
(
    teacher_id       int primary key auto_increment comment '教师id',
    teacher_name     varchar(50) comment '教师姓名',
    teacher_phone    varchar(50) comment '教师电话',
    teacher_password varchar(50) comment '教师密码',
    gender           int comment '性别',
    birthday         date comment '生日',
    start_year       date comment '入职时间'
) engine = InnoDB
  auto_increment = 1000
  default charset = utf8;

# 班级表
create table if not exists classes
(
    class_id   int primary key auto_increment comment '班级id',
    class_name varchar(50) comment '班级名称',
    teacher_id int comment '班主任id',
    foreign key (teacher_id) references teachers (teacher_id)
) engine = InnoDB
  auto_increment = 1000
  default charset = utf8;

# 学生表
create table if not exists students
(
    student_id       int primary key auto_increment comment '学生id',
    student_name     varchar(50) comment '学生姓名',
    student_phone    varchar(50) comment '学生电话',
    student_password varchar(50) comment '学生密码',
    gender           int comment '性别',
    birthday         date comment '生日',
    start_year       date comment '入学时间',
    years            int comment '学年制',
    class_id         int comment '班级',
    foreign key (class_id) references classes (class_id)
) engine = InnoDB
  auto_increment = 1000
  default charset = utf8;

# 课程信息表
create table if not exists courses
(
    course_id         int primary key auto_increment comment '课程id',
    course_name       varchar(50) comment '课程名称',
    teacher_id        int comment '教师id',
    course_time       varchar(50) comment '上课时间',
    course_place      varchar(50) comment '上课地点',
    course_credit     int comment '学分',
    course_hour       int comment '学时',
    course_week       int comment '周数',
    course_start_time date comment '开课时间',
    course_end_time   date comment '结课时间',
    foreign key (teacher_id) references teachers (teacher_id)
) engine = InnoDB
  auto_increment = 1000
  default charset = utf8;

# 课程安排表
create table if not exists schedules
(
    schedule_id int primary key auto_increment comment '课程表id',
    student_id  int comment '学生id',
    course_id   int comment '课程id',
    foreign key (student_id) references students (student_id),
    foreign key (course_id) references courses (course_id)
) engine = InnoDB
  auto_increment = 1000
  default charset = utf8;

# 成绩表
create table if not exists scores
(
    score_id   int primary key auto_increment comment '成绩id',
    student_id int comment '学生id',
    course_id  int comment '课程id',
    score      int comment '成绩',
    foreign key (student_id) references students (student_id),
    foreign key (course_id) references courses (course_id)
) engine = InnoDB
  auto_increment = 1000
  default charset = utf8;