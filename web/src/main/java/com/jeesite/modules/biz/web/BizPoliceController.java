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
import com.jeesite.modules.biz.entity.BizPolice;
import com.jeesite.modules.biz.service.BizPoliceService;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.service.ConfigService;

/**
 * 警员表Controller
 * @author 长江
 * @version 2019-01-12
 */
@Controller("BizPoliceController-v1")
@RequestMapping(value = "${adminPath}/biz/bizPolice")
public class BizPoliceController extends BaseController {

	@Autowired
	private BizPoliceService bizPoliceService;
	@Autowired
	private ConfigService configService;
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public BizPolice get(String policeCode, boolean isNewRecord) {
		return bizPoliceService.get(policeCode, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("biz:bizPolice:view")
	@RequestMapping(value = {"list", ""})
	public String list(BizPolice bizPolice, Model model) {
		model.addAttribute("bizPolice", bizPolice);
		return "modules/biz/bizPoliceList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("biz:bizPolice:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<BizPolice> listData(BizPolice bizPolice, HttpServletRequest request, HttpServletResponse response) {
		bizPolice.setPage(new Page<>(request, response));
		Page<BizPolice> page = bizPoliceService.findPage(bizPolice);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("biz:bizPolice:view")
	@RequestMapping(value = "form")
	public String form(BizPolice bizPolice, Model model) {
		model.addAttribute("bizPolice", bizPolice);
//		Config config=ConfigUtils.getConfig("sys.baidu.ak");
//		model.addAttribute("baidu_ak",config==null?"":config.getConfigValue());
		return "modules/biz/bizPoliceForm";
	}

	/**
	 * 保存警员表
	 */
	@RequiresPermissions("biz:bizPolice:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated BizPolice bizPolice) {
		bizPoliceService.save(bizPolice);
		return renderResult(Global.TRUE, text("保存警员成功！"));
	}
	
	/**
	 * 停用警员表
	 */
	@RequiresPermissions("biz:bizPolice:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(BizPolice bizPolice) {
		bizPolice.setStatus(BizPolice.STATUS_DISABLE);
		bizPoliceService.updateStatus(bizPolice);
		return renderResult(Global.TRUE, text("停用警员成功"));
	}
	
	/**
	 * 启用警员表
	 */
	@RequiresPermissions("biz:bizPolice:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(BizPolice bizPolice) {
		bizPolice.setStatus(BizPolice.STATUS_NORMAL);
		bizPoliceService.updateStatus(bizPolice);
		return renderResult(Global.TRUE, text("启用警员成功"));
	}
	
	/**
	 * 删除警员表
	 */
	@RequiresPermissions("biz:bizPolice:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(BizPolice bizPolice) {
		bizPoliceService.delete(bizPolice);
		return renderResult(Global.TRUE, text("删除警员成功！"));
	}

	/**
	 * 下载导入警员数据模板
	 */
	@RequiresPermissions("biz:bizPolice:view")
	@RequestMapping(value = "importTemplate")
	public void importTemplate(HttpServletResponse response) {
		BizPolice bizPolice = new BizPolice();
		List<BizPolice> list = ListUtils.newArrayList(bizPolice);
		String fileName = "警员数据模板.xlsx";
		try(ExcelExport ee = new ExcelExport("银川市公安局金凤区分局人员相关信息字段表", BizPolice.class, Type.IMPORT)){
			ee.setDataList(list).write(response, fileName);
		}
	}
	/**
	 * 导入警员数据
	 */
	@ResponseBody
	@RequiresPermissions("biz:bizPolice:edit")
	@PostMapping(value = "importData")
	public String importData(MultipartFile file, String updateSupport) {
		try {
			boolean isUpdateSupport = Global.YES.equals(updateSupport);
			String message = bizPoliceService.importData(file, isUpdateSupport);
			return renderResult(Global.TRUE, "posfull:"+message);
		} catch (Exception ex) {
			return renderResult(Global.FALSE, "posfull:"+ex.getMessage());
		}
	}
	/**
	 * 导出警员数据
	 */
	@RequiresPermissions("biz:bizPolice:view")
	@RequestMapping(value = "exportData")
	public void exportData(BizPolice bizPolice, Boolean isAll, String ctrlPermi, HttpServletResponse response) {
//		bizPolice.getCity().setIsQueryChildren(true);
//		bizPolice.getArea().setIsQueryChildren(true);
//		if (!(isAll != null && isAll)){
//			bizPoliceService.addDataScopeFilter(bizPolice, ctrlPermi);
//		}
		List<BizPolice> list = bizPoliceService.findList(bizPolice);
		String fileName = "警员数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try(ExcelExport ee = new ExcelExport("银川市公安局金凤区分局人员相关信息字段表", BizPolice.class)){
			ee.setDataList(list).write(response, fileName);
		}
	}
	@RequiresPermissions("user")
	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData( Boolean isAll,String policeCode, String isShowCode, String ctrlPermi) {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		BizPolice where = new BizPolice();
		where.setStatus(Office.STATUS_NORMAL);
		where.setPoliceCode(policeCode);
		if (!(isAll != null && isAll)){
			bizPoliceService.addDataScopeFilter(where, ctrlPermi);
		}
		List<BizPolice> list = bizPoliceService.findList(where);
		for (int i = 0; i < list.size(); i++) {
			BizPolice e = list.get(i);
			Map<String, Object> map = MapUtils.newHashMap();
			map.put("id", e.getPoliceCode());
			map.put("pId", "0");
			map.put("name",e.getPoliceName());
			mapList.add(map);
		}
		return mapList;
	}
}