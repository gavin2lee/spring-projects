package cn.wonhigh.retail.fas.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.wonhigh.retail.fas.dal.database.SystemParamSetMapper;

import com.yougou.logistics.base.dal.database.BaseCrudMapper;

/**
 * 请写出类的用途 
 * @author user
 * @date  2015-10-22 10:32:22
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
@Service("systemParamSetService")
class SystemParamSetServiceImpl extends BaseServiceImpl implements SystemParamSetService {
    @Resource
    private SystemParamSetMapper systemParamSetMapper;

    @Override
    public BaseCrudMapper init() {
        return systemParamSetMapper;
    }
}