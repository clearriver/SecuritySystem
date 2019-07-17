/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.biz.entity;

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
 * 警员表Entity
 * @author 长江
 * @version 2019-01-12
 */
@Table(name="${_prefix}biz_police", alias="a", columns={
		@Column(name="police_code", attrName="policeCode", label="警员编号", isPK=true),
		@Column(name="police_name", attrName="policeName", label="警员姓名", queryType=QueryType.LIKE),
		@Column(name="police_type", attrName="policeType", label="警员类型"),
		@Column(name="office", attrName="office.officeCode", label="所属机构"),
		@Column(name="police_state", attrName="policeState", label="岗位状态", comment="岗位状态"),
		@Column(name="phone", attrName="phone", label="移动电话"),
		@Column(name="remark", attrName="remark", label="备注")
	}, joinTable={
			@JoinTable(type=Type.LEFT_JOIN, entity=Office.class, alias="o", 
					on="o.office_code = a.office ",attrName="office",
					columns={@Column(includeEntity=Office.class)}),
	}, orderBy="a.police_code DESC"
)

@Valid
@ExcelFields({
	@ExcelField(title="人员姓名", attrName="policeName", align=Align.CENTER, sort=10),
	@ExcelField(title="人员类型", attrName="policeType", align=Align.CENTER, sort=20,dictType="sys_biz_police_type"),
	@ExcelField(title="所属单位", attrName="office.officeName", align = Align.CENTER, sort=30, fieldType=OfficeType.class),
	@ExcelField(title="移动电话", attrName="phone", align=Align.CENTER, sort=90),
	@ExcelField(title="警号", attrName="policeCode", align=Align.CENTER, sort=10),
	@ExcelField(title="岗位状态", attrName="policeState", align=Align.CENTER, sort=100,dictType="sys_biz_police_state", type=ExcelField.Type.ALL),
})
public class BizPolice extends DataEntity<BizPolice> {
	
	private static final long serialVersionUID = 1L;
	private String policeCode;		// 警员编号
	private String policeName;		// 警员姓名
	private String policeType;		// 警员类型
	private Office office;		// 所属机构
	private String policeState;		// 岗位状态
	private String phone;		// 移动电话
	private String remark;		// 备注
	
	public BizPolice() {
		this(null);
	}

	public BizPolice(String id){
		super(id);
	}
	
	public String getPoliceCode() {
		return policeCode;
	}

	public void setPoliceCode(String policeCode) {
		this.policeCode = policeCode;
	}

	@NotBlank(message="警员名称不能为空")
	@Length(min=0, max=200, message="警员名称长度不能超过 200 个字符")
	public String getPoliceName() {
		return policeName;
	}

	public void setPoliceName(String policeName) {
		this.policeName = policeName;
	}
	@Length(min=0, max=100, message="移动电话长度不能超过 100 个字符")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPoliceType() {
		return policeType;
	}

	public void setPoliceType(String policeType) {
		this.policeType = policeType;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public String getPoliceState() {
		return policeState;
	}

	public void setPoliceState(String policeState) {
		this.policeState = policeState;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}