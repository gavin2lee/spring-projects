package cn.wonhigh.retail.fas.manager;

import cn.wonhigh.retail.fas.service.ShopGroupDtlService;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 请写出类的用途 
 * @author wangm
 * @date  2015-01-22 11:41:25
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
@Service("shopGroupDtlManager")
class ShopGroupDtlManagerImpl extends BaseCrudManagerImpl implements ShopGroupDtlManager {
    @Resource
    private ShopGroupDtlService shopGroupDtlService;

    @Override
    public BaseCrudService init() {
        return shopGroupDtlService;
    }
}