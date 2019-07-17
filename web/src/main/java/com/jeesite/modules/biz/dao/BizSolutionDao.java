/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.biz.dao;

import java.util.List;
import java.util.Map;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.biz.entity.BizSolution;

/**
 * 场所表DAO接口
 * @author 长江
 * @version 2019-01-12
 */
@MyBatisDao("BizSolutionDao")
public interface BizSolutionDao extends CrudDao<BizSolution> {
	public List<Map<String, Object>> queryMap(Map<String,Object> param);
}