SET SESSION FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS js_biz_police;

-- 警员表
CREATE TABLE js_biz_police
(
	police_code varchar(100) NOT NULL COMMENT '警员编号',
	police_name varchar(200) NOT NULL COMMENT '警员名称',
	police_type varchar(100) NOT NULL COMMENT '警员类型',
	office varchar(100)  COMMENT '所属机构',
	police_state varchar(100)  COMMENT '岗位状态',
	phone varchar(100) COMMENT '移动电话',
	remark varchar(300) COMMENT '备注',
	PRIMARY KEY (police_code)
) COMMENT = '警员表';


/* Create Indexes */
CREATE INDEX idx_biz_police_pn ON js_biz_police (police_name ASC);

DROP TABLE IF EXISTS js_biz_icon;
-- 图标表
CREATE TABLE js_biz_icon
(
	icon_code varchar(100) NOT NULL COMMENT '图标组号',
	icon_name varchar(200) COMMENT '图标名称',
	icon_type varchar(100) COMMENT '图标类型',
	remark varchar(300) COMMENT '备注',
	PRIMARY KEY (icon_code)
) COMMENT = '警员表';


/* Create Indexes */
CREATE INDEX idx_biz_icon_pn ON js_biz_icon (icon_name ASC);


DROP TABLE IF EXISTS js_biz_activity;
-- 活动表
CREATE TABLE js_biz_activity
(
	activity_code varchar(100) NOT NULL COMMENT '活动编号',
	activity_name varchar(200) NOT NULL COMMENT '活动名称',
	activity_date timestamp  COMMENT '活动时间',
	address varchar(300)  COMMENT '活动主场地地址',
	coordinates varchar(100)  COMMENT '地理坐标',
	unit varchar(100)  COMMENT '参与单位',
	service_level varchar(20)  COMMENT '勤务级别',
	security_mode varchar(20)  COMMENT '安保模式（文体活动、会展会务活动、考察调研）',
	risk_level varchar(20)  COMMENT '风险等级',
	chief varchar(200)  COMMENT '安保总负责人姓名',
	remark varchar(3000) COMMENT '备注',
	PRIMARY KEY (activity_code)
) COMMENT = '活动表';


/* Create Indexes */
CREATE INDEX idx_biz_activity_pn ON js_biz_activity (activity_name ASC);

DROP TABLE IF EXISTS js_biz_solution;
-- 安保警力部署表
CREATE TABLE js_biz_solution
(
	solution_code varchar(100) NOT NULL COMMENT '安保警力部署组号',
	activity varchar(100) NOT NULL COMMENT '活动编号',
	office varchar(100) NOT NULL COMMENT '负责单位名称',
	start_position varchar(200) COMMENT '负责巡逻路段起点名称',
	start_coordinates varchar(200) COMMENT '负责巡逻路段起点坐标',
	end_position varchar(200) COMMENT '负责巡逻路段终点名称',
	end_coordinates varchar(200) COMMENT '负责巡逻路段终点坐标',
	side varchar(2) COMMENT '巡逻路段(左侧/右侧)',
	chief VARCHAR(300) COMMENT '负责人',
	core_task varchar(3000) COMMENT '工作要点',
	chief varchar(200)  COMMENT '安保总负责人姓名',
	persons varchar(500)  COMMENT '警员列表',
	remark varchar(3000) COMMENT '备注',
	PRIMARY KEY (solution_code)
) COMMENT = '安保警力部署表';

/* Create Indexes */
CREATE INDEX idx_biz_solution_pn ON js_biz_solution (solution_code ASC);


DROP TABLE IF EXISTS js_biz_fixed_plan;
-- 固定执勤部署图表
CREATE TABLE js_biz_fixed_plan
(
	plan_code varchar(100) NOT NULL COMMENT '安保警力部署编号',
	activity varchar(100) NOT NULL COMMENT '活动编号',
	office varchar(200) NOT NULL COMMENT '负责单位名称',
	person varchar(200) COMMENT '执勤人',
	position varchar(300) COMMENT '执勤位置',
	coordinates varchar(100)  COMMENT '地理坐标',
	core_task varchar(500) COMMENT '工作要点',
	plan_type varchar(2) COMMENT '固定执勤类型(主场地/制高点/备勤处置)',
	icon varchar(100)  COMMENT '警员图标选择',
	remark varchar(500) COMMENT '备注',
	PRIMARY KEY (plan_code)
) COMMENT = '固定执勤部署表';

/* Create Indexes */
CREATE INDEX idx_biz_fixed_plan_pn ON js_biz_fixed_plan (plan_code ASC);


-- Drop table

DROP TABLE IF EXISTS js_biz_action_plan;

CREATE TABLE js_biz_action_plan (
	action_code varchar(100) NOT NULL COMMENT '安保警力部署编号',
	activity varchar(100) COMMENT '活动编号',
	action_type varchar(2) COMMENT '方案类型(整体方案/应急预案)',
	title varchar(300) COMMENT '标题',
	detail varchar(10000) COMMENT '方案内容',
	PRIMARY KEY (action_code)
) ;
CREATE INDEX idx_biz_action_plan_pn ON js_biz_action_plan (action_code) ;


ALTER TABLE js_biz_solution add COLUMN duty_type VARCHAR(20) comment '巡逻类型：人员巡逻，车辆巡逻';
ALTER TABLE js_biz_solution add COLUMN icon VARCHAR(20) comment '车辆图标';


