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
import com.jeesite.modules.biz.dao.BizSolutionDao;
import com.jeesite.modules.biz.entity.BizSolution;
import com.jeesite.modules.file.utils.FileUploadUtils;

/**
 * 方案表Service
 * @author 长江
 * @version 2019-01-12
 */
@Service("BizSolutionService")
@Transactional(readOnly=true)
public class BizSolutionService extends CrudService<BizSolutionDao, BizSolution>{
	
	/**
	 * 获取单条数据
	 * @param BizSolution
	 * @return
	 */
	@Override
	public BizSolution get(String bizSolution) {
		return super.get(bizSolution);
	}	
	public BizSolution getBizSolution(String bizSolution) {
		return super.get(bizSolution);
	}
	/**
	 * 获取单条数据
	 * @param bizSolution
	 * @return
	 */
	@Override
	public BizSolution get(BizSolution bizSolution) {
		return super.get(bizSolution);
	}
	/**
	 * 查询分页数据
	 * @param bizSolution 查询条件
	 * @param bizSolution.page 分页对象
	 * @return
	 */
	@Override
	public Page<BizSolution> findPage(BizSolution bizSolution) {
		return super.findPage(bizSolution);
	}
	public List<Map<String, Object>> queryMap(Map<String,Object> param) {
		return this.dao.queryMap(param);
	}
	/**
	 * 保存数据（插入或更新）
	 * @param bizSolution
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(BizSolution bizSolution) {
		super.save(bizSolution);
		// 保存上传图片
		FileUploadUtils.saveFileUpload(bizSolution.getId(), "bizSolution_image");
		// 保存上传附件
		FileUploadUtils.saveFileUpload(bizSolution.getId(), "bizSolution_file");
	}
	
	/**
	 * 更新状态
	 * @param bizSolution
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(BizSolution bizSolution) {
		super.updateStatus(bizSolution);
	}
	
	/**
	 * 删除数据
	 * @param bizSolution
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(BizSolution bizSolution) {
		super.delete(bizSolution);
	}

	/**
	 * 导入方案数据
	 * @param file 导入的方案数据文件
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
			List<BizSolution> list = ei.getDataList(BizSolution.class);
			for (BizSolution bizSolution : list) {
				try{
					// 验证数据文件
					ValidatorUtils.validateWithException(bizSolution);
					// 验证是否存在这个用户
					BizSolution b=this.get(bizSolution.getSolutionCode());
					if(b==null) {
						bizSolution.setIsNewRecord(true);
						this.save(bizSolution);
						successNum++;
						successMsg.append("<br/>" + successNum + "、方案 " + bizSolution.getSolutionCode() + " 导入成功");
					}else if (isUpdateSupport){
//						ei.getDataRowNum()
						this.save(b);
						successNum++;
						successMsg.append("<br/>" + successNum + "、方案 " + bizSolution.getSolutionCode() + " 更新成功");
					} else {
						failureNum++;
						failureMsg.append("<br/>" + failureNum + "、方案 " + bizSolution.getSolutionCode()+ " 已存在");
					}
				} catch (Exception e) {
					failureNum++;
					String msg = "<br/>" + failureNum + "、方案 " + bizSolution.getSolutionCode()+ " 导入失败：";
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