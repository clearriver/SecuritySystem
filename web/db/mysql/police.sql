SET SESSION FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS js_biz_place;

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

