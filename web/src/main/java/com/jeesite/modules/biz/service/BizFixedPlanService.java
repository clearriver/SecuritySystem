/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.biz.service;

import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.common.service.ServiceException;
import com.jeesite.common.utils.excel.ExcelImport;
import com.jeesite.common.validator.ValidatorUtils;
import com.jeesite.modules.biz.dao.BizFixedPlanDao;
import com.jeesite.modules.biz.entity.BizFixedPlan;
import com.jeesite.modules.file.utils.FileUploadUtils;

/**
 * 固定部署表Service
 * @author 长江
 * @version 2019-01-12
 */
@Service
@Transactional(readOnly=true)
public class BizFixedPlanService extends CrudService<BizFixedPlanDao, BizFixedPlan> {
	/**
	 * 获取单条数据
	 * @param BizFixedPlan
	 * @return
	 */
	@Override
	public BizFixedPlan get(String bizFixedPlan) {
		return super.get(bizFixedPlan);
	}	
	public BizFixedPlan getBizFixedPlan(String bizFixedPlan) {
		return super.get(bizFixedPlan);
	}
	/**
	 * 获取单条数据
	 * @param bizFixedPlan
	 * @return
	 */
	@Override
	public BizFixedPlan get(BizFixedPlan bizFixedPlan) {
		return super.get(bizFixedPlan);
	}
	/**
	 * 查询分页数据
	 * @param bizFixedPlan 查询条件
	 * @param bizFixedPlan.page 分页对象
	 * @return
	 */
	@Override
	public Page<BizFixedPlan> findPage(BizFixedPlan bizFixedPlan) {
		return super.findPage(bizFixedPlan);
	}
	/**
	 * 获取单条数据
	 * @param bizFixedPlan
	 * @return
	 */
	public List<Map<String, Object>> queryMap(Map<String,Object> param) {
		return this.dao.queryMap(param);
	}
	/**
	 * 保存数据（插入或更新）
	 * @param bizFixedPlan
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(BizFixedPlan bizFixedPlan) {
		super.save(bizFixedPlan);
		// 保存上传图片
		FileUploadUtils.saveFileUpload(bizFixedPlan.getId(), "bizFixedPlan_image");
		// 保存上传附件
		FileUploadUtils.saveFileUpload(bizFixedPlan.getId(), "bizFixedPlan_file");
	}
	
	/**
	 * 更新状态
	 * @param bizFixedPlan
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(BizFixedPlan bizFixedPlan) {
		super.updateStatus(bizFixedPlan);
	}
	
	/**
	 * 删除数据
	 * @param bizFixedPlan
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(BizFixedPlan bizFixedPlan) {
		super.delete(bizFixedPlan);
	}

	/**
	 * 导入固定部署数据
	 * @param file 导入的固定部署数据文件
	 * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
	 */
	@Transactional(readOnly=false)
	public String importData(MultipartFile file, Boolean isUpdateSupport) {
		if (file == null){
			throw new ServiceException("请选择导入的数据文件！");
		}
		int successNum = 0; int failureNum = 0;
		StringBuilder successMsg = new StringBuilder();
		StringBuilder failureMsg = new StringBuilder();
		try(ExcelImport ei = new ExcelImport(file, 2, 0)){
			List<BizFixedPlan> list = ei.getDataList(BizFixedPlan.class);
			for (BizFixedPlan bizFixedPlan : list) {
				try{
					// 验证数据文件
					ValidatorUtils.validateWithException(bizFixedPlan);
					// 验证是否存在这个用户
					BizFixedPlan b=this.get(bizFixedPlan.getPlanCode());
					if(b==null) {
						bizFixedPlan.setIsNewRecord(true);
						this.save(bizFixedPlan);
						successNum++;
						successMsg.append("<br/>" + successNum + "、固定部署" + bizFixedPlan.getPlanCode() + " 导入成功");
					}else if (isUpdateSupport){
//						ei.getDataRowNum()
						this.save(b);
						successNum++;
						successMsg.append("<br/>" + successNum + "、固定部署 " + bizFixedPlan.getPlanCode() + " 更新成功");
					} else {
						failureNum++;
						failureMsg.append("<br/>" + failureNum + "、固定部署 " + bizFixedPlan.getPlanCode()+ " 已存在");
					}
				} catch (Exception e) {
					failureNum++;
					String msg = "<br/>" + failureNum + "、固定部署 " + bizFixedPlan.getPlanCode()+ " 导入失败：";
					if (e instanceof ConstraintViolationException){
						List<String> messageList = ValidatorUtils.extractPropertyAndMessageAsList((ConstraintViolationException)e, ": ");
						for (String message : messageList) {
							msg += message + "; ";
						}
					}else{
						msg += e.getMessage();
					}
					failureMsg.append(msg);
					logger.error(msg, e);
				}
			}
		} catch (Exception e) {
			failureMsg.append(e.getMessage());
			logger.error(e.getMessage(), e);
		}
		if (failureNum > 0) {
			failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
			throw new ServiceException(failureMsg.toString());
		}else{
			successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
		}
		return successMsg.toString();
	}

}