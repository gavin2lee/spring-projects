package cn.wonhigh.retail.fas.service;

import cn.wonhigh.retail.fas.dal.database.InsideBizTypeMapper;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 请写出类的用途 
 * @author wangyj
 * @date  2015-04-02 13:45:59
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
@Service("insideBizTypeService")
class InsideBizTypeServiceImpl extends BaseCrudServiceImpl implements InsideBizTypeService {
    @Resource
    private InsideBizTypeMapper insideBizTypeMapper;

    @Override
    public BaseCrudMapper init() {
        return insideBizTypeMapper;
    }
}