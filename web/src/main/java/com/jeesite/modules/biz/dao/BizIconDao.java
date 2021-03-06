/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.biz.dao;

import java.util.List;
import java.util.Map;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.biz.entity.BizIcon;

/**
 * 场所表DAO接口
 * @author 长江
 * @version 2019-01-12
 */
@MyBatisDao("BizIconDao")
public interface BizIconDao extends CrudDao<BizIcon> {
	public List<Map<String, Object>> queryMap(Map<String,Object> param);
}