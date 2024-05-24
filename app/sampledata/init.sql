drop database if exists AndroidDB;
create database if not exists AndroidDB default charset = utf8 collate = utf8_general_ci;
use AndroidDB;

# 管理员表
create table if not exists admins
(
    admin_id       int primary key auto_increment comment '管理员id',
    admin_name     varchar(50) comment '管理员姓名',
    admin_password varchar(50) comment '管理员密码',
    gender         int comment '性别 0: 男 1: 女',
    birthday       date comment '生日',
    start_time     date comment '入职时间'
) engine = InnoDB
  auto_increment = 1000
  default charset = utf8;

# 教师表
create table if not exists teachers
(
    teacher_id       int primary key auto_increment comment '教师id',
    teacher_name     varchar(50) comment '教师姓名',
    teacher_password varchar(50) comment '教师密码',
    gender           int comment '性别 0: 男 1: 女',
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
    gender           int comment '性别 0: 男 1: 女',
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
insert into admins (admin_name, admin_password, gender, birthday, start_time)
values ('admin', '202cb962ac59075b964b07152d234b70LJM', 0, '1995-01-01', '2015-08-01');
insert into teachers (teacher_name, teacher_password, gender, birthday, start_time)
values ('teacher', '202cb962ac59075b964b07152d234b70LJM', 1, '1990-01-01', '2010-08-01');
insert into students (student_name, student_password, gender, birthday, start_year, years)
values ('student', '202cb962ac59075b964b07152d234b70LJM', 0, '2000-10-29', 2024, 4);
insert into courses (course_name, course_credit, course_hour, course_week, course_type)
values ('移动终端应用开发', 3, 32, 18, 0);
insert into schedules (teacher_id, course_id, course_day, course_time, course_place, years, terms)
values (1000, 1000, 4, 5, 'S104', 2023, 1);
insert into scores (student_id, course_id, schedule_id, score)
values (1000, 1000, 1000, 90);

#管理员
INSERT INTO admins (admin_name, admin_password, gender, birthday, start_time) VALUES
('Admin1', '202cb962ac59075b964b07152d234b70LJM', 0, '1990-01-01', '2020-01-01'),
('Admin2', '202cb962ac59075b964b07152d234b70LJM', 1, '1992-02-14', '2021-02-15'),
('Admin3', '202cb962ac59075b964b07152d234b70LJM', 0, '1988-12-25', '2018-01-01'),
('Admin4', '202cb962ac59075b964b07152d234b70LJM', 1, '1995-05-20', '2022-05-21'),
('Admin5', '202cb962ac59075b964b07152d234b70LJM', 0, '1991-03-15', '2019-03-16'),
('Admin6', '202cb962ac59075b964b07152d234b70LJM', 1, '1994-06-30', '2020-07-01'),
('Admin7', '202cb962ac59075b964b07152d234b70LJM', 0, '1993-04-22', '2021-04-23'),
('Admin8', '202cb962ac59075b964b07152d234b70LJM', 1, '1996-09-10', '2023-09-11');
INSERT INTO admins (admin_name, admin_password, gender, birthday, start_time) VALUES
('Admin9', '202cb962ac59075b964b07152d234b70LJM', 0, '1997-08-08', '2022-08-09'),
('Admin10', '202cb962ac59075b964b07152d234b70LJM', 1, '1998-11-11', '2023-11-12');

#教师测试数据  教师id:1001-1009
INSERT INTO teachers (teacher_name, teacher_password, gender, birthday, start_time) VALUES
('张老师', '202cb962ac59075b964b07152d234b70LJM', 0, '1980-01-01', '2010-02-15'),
('李老师', '202cb962ac59075b964b07152d234b70LJM', 1, '1985-05-10', '2012-06-01'),
('王老师', '202cb962ac59075b964b07152d234b70LJM', 0, '1990-03-17', '2015-04-01'),
('赵老师', '202cb962ac59075b964b07152d234b70LJM', 1, '1978-12-25', '2008-01-01'),
('陈老师', '202cb962ac59075b964b07152d234b70LJM', 0, '1988-09-15', '2018-02-14'),
('刘老师', '202cb962ac59075b964b07152d234b70LJM', 1, '1992-06-30', '2020-07-01'),
('周老师', '202cb962ac59075b964b07152d234b70LJM', 0, '1983-11-11', '2013-10-15');
INSERT INTO teachers (teacher_name, teacher_password, gender, birthday, start_time) VALUES
('高老师', '202cb962ac59075b964b07152d234b70LJM', 1, '1986-02-28', '2016-03-15'),
('孙老师', '202cb962ac59075b964b07152d234b70LJM', 0, '1995-04-01', '2022-05-01');

#学生表测试数据  学生id:1001-1009
INSERT INTO students (student_name, student_password, gender, birthday, start_year, years) VALUES
('张三', '202cb962ac59075b964b07152d234b70LJM', 0, '2000-01-01', 2018, 4),
('李四', '202cb962ac59075b964b07152d234b70LJM', 1, '2001-05-10', 2019, 4),
('王五', '202cb962ac59075b964b07152d234b70LJM', 0, '2002-03-17', 2020, 4),
('赵六', '202cb962ac59075b964b07152d234b70LJM', 1, '1999-12-25', 2017, 5),
('陈七', '202cb962ac59075b964b07152d234b70LJM', 0, '2003-09-15', 2021, 2),
('刘八', '202cb962ac59075b964b07152d234b70LJM', 1, '2004-06-30', 2022, 4),
('周九', '202cb962ac59075b964b07152d234b70LJM', 0, '2005-11-11', 2023, 4);
INSERT INTO students (student_name, student_password, gender, birthday, start_year, years) VALUES
('吴十', '202cb962ac59075b964b07152d234b70LJM', 1, '2006-02-28', 2023, 4),
('郑十一', '202cb962ac59075b964b07152d234b70LJM', 0, '2007-04-01', 2024, 4);

# 课程信息表测试数据  课程id:1001-1047
INSERT INTO courses (course_name, course_credit, course_hour, course_week, course_type) VALUES
('数学分析', 4, 48, 16, 0),
('线性代数', 3, 36, 14, 0),
('大学英语', 2, 32, 18, 0),
('中国历史', 2, 32, 12, 1),
('程序设计', 3, 48, 16, 1),
('物理基础', 3, 36, 14, 0),
('心理学导论', 2, 32, 10, 1),
('艺术欣赏', 1, 16, 8, 1);
INSERT INTO courses (course_name, course_credit, course_hour, course_week, course_type) VALUES
('微观经济学', 3, 40, 15, 0),
('体育与健康', 1, 16, 10, 0);
INSERT INTO courses (course_name, course_credit, course_hour, course_week, course_type) VALUES
('微积分', 4, 48, 16, 0),
('高级线性代数', 3, 36, 14, 0),
('大学英语II', 2, 32, 18, 0),
('中国古代史', 2, 32, 12, 1),
('数据结构', 3, 48, 16, 1),
('量子力学', 3, 36, 14, 0),
('社会学导论', 2, 32, 10, 1),
('电影艺术', 1, 16, 8, 1),

('宏观经济学', 3, 40, 15, 0),
('体育与健康II', 1, 16, 10, 0),
('生物学导论', 2, 32, 12, 0),
('编程实战', 3, 64, 18, 1),
('文学批评', 2, 32, 14, 1),
('统计学原理', 3, 48, 16, 0),
('环境科学', 2, 32, 10, 0),
('哲学概论', 2, 32, 12, 1),

('心理学进阶', 3, 48, 16, 1),
('国际贸易', 3, 36, 14, 0),
('音乐基础', 1, 16, 8, 1),
('现代艺术史', 2, 32, 12, 1),
('计算机网络', 3, 48, 16, 1),
('物理实验', 2, 32, 10, 0),
('伦理学', 2, 32, 12, 1),
('有机化学', 3, 48, 16, 0),

('英语写作', 2, 32, 14, 0),
('金融学', 3, 36, 14, 0),
('世界地理', 2, 32, 10, 1),
('编程实践', 3, 64, 18, 1),
('中国近现代史', 2, 32, 12, 1),
('离散数学', 3, 48, 16, 0),
('经济学原理', 3, 40, 15, 0),
('数据库系统', 3, 48, 16, 1),

('创业管理', 2, 32, 12, 1),
('音乐欣赏', 1, 16, 8, 1),
('天体物理', 3, 36, 14, 0),
('市场营销', 3, 36, 14, 0),
('人力资源管理', 3, 48, 16, 0);

## 课程安排表测试数据    课程安排表id：1001-1030
INSERT INTO schedules (teacher_id, course_id, course_day, course_time, course_place, years, terms) VALUES
(1005, 1030, 3, 5, '地点17', 2022, 0),
(1009, 1020, 7, 7, '地点4', 2024, 1),
(1002, 1036, 5, 8, '地点1', 2021, 0),
(1003, 1004, 3, 3, '地点21', 2022, 1),
(1008, 1002, 7, 5, '地点9', 2021, 1),
(1008, 1022, 6, 2, '地点17', 2023, 1),
(1004, 1032, 6, 4, '地点29', 2022, 1),
(1005, 1004, 5, 2, '地点14', 2024, 0),
(1002, 1011, 1, 1, '地点9', 2021, 1),
(1008, 1038, 3, 1, '地点11', 2024, 0),
(1002, 1016, 4, 6, '地点14', 2024, 0),
(1004, 1014, 3, 6, '地点25', 2021, 0),
(1006, 1044, 4, 7, '地点11', 2023, 0),
(1003, 1006, 5, 2, '地点15', 2022, 1),
(1004, 1007, 6, 5, '地点19', 2024, 0),
(1002, 1026, 5, 8, '地点5', 2021, 1),
(1005, 1029, 5, 7, '地点3', 2022, 1),
(1003, 1001, 4, 2, '地点23', 2023, 0),
(1008, 1006, 3, 3, '地点21', 2021, 1),
(1007, 1003, 5, 1, '地点19', 2021, 1),
(1001, 1015, 1, 6, '地点3', 2024, 1),
(1008, 1036, 2, 7, '地点6', 2022, 1),
(1002, 1013, 7, 6, '地点12', 2022, 0),
(1004, 1014, 5, 8, '地点14', 2021, 1),
(1001, 1047, 6, 7, '地点25', 2023, 1),
(1007, 1013, 6, 7, '地点25', 2023, 0),
(1007, 1043, 7, 6, '地点9', 2022, 0),
(1005, 1045, 6, 3, '地点22', 2023, 0),
(1002, 1019, 7, 8, '地点20', 2023, 1);

# 成绩表测试数据
INSERT INTO scores (student_id, course_id, schedule_id, score) VALUES
(1001, 1035, 1023, 70),
(1006, 1040, 1026, 70),
(1007, 1039, 1021, 35),
(1006, 1009, 1017, 63),
(1004, 1043, 1027, 31),
(1005, 1007, 1013, 41),
(1005, 1027, 1020, 44),
(1008, 1009, 1008, 35),
(1001, 1020, 1028, 23),
(1005, 1031, 1021, 45),
(1006, 1016, 1026, 92),
(1008, 1021, 1024, 61),
(1009, 1040, 1006, 72),
(1008, 1002, 1023, 32),
(1007, 1020, 1003, 13),
(1001, 1016, 1022, 47),
(1008, 1045, 1017, 10),
(1009, 1002, 1007, 33),
(1001, 1028, 1002, 82),
(1006, 1044, 1028, 30),
(1002, 1035, 1028, 23),
(1001, 1047, 1007, 90),
(1005, 1007, 1008, 22),
(1007, 1002, 1016, 27),
(1003, 1046, 1026, 74),
(1003, 1025, 1022, 95),
(1005, 1043, 1026, 22),
(1002, 1037, 1026, 44),
(1003, 1017, 1017, 36);