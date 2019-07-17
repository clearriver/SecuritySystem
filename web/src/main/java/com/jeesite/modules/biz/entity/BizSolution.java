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
 * 安保警力部署表Entity
 * @author 长江
 * @version 2019-01-12
 */
@Table(name="${_prefix}biz_solution", alias="a", columns={
		@Column(name="solution_code", attrName="solutionCode", label="安保警力部署编号", isPK=true),
		@Column(name="activity", attrName="activity.activityCode", label="活动编号"),
		@Column(name="office", attrName="office.officeCode", label="负责单位名称"),
		@Column(name="side", attrName="side", label="巡逻路段(左侧/右侧)"),
		@Column(name="chief", attrName="chief", label="负责人"),
		@Column(name="start_position", attrName="startPosition", label="负责巡逻路段起点名称", queryType=QueryType.LIKE),
		@Column(name="start_coordinates", attrName="startCoordinates", label="起点坐标"),
		@Column(name="end_position", attrName="endPosition", label="负责巡逻路段终点名称", queryType=QueryType.LIKE),
		@Column(name="end_coordinates", attrName="endCoordinates", label="终点坐标"),
		@Column(name="core_task", attrName="coreTask", label="工作要点", queryType=QueryType.LIKE),
		@Column(name="duty_type", attrName="dutyType", label="巡逻类型：人员巡逻，车辆巡逻"),
		@Column(name="icon", attrName="icon.id", label="车辆图标"),
		@Column(name="persons", attrName="persons", label="警员列表"),
		@Column(name="line_color", attrName="lineColor", label="路段颜色"),
		@Column(name="remark", attrName="remark", label="备注")
	}, joinTable={
			@JoinTable(type=Type.LEFT_JOIN, entity=BizActivity.class, alias="ba", 
					on="ba.activity_code = a.activity ",attrName="activity",
					columns={@Column(includeEntity=BizActivity.class)}),
			@JoinTable(type=Type.LEFT_JOIN, entity=FileUpload.class, alias="bi", 
				on="bi.id = a.icon ",attrName="icon",
				columns={@Column(includeEntity=FileUpload.class)}),
			@JoinTable(type=Type.LEFT_JOIN, entity=Office.class, alias="o", 
					on="o.office_code = a.office ",attrName="office",
					columns={@Column(includeEntity=Office.class)}),
	}, orderBy="a.solution_code DESC"
)

@Valid
@ExcelFields({
	@ExcelField(title="安保警力部署组号", attrName="solutionCode", align=Align.CENTER, sort=10),
	@ExcelField(title="活动名称", attrName="activity.activityName", align=Align.CENTER, sort=20),
	@ExcelField(title="负责单位名称", attrName="office.officeName", align=Align.CENTER, sort=30),
	@ExcelField(title="负责人", attrName="chief", align=Align.CENTER, sort=40),
	@ExcelField(title="负责巡逻路段起点名称", attrName="startPosition", align=Align.CENTER, sort=50),
	@ExcelField(title="负责巡逻路段起点名称", attrName="endPosition", align=Align.CENTER, sort=60),
	@ExcelField(title="巡逻路段", attrName="side", align=Align.CENTER, sort=70,dictType="sys_biz_side_type"),
	@ExcelField(title="工作要点", attrName="coreTask", align=Align.CENTER, sort=80),
	@ExcelField(title="巡逻类型", attrName="dutyType", align=Align.CENTER, sort=30,dictType="sys_biz_duty_type"),
	@ExcelField(title="警员列表", attrName="persons", align=Align.CENTER, sort=90),
	@ExcelField(title="路段颜色", attrName="lineColor", align=Align.CENTER, sort=90),
	@ExcelField(title="备注", attrName="remark", align=Align.CENTER, sort=100, type=ExcelField.Type.ALL),
})
public class BizSolution extends DataEntity<BizSolution> {
	
	private static final long serialVersionUID = 1L;
	private String solutionCode;		// 安保警力部署编号
	private BizActivity activity;		// 活动编号
	private Office office;		// 负责单位名称
	private String side;		// 巡逻路段(左侧/右侧)
	private String chief;		// 负责人
	private String startPosition;		// 负责巡逻路段起点名称
	private String startCoordinates;		// 起点坐标
	private String endPosition;		// 负责巡逻路段起点名称
	private String endCoordinates;		// 终点坐标
	private String coreTask;		// 工作要点
	private String dutyType;		// 巡逻类型：人员巡逻，车辆巡逻
	private FileUpload icon;		// 车辆图标
	private String persons;// 警员列表
	private String personNames;// 警员列表
	private String lineColor;// 警员列表
	private String remark;		// 备注

	public String getPersonCodes() {
		return persons;
	}
	public void setPersonCodes(String persons) {
		this.persons = persons;
	}
	public String getPersonNames(){
		return personNames;
	}
	public void setPersonNames(String personNames) {
		this.personNames = personNames;
	}

	//	private List<BizPolice> personList=ListUtils.newArrayList();		// 警员列表
//	public List<BizPolice> getPersonList() {
//		return personList;
//	}
//	public void setPersonList(List<BizPolice> personList) {
//		this.personList = personList;
//	}
//	public String[] getPersons(){
//		List<String> list = ListUtils.extractToList(personList, "policeCode");
//		return list.toArray(new String[list.size()]);
//	}
//	public void setPersons(String[] persons) {
//		for (String val : persons){
//			if (StringUtils.isNotBlank(val)){
//				BizPolice e = new BizPolice();
//				e.setPoliceCode(val);
//				this.personList.add(e);
//			}
//		}
//	}
	public BizSolution() {
		this(null);
	}

	public BizSolution(String id){
		super(id);
	}

	@Length(min=0, max=100, message="安保警力部署编号长度不能超过 100 个字符")
	public String getSolutionCode() {
		return solutionCode;
	}

	public void setSolutionCode(String solutionCode) {
		this.solutionCode = solutionCode;
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

	public String getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(String startPosition) {
		this.startPosition = startPosition;
	}

	public String getEndPosition() {
		return endPosition;
	}

	public void setEndPosition(String endPosition) {
		this.endPosition = endPosition;
	}

	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
	}

	public String getCoreTask() {
		return coreTask;
	}

	public void setCoreTask(String coreTask) {
		this.coreTask = coreTask;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getStartCoordinates() {
		return startCoordinates;
	}
	public void setStartCoordinates(String startCoordinates) {
		this.startCoordinates = startCoordinates;
	}
	public String getEndCoordinates() {
		return endCoordinates;
	}
	public void setEndCoordinates(String endCoordinates) {
		this.endCoordinates = endCoordinates;
	}
	public String getPersons() {
		return persons;
	}
	public void setPersons(String persons) {
		this.persons = persons;
	}
	public String getChief() {
		return chief;
	}
	public void setChief(String chief) {
		this.chief = chief;
	}
	public String getDutyType() {
		return dutyType;
	}
	public void setDutyType(String dutyType) {
		this.dutyType = dutyType;
	}
	public FileUpload getIcon() {
		return icon;
	}
	public void setIcon(FileUpload icon) {
		this.icon = icon;
	}
	public String getLineColor() {
		return lineColor;
	}
	public void setLineColor(String lineColor) {
		this.lineColor = lineColor;
	}
}