/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.biz.entity;

import javax.validation.Valid;

import org.hibernate.validator.constraints.Length;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelField.Align;
import com.jeesite.common.utils.excel.annotation.ExcelFields;
import com.jeesite.modules.file.entity.FileUpload;
import com.jeesite.modules.sys.entity.Office;

/**
 * 场所表Entity
 * @author 长江
 * @version 2019-01-12
 */
@Table(name="${_prefix}biz_fixed_plan", alias="a", columns={
		@Column(name="plan_code", attrName="planCode", label="安保警力部署编号", isPK=true),
		@Column(name="activity", attrName="activity.activityCode", label="活动编号"),
		@Column(name="office", attrName="office.officeCode", label="负责单位名称"),
		@Column(name="chief", attrName="chief", label="负责人"),
		@Column(name="person", attrName="person.policeCode", label="执勤人", queryType=QueryType.LIKE),
		@Column(name="position", attrName="position", label="执勤位置", queryType=QueryType.LIKE),
		@Column(name="coordinates", attrName="coordinates", label="地理坐标"),
		@Column(name="core_task", attrName="coreTask", label="工作要点", queryType=QueryType.LIKE),
		@Column(name="plan_type", attrName="planType", label="固定执勤类型(主场地/制高点/备勤处置)"),
		@Column(name="icon", attrName="icon.id", label="警员图标"),
		@Column(name="remark", attrName="remark", label="备注")
	}, joinTable={
			@JoinTable(type=Type.LEFT_JOIN, entity=BizActivity.class, alias="ba", 
					on="ba.activity_code = a.activity ",attrName="activity",
					columns={@Column(includeEntity=BizActivity.class)}),
			@JoinTable(type=Type.LEFT_JOIN, entity=Office.class, alias="o", 
					on="o.office_code = a.office ",attrName="office",
					columns={@Column(includeEntity=Office.class)}),
			@JoinTable(type=Type.LEFT_JOIN, entity=FileUpload.class, alias="bi", 
					on="bi.id = a.icon ",attrName="icon",
					columns={@Column(includeEntity=FileUpload.class)}),
			@JoinTable(type=Type.LEFT_JOIN, entity=BizPolice.class, alias="bp", 
				on="bp.police_code = a.person ",attrName="person",
				columns={@Column(includeEntity=BizPolice.class)}),
	}, orderBy="a.plan_code DESC"
)

@Valid
@ExcelFields({
	@ExcelField(title="安保警力部署编号", attrName="planCode", align=Align.CENTER, sort=10),
	@ExcelField(title="活动名称", attrName="activity.activityName", align=Align.CENTER, sort=20),
	@ExcelField(title="负责单位名称", attrName="office.officeName", align=Align.CENTER, sort=30),
	@ExcelField(title="执勤人", attrName="person.policeName", align=Align.CENTER, sort=30),
	@ExcelField(title="负责人", attrName="chief", align=Align.CENTER, sort=30),
	@ExcelField(title="执勤位置", attrName="position", align=Align.CENTER, sort=30),
	@ExcelField(title="地理坐标", attrName="coordinates", align=Align.CENTER, sort=30),
	@ExcelField(title="工作要点", attrName="coreTask", align=Align.CENTER, sort=30),
	@ExcelField(title="固定执勤类型", attrName="planType", align=Align.CENTER, sort=30,dictType="sys_biz_plan_type"),
	@ExcelField(title="警员图标", attrName="icon.fileName", align=Align.CENTER),
	@ExcelField(title="备注", attrName="remark", align=Align.CENTER, sort=10, type=ExcelField.Type.ALL),
})
public class BizFixedPlan extends DataEntity<BizFixedPlan> {
	
	private static final long serialVersionUID = 1L;
	private String planCode;		// 安保警力部署编号
	private BizActivity activity;		// 活动名称
	private Office office;		// 负责单位名称
	private BizPolice person;		// 执勤人
	private String chief;		// 执勤位置
	private String position;		// 执勤位置
	private String coordinates;		// 地理坐标
	private String coreTask;		// 工作要点
	private String planType;		// 固定执勤类型(主场地/制高点/备勤处置)
	private FileUpload icon;		// 警员图标
	private String remark;		// 备注
	
	public BizFixedPlan() {
		this(null);
	}

	public BizFixedPlan(String id){
		super(id);
	}

	@Length(min=0, max=100, message="安保警力部署编号长度不能超过 100 个字符")
	public String getPlanCode() {
		return planCode;
	}
	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public BizActivity getActivity() {
		return activity;
	}

	public void setActivity(BizActivity activity) {
		this.activity = activity;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public BizPolice getPerson() {
		return person;
	}

	public void setPerson(BizPolice person) {
		this.person = person;
	}

	public String getChief() {
		return chief;
	}

	public void setChief(String chief) {
		this.chief = chief;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}

	public String getCoreTask() {
		return coreTask;
	}

	public void setCoreTask(String coreTask) {
		this.coreTask = coreTask;
	}

	public String getPlanType() {
		return planType;
	}

	public void setPlanType(String planType) {
		this.planType = planType;
	}

	public FileUpload getIcon() {
		return icon;
	}

	public void setIcon(FileUpload icon) {
		this.icon = icon;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}