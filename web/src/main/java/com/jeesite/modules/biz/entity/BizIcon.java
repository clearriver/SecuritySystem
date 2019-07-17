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
 * 场所表Entity
 * @author 长江
 * @version 2019-01-12
 */
@Table(name="${_prefix}biz_icon", alias="a", columns={
		@Column(name="icon_code", attrName="iconCode", label="图标组号", isPK=true),
		@Column(name="icon_name", attrName="iconName", label="图标名称", queryType=QueryType.LIKE),
		@Column(name="icon_type", attrName="iconType", label="图标类型"),
		@Column(name="remark", attrName="remark", label="备注")
	}, orderBy="a.icon_code DESC"
)

@Valid
@ExcelFields({
	@ExcelField(title="图标组号", attrName="iconCode", align=Align.CENTER, sort=10),
	@ExcelField(title="图标名称", attrName="iconName", align=Align.CENTER, sort=20),
	@ExcelField(title="图标类型", attrName="iconType", align=Align.CENTER, sort=30,dictType="sys_biz_icon_type"),
	@ExcelField(title="备注", attrName="remark", align=Align.CENTER, sort=10, type=ExcelField.Type.ALL),
})
public class BizIcon extends DataEntity<BizIcon> {
	
	private static final long serialVersionUID = 1L;
	private String iconCode;		// 图标组号
	private String iconName;		// 图标名称
	private String iconType;		// 图标类型
	private String remark;		// 备注
	
	public BizIcon() {
		this(null);
	}

	public BizIcon(String id){
		super(id);
	}
	
//	@NotBlank(message="图标组号")
	@Length(min=0, max=100, message="图标组号长度不能超过 100 个字符")
	public String getIconCode() {
		return iconCode;
	}

	public void setIconCode(String iconCode) {
		this.iconCode = iconCode;
	}
	public String getIconName() {
		return iconName;
	}
	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	public String getIconType() {
		return iconType;
	}

	public void setIconType(String iconType) {
		this.iconType = iconType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}