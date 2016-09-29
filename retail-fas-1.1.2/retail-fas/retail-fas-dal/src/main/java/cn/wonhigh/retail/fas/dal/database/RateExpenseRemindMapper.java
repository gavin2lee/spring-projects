package cn.wonhigh.retail.fas.dal.database;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.wonhigh.retail.fas.common.model.ShopBalanceDate;

import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;

/**
 * 请写出类的用途 
 * @author wangxy
 * @date  2015-06-25 10:44:22
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
public interface RateExpenseRemindMapper extends BaseCrudMapper {
	
	int selectTwoMonthsInvalidCount(@Param("params") Map<String, Object> params);

	List<ShopBalanceDate> selectTwoMonthsInvalidByPage(@Param("page") SimplePage page,@Param("orderByField") String orderByField,
			@Param("orderBy") String orderBy,@Param("params") Map<String, Object> params);
	
}