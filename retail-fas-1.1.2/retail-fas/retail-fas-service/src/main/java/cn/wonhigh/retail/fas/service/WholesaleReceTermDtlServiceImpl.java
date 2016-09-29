package cn.wonhigh.retail.fas.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.wonhigh.retail.fas.dal.database.WholesaleReceTermDtlMapper;

import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;

/**
 * 请写出类的用途 
 * @author admin
 * @date  2014-09-17 18:00:36
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
@Service("wholesaleReceTermsDtlService")
class WholesaleReceTermDtlServiceImpl extends BaseServiceImpl implements WholesaleReceTermDtlService {
    @Resource
    private WholesaleReceTermDtlMapper wholesaleReceTermDtlMapper;

    @Override
    public BaseCrudMapper init() {
        return wholesaleReceTermDtlMapper;
    }
}