package cn.wonhigh.retail.fas.api.dal;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 请写出类的用途 
 * @author yang.y
 * @date  2014-08-26 15:42:01
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
public interface SettleNewStyleDtlApiMapper {
	
	int selectCount(@Param("params")Map<String, Object> params);
}