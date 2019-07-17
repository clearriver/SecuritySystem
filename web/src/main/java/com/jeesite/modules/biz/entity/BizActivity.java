/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.biz.entity;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

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
import com.jeesite.common.utils.excel.fieldtype.AreaType;
import com.jeesite.common.utils.excel.fieldtype.OfficeType;
import com.jeesite.modules.sys.entity.Area;
import com.jeesite.modules.sys.entity.Office;

/**
  *     活动表Entity
 * @author 长江
 * @version 2019-01-12
 */
@Table(name="${_prefix}biz_activity", alias="a", columns={
		@Column(name="activity_code", attrName="activityCode", label="活动编号", isPK=true),
		@Column(name="activity_name", attrName="activityName", label="活动名称", queryType=QueryType.LIKE),
		@Column(name="activity_date", attrName="activityDate", label="活动时间"),
		@Column(name="address", attrName="address", label="活动主场地地址", queryType=QueryType.LIKE),
		@Column(name="coordinates", attrName="coordinates", label="地理坐标"),
		@Column(name="unit", attrName="unit", label="参与单位", queryType=QueryType.LIKE),
		@Column(name="service_level", attrName="serviceLevel", label="勤务级别"),
		@Column(name="security_mode", attrName="securityMode", label="安保模式（文体活动、会展会务活动、考察调研）"),
		@Column(name="risk_level", attrName="riskLevel", label="风险等级"),
		@Column(name="chief", attrName="chief", label="安保总负责人姓名", queryType=QueryType.LIKE),
		@Column(name="remark", attrName="remark", label="备注")
	}, orderBy="a.activity_code DESC"
)

@Valid
@ExcelFields({
	@ExcelField(title="活动编号", attrName="activityCode", align=Align.CENTER, sort=10),
	@ExcelField(title="活动名称", attrName="activityName", align=Align.CENTER, sort=20),
	@ExcelField(title="活动时间", attrName="activityDate", align=Align.CENTER, sort=30, dataFormat="yyyy-MM-dd"),
	@ExcelField(title="活动主场地地址", attrName="address", align=Align.CENTER, sort=40),
	@ExcelField(title="参与单位", attrName="unit", align=Align.CENTER, sort=50),
	@ExcelField(title="勤务级别", attrName="serviceLevel", align=Align.CENTER, sort=60,dictType="sys_biz_service_level"),
	@ExcelField(title="安保模式", attrName="securityMode", align=Align.CENTER, sort=70,dictType="sys_biz_security_mode"),
	@ExcelField(title="风险等级", attrName="riskLevel", align=Align.CENTER, sort=80,dictType="sys_biz_risk_level"),
	@ExcelField(title="安保总负责人姓名", attrName="chief", align=Align.CENTER, sort=90),
	@ExcelField(title="备注", attrName="remark", align=Align.CENTER, sort=100, type=ExcelField.Type.ALL),
})
public class BizActivity extends DataEntity<BizActivity> {
	private static final long serialVersionUID = 1L;
	private String activityCode;		// 活动编号
	private String activityName;		// 活动名称
	private Date activityDate;		// 活动时间
	private String address;		// 活动主场地地址
	private String coordinates;		// 地理坐标
	private String unit;		// 参与单位
	private String serviceLevel;		// 勤务级别
	private String securityMode;		// 安保模式（文体活动、会展会务活动、考察调研）
	private String riskLevel;		// 风险等级
	private String chief;		// 安保总负责人姓名
	private String remark;		// 备注
	
	public BizActivity() {
		this(null);
	}

	public BizActivity(String id){
		super(id);
	}
	
	@Length(min=0, max=100, message="活动编号长度不能超过 100 个字符")
	public String getActivityCode() {
		return activityCode;
	}
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public Date getActivityDate() {
		return activityDate;
	}
	public void setActivityDate(Date activityDate) {
		this.activityDate = activityDate;
	}

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public String getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}

	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getServiceLevel() {
		return serviceLevel;
	}
	public void setServiceLevel(String serviceLevel) {
		this.serviceLevel = serviceLevel;
	}

	public String getSecurityMode() {
		return securityMode;
	}
	public void setSecurityMode(String securityMode) {
		this.securityMode = securityMode;
	}

	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}

	public String getChief() {
		return chief;
	}
	public void setChief(String chief) {
		this.chief = chief;
	}

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}