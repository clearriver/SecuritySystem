/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.biz.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.hibernate.validator.constraints.Length;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelField.Align;
import com.jeesite.common.utils.excel.annotation.ExcelFields;
import com.jeesite.modules.sys.entity.Office;

/**
 * 5方案管理表Entity
 * @author 长江
 * @version 2019-01-12
 */
@Table(name="${_prefix}biz_action_plan", alias="a", columns={
		@Column(name="action_code", attrName="actionCode", label="方案编号", isPK=true),
		@Column(name="activity", attrName="activity.activityCode", label="活动编号"),
		@Column(name="action_type", attrName="actionType", label="方案类型"),
		@Column(name="title", attrName="title", label="标题", queryType=QueryType.LIKE),
		@Column(name="detail", attrName="detail", label="详细内容")
	}, joinTable={
			@JoinTable(type=Type.LEFT_JOIN, entity=BizActivity.class, alias="ba", 
					on="ba.activity_code = a.activity ",attrName="activity",
					columns={@Column(includeEntity=BizActivity.class)}),
	}, orderBy="a.action_code DESC"
)

@Valid
@ExcelFields({
	@ExcelField(title="方案编号", attrName="actionCode", align=Align.CENTER, sort=10),
	@ExcelField(title="活动名称", attrName="activity.activityName", align=Align.CENTER, sort=20),
	@ExcelField(title="方案类型", attrName="actionType", align=Align.CENTER, sort=20,dictType="sys_biz_action_type"),
	@ExcelField(title="标题", attrName="title", align=Align.CENTER, sort=20),
	@ExcelField(title="详细内容", attrName="detail", align=Align.CENTER, sort=10, type=ExcelField.Type.ALL),
})
public class BizActionPlan extends DataEntity<BizActionPlan> {
	
	private static final long serialVersionUID = 1L;
	private String actionCode;		// 方案编号
	private BizActivity activity;		// 活动编号
	private String actionType;		// 方案类型
	private String title;		// 标题
	private String detail;// 详细内容
	public BizActionPlan() {
		this(null);
	}

	public BizActionPlan(String id){
		super(id);
	}

	public String getActionCode() {
		return actionCode;
	}

	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}

	public BizActivity getActivity() {
		return activity;
	}

	public void setActivity(BizActivity activity) {
		this.activity = activity;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}