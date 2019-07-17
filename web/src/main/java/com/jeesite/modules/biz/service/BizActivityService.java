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
import com.jeesite.modules.biz.dao.BizActivityDao;
import com.jeesite.modules.biz.entity.BizActivity;
import com.jeesite.modules.file.utils.FileUploadUtils;

/**
 * 活动表Service
 * @author 长江
 * @version 2019-01-12
 */
@Service
@Transactional(readOnly=true)
public class BizActivityService extends CrudService<BizActivityDao, BizActivity> {
	/**
	 * 获取单条数据
	 * @param BizActivity
	 * @return
	 */
	@Override
	public BizActivity get(String bizActivity) {
		return super.get(bizActivity);
	}	
	public BizActivity getBizActivity(String bizActivity) {
		return super.get(bizActivity);
	}
	/**
	 * 获取单条数据
	 * @param bizActivity
	 * @return
	 */
	@Override
	public BizActivity get(BizActivity bizActivity) {
		return super.get(bizActivity);
	}
	/**
	 * 查询分页数据
	 * @param bizActivity 查询条件
	 * @param bizActivity.page 分页对象
	 * @return
	 */
	@Override
	public Page<BizActivity> findPage(BizActivity bizActivity) {
		return super.findPage(bizActivity);
	}
	/**
	 * 获取单条数据
	 * @param bizActivity
	 * @return
	 */
	public List<Map<String, Object>> queryMap(Map<String,Object> param) {
		return this.dao.queryMap(param);
	}
	/**
	 * 保存数据（插入或更新）
	 * @param bizActivity
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(BizActivity bizActivity) {
		super.save(bizActivity);
		// 保存上传图片
		FileUploadUtils.saveFileUpload(bizActivity.getId(), "bizActivity_image");
		// 保存上传附件
		FileUploadUtils.saveFileUpload(bizActivity.getId(), "bizActivity_file");
	}
	
	/**
	 * 更新状态
	 * @param bizActivity
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(BizActivity bizActivity) {
		super.updateStatus(bizActivity);
	}
	
	/**
	 * 删除数据
	 * @param bizActivity
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(BizActivity bizActivity) {
		super.delete(bizActivity);
	}

	/**
	 * 导入活动数据
	 * @param file 导入的活动数据文件
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
			List<BizActivity> list = ei.getDataList(BizActivity.class);
			for (BizActivity bizActivity : list) {
				try{
					// 验证数据文件
					ValidatorUtils.validateWithException(bizActivity);
					// 验证是否存在这个用户
					BizActivity b=this.get(bizActivity.getActivityCode());
					if(b==null) {
						bizActivity.setIsNewRecord(true);
						this.save(bizActivity);
						successNum++;
						successMsg.append("<br/>" + successNum + "、活动 " + bizActivity.getActivityCode() + " 导入成功");
					}else if (isUpdateSupport){
//						ei.getDataRowNum()
						this.save(b);
						successNum++;
						successMsg.append("<br/>" + successNum + "、活动 " + bizActivity.getActivityCode() + " 更新成功");
					} else {
						failureNum++;
						failureMsg.append("<br/>" + failureNum + "、活动 " + bizActivity.getActivityCode()+ " 已存在");
					}
				} catch (Exception e) {
					failureNum++;
					String msg = "<br/>" + failureNum + "、活动 " + bizActivity.getActivityCode()+ " 导入失败：";
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
	/**
	 * 添加数据权限过滤条件
	 */
	@Override
	public void addDataScopeFilter(BizActivity bizActivity, String ctrlPermi) {
		bizActivity.getSqlMap().getDataScope().addFilter("dsf", "BizActivity", "a.activity_code", ctrlPermi);
	}

}