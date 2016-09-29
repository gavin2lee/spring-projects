package cn.wonhigh.retail.fas.dal.database;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.wonhigh.retail.fas.common.model.Company;

import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;

/**
 * 请写出类的用途 
 * @author wangxy
 * @date  2015-12-31 16:10:13
 * @version 1.0.0
 * @copyright (C) 2013 WonHigh Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the WonHigh technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
public interface BillInvCostAdjustMapper extends BaseCrudMapper {
	
	int findCompanyCount(@Param("params")Map<String, Object> params) throws Exception;

	List<Company> findCompanyByPage(@Param("page")SimplePage page, @Param("orderByField")String orderByField, 
			@Param("orderBy")String orderBy, @Param("params")Map<String, Object> params) throws Exception;
	
	BigDecimal sumDiverAmount(@Param("params")Map<String, Object> params) throws Exception;

}