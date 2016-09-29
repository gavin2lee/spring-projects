package cn.wonhigh.retail.fas.manager;

import cn.wonhigh.retail.fas.service.CompanyBrandSettlePeriodService;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 请写出类的用途 
 * @author user
 * @date  2015-10-13 12:04:21
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
@Service("companyBrandSettlePeriodManager")
class CompanyBrandSettlePeriodManagerImpl extends BaseCrudManagerImpl implements CompanyBrandSettlePeriodManager {
    @Resource
    private CompanyBrandSettlePeriodService companyBrandSettlePeriodService;

    @Override
    public BaseCrudService init() {
        return companyBrandSettlePeriodService;
    }
}