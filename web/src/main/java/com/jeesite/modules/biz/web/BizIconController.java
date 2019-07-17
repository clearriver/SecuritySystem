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
import com.jeesite.modules.biz.entity.BizIcon;
import com.jeesite.modules.biz.service.BizIconService;
import com.jeesite.modules.file.entity.FileUpload;
import com.jeesite.modules.file.service.FileUploadService;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.service.ConfigService;

/**
 * 图标表Controller
 * @author 长江
 * @version 2019-01-12
 */
@Controller("BizIconController-v1")
@RequestMapping(value = "${adminPath}/biz/bizIcon")
public class BizIconController extends BaseController {

	@Autowired
	private BizIconService bizIconService;
	@Autowired
	private ConfigService configService;
	@Autowired
	private FileUploadService fileUploadService;
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public BizIcon get(String iconCode, boolean isNewRecord) {
		return bizIconService.get(iconCode, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("biz:bizIcon:view")
	@RequestMapping(value = {"list", ""})
	public String list(BizIcon bizIcon, Model model) {
		model.addAttribute("bizIcon", bizIcon);
		return "modules/biz/bizIconList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("biz:bizIcon:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<BizIcon> listData(BizIcon bizIcon, HttpServletRequest request, HttpServletResponse response) {
		bizIcon.setPage(new Page<>(request, response));
		Page<BizIcon> page = bizIconService.findPage(bizIcon);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("biz:bizIcon:view")
	@RequestMapping(value = "form")
	public String form(BizIcon bizIcon, Model model) {
		model.addAttribute("bizIcon", bizIcon);
		return "modules/biz/bizIconForm";
	}

	/**
	 * 保存图标表
	 */
	@RequiresPermissions("biz:bizIcon:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated BizIcon bizIcon) {
		bizIconService.save(bizIcon);
		return renderResult(Global.TRUE, text("保存图标成功！"));
	}
	
	/**
	 * 停用图标表
	 */
	@RequiresPermissions("biz:bizIcon:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(BizIcon bizIcon) {
		bizIcon.setStatus(BizIcon.STATUS_DISABLE);
		bizIconService.updateStatus(bizIcon);
		return renderResult(Global.TRUE, text("停用图标成功"));
	}
	
	/**
	 * 启用图标表
	 */
	@RequiresPermissions("biz:bizIcon:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(BizIcon bizIcon) {
		bizIcon.setStatus(BizIcon.STATUS_NORMAL);
		bizIconService.updateStatus(bizIcon);
		return renderResult(Global.TRUE, text("启用图标成功"));
	}
	
	/**
	 * 删除图标表
	 */
	@RequiresPermissions("biz:bizIcon:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(BizIcon bizIcon) {
		bizIconService.delete(bizIcon);
		return renderResult(Global.TRUE, text("删除图标成功！"));
	}

	/**
	 * 下载导入图标数据模板
	 */
	@RequiresPermissions("biz:bizIcon:view")
	@RequestMapping(value = "importTemplate")
	public void importTemplate(HttpServletResponse response) {
		BizIcon bizIcon = new BizIcon();
		List<BizIcon> list = ListUtils.newArrayList(bizIcon);
		String fileName = "图标数据模板.xlsx";
		try(ExcelExport ee = new ExcelExport("银川市公安局金凤区分局人员相关信息字段表", BizIcon.class, Type.IMPORT)){
			ee.setDataList(list).write(response, fileName);
		}
	}
	/**
	 * 导入图标数据
	 */
	@ResponseBody
	@RequiresPermissions("biz:bizIcon:edit")
	@PostMapping(value = "importData")
	public String importData(MultipartFile file, String updateSupport) {
		try {
			boolean isUpdateSupport = Global.YES.equals(updateSupport);
			String message = bizIconService.importData(file, isUpdateSupport);
			return renderResult(Global.TRUE, "posfull:"+message);
		} catch (Exception ex) {
			return renderResult(Global.FALSE, "posfull:"+ex.getMessage());
		}
	}
	/**
	 * 导出图标数据
	 */
	@RequiresPermissions("biz:bizIcon:view")
	@RequestMapping(value = "exportData")
	public void exportData(BizIcon bizIcon, Boolean isAll, String ctrlPermi, HttpServletResponse response) {
//		bizIcon.getCity().setIsQueryChildren(true);
//		bizIcon.getArea().setIsQueryChildren(true);
//		if (!(isAll != null && isAll)){
//			bizIconService.addDataScopeFilter(bizIcon, ctrlPermi);
//		}
		List<BizIcon> list = bizIconService.findList(bizIcon);
		String fileName = "图标数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try(ExcelExport ee = new ExcelExport("银川市公安局金凤区分局人员相关信息字段表", BizIcon.class)){
			ee.setDataList(list).write(response, fileName);
		}
	}

	@RequestMapping(value = "treeData")
	@ResponseBody
	public List<Map<String, Object>> treeData( Boolean isAll,String bizType, String isShowCode, String ctrlPermi) {
		List<Map<String, Object>> mapList = ListUtils.newArrayList();
		FileUpload where = new FileUpload();
		where.setStatus(Office.STATUS_NORMAL);
		where.setBizType(bizType); 
		List<FileUpload> list = fileUploadService.findList(where);
		for (int i = 0; i < list.size(); i++) {
			FileUpload e = list.get(i);
			Map<String, Object> map = MapUtils.newHashMap();
			map.put("id", e.getId());
			map.put("pId", "0");
			map.put("name",e.getFileName());
			mapList.add(map);
		}
		return mapList;
	}
}