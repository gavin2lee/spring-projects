package cn.wonhigh.retail.fas.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wonhigh.retail.fas.common.dto.ItemSaleDtlDto;
import cn.wonhigh.retail.fas.common.dto.POSOcPagingDto;
import cn.wonhigh.retail.fas.common.dto.POSOrderAndReturnExMainDtlDto;
import cn.wonhigh.retail.fas.manager.ShopSaleDetailManager;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.exception.ManagerException;

@Controller
@RequestMapping("/shop_sale_detail")
@ModuleVerify("30170013")
public class ShopSaleDetailController extends ExcelImportController<ItemSaleDtlDto>{
	
	@Resource
	private ShopSaleDetailManager shopSaleDetailManagerImpl;
	
	private static final XLogger LOGGER = XLoggerFactory.getXLogger(ShopSaleDetailController.class);
	
	private static Integer ALL_RECORD = 2;
	
	@Override
	protected CrudInfo init() {
		// TODO Auto-generated method stub
		return new CrudInfo("IndepShop_management/sale_detail/",
				shopSaleDetailManagerImpl);
	}
	
	@RequestMapping(value = "/list.json")
	@ResponseBody
	public Map<String,Object> queryList(HttpServletRequest req, Model model)
			throws ManagerException {
		
		Map<String, Object> params = builderParams(req, model);
		params.put("invoiceNoFlag", ALL_RECORD);
		params.put("invoiceNo", null);
		List<ItemSaleDtlDto> itemSaleDtlDtos = null;
		
		POSOcPagingDto<POSOrderAndReturnExMainDtlDto> orderAndReturnExMainDtlDtoList = shopSaleDetailManagerImpl.queryShopSaleDetailListRemote(params);
		
		if(orderAndReturnExMainDtlDtoList == null || orderAndReturnExMainDtlDtoList.getResult() == null ||  orderAndReturnExMainDtlDtoList.getResult().size() < 1){
			return null;
		}
		List<POSOrderAndReturnExMainDtlDto> OrderAndReturnExMainList = orderAndReturnExMainDtlDtoList.getResult();
		itemSaleDtlDtos = shopSaleDetailManagerImpl.converDateToViewData(OrderAndReturnExMainList);
		
		int total = orderAndReturnExMainDtlDtoList.getTotal();
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("total", total);
		obj.put("rows", itemSaleDtlDtos);
		return obj;
	}

	@Override
	protected String[] getImportProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean doBatchAdd(List list) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	protected List<ItemSaleDtlDto> queryExportData(Map<String,Object> params) throws ManagerException {
		// TODO Auto-generated method stub
		params.put("pageSize", Integer.MAX_VALUE);
		params.put("pageNumber", 1);
		params.put("invoiceNoFlag", ALL_RECORD);
		params.put("invoiceNo", null);
		POSOcPagingDto<POSOrderAndReturnExMainDtlDto> orderAndReturnExMainDtlDtoList = shopSaleDetailManagerImpl.queryShopSaleDetailListRemote(params);
		if(orderAndReturnExMainDtlDtoList == null || orderAndReturnExMainDtlDtoList.getResult() == null ||  orderAndReturnExMainDtlDtoList.getResult().size() < 1){
			return null;
		}
		List<POSOrderAndReturnExMainDtlDto> OrderAndReturnExMainList = orderAndReturnExMainDtlDtoList.getResult();
		
		for(POSOrderAndReturnExMainDtlDto dto : OrderAndReturnExMainList){
			LOGGER.info(" 店铺编码  --> "+dto.getShopNo()+" , 店铺名称 --> "+dto.getShopName());
		}
		
		List<ItemSaleDtlDto> itemSaleDtlDtos = shopSaleDetailManagerImpl.converDateToViewData(OrderAndReturnExMainList);
		return itemSaleDtlDtos;
	}
}
