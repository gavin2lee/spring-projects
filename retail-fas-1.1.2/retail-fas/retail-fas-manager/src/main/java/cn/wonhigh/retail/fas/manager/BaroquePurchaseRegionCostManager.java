package cn.wonhigh.retail.fas.manager;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;

public interface BaroquePurchaseRegionCostManager extends BaseCrudManager {

	boolean updateRegionCost(String originalNos, String itemNo) throws ManagerException;
	
	boolean updateRegionCost(String ids) throws ManagerException;
}
