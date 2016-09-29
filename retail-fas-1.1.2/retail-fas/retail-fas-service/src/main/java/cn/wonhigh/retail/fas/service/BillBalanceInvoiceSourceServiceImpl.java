package cn.wonhigh.retail.fas.service;

import cn.wonhigh.retail.fas.common.model.BillBalanceInvoiceSource;
import cn.wonhigh.retail.fas.dal.database.BillBalanceInvoiceSourceMapper;

import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.dal.database.BaseCrudMapper;
import com.yougou.logistics.base.service.BaseCrudServiceImpl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 请写出类的用途 
 * @author chen.mj
 * @date  2014-09-30 11:19:51
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
@Service("billBalanceInvoiceSourceService")
class BillBalanceInvoiceSourceServiceImpl extends BaseCrudServiceImpl implements BillBalanceInvoiceSourceService {
    @Resource
    private BillBalanceInvoiceSourceMapper billBalanceInvoiceSourceMapper;

    @Override
    public BaseCrudMapper init() {
        return billBalanceInvoiceSourceMapper;
    }

	@Override
	public void deleteInvoiceSource(String billNo) {
		billBalanceInvoiceSourceMapper.deleteInvoiceSource(billNo);
	}

	@Override
	public int updateAmountByBillNo(BillBalanceInvoiceSource billBalanceInvoiceSource)throws ServiceException {
		return billBalanceInvoiceSourceMapper.updateAmountByBillNo(billBalanceInvoiceSource);
	}
}