/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.biz.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import com.jeesite.modules.biz.entity.BizSolution;
import com.jeesite.modules.biz.service.BizPoliceService;
import com.jeesite.modules.biz.service.BizSolutionService;

/**
 * 方案表Controller
 * @author 长江
 * @version 2019-01-12
 */
@Controller("BizSolutionController-v1")
@RequestMapping(value = "${adminPath}/biz/bizSolution")
public class BizSolutionController extends BaseController {
	@Autowired
	private BizSolutionService bizSolutionService; 
	@Autowired
	private BizPoliceService bizPoliceService;
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public BizSolution get(String solutionCode, boolean isNewRecord) {
		BizSolution bizSolution=bizSolutionService.get(solutionCode, isNewRecord);
		String personCodes=bizSolution.getPersonCodes();
		if(StringUtils.isNotBlank(personCodes)) {
			List<String> list =Arrays.asList(personCodes.split(","));
			ArrayList<String> codes=new ArrayList<String>();
			list.forEach(code->{
				codes.add("'"+code+"'");
			});
			List<Map<String, Object>> r=bizPoliceService.queryMap(new HashMap<String,Object>(){{put("andsql","and p.police_code in("+String.join(",",  codes.toArray(new String[codes.size()]))+") ");}});
			codes.clear();
		    r.forEach(m->{
		    	codes.add(String.valueOf(m.get("policeName")));
		    });
		    bizSolution.setPersonNames(String.join(",",codes.toArray(new String[codes.size()])));
		}
		return bizSolution;
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("biz:bizSolution:view")
	@RequestMapping(value = {"list", ""})
	public String list(BizSolution bizSolution, Model model) {
		model.addAttribute("bizSolution", bizSolution);
		return "modules/biz/bizSolutionList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("biz:bizSolution:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<BizSolution> listData(BizSolution bizSolution, HttpServletRequest request, HttpServletResponse response) {
		bizSolution.setPage(new Page<>(request, response));
		Page<BizSolution> page = bizSolutionService.findPage(bizSolution);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("biz:bizSolution:view")
	@RequestMapping(value = "form")
	public String form(BizSolution bizSolution, Model model) {
		model.addAttribute("bizSolution", bizSolution);
		return "modules/biz/bizSolutionForm";
	}

	/**
	 * 保存方案表
	 */
	@RequiresPermissions("biz:bizSolution:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated BizSolution bizSolution) {
		bizSolutionService.save(bizSolution);
		return renderResult(Global.TRUE, text("保存方案成功！"));
	}
	
	/**
	 * 停用方案表
	 */
	@RequiresPermissions("biz:bizSolution:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(BizSolution bizSolution) {
		bizSolution.setStatus(BizSolution.STATUS_DISABLE);
		bizSolutionService.updateStatus(bizSolution);
		return renderResult(Global.TRUE, text("停用方案成功"));
	}
	
	/**
	 * 启用方案表
	 */
	@RequiresPermissions("biz:bizSolution:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(BizSolution bizSolution) {
		bizSolution.setStatus(BizSolution.STATUS_NORMAL);
		bizSolutionService.updateStatus(bizSolution);
		return renderResult(Global.TRUE, text("启用方案成功"));
	}
	
	/**
	 * 删除方案表
	 */
	@RequiresPermissions("biz:bizSolution:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(BizSolution bizSolution) {
		bizSolutionService.delete(bizSolution);
		return renderResult(Global.TRUE, text("删除方案成功！"));
	}

	/**
	 * 下载导入方案数据模板
	 */
	@RequiresPermissions("biz:bizSolution:view")
	@RequestMapping(value = "importTemplate")
	public void importTemplate(HttpServletResponse response) {
		BizSolution bizSolution = new BizSolution();
		List<BizSolution> list = ListUtils.newArrayList(bizSolution);
		String fileName = "方案数据模板.xlsx";
		try(ExcelExport ee = new ExcelExport("银川市公安局金凤区分局人员相关信息字段表", BizSolution.class, Type.IMPORT)){
			ee.setDataList(list).write(response, fileName);
		}
	}
	/**
	 * 导入方案数据
	 */
	@ResponseBody
	@RequiresPermissions("biz:bizSolution:edit")
	@PostMapping(value = "importData")
	public String importData(MultipartFile file, String updateSupport) {
		try {
			boolean isUpdateSupport = Global.YES.equals(updateSupport);
			String message = bizSolutionService.importData(file, isUpdateSupport);
			return renderResult(Global.TRUE, "posfull:"+message);
		} catch (Exception ex) {
			return renderResult(Global.FALSE, "posfull:"+ex.getMessage());
		}
	}
	/**
	 * 导出方案数据
	 */
	@RequiresPermissions("biz:bizSolution:view")
	@RequestMapping(value = "exportData")
	public void exportData(BizSolution bizSolution, Boolean isAll, String ctrlPermi, HttpServletResponse response) {
//		bizSolution.getCity().setIsQueryChildren(true);
//		bizSolution.getArea().setIsQueryChildren(true);
//		if (!(isAll != null && isAll)){
//			bizSolutionService.addDataScopeFilter(bizSolution, ctrlPermi);
//		}
		List<BizSolution> list = bizSolutionService.findList(bizSolution);
		String fileName = "方案数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try(ExcelExport ee = new ExcelExport("银川市公安局金凤区分局人员相关信息字段表", BizSolution.class)){
			ee.setDataList(list).write(response, fileName);
		}
	}

}