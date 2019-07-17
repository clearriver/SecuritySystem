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
import com.jeesite.modules.biz.entity.BizFixedPlan;
import com.jeesite.modules.biz.service.BizFixedPlanService;
import com.jeesite.modules.sys.service.ConfigService;

/**
 * 固定方案表Controller
 * @author 长江
 * @version 2019-01-12
 */
@Controller("BizFixedPlanController-v1")
@RequestMapping(value = "${adminPath}/biz/bizFixedPlan")
public class BizFixedPlanController extends BaseController {

	@Autowired
	private BizFixedPlanService bizFixedPlanService;
	@Autowired
	private ConfigService configService;
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public BizFixedPlan get(String planCode, boolean isNewRecord) {
		return bizFixedPlanService.get(planCode, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("biz:bizFixedPlan:view")
	@RequestMapping(value = {"list", ""})
	public String list(BizFixedPlan bizFixedPlan, Model model) {
		model.addAttribute("bizFixedPlan", bizFixedPlan);
		return "modules/biz/bizFixedPlanList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("biz:bizFixedPlan:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<BizFixedPlan> listData(BizFixedPlan bizFixedPlan, HttpServletRequest request, HttpServletResponse response) {
		bizFixedPlan.setPage(new Page<>(request, response));
		Page<BizFixedPlan> page = bizFixedPlanService.findPage(bizFixedPlan);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("biz:bizFixedPlan:view")
	@RequestMapping(value = "form")
	public String form(BizFixedPlan bizFixedPlan, Model model) {
		model.addAttribute("bizFixedPlan", bizFixedPlan);
		return "modules/biz/bizFixedPlanForm";
	}

	/**
	 * 保存固定方案表
	 */
	@RequiresPermissions("biz:bizFixedPlan:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated BizFixedPlan bizFixedPlan) {
		bizFixedPlanService.save(bizFixedPlan);
		return renderResult(Global.TRUE, text("保存固定方案成功！"));
	}
	
	/**
	 * 停用固定方案表
	 */
	@RequiresPermissions("biz:bizFixedPlan:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(BizFixedPlan bizFixedPlan) {
		bizFixedPlan.setStatus(BizFixedPlan.STATUS_DISABLE);
		bizFixedPlanService.updateStatus(bizFixedPlan);
		return renderResult(Global.TRUE, text("停用固定方案成功"));
	}
	
	/**
	 * 启用固定方案表
	 */
	@RequiresPermissions("biz:bizFixedPlan:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(BizFixedPlan bizFixedPlan) {
		bizFixedPlan.setStatus(BizFixedPlan.STATUS_NORMAL);
		bizFixedPlanService.updateStatus(bizFixedPlan);
		return renderResult(Global.TRUE, text("启用固定方案成功"));
	}
	
	/**
	 * 删除固定方案表
	 */
	@RequiresPermissions("biz:bizFixedPlan:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(BizFixedPlan bizFixedPlan) {
		bizFixedPlanService.delete(bizFixedPlan);
		return renderResult(Global.TRUE, text("删除固定方案成功！"));
	}

	/**
	 * 下载导入固定方案数据模板
	 */
	@RequiresPermissions("biz:bizFixedPlan:view")
	@RequestMapping(value = "importTemplate")
	public void importTemplate(HttpServletResponse response) {
		BizFixedPlan bizFixedPlan = new BizFixedPlan();
		List<BizFixedPlan> list = ListUtils.newArrayList(bizFixedPlan);
		String fileName = "固定方案数据模板.xlsx";
		try(ExcelExport ee = new ExcelExport("银川市公安局金凤区分局人员相关信息字段表", BizFixedPlan.class, Type.IMPORT)){
			ee.setDataList(list).write(response, fileName);
		}
	}
	/**
	 * 导入固定方案数据
	 */
	@ResponseBody
	@RequiresPermissions("biz:bizFixedPlan:edit")
	@PostMapping(value = "importData")
	public String importData(MultipartFile file, String updateSupport) {
		try {
			boolean isUpdateSupport = Global.YES.equals(updateSupport);
			String message = bizFixedPlanService.importData(file, isUpdateSupport);
			return renderResult(Global.TRUE, "posfull:"+message);
		} catch (Exception ex) {
			return renderResult(Global.FALSE, "posfull:"+ex.getMessage());
		}
	}
	/**
	 * 导出固定方案数据
	 */
	@RequiresPermissions("biz:bizFixedPlan:view")
	@RequestMapping(value = "exportData")
	public void exportData(BizFixedPlan bizFixedPlan, Boolean isAll, String ctrlPermi, HttpServletResponse response) {
//		bizFixedPlan.getCity().setIsQueryChildren(true);
//		bizFixedPlan.getArea().setIsQueryChildren(true);
//		if (!(isAll != null && isAll)){
//			bizFixedPlanService.addDataScopeFilter(bizFixedPlan, ctrlPermi);
//		}
		List<BizFixedPlan> list = bizFixedPlanService.findList(bizFixedPlan);
		String fileName = "固定方案数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try(ExcelExport ee = new ExcelExport("银川市公安局金凤区分局人员相关信息字段表", BizFixedPlan.class)){
			ee.setDataList(list).write(response, fileName);
		}
	}

}