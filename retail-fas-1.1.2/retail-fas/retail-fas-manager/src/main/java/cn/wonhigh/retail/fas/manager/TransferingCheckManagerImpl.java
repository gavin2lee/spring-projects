package cn.wonhigh.retail.fas.manager;

import java.util.List;
import java.util.Map;

import cn.wonhigh.retail.fas.common.dto.TransferingCheckDto;
import cn.wonhigh.retail.fas.service.TransferingCheckService;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 收发货在途对账报表
 * @author ning.ly
 * @date  2015-04-24 10:10:28
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
@Service("transferingCheckManager")
class TransferingCheckManagerImpl extends BaseCrudManagerImpl implements TransferingCheckManager {
    @Resource
    private TransferingCheckService transferingCheckService;

    @Override
    public BaseCrudService init() {
        return transferingCheckService;
    }

	@Override
	public List<TransferingCheckDto> selectTransferingCheckDtl(SimplePage page,
			String sortColumn, String sortOrder, Map<String, Object> params)
			throws ManagerException {
		List<TransferingCheckDto> dto = null;
		try {
			dto = transferingCheckService.selectTransferingCheckDtl(page, sortColumn, sortOrder, params);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);

		}
		return dto;
	}

	@Override
	public List<TransferingCheckDto> selectTransferingCheckTotal(
			SimplePage page, String sortColumn, String sortOrder,
			Map<String, Object> params) throws ManagerException {
		List<TransferingCheckDto> dto = null;
		try {
			dto = transferingCheckService.selectTransferingCheckTotal(page, sortColumn, sortOrder, params);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);

		}
		return dto;
	}

	@Override
	public int selectTransferingCheckDtlCount(Map<String, Object> params)
			throws ManagerException {
		int iCount = 0;
		try {
			iCount = transferingCheckService.selectTransferingCheckDtlCount(params);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);

		}
		return iCount;
	}

	@Override
	public List<TransferingCheckDto> selectTransferingCheckGatherFooter(SimplePage page,
			Map<String, Object> params) throws ManagerException {
		List<TransferingCheckDto> dto = null;
		try {
			dto = transferingCheckService.selectTransferingCheckGatherFooter(page,params);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);

		}
		return dto;
	}

	@Override
	public List<TransferingCheckDto> selectTransferingCheckDtlFooter(SimplePage page,
			Map<String, Object> params) throws ManagerException {
		List<TransferingCheckDto> dto = null;
		try {
			dto = transferingCheckService.selectTransferingCheckDtlFooter(page,params);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);

		}
		return dto;
	}

	@Override
	public int findReceiveAndSendDtlCount(Map<String, Object> params) {
		return transferingCheckService.findReceiveAndSendDtlCount(params);
	}

	@Override
	public List<TransferingCheckDto> findReceiveAndSendDtl(SimplePage page,
			String sortColumn, String sortOrder, Map<String, Object> params) {
		return transferingCheckService.findReceiveAndSendDtl(page, sortColumn, sortOrder, params);
	}

	@Override
	public List<TransferingCheckDto> findReceiveAndSendDtlFooter(Object object,
			Map<String, Object> params) {
		return transferingCheckService.findReceiveAndSendDtlFooter(object,params);
	}

	@Override
	public int findReceiveAndSendSumCount(Map<String, Object> params) {
		return transferingCheckService.findReceiveAndSendSumCount(params);
	}

	@Override
	public List<TransferingCheckDto> findReceiveAndSendSum(SimplePage page,
			String sortColumn, String sortOrder, Map<String, Object> params) {
		return transferingCheckService.findReceiveAndSendSum(page, sortColumn, sortOrder, params);
	}

	@Override
	public List<TransferingCheckDto> findReceiveAndSendSumFooter(Object object,
			Map<String, Object> params) {
		return transferingCheckService.findReceiveAndSendSumFooter(object,params);
	}
}