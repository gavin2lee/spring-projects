package cn.wonhigh.retail.fas.manager;

import cn.wonhigh.retail.fas.service.TransferBalanceDateService;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 请写出类的用途 
 * @author user
 * @date  2016-07-05 14:55:50
 * @version 1.0.8
 * @copyright (C) 2013 WonHigh Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the WonHigh technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
@Service("transferBalanceDateManager")
class TransferBalanceDateManagerImpl extends BaseCrudManagerImpl implements TransferBalanceDateManager {
    @Resource
    private TransferBalanceDateService transferBalanceDateService;

    @Override
    public BaseCrudService init() {
        return transferBalanceDateService;
    }
}