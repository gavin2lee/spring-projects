package cn.wonhigh.retail.fas.manager;

import java.util.List;
import java.util.Map;

import cn.wonhigh.retail.fas.common.dto.ItemSaleDtlDto;
import cn.wonhigh.retail.fas.common.dto.POSOcPagingDto;
import cn.wonhigh.retail.fas.common.dto.POSOrderAndReturnExMainDtlDto;
import cn.wonhigh.retail.oc.common.api.dto.OrderAndReturnExMainDtlDto;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;

public interface ShopSaleDetailManager extends BaseCrudManager{
	
	public POSOcPagingDto<POSOrderAndReturnExMainDtlDto> queryShopSaleDetailListRemote(Map<String, Object> params) throws ManagerException;
	
	public List<ItemSaleDtlDto> converDateToViewData(List<POSOrderAndReturnExMainDtlDto> OrderAndReturnExMainList);
}
