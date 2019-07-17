/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.biz.web;

import java.util.List;
import java.util.Map;

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
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.utils.excel.ExcelExport;
import com.jeesite.common.utils.excel.annotation.ExcelField.Type;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.biz.entity.BizActivity;
import com.jeesite.modules.biz.service.BizActivityService;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.service.ConfigService;

/**
 * 活动表Controller
 * @author 长江
 * @version 2019-01-12
 */
@Controller("BizActivityController-v1")
@RequestMapping(value = "${adminPath}/biz/bizActivity")
public class BizActivityController extends BaseController {

	@Autowired
	private BizActivityService bizActivityService;
	@Autowired
	private ConfigService configService;
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public BizActivity get(String activityCode, boolean isNewRecord) {
		return bizActivityService.get(activityCode, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("biz:bizActivity:view")
	@RequestMapping(value = {"list", ""})
	public String list(BizActivity bizActivity, Model model) {
		model.addAttribute("bizActivity", bizActivity);
		return "modules/biz/bizActivityList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("biz:bizActivity:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<BizActivity> listData(BizActivity bizActivity, HttpServletRequest request, HttpServletResponse response) {
		bizActivity.setPage(new Page<>(request, response));
		Page<BizActivity> page = bizActivityService.findPage(bizActivity);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("biz:bizActivity:view")
	@RequestMapping(value = "form")
	public String form(BizActivity bizActivity, Model model) {
		model.addAttribute("bizActivity", bizActivity);
		return "modules/biz/bizActivityForm";
	}

	/**
	 * 保存活动表
	 */
	@RequiresPermissions("biz:bizActivity:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated BizActivity bizActivity) {
		bizActivityService.save(bizActivity);
		return renderResult(Global.TRUE, text("保存活动成功！"));
	}
	
	/**
	 * 停用活动表
	 */
	@RequiresPermissions("biz:bizActivity:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(BizActivity bizActivity) {
		bizActivity.setStatus(BizActivity.STATUS_DISABLE);
		bizActivityService.updateStatus(bizActivity);
		return renderResult(Global.TRUE, text("停用活动成功"));
	}
	
	/**
	 * 启用活动表
	 */
	@RequiresPermissions("biz:bizActivity:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(BizActivity bizActivity) {
		bizActivity.setStatus(BizActivity.STATUS_NORMAL);
		bizActivityService.updateStatus(bizActivity);
		return renderResult(Global.TRUE, text("启用活动成功"));
	}
	
	/**
	 * 删除活动表
	 */
	@RequiresPermissions("biz:bizActivity:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(BizActivity bizActivity) {
		bizActivityService.delete(bizActivity);
		return renderResult(Global.TRUE, text("删除活动成功！"));
	}

	/**
	 * 下载导入活动数据模板
	 */
	@RequiresPermissions("biz:bizActivity:view")
	@RequestMapping(value = "importTemplate")
	public void importTemplate(HttpServletResponse response) {
		BizActivity bizActivity = new BizActivity();
		List<BizActivity> list = ListUtils.newArrayList(bizActivity);
		String fileName = "活动数据模板.xlsx";
		try(ExcelExport ee = new ExcelExport("银川市公安局金凤区分局人员相关信息字段表", BizActivity.class, Type.IMPORT)){
			ee.setDataList(list).write(response, fileName);
		}
	}
	/**
	 * 导入活动数据
	 */
	@ResponseBody
	@RequiresPermissions("biz:bizActivity:edit")
	@PostMapping(value = "importData")
	public String importData(MultipartFile file, String updateSupport) {
		try {
			boolean isUpdateSupport = Global.YES.equals(updateSupport);
			String message = bizActivityService.importData(file, isUpdateSupport);
			return renderResult(Global.TRUE, "posfull:"+message);
		} catch (Exception ex) {
			return renderResult(Global.FALSE, "posfull:"+ex.getMessage());
		}
	}
	/**
	 * 导出活动数据
	 */
	@RequiresPermissions("biz:bizActivity:view")
	@RequestMapping(value = "exportData")
	public void exportData(BizActivity bizActivity, Boolean isAll, String ctrlPermi, HttpServletResponse response) {
//		bizActivity.getCity().setIsQueryChildren(true);
//		bizActivity.getArea().setIsQueryChildren(true);
//		if (!(isAll != null && isAll)){
//			bizActivityService.addDataScopeFilter(bizActivity, ctrlPermi);
//		}
		List<BizActivity> list = bizActivityService.findList(bizActivity);
		String fileName = "活动数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try(ExcelExport ee = new ExcelExport("银川市公安局金凤区分局人员相关信息字段表", BizActivity.class)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	@RequiresPermissions("user")
	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData( Boolean isAll,String companyCode, String isShowCode, String ctrlPermi) {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		BizActivity where = new BizActivity();
		where.setStatus(Office.STATUS_NORMAL);
		where.setActivityCode(companyCode);
		if (!(isAll != null && isAll)){
			bizActivityService.addDataScopeFilter(where, ctrlPermi);
		}
		List<BizActivity> list = bizActivityService.findList(where);
		for (int i = 0; i < list.size(); i++) {
			BizActivity e = list.get(i);
			Map<String, Object> map = MapUtils.newHashMap();
			map.put("id", e.getActivityCode());
			map.put("pId", "0");
			map.put("name",e.getActivityName());
			mapList.add(map);
		}
		return mapList;
	}
}