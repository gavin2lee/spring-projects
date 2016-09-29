/**
 * title:HqOtherEntryManager.java
 * package:cn.wonhigh.retail.fas.manager
 * description:TODO
 * auther:user
 * date:2015-5-8 上午10:47:31
 */
package cn.wonhigh.retail.fas.manager;

import java.util.List;
import java.util.Map;

import cn.wonhigh.retail.fas.common.model.BillBuyBalance;
import cn.wonhigh.retail.fas.common.model.BillSaleBalance;

import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.manager.BaseCrudManager;

/**
 * 
 */
public interface AreaOtherStockInEntryManager extends BaseCrudManager {

	/**
	 * 查询合计行
	 * @param params
	 * @return
	 */
	List<BillSaleBalance> findTotalRow(Map<String, Object> params);
	
	int selectDtlCount(Map<String, Object> params);

	List<BillBuyBalance> selectDtlByPage(SimplePage page, String orderByField, String orderBy, Map<String,Object> params);

	List<BillBuyBalance> selectDtlTotalRow(Map<String, Object> params);

}
