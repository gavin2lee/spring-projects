package cn.wonhigh.retail.fas.service;

import java.util.List;
import java.util.Map;

import cn.wonhigh.retail.fas.common.model.BillShopBalanceSet;
import cn.wonhigh.retail.fas.dal.database.BillShopBalanceSetMapper;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 请写出类的用途 
 * @author Administrator
 * @date  2015-01-05 14:11:37
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
@Service("billShopBalanceSetService")
class BillShopBalanceSetServiceImpl extends BaseServiceImpl implements BillShopBalanceSetService {
    @Resource
    private BillShopBalanceSetMapper billShopBalanceSetMapper;

    @Override
    public BaseCrudMapper init() {
        return billShopBalanceSetMapper;
    }

	@Override
	public List<BillShopBalanceSet> selectByParams(Map<String, Object> params)
			throws ServiceException {
		return billShopBalanceSetMapper.selectByParams(params);
	}
}