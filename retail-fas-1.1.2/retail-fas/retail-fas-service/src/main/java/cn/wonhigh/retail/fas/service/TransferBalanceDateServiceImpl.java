package cn.wonhigh.retail.fas.service;

import cn.wonhigh.retail.fas.dal.database.TransferBalanceDateMapper;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
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
@Service("transferBalanceDateService")
class TransferBalanceDateServiceImpl extends BaseCrudServiceImpl implements TransferBalanceDateService {
    @Resource
    private TransferBalanceDateMapper transferBalanceDateMapper;

    @Override
    public BaseCrudMapper init() {
        return transferBalanceDateMapper;
    }
}