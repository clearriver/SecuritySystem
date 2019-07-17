/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.biz.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.utils.excel.ExcelExport;
import com.jeesite.common.utils.excel.annotation.ExcelField.Type;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.biz.entity.BizActionPlan;
import com.jeesite.modules.biz.service.BizActionPlanService;
import com.jeesite.modules.biz.service.BizPoliceService;

/**
 * 方案表Controller
 * @author 长江
 * @version 2019-01-12
 */
@Controller("BizActionPlanController-v1")
@RequestMapping(value = "${adminPath}/biz/bizActionPlan")
public class BizActionPlanController extends BaseController {
	@Autowired
	private BizActionPlanService bizActionPlanService; 
	@Autowired
	private BizPoliceService bizPoliceService;
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public BizActionPlan get(String actionCode, boolean isNewRecord) {
		return bizActionPlanService.get(actionCode, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("biz:bizActionPlan:view")
	@RequestMapping(value = {"list", ""})
	public String list(BizActionPlan bizActionPlan, Model model) {
		model.addAttribute("bizActionPlan", bizActionPlan);
		return "modules/biz/bizActionPlanList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("biz:bizActionPlan:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<BizActionPlan> listData(BizActionPlan bizActionPlan, HttpServletRequest request, HttpServletResponse response) {
		bizActionPlan.setPage(new Page<>(request, response));
		Page<BizActionPlan> page = bizActionPlanService.findPage(bizActionPlan);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("biz:bizActionPlan:view")
	@RequestMapping(value = "form")
	public String form(BizActionPlan bizActionPlan, Model model) {
		model.addAttribute("bizActionPlan", bizActionPlan);
		return "modules/biz/bizActionPlanForm";
	}

	/**
	 * 保存方案表
	 */
	@RequiresPermissions("biz:bizActionPlan:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated BizActionPlan bizActionPlan) {
		bizActionPlanService.save(bizActionPlan);
		return renderResult(Global.TRUE, text("保存方案成功！"));
	}
	
	/**
	 * 停用方案表
	 */
	@RequiresPermissions("biz:bizActionPlan:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(BizActionPlan bizActionPlan) {
		bizActionPlan.setStatus(BizActionPlan.STATUS_DISABLE);
		bizActionPlanService.updateStatus(bizActionPlan);
		return renderResult(Global.TRUE, text("停用方案成功"));
	}
	
	/**
	 * 启用方案表
	 */
	@RequiresPermissions("biz:bizActionPlan:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(BizActionPlan bizActionPlan) {
		bizActionPlan.setStatus(BizActionPlan.STATUS_NORMAL);
		bizActionPlanService.updateStatus(bizActionPlan);
		return renderResult(Global.TRUE, text("启用方案成功"));
	}
	
	/**
	 * 删除方案表
	 */
	@RequiresPermissions("biz:bizActionPlan:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(BizActionPlan bizActionPlan) {
		bizActionPlanService.delete(bizActionPlan);
		return renderResult(Global.TRUE, text("删除方案成功！"));
	}

	/**
	 * 下载导入方案数据模板
	 */
	@RequiresPermissions("biz:bizActionPlan:view")
	@RequestMapping(value = "importTemplate")
	public void importTemplate(HttpServletResponse response) {
		BizActionPlan bizActionPlan = new BizActionPlan();
		List<BizActionPlan> list = ListUtils.newArrayList(bizActionPlan);
		String fileName = "方案数据模板.xlsx";
		try(ExcelExport ee = new ExcelExport("银川市公安局金凤区分局人员相关信息字段表", BizActionPlan.class, Type.IMPORT)){
			ee.setDataList(list).write(response, fileName);
		}
	}
	/**
	 * 导入方案数据
	 */
	@ResponseBody
	@RequiresPermissions("biz:bizActionPlan:edit")
	@PostMapping(value = "importData")
	public String importData(MultipartFile file, String updateSupport) {
		try {
			boolean isUpdateSupport = Global.YES.equals(updateSupport);
			String message = bizActionPlanService.importData(file, isUpdateSupport);
			return renderResult(Global.TRUE, "posfull:"+message);
		} catch (Exception ex) {
			return renderResult(Global.FALSE, "posfull:"+ex.getMessage());
		}
	}
	/**
	 * 导出方案数据
	 */
	@RequiresPermissions("biz:bizActionPlan:view")
	@RequestMapping(value = "exportData")
	public void exportData(BizActionPlan bizActionPlan, Boolean isAll, String ctrlPermi, HttpServletResponse response) {
//		bizActionPlan.getCity().setIsQueryChildren(true);
//		bizActionPlan.getArea().setIsQueryChildren(true);
//		if (!(isAll != null && isAll)){
//			bizActionPlanService.addDataScopeFilter(bizActionPlan, ctrlPermi);
//		}
		List<BizActionPlan> list = bizActionPlanService.findList(bizActionPlan);
		String fileName = "方案数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try(ExcelExport ee = new ExcelExport("银川市公安局金凤区分局人员相关信息字段表", BizActionPlan.class)){
			ee.setDataList(list).write(response, fileName);
		}
	}

}