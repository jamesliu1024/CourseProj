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
    years            int comment '学年制',
    teacher_id       int comment '导师id',
    foreign key (teacher_id) references teachers (teacher_id)
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
    terms        int comment '学期 0:上学期 1:下学期  如果是2-8月，上年度的下半学期，9-1月是当前年度的上半学期',
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
    schedule_id int comment '课程表id',
    score       int comment '成绩',
    foreign key (student_id) references students (student_id),
    foreign key (schedule_id) references schedules (schedule_id)
) engine = InnoDB
  auto_increment = 1000
  default charset = utf8;

# 测试数据
insert into admins (admin_name, admin_password, gender, birthday, start_time)
values ('admin', '202cb962ac59075b964b07152d234b70LJM', 0, '1995-01-01', '2015-08-01');
insert into teachers (teacher_name, teacher_password, gender, birthday, start_time)
values ('王老师', '202cb962ac59075b964b07152d234b70LJM', 1, '1990-01-01', '2010-08-01');
insert into students (student_name, student_password, gender, birthday, start_year, years, teacher_id)
values ('刘同学', '202cb962ac59075b964b07152d234b70LJM', 0, '2000-10-29', 2023, 4, 1000);
# 课程1
insert into courses (course_name, course_credit, course_hour, course_week, course_type)
values ('移动终端应用开发', 3, 32, 18, 0);
insert into schedules (teacher_id, course_id, course_day, course_time, course_place, years, terms)
values (1000, 1000, 4, 5, 'S104', 2023, 1);
insert into scores (student_id, schedule_id, score)
values (1000, 1000, 90);

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
INSERT INTO students (student_name, student_password, gender, birthday, start_year, years, teacher_id) VALUES
('张三', '202cb962ac59075b964b07152d234b70LJM', 0, '2000-01-01', 2018, 4, 1000),
('李四', '202cb962ac59075b964b07152d234b70LJM', 1, '2001-05-10', 2019, 4, 1001),
('王五', '202cb962ac59075b964b07152d234b70LJM', 0, '2002-03-17', 2020, 4, 1000),
('赵六', '202cb962ac59075b964b07152d234b70LJM', 1, '1999-12-25', 2017, 5, 1002),
('陈七', '202cb962ac59075b964b07152d234b70LJM', 0, '2003-09-15', 2021, 2, 1003),
('刘八', '202cb962ac59075b964b07152d234b70LJM', 1, '2004-06-30', 2022, 4, 1000),
('周九', '202cb962ac59075b964b07152d234b70LJM', 0, '2005-11-11', 2023, 4, 1000),
('吴十', '202cb962ac59075b964b07152d234b70LJM', 1, '2006-02-28', 2023, 4, 1000),
('郑十一', '202cb962ac59075b964b07152d234b70LJM', 0, '2007-04-01', 2024, 4, 1000);

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
(1005, 1030, 3, 5, '地点17', 2023, 1), # 可用 1001
(1009, 1020, 7, 7, '地点4', 2023, 1), # 可用 1002
(1002, 1036, 5, 8, '地点1', 2023, 1), # 可用 1003
(1003, 1004, 3, 3, '地点21', 2023, 1), # 可用 1004
(1008, 1002, 7, 5, '地点9', 2021, 1), # 可用 1005
(1008, 1022, 6, 2, '地点17', 2023, 1), # 可用 1006
(1004, 1032, 6, 4, '地点29', 2023, 1), # 可用 1007
(1005, 1004, 5, 2, '地点14', 2023, 1), # 可用 1008
(1002, 1011, 1, 1, '地点9', 2023, 1), # 可用 1009
(1003, 1013, 3, 8, 'D204', 2024, 0),
(1004, 1019, 2, 1, 'C204', 2023, 1),
(1004, 1038, 4, 4, 'A306', 2024, 0),
(1009, 1008, 3, 4, 'U308', 2025, 0),
(1003, 1017, 5, 1, 'B307', 2024, 0),
(1003, 1035, 5, 1, 'C208', 2025, 1),
(1009, 1014, 5, 6, 'A106', 2025, 1),
(1009, 1002, 5, 4, 'C101', 2023, 1),
(1008, 1026, 3, 4, 'D307', 2024, 1),
(1009, 1045, 4, 3, 'D301', 2024, 0),
(1001, 1024, 1, 8, 'C303', 2024, 0),
(1002, 1047, 2, 7, 'C106', 2023, 1),
(1001, 1012, 2, 6, 'C308', 2025, 0),
(1009, 1011, 1, 7, 'T101', 2024, 0),
(1001, 1001, 2, 8, 'E102', 2024, 0),
(1007, 1026, 4, 1, 'B103', 2023, 0),
(1005, 1003, 1, 4, 'E302', 2024, 1),
(1005, 1020, 2, 7, 'S2-302', 2023, 1),
(1004, 1034, 3, 8, 'C305', 2023, 0),
(1007, 1033, 4, 1, 'B206', 2025, 1),
(1003, 1031, 1, 4, 'S2-207', 2024, 1),
(1008, 1031, 4, 6, 'S2-202', 2024, 0),
(1004, 1037, 4, 8, 'C301', 2025, 0),
(1001, 1027, 2, 7, 'C301', 2025, 1),
(1008, 1036, 4, 8, 'B306', 2024, 0),
(1008, 1020, 5, 4, 'S301', 2024, 0),
(1004, 1028, 3, 4, 'B207', 2025, 0),
(1007, 1015, 1, 2, 'S301', 2024, 1),
(1009, 1009, 4, 4, 'A305', 2023, 1);

# 成绩表测试数据
INSERT INTO scores (student_id, schedule_id, score) VALUES
(1001, 1000, 70),
(1006, 1000, 70),
(1007, 1000, 35),
(1006, 1017, 63),
(1004, 1027, 31),
(1005, 1013, 41),
(1005, 1020, 44),
(1008, 1008, 35),
(1001, 1028, 23),
(1005, 1021, 45),
(1006, 1026, 92),
(1008, 1024, 61),
(1009, 1006, 72),
(1008, 1023, 32),
(1007, 1003, 13),
(1001, 1022, 47),
(1008, 1017, 10),
(1009, 1007, 33),
(1001, 1002, 82),
(1006, 1028, 30),
(1002, 1028, 23),
(1001, 1007, 90),
(1005, 1008, 22),
(1007, 1016, 27),
(1003, 1026, 74),
(1003, 1022, 95),
(1005, 1026, 22),
(1002, 1026, 44),
(1003, 1017, 36);

INSERT INTO scores (student_id, schedule_id, score) VALUES
(1000, 1001, 98),
(1000, 1002, 88),
(1000, 1003, 85),
(1000, 1004, 97),
(1000, 1005, 77),
(1000, 1006, 86),
(1000, 1007, 94),
(1000, 1008, 50),
(1000, 1009, 0);