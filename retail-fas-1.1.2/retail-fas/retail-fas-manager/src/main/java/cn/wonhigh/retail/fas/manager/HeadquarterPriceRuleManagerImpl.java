package cn.wonhigh.retail.fas.manager;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.wonhigh.retail.fas.service.HeadquarterPriceRuleService;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;

/**
 * 请写出类的用途 
 * @author wang.xy1
 * @date  2014-09-01 09:25:14
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
@Service("headquarterPriceRuleManager")
class HeadquarterPriceRuleManagerImpl extends BaseCrudManagerImpl implements HeadquarterPriceRuleManager {
    @Resource
    private HeadquarterPriceRuleService headquarterPriceRuleService;

    @Override
    public BaseCrudService init() {
        return headquarterPriceRuleService;
    }

	@Override
	public int checkIsRuleRefered(Map<String, Object> params)
			throws ManagerException {
		try {
			return headquarterPriceRuleService.checkIsRuleRefered(params);
		} catch (ServiceException e) {
			throw new ManagerException();
		}
	}
}