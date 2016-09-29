package cn.wonhigh.retail.fas.manager;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.wonhigh.retail.fas.service.BillSplitDtlService;

import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;

/**
 * 请写出类的用途 
 * @author yang.y
 * @date  2014-08-29 13:20:36
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
@Service("billSplitDtlManager")
class BillSplitDtlManagerImpl extends BaseCrudManagerImpl implements BillSplitDtlManager {
    @Resource
    private BillSplitDtlService billSplitDtlService;

    @Override
    public BaseCrudService init() {
        return billSplitDtlService;
    }
}