package cn.wonhigh.retail.fas.dal.database;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.wonhigh.retail.fas.common.model.SettlementBodyChangeRecord;

import com.yougou.logistics.base.dal.database.BaseCrudMapper;

/**
 * 请写出类的用途 
 * @author user
 * @date  2015-12-28 11:09:32
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
public interface SettlementBodyChangeRecordMapper extends BaseCrudMapper {
	public SettlementBodyChangeRecord selectByPageSum(@Param("params") Map<String, Object> params);
}