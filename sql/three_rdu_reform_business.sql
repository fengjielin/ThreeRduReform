-- ============================================================
-- 三教改革资讯平台 - 业务数据表SQL脚本
-- 数据库: three_rdu_reform
-- 版本: V1.0
-- 创建日期: 2026-04-07
-- ============================================================

-- ----------------------------
-- 1、院校信息表
-- ----------------------------
drop table if exists base_college;
create table base_college (
  college_id        bigint(20)      not null auto_increment    comment '院校ID',
  parent_id         bigint(20)      default 0                  comment '父院校ID',
  ancestors         varchar(50)     default ''                  comment '祖级列表',
  college_code      varchar(20)     default ''                  comment '院校编码',
  college_name      varchar(100)    not null                    comment '院校名称',
  college_short_name varchar(50)    default null               comment '院校简称',
  college_type      varchar(20)     default null               comment '院校类型',
  area              varchar(100)    default null               comment '所在地区',
  address           varchar(255)    default null               comment '详细���址',
  contact           varchar(50)     default null               comment '联系人',
  phone             varchar(20)     default null               comment '联系电话',
  intro             text                                       comment '院校简介',
  order_num         int(4)          default 0                  comment '显示顺序',
  status            char(1)         default '0'                comment '院校状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (college_id),
  key idx_parent_id (parent_id)
) engine=innodb auto_increment=100 comment = '院校信息表';

-- 初始化数据
insert into base_college values(1, 0, '0', 'GDJC', '广东交通职业技术学院', '广交院', '专科', '广东省广州市', '广州市天河区', '张老师', '020-12345678', '国家示范性高职院校', 1, '0', '0', 'admin', sysdate(), '', null, '');
insert into base_college values(2, 1, '0,1', 'GDJC-JS', '广东交通职业技术学院(计算机学院)', '计算机学院', '专科', '广东省广州市', '广州市天河区', '王老师', '020-12345679', '计算机学院', 1, '0', '0', 'admin', sysdate(), '', null, '');


-- ----------------------------
-- 2、专业信息表
-- ----------------------------
drop table if exists base_major;
create table base_major (
  major_id          bigint(20)      not null auto_increment    comment '专业ID',
  major_code        varchar(20)     not null                    comment '专业编码',
  major_name        varchar(100)    not null                    comment '专业名称',
  major_short_name  varchar(50)    default null               comment '专业简称',
  major_category    varchar(50)     default null               comment '专业类别',
  duration          varchar(20)     default null               comment '学制',
  edu_level         varchar(20)     default null               comment '学历层次',
  intro             text                                       comment '专业简介',
  status            char(1)         default '0'                comment '专业状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (major_id),
  unique key uk_major_code (major_code)
) engine=innodb auto_increment=100 comment = '专业信息表';

-- 初始化数据
insert into base_major values(1, 'JSJ01', '计算机应用技术', '计应', '计算机类', '3年', '专科', '培养掌握计算机应用技术的高级技能人才', '0', '0', 'admin', sysdate(), '', null, '');
insert into base_major values(2, 'SZJJ01', '数字媒体技术', '数媒', '计算机类', '3年', '专科', '培养数字媒体设计与开发人才', '0', '0', 'admin', sysdate(), '', null, '');
insert into base_major values(3, 'JYGC01', '计算机网络技术', '网络', '计算机类', '3年', '专科', '培养网络工程设计与运维人才', '0', '0', 'admin', sysdate(), '', null, '');
insert into base_major values(4, 'JYJC01', '软件技术', '软件', '计算机类', '3年', '专科', '培养软件开发与测试人才', '0', '0', 'admin', sysdate(), '', null, '');


-- ----------------------------
-- 3、教师信息表
-- ----------------------------
drop table if exists base_teacher;
create table base_teacher (
  teacher_id        bigint(20)      not null auto_increment    comment '教师ID',
  teacher_code      varchar(20)     not null                    comment '教师工号',
  teacher_name      varchar(50)     not null                    comment '教师姓名',
  teacher_type      varchar(20)     not null                    comment '教师类型（enterprise企业导师 teacher教师）',
  sex               char(1)         default '0'                comment '性别（0男 1女）',
  phone             varchar(11)     default null               comment '手机号码',
  email             varchar(100)    default null               comment '邮箱',
  avatar            varchar(255)    default null               comment '个人照片',
  intro             text                                       comment '个人简介',
  college_id        bigint(20)      default null               comment '所属院校',
  sort              int(4)          default 0                  comment '显示顺序',
  status            char(1)         default '0'                comment '状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (teacher_id),
  unique key uk_teacher_code (teacher_code),
  key idx_college_id (college_id)
) engine=innodb auto_increment=100 comment = '教师信息表';

-- 初始化数据
insert into base_teacher values(1, 'T2024001', '张三', 'teacher', '0', '13800138001', 'zhangsan@example.com', null, '计算机专业骨干教师，从事教育工作10年，主讲Java程序设计、数据结构等课程', 1, 1, '0', '0', 'admin', sysdate(), '', null, '');
insert into base_teacher values(2, 'T2024002', '李四', 'enterprise', '1', '13800138002', 'lisi@company.com', null, '企业技术专家，阿里巴巴高级工程师，15年互联网从业经验', 1, 2, '0', '0', 'admin', sysdate(), '', null, '');
insert into base_teacher values(3, 'T2024003', '王五', 'teacher', '0', '13800138003', 'wangwu@example.com', null, '副教授，硕士生导师，主要研究方向为人工智能', 1, 3, '0', '0', 'admin', sysdate(), '', null, '');
insert into base_teacher values(4, 'T2024004', '赵六', 'enterprise', '0', '13800138004', 'zhaoliu@tech.com', null, '华为技术有限公司高级工程师，擅长云计算与大数据技术', 2, 4, '0', '0', 'admin', sysdate(), '', null, '');


-- ----------------------------
-- 4、学生信息表
-- ----------------------------
drop table if exists base_student;
create table base_student (
  student_id        bigint(20)      not null auto_increment    comment '学生ID',
  student_code      varchar(20)     not null                    comment '学号',
  student_name      varchar(50)     not null                    comment '学生姓名',
  sex               char(1)         not null                    comment '性别（0男 1女）',
  birthday          date            default null               comment '出生日期',
  nation            varchar(20)     default null               comment '民族',
  id_card           varchar(18)     default null               comment '身份证号',
  phone             varchar(11)     default null               comment '手机号码',
  college_id        bigint(20)      not null                   comment '所属院校',
  major_id          bigint(20)      not null                   comment '所学专业',
  grade             varchar(20)     default null               comment '年级',
  class_name        varchar(50)     default null               comment '班级',
  entrance_date     date            default null               comment '入校时间',
  graduation_date   date            default null               comment '毕业时间',
  graduation_dest   varchar(255)    default null               comment '毕业去向',
  status            char(1)         default '0'                comment '状态（0在读 1毕业 2肄业）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (student_id),
  unique key uk_student_code (student_code),
  key idx_college_id (college_id),
  key idx_major_id (major_id)
) engine=innodb auto_increment=100 comment = '学生信息表';

-- 初始化数据
insert into base_student values(1, 'S20240001', '王小明', '0', '2005-03-15', '汉族', '440106200503151234', '13900139001', 1, 1, '2024级', '24计应1班', '2024-09-01', null, null, '0', '0', 'admin', sysdate(), '', null, '2024级计算机应用技术专业新生');
insert into base_student values(2, 'S20240002', '李小红', '1', '2005-06-20', '汉族', '440106200506201234', '13900139002', 1, 2, '2024级', '24数媒1班', '2024-09-01', null, null, '0', '0', 'admin', sysdate(), '', null, '');
insert into base_student values(3, 'S20230001', '张小华', '0', '2004-02-10', '汉族', '440106200402101234', '13900139003', 1, 1, '2023级', '23计应2班', '2023-09-01', null, null, '0', '0', 'admin', sysdate(), '', null, '');
insert into base_student values(4, 'S20220001', '刘美丽', '1', '2003-09-05', '汉族', '440106200309051234', '13900139004', 1, 3, '2022级', '22网络1班', '2022-09-01', '2025-06-30', '就业-腾讯科技', '1', '0', 'admin', sysdate(), '', null, '已顺利毕业并就业');
insert into base_student values(5, 'S20210001', '陈大力', '0', '2002-12-25', '汉族', '440106200212251234', '13900139005', 1, 4, '2021级', '21软件1班', '2021-09-01', '2024-06-30', '升学-专升本', '1', '0', 'admin', sysdate(), '', null, '升入本科院校继续深造');


-- ----------------------------
-- 5、教师与院校关联表
-- ----------------------------
drop table if exists base_teacher_college;
create table base_teacher_college (
  teacher_id        bigint(20)      not null                   comment '教师ID',
  college_id        bigint(20)      not null                   comment '院校ID',
  primary key (teacher_id, college_id),
  key idx_college_id (college_id)
) engine=innodb comment = '教师与院校关联表';

-- 初始化关联数据
insert into base_teacher_college values(1, 1);
insert into base_teacher_college values(1, 2);
insert into base_teacher_college values(2, 1);
insert into base_teacher_college values(3, 1);
insert into base_teacher_college values(3, 2);
insert into base_teacher_college values(4, 2);


-- ----------------------------
-- 6、院校与专业关联表
-- ----------------------------
drop table if exists base_college_major;
create table base_college_major (
  college_id        bigint(20)      not null                   comment '院校ID',
  major_id          bigint(20)      not null                   comment '专业ID',
  primary key (college_id, major_id),
  key idx_major_id (major_id)
) engine=innodb comment = '院校与专业关联表';

-- 初始化关联数据
insert into base_college_major values(1, 1);
insert into base_college_major values(1, 2);
insert into base_college_major values(1, 3);
insert into base_college_major values(1, 4);
insert into base_college_major values(2, 1);
insert into base_college_major values(2, 2);


-- ============================================================
-- 字典数据初始化
-- ============================================================

-- 字典类型初始化
insert into sys_dict_type values(100, '教师类型', 'teacher_type', '0', 'admin', sysdate(), '', null, '教师类型列表');
insert into sys_dict_type values(101, '院校类型', 'college_type', '0', 'admin', sysdate(), '', null, '院校类型列表');
insert into sys_dict_type values(102, '学历层次', 'edu_level', '0', 'admin', sysdate(), '', null, '学历层次列表');
insert into sys_dict_type values(103, '学生状态', 'student_status', '0', 'admin', sysdate(), '', null, '学生状态列表');
insert into sys_dict_type values(104, '毕业去向', 'graduation_dest', '0', 'admin', sysdate(), '', null, '毕业去向列表');
insert into sys_dict_type values(105, '专业类别', 'major_category', '0', 'admin', sysdate(), '', null, '专业类别列表');

-- 教师类型字典数据
insert into sys_dict_data values(1001, 1, '企业导师', 'T01', 'teacher_type', '', 'warning', 'Y', '0', 'admin', sysdate(), '', null, '来自合作企业的导师');
insert into sys_dict_data values(1002, 2, '教师', 'T02', 'teacher_type', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '院校正式教师');

-- 院校类型字典数据
insert into sys_dict_data values(1011, 1, '本科', 'T01', 'college_type', '', '', 'Y', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1012, 2, '专科', 'T02', 'college_type', '', '', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1013, 3, '中职', 'T03', 'college_type', '', '', 'N', '0', 'admin', sysdate(), '', null, '');

-- 学历层次字典数据
insert into sys_dict_data values(1021, 1, '中职', 'T03', 'edu_level', '', '', 'Y', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1022, 2, '专科', 'T02', 'edu_level', '', '', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1023, 3, '本科', 'T01', 'edu_level', '', '', 'N', '0', 'admin', sysdate(), '', null, '');

-- 学生状态字典数据
insert into sys_dict_data values(1031, 1, '在读', '0', 'student_status', '', 'primary', 'Y', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1032, 2, '毕业', '1', 'student_status', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1033, 3, '肄业', '2', 'student_status', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');

-- 毕业去向字典数据
insert into sys_dict_data values(1041, 1, '就业', '0', 'graduation_dest', '', 'success', 'Y', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1042, 2, '升学', '1', 'graduation_dest', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1043, 3, '创业', '2', 'graduation_dest', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1044, 4, '其他', '3', 'graduation_dest', '', 'info', 'N', '0', 'admin', sysdate(), '', null, '');

-- 专业类别字典数据
insert into sys_dict_data values(1051, 1, '计算机类', '计算机类', 'major_category', '', 'primary', 'Y', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1052, 2, '机械类', '机械类', 'major_category', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1053, 3, '电子类', '电子类', 'major_category', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1054, 4, '经济管理类', '经济管理类', 'major_category', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '');
insert into sys_dict_data values(1055, 5, '艺术类', '艺术类', 'major_category', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '');

-- ============================================================
-- 基础模块菜单初始化
-- ============================================================

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('基础信息', '0', '1', 'base', '#', 1, 0, 'M', '0', '0', '', '#', 'admin', sysdate(), '', null, '基础信息');

SELECT @parentIdM := LAST_INSERT_ID();

-- ----------------------------
-- 院校管理菜单
-- ----------------------------
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('院校管理', @parentIdM, '1', 'college', 'base/college/index', 1, 0, 'C', '0', '0', 'base:college:list', '#', 'admin', sysdate(), '', null, '院校管理菜单');

SELECT @parentId := LAST_INSERT_ID();

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('院校查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'base:college:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('院校新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'base:college:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('院校修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'base:college:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('院校删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'base:college:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('院校导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'base:college:export',       '#', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- 专业管理菜单
-- ----------------------------
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('专业管理', @parentIdM, '2', 'major', 'base/major/index', 1, 0, 'C', '0', '0', 'base:major:list', '#', 'admin', sysdate(), '', null, '专业管理菜单');

SELECT @parentId := LAST_INSERT_ID();

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('专业查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'base:major:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('专业新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'base:major:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('专业修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'base:major:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('专业删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'base:major:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('专业导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'base:major:export',       '#', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- 教师管理菜单
-- ----------------------------
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('教师管理', @parentIdM, '3', 'teacher', 'base/teacher/index', 1, 0, 'C', '0', '0', 'base:teacher:list', '#', 'admin', sysdate(), '', null, '教师管理菜单');

SELECT @parentId := LAST_INSERT_ID();

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('教师查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'base:teacher:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('教师新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'base:teacher:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('教师修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'base:teacher:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('教师删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'base:teacher:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('教师导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'base:teacher:export',       '#', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- 学生管理菜单
-- ----------------------------
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('学生管理', @parentIdM, '4', 'student', 'base/student/index', 1, 0, 'C', '0', '0', 'base:student:list', '#', 'admin', sysdate(), '', null, '学生管理菜单');

SELECT @parentId := LAST_INSERT_ID();

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('学生查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'base:student:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('学生新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'base:student:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('学生修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'base:student:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('学生删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'base:student:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('学生导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'base:student:export',       '#', 'admin', sysdate(), '', null, '');

-- ============================================================
-- 执行完毕
-- ============================================================
