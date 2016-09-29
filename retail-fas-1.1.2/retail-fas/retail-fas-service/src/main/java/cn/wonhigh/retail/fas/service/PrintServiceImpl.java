package cn.wonhigh.retail.fas.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import cn.wonhigh.retail.fas.common.dto.PrintBalanceDto;
import cn.wonhigh.retail.fas.common.utils.FasUtil;
import cn.wonhigh.retail.fas.dal.database.PrintMapper;

import com.yougou.logistics.base.common.exception.ServiceException;

/**
 * 请写出类的用途 
 * @author yang.y
 * @date  2014-08-27 14:16:31
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * orderUnitizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
@Service("printService")
class PrintServiceImpl implements PrintService {
    @Resource
    private PrintMapper printMapper;
	
	@Override
	public Map<String, Object> getPrintListByBalanceNo(String balanceNo)
			throws ServiceException {
		try {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap = setColumnMapList(balanceNo,resultMap);
			resultMap = setRowMapList(balanceNo,resultMap);
    		return resultMap;
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getBalanceGatherListByBalanceNo(
			String balanceNo, Date balanceEndDate) throws ServiceException {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(balanceEndDate);
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) +1;
			Map<String, Object> brandsMap = new HashMap<String, Object>();
			balanceNo = FasUtil.formatInQueryCondition(balanceNo);
    		List<PrintBalanceDto> lstItem = printMapper.selectBalanceGatherList(balanceNo);
    		// 行记录
    		for (PrintBalanceDto balanceDto : lstItem) {
    			String brandUnitNo = balanceDto.getBrandUnitNo();
    			String categoryNo = balanceDto.getCategoryNo();
    			String groupName = balanceDto.getGroupName();
    			String categoryName = balanceDto.getCategoryName();
    			String genderName = balanceDto.getGenderName();
    			String key = categoryNo.equals("01") ? groupName+categoryName+genderName : categoryName ;
    			if(null == brandsMap.get(brandUnitNo)){
    				Map<String, Object> map = new HashMap<String, Object>();
    				List<Map<String, Object>> columnList = getColumnList();
    				List<Map<String, Object>> yearColumnList = getYearColumnList();
    				Map<String, Object> rowMap = new HashMap<String, Object>();
    				List<PrintBalanceDto> footerList = new ArrayList<PrintBalanceDto>();
    				List<PrintBalanceDto> yearFooterList = new ArrayList<PrintBalanceDto>();
    				Map<String, Object> notShoesMap = new HashMap<String, Object>();
    				List<PrintBalanceDto> list = new ArrayList<PrintBalanceDto>();
    				list.add(balanceDto);
    				rowMap.put(key, list);
    				map.put("columnList", columnList);
    				map.put("rowMap", rowMap);
    				map.put("footerList", footerList);
    				map.put("yearFooterList", yearFooterList);
    				map.put("yearColumnList", yearColumnList);
    				map.put("notShoesMap", notShoesMap);
    				brandsMap.put(brandUnitNo, map);
    			}else{
    				Map<String, Object> map = (Map<String, Object>) brandsMap.get(brandUnitNo);
    				Map<String, Object> rowMap = (Map<String, Object>) map.get("rowMap");
    				if(null==rowMap.get(key)){
    					List<PrintBalanceDto> list = new ArrayList<PrintBalanceDto>();
    					list.add(balanceDto);
    					rowMap.put(key, list);
    				}else{
    					List<PrintBalanceDto> list = (List<PrintBalanceDto>) rowMap.get(key);
    					list.add(balanceDto);
    				}
    			}
    			if(!"01".equals(balanceDto.getCategoryNo())){
    				balanceDto.setGenderName(balanceDto.getGroupName());
    			}
			}
    		// 工鞋计算
			String strMonth = month <10 ? "0"+month : String.valueOf(month);
			Map<String, Object> params = new HashMap<>();
			params.put("brandUnitNo", lstItem.get(0).getBrandUnitNo());
			params.put("yearMonthStart", year+"-"+strMonth);
			params.put("yearMonthEnd", year+"-"+strMonth);
			String lstBalanceNo =  printMapper.selectWorkShoesBalanceNo(params);
			List<PrintBalanceDto> lstWorkShoes = new ArrayList<>();
			if(StringUtils.isNotBlank(lstBalanceNo)){
				lstWorkShoes = printMapper.selectBalanceGatherList(lstBalanceNo);
			}
    		// 合计行
    		List<PrintBalanceDto> lstFooter = printMapper.selectBalanceGatherFooter(balanceNo);
    		for (PrintBalanceDto footerDto : lstFooter) {
    			String brandUnitNo = footerDto.getBrandUnitNo();
    			String categoryNo = footerDto.getCategoryNo();
    			String groupName = footerDto.getGroupName();
    			String categoryName = footerDto.getCategoryName();
    			String genderName = footerDto.getGenderName();
    			String key = categoryNo.equals("01") ? groupName+categoryName+genderName : categoryName ;
    			Map<String, Object> map = (Map<String, Object>) brandsMap.get(brandUnitNo);
    			Map<String, Object> rowMap = (Map<String, Object>) map.get("rowMap");
    			List<PrintBalanceDto> footerList = (List<PrintBalanceDto>) map.get("footerList");
    			Map<String, Object> notShoesMap = (Map<String, Object>) map.get("notShoesMap");
    			List<PrintBalanceDto> list = (List<PrintBalanceDto>) rowMap.get(key);
    			if("合计".equals(footerDto.getSalerName()) && categoryNo.equals("01")){// 鞋类合计
    				// 工鞋计算
    				for (PrintBalanceDto shoes : lstWorkShoes) {
						if(groupName.equals(shoes.getGroupName())){
							footerDto.setSendAmount(shoes.getSendAmount().add(footerDto.getSendAmount()));
							footerDto.setReturnAmount(shoes.getReturnAmount().add(footerDto.getReturnAmount()));
							footerDto.setCustomReturnAmount(shoes.getCustomReturnAmount().add(footerDto.getCustomReturnAmount()));
							footerDto.setDeductionAmount(shoes.getDeductionAmount().add(footerDto.getDeductionAmount()));
							footerDto.setBalanceAmount(shoes.getBalanceAmount().add(footerDto.getBalanceAmount()));
							footerDto.setSendQty(shoes.getSendQty()+footerDto.getSendQty());
							footerDto.setReturnQty(shoes.getReturnQty()+footerDto.getReturnQty());
							footerDto.setCustomReturnQty(shoes.getCustomReturnQty()+footerDto.getCustomReturnQty());
							footerDto.setBalanceQty(shoes.getBalanceQty()+footerDto.getBalanceQty());
						}
					}
    				footerList.add(footerDto);
    			}else if("合计".equals(footerDto.getSalerName()) && !categoryNo.equals("01")){// 非鞋类合计
    				if(null == notShoesMap.get(key)){
    					notShoesMap.put(key, footerDto);
    					footerList.add(footerDto);
    				}else{
    					PrintBalanceDto dto = (PrintBalanceDto) notShoesMap.get(key);
    					dto.setSendAmount(dto.getSendAmount().add(footerDto.getSendAmount()));
    					dto.setReturnAmount(dto.getReturnAmount().add(footerDto.getReturnAmount()));
    					dto.setCustomReturnAmount(dto.getCustomReturnAmount().add(footerDto.getCustomReturnAmount()));
    					dto.setDeductionAmount(dto.getDeductionAmount().add(footerDto.getDeductionAmount()));
    					dto.setBalanceAmount(dto.getBalanceAmount().add(footerDto.getBalanceAmount()));
    					dto.setSendQty(dto.getSendQty()+footerDto.getSendQty());
    					dto.setReturnQty(dto.getReturnQty()+footerDto.getReturnQty());
    					dto.setCustomReturnQty(dto.getCustomReturnQty()+footerDto.getCustomReturnQty());
    					dto.setBalanceQty(dto.getBalanceQty()+footerDto.getBalanceQty());
    				}
    				PrintBalanceDto balanceDto = (PrintBalanceDto) notShoesMap.get(key);
    				balanceDto.setCost(balanceDto.getSendQty().intValue() == 0?new BigDecimal(0):balanceDto.getSendAmount().divide(new BigDecimal(balanceDto.getSendQty()), 2,BigDecimal.ROUND_UP));
    			}else if("小计".equals(footerDto.getSalerName()) && categoryNo.equals("01")){// 分类小计
    				// 工鞋计算
    				for (PrintBalanceDto shoes : lstWorkShoes) {
						if(groupName.equals(shoes.getGroupName()) 
								&& genderName.equals(shoes.getGenderName())){
							list.add(shoes);
							shoes.setSalerName(shoes.getSalerName().concat("(").concat("工作鞋").concat(")"));
							footerDto.setSendAmount(shoes.getSendAmount().add(footerDto.getSendAmount()));
							footerDto.setReturnAmount(shoes.getReturnAmount().add(footerDto.getReturnAmount()));
							footerDto.setCustomReturnAmount(shoes.getCustomReturnAmount().add(footerDto.getCustomReturnAmount()));
							footerDto.setDeductionAmount(shoes.getDeductionAmount().add(footerDto.getDeductionAmount()));
							footerDto.setBalanceAmount(shoes.getBalanceAmount().add(footerDto.getBalanceAmount()));
							footerDto.setSendQty(shoes.getSendQty()+footerDto.getSendQty());
							footerDto.setReturnQty(shoes.getReturnQty()+footerDto.getReturnQty());
							footerDto.setCustomReturnQty(shoes.getCustomReturnQty()+footerDto.getCustomReturnQty());
							footerDto.setBalanceQty(shoes.getBalanceQty()+footerDto.getBalanceQty());
						}
					}
    				if(list.size() >1){
    					footerDto.setiCount(list.size());
    					list.add(footerDto);
    				}
    			}
			}
    		// 数据拼装
    		for (Entry<String, Object> entry : brandsMap.entrySet()) {
    			List<Map<String, Object>> allList = new ArrayList<Map<String, Object>>();
    			String brandUnitNo = entry.getKey();
				Map<String, Object> map = (Map<String, Object>) entry.getValue();
				Map<String, Object> rowMap = (Map<String, Object>) map.get("rowMap");
				List<PrintBalanceDto> footerList = (List<PrintBalanceDto>) map.get("footerList");
				for (PrintBalanceDto footerDto : footerList) {
					Map<String, Object> allMap = new HashMap<String, Object>();
					List<PrintBalanceDto> rowList = new ArrayList<PrintBalanceDto>();
					String groupKey = footerDto.getCategoryNo().equals("01") ? footerDto.getGroupName()+footerDto.getCategoryName() : footerDto.getCategoryName() ;
					for (Entry<String, Object> rowEntry : rowMap.entrySet()) {
						String rowKey = rowEntry.getKey();
						List<PrintBalanceDto> list = (List<PrintBalanceDto>) rowEntry.getValue();
						if(rowKey.indexOf(groupKey)!=-1){
							rowList.addAll(list);
						}
					}
					rowList.add(footerDto);
					allMap.put("title", groupKey +"进货汇总");
					allMap.put("rowList", rowList);
					allList.add(allMap);
				}
				allList.add(getYearFooterMap(brandUnitNo,year,month));
				map.remove("rowMap");
				map.remove("footerMap");
				String title = year+"年"+month+"月（全月）"+brandUnitNo+"品牌对账单";
				map.put("title", title);
				map.put("rowList", allList);
			}
    		
    		return brandsMap;
		} catch (Exception e) {
			throw new ServiceException("", e);
		}
	}
	
	
	private Map<String, Object> getYearFooterMap(String brandUnitNo,int year,int month){
			List<PrintBalanceDto> newYearFooterList = new ArrayList<PrintBalanceDto>();
			Map<String, Object> yearFooterMap = new HashMap<String, Object>();
			Map<String, Object> notShoesMap = new HashMap<String, Object>();
			Map<String, Object> shoesMap = new HashMap<String, Object>();
			Map<String, Object> params = new HashMap<String, Object>();
			String strMonth = month <10 ? "0"+month : String.valueOf(month);
			params.put("brandUnitNo", brandUnitNo);
			params.put("yearMonthStart", year+"-"+"01");
			params.put("yearMonthEnd", year+"-"+strMonth);
			List<PrintBalanceDto> lstItem = printMapper.selectYearBalanceGatherFooter(params);
			for (PrintBalanceDto dto : lstItem) {
				if("01".equals(dto.getCategoryNo())){
					PrintBalanceDto newDto = new PrintBalanceDto();
					newDto.setSendAmount(dto.getSendAmount());
					newDto.setReturnAmount(dto.getReturnAmount());
					newDto.setCustomReturnAmount(dto.getCustomReturnAmount());
					newDto.setBalanceAmount(dto.getBalanceAmount());
					newDto.setSendQty(dto.getSendQty());
					newDto.setReturnQty(dto.getReturnQty());
					newDto.setCustomReturnQty(dto.getCustomReturnQty());
					newDto.setBalanceQty(dto.getBalanceQty());
					newDto.setTitle(dto.getGroupName()+dto.getGenderName()+"鞋");
					newDto.setCost(newDto.getSendQty().intValue() == 0?new BigDecimal(0):newDto.getSendAmount().divide(new BigDecimal(newDto.getSendQty()), 2,BigDecimal.ROUND_UP));
					newYearFooterList.add(newDto);
				}else if(!"06".equals(dto.getCategoryNo())){
					String key = dto.getCategoryNo();
					if(null == notShoesMap.get(key)){
						notShoesMap.put(key, dto);
						dto.setTitle(dto.getCategoryName());
						newYearFooterList.add(dto);
					}else{
						PrintBalanceDto newDto  = (PrintBalanceDto) notShoesMap.get(key);
						newDto.setSendAmount(newDto.getSendAmount().add(dto.getSendAmount()));
						newDto.setReturnAmount(newDto.getReturnAmount().add(dto.getReturnAmount()));
						newDto.setCustomReturnAmount(newDto.getCustomReturnAmount().add(dto.getCustomReturnAmount()));
						newDto.setBalanceAmount(newDto.getBalanceAmount().add(dto.getBalanceAmount()));
						newDto.setSendQty(newDto.getSendQty()+dto.getSendQty());
						newDto.setReturnQty(newDto.getReturnQty()+dto.getReturnQty());
						newDto.setCustomReturnQty(newDto.getCustomReturnQty()+dto.getCustomReturnQty());
						newDto.setBalanceQty(newDto.getBalanceQty()+dto.getBalanceQty());
					}
				}
			}
			for (PrintBalanceDto dto : lstItem) {// 供应商分类统计
				if("01".equals(dto.getCategoryNo()) ){
					String key = dto.getGroupNo();
					if(null == shoesMap.get(key)){
						PrintBalanceDto newDto = new PrintBalanceDto();
						newDto.setSendAmount(dto.getSendAmount());
						newDto.setReturnAmount(dto.getReturnAmount());
						newDto.setCustomReturnAmount(dto.getCustomReturnAmount());
						newDto.setBalanceAmount(dto.getBalanceAmount());
						newDto.setSendQty(dto.getSendQty());
						newDto.setReturnQty(dto.getReturnQty());
						newDto.setCustomReturnQty(dto.getCustomReturnQty());
						newDto.setBalanceQty(dto.getBalanceQty());
						newDto.setTitle(dto.getGroupName()+"鞋合计");
						newDto.setCost(newDto.getSendQty().intValue() == 0?new BigDecimal(0):newDto.getSendAmount().divide(new BigDecimal(newDto.getSendQty()), 2,BigDecimal.ROUND_UP));
						shoesMap.put(key, newDto);
						newYearFooterList.add(newDto);
					}else{
						PrintBalanceDto newDto  = (PrintBalanceDto) shoesMap.get(key);
						newDto.setSendAmount(newDto.getSendAmount().add(dto.getSendAmount()));
						newDto.setReturnAmount(newDto.getReturnAmount().add(dto.getReturnAmount()));
						newDto.setCustomReturnAmount(newDto.getCustomReturnAmount().add(dto.getCustomReturnAmount()));
						newDto.setBalanceAmount(newDto.getBalanceAmount().add(dto.getBalanceAmount()));
						newDto.setSendQty(newDto.getSendQty()+dto.getSendQty());
						newDto.setReturnQty(newDto.getReturnQty()+dto.getReturnQty());
						newDto.setCustomReturnQty(newDto.getCustomReturnQty()+dto.getCustomReturnQty());
						newDto.setBalanceQty(newDto.getBalanceQty()+dto.getBalanceQty());
						newDto.setCost(newDto.getSendQty().intValue() == 0?new BigDecimal(0):newDto.getSendAmount().divide(new BigDecimal(newDto.getSendQty()), 2,BigDecimal.ROUND_UP));
					}
				}
			}
			PrintBalanceDto shoesFooterDto = new PrintBalanceDto();
			for (PrintBalanceDto dto : lstItem) {// 性别分类统计
				if("01".equals(dto.getCategoryNo())){
					String key = dto.getGender();
					if(null == shoesMap.get(key)){
						PrintBalanceDto newDto = new PrintBalanceDto();
						newDto.setSendAmount(dto.getSendAmount());
						newDto.setReturnAmount(dto.getReturnAmount());
						newDto.setCustomReturnAmount(dto.getCustomReturnAmount());
						newDto.setBalanceAmount(dto.getBalanceAmount());
						newDto.setSendQty(dto.getSendQty());
						newDto.setReturnQty(dto.getReturnQty());
						newDto.setCustomReturnQty(dto.getCustomReturnQty());
						newDto.setBalanceQty(dto.getBalanceQty());
						newDto.setCost(newDto.getSendQty().intValue() == 0?new BigDecimal(0):newDto.getSendAmount().divide(new BigDecimal(newDto.getSendQty()), 2,BigDecimal.ROUND_UP));
						newDto.setTitle(dto.getGenderName()+"鞋合计");
						shoesMap.put(key, newDto);
						newYearFooterList.add(newDto);
					}else{
						PrintBalanceDto newDto  = (PrintBalanceDto) shoesMap.get(key);
						newDto.setSendAmount(newDto.getSendAmount().add(dto.getSendAmount()));
						newDto.setReturnAmount(newDto.getReturnAmount().add(dto.getReturnAmount()));
						newDto.setCustomReturnAmount(newDto.getCustomReturnAmount().add(dto.getCustomReturnAmount()));
						newDto.setBalanceAmount(newDto.getBalanceAmount().add(dto.getBalanceAmount()));
						newDto.setSendQty(newDto.getSendQty()+dto.getSendQty());
						newDto.setReturnQty(newDto.getReturnQty()+dto.getReturnQty());
						newDto.setCustomReturnQty(newDto.getCustomReturnQty()+dto.getCustomReturnQty());
						newDto.setBalanceQty(newDto.getBalanceQty()+dto.getBalanceQty());
						newDto.setCost(newDto.getSendQty().intValue() == 0?new BigDecimal(0):newDto.getSendAmount().divide(new BigDecimal(newDto.getSendQty()), 2,BigDecimal.ROUND_UP));
					}
					shoesFooterDto.setSendAmount(shoesFooterDto.getSendAmount().add(dto.getSendAmount()));
					shoesFooterDto.setReturnAmount(shoesFooterDto.getReturnAmount().add(dto.getReturnAmount()));
					shoesFooterDto.setCustomReturnAmount(shoesFooterDto.getCustomReturnAmount().add(dto.getCustomReturnAmount()));
					shoesFooterDto.setBalanceAmount(shoesFooterDto.getBalanceAmount().add(dto.getBalanceAmount()));
					shoesFooterDto.setSendQty(shoesFooterDto.getSendQty()+dto.getSendQty());
					shoesFooterDto.setReturnQty(shoesFooterDto.getReturnQty()+dto.getReturnQty());
					shoesFooterDto.setCustomReturnQty(shoesFooterDto.getCustomReturnQty()+dto.getCustomReturnQty());
					shoesFooterDto.setBalanceQty(shoesFooterDto.getBalanceQty()+dto.getBalanceQty());
				}
			}
			shoesFooterDto.setCost(shoesFooterDto.getSendQty().intValue() == 0?new BigDecimal(0):shoesFooterDto.getSendAmount().divide(new BigDecimal(shoesFooterDto.getSendQty()), 2,BigDecimal.ROUND_UP));
			shoesFooterDto.setTitle("鞋合计");
			newYearFooterList.add(shoesFooterDto);
			// 工作鞋
			String lstBalanceNo =  printMapper.selectWorkShoesBalanceNo(params);
			if(StringUtils.isNotBlank(lstBalanceNo)){
				params.put("balanceNo", lstBalanceNo);
				List<PrintBalanceDto> lstYearWorkShoes = printMapper.selectYearBalanceGatherFooter(params);
				Map<String, Object> workShoesMap = new HashMap<>();
				for (PrintBalanceDto workShoes : lstYearWorkShoes) {
					String groupKey = workShoes.getGroupName();
					if(null == workShoesMap.get(groupKey)){
						PrintBalanceDto newDto = new PrintBalanceDto();
						newDto.setSendAmount(workShoes.getSendAmount());
						newDto.setReturnAmount(workShoes.getReturnAmount());
						newDto.setCustomReturnAmount(workShoes.getCustomReturnAmount());
						newDto.setBalanceAmount(workShoes.getBalanceAmount());
						newDto.setSendQty(workShoes.getSendQty());
						newDto.setReturnQty(workShoes.getReturnQty());
						newDto.setCustomReturnQty(workShoes.getCustomReturnQty());
						newDto.setBalanceQty(workShoes.getBalanceQty());
						newDto.setCost(newDto.getSendQty().intValue() == 0?new BigDecimal(0):newDto.getSendAmount().divide(new BigDecimal(newDto.getSendQty()), 2,BigDecimal.ROUND_UP));
						newDto.setTitle(groupKey+"工鞋(物料)");
						workShoesMap.put(groupKey, newDto);
						newYearFooterList.add(newDto);
					}else{
						PrintBalanceDto oldDto = (PrintBalanceDto) workShoesMap.get(groupKey);
						oldDto.setSendAmount(oldDto.getSendAmount().add(workShoes.getSendAmount()));
						oldDto.setReturnAmount(oldDto.getReturnAmount().add(workShoes.getReturnAmount()));
						oldDto.setCustomReturnAmount(oldDto.getCustomReturnAmount().add(workShoes.getCustomReturnAmount()));
						oldDto.setBalanceAmount(oldDto.getBalanceAmount().add(workShoes.getBalanceAmount()));
						oldDto.setSendQty(oldDto.getSendQty()+workShoes.getSendQty());
						oldDto.setReturnQty(oldDto.getReturnQty()+workShoes.getReturnQty());
						oldDto.setCustomReturnQty(oldDto.getCustomReturnQty()+workShoes.getCustomReturnQty());
						oldDto.setBalanceQty(oldDto.getBalanceQty()+workShoes.getBalanceQty());
						oldDto.setCost(oldDto.getSendQty().intValue() == 0?new BigDecimal(0):oldDto.getSendAmount().divide(new BigDecimal(oldDto.getSendQty()), 2,BigDecimal.ROUND_UP));
					}
				}
				
			}
			
			yearFooterMap.put("title", year +"年度截至当月累计");
			yearFooterMap.put("rowList", newYearFooterList);
		return yearFooterMap;
	}
	
	private Map<String, Object> setColumnMapList(String balanceNo,Map<String, Object> resultMap) {
		List<Map<String, Object>> columnList = new ArrayList<Map<String, Object>>();
		List<PrintBalanceDto> orderUnitList = printMapper.selectSendColumnByBalanceNo(balanceNo);
		Map<String, Object> indexMap = new HashMap<String, Object>();
		indexMap.put("field", "ck");
		indexMap.put("title", "序号");
		indexMap.put("width", 30);
		columnList.add(indexMap);
		Map<String, Object> codeMap = new HashMap<String, Object>();
		codeMap.put("field", "itemCode");
		codeMap.put("title", "货号");
		codeMap.put("width", 80);
		columnList.add(codeMap);
		Map<String, Object> nameMap = new HashMap<String, Object>();
		nameMap.put("field", "itemName");
		nameMap.put("title", "中文名称");
		nameMap.put("width", 100);
		columnList.add(nameMap);
		for (PrintBalanceDto balance : orderUnitList) {
			Map<String, Object> orderUnitMap = new HashMap<String, Object>();
			orderUnitMap.put("field", balance.getOrderUnitNo());
			orderUnitMap.put("title", balance.getOrderUnitName());
			orderUnitMap.put("width", 40);
			columnList.add(orderUnitMap);
		}
		Map<String, Object> sendQtyMap = new HashMap<String, Object>();
		sendQtyMap.put("field", "sendQty");
		sendQtyMap.put("title", "合计");
		sendQtyMap.put("width", 40);
		columnList.add(sendQtyMap);
		Map<String, Object> purchasePriceMap = new HashMap<String, Object>();
		purchasePriceMap.put("field", "purchasePrice");
		purchasePriceMap.put("title", "采购价");
		purchasePriceMap.put("width", 40);
		columnList.add(purchasePriceMap);
		Map<String, Object> materialPriceMap = new HashMap<String, Object>();
		materialPriceMap.put("field", "materialPrice");
		materialPriceMap.put("title", "物料价");
		materialPriceMap.put("width", 40);
		columnList.add(materialPriceMap);
		Map<String, Object> sendAmountMap = new HashMap<String, Object>();
		sendAmountMap.put("field", "sendAmount");
		sendAmountMap.put("title", "出货金额");
		sendAmountMap.put("width", 50);
		columnList.add(sendAmountMap);
		// 残鞋
		Map<String, Object> codeMap1 = new HashMap<String, Object>();
		codeMap1.put("field", "itemCode1");
		codeMap1.put("title", "货号");
		codeMap1.put("width", 80);
		columnList.add(codeMap1);
		Map<String, Object> sendQtyMap1 = new HashMap<String, Object>();
		sendQtyMap1.put("field", "sendQty1");
		sendQtyMap1.put("title", "数量");
		sendQtyMap1.put("width", 40);
		columnList.add(sendQtyMap1);
		Map<String, Object> purchasePriceMap1 = new HashMap<String, Object>();
		purchasePriceMap1.put("field", "purchasePrice1");
		purchasePriceMap1.put("title", "采购价");
		purchasePriceMap1.put("width", 40);
		columnList.add(purchasePriceMap1);
		Map<String, Object> sendAmountMap1 = new HashMap<String, Object>();
		sendAmountMap1.put("field", "sendAmount1");
		sendAmountMap1.put("title", "金额");
		sendAmountMap1.put("width", 50);
		columnList.add(sendAmountMap1);
		// 退货
		Map<String, Object> codeMap2 = new HashMap<String, Object>();
		codeMap2.put("field", "itemCode2");
		codeMap2.put("title", "货号");
		codeMap2.put("width", 80);
		columnList.add(codeMap2);
		//modify
		Map<String, Object> itemCodeNameMap = new HashMap<String, Object>();
		itemCodeNameMap.put("field", "itemCodeName");
		itemCodeNameMap.put("title", "货号名称");
		itemCodeNameMap.put("width", 80);
		columnList.add(itemCodeNameMap);
		
		Map<String, Object> organNameMap = new HashMap<String, Object>();
		organNameMap.put("field", "organName");
		organNameMap.put("title", "退货城市");
		organNameMap.put("width", 40);
		columnList.add(organNameMap);
		
		Map<String, Object> sendQtyMap2 = new HashMap<String, Object>();
		sendQtyMap2.put("field", "sendQty2");
		sendQtyMap2.put("title", "数量");
		sendQtyMap2.put("width", 40);
		columnList.add(sendQtyMap2);
		Map<String, Object> purchasePriceMap2 = new HashMap<String, Object>();
		purchasePriceMap2.put("field", "purchasePrice2");
		purchasePriceMap2.put("title", "采购价");
		purchasePriceMap2.put("width", 40);
		columnList.add(purchasePriceMap2);
		
		//modify
		Map<String, Object> materialPrice1 = new HashMap<String, Object>();
		materialPrice1.put("field", "materialPrice1");
		materialPrice1.put("title", "物料价");
		materialPrice1.put("width", 40);
		columnList.add(materialPrice1);
		
		Map<String, Object> sendAmountMap2 = new HashMap<String, Object>();
		sendAmountMap2.put("field", "sendAmount2");
		sendAmountMap2.put("title", "金额");
		sendAmountMap2.put("width", 50);
		columnList.add(sendAmountMap2);
		resultMap.put("columnList", columnList);
		return resultMap;
	}
	
	private Map<String, Object> setRowMapList(String balanceNo,Map<String, Object> resultMap) {
		List<Map<String, Object>> rowMapList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> footerMapList = new ArrayList<Map<String, Object>>();
		List<PrintBalanceDto> rowSendList = printMapper.selectSendRowByBalanceNo(balanceNo);
		List<PrintBalanceDto> qtySendList = printMapper.selectSendQtyByBalanceNo(balanceNo);
		List<PrintBalanceDto> customReturnList = printMapper.selectCustomReturnQtyByBalanceNo(balanceNo);
		List<PrintBalanceDto> returnList = printMapper.selectReturnQtyByBalanceNo(balanceNo);
		int[] size = {rowSendList.size(),customReturnList.size(),returnList.size()};
		Arrays.sort(size);
		int maxSize = size[size.length-1];
		int allFooterQty = 0 ;
		BigDecimal allFooterAmount = new BigDecimal(0);
		int allFooterQty1 = 0 ;
		BigDecimal allFooterAmount1 = new BigDecimal(0);
		int allFooterQty2 = 0 ;
		BigDecimal allFooterAmount2 = new BigDecimal(0);
		Map<String, Object> footerMap = new HashMap<String, Object>();
		for(int i =0;i<maxSize;i++){
			Map<String, Object> map = new HashMap<String, Object>();
			if(rowSendList.size() > i){
				Integer allQty = new Integer(0);
				BigDecimal allAmount = new BigDecimal(0);
				BigDecimal purchasePrice = new BigDecimal(0);
				BigDecimal materialPrice = new BigDecimal(0);
				PrintBalanceDto balance = rowSendList.get(i);
				map.put("itemCode", balance.getItemCode());
				map.put("itemName", balance.getItemName());
				for (PrintBalanceDto balanceQty : qtySendList) {
					if(i==0){
						String footerOrderUnitNo = balanceQty.getOrderUnitNo();
						Integer footerSendQty = balanceQty.getSendQty();
						allFooterQty += balanceQty.getSendQty();
						allFooterAmount = allFooterAmount.add(balanceQty.getSendAmount());
						if(null == footerMap.get(footerOrderUnitNo)){
							footerMap.put(footerOrderUnitNo, footerSendQty);
						}else{
							Integer footerQty = (Integer) footerMap.get(balanceQty.getOrderUnitNo());
							footerMap.put(footerOrderUnitNo, footerSendQty + footerQty);
						}
					}
					if(balanceQty.getItemNo().equals(balance.getItemNo())){
						String orderUnitNo = balanceQty.getOrderUnitNo();
						Integer sendQty = balanceQty.getSendQty();
						BigDecimal sendAmount = balanceQty.getSendAmount();
						if(null == map.get(orderUnitNo)){
							purchasePrice = balanceQty.getPurchasePrice();
							materialPrice = balanceQty.getMaterialPrice();
							map.put(orderUnitNo, sendQty);
						}else{
							Integer orderUnitQty = (Integer) map.get(orderUnitNo);
							map.put(orderUnitNo, orderUnitQty + sendQty);
						}
						allQty += sendQty;
						allAmount = allAmount.add(sendAmount); 
					}
				}
				map.put("sendQty", allQty);
				map.put("purchasePrice", purchasePrice);
				map.put("materialPrice", materialPrice);
				map.put("sendAmount", allAmount);
			}
			if(customReturnList.size() > i){
				PrintBalanceDto balance = customReturnList.get(i);
				allFooterQty1 += balance.getSendQty();
				allFooterAmount1 = allFooterAmount1.add(balance.getSendAmount());
				map.put("itemCode1", balance.getItemCode());
				map.put("sendQty1", balance.getSendQty());
				map.put("purchasePrice1", balance.getPurchasePrice());
				map.put("sendAmount1", balance.getSendAmount());
			}
			if(returnList.size() > i){
				PrintBalanceDto balance = returnList.get(i);
				allFooterQty2 += balance.getSendQty();
				allFooterAmount2 = allFooterAmount2.add(balance.getSendAmount());
				map.put("itemCode2", balance.getItemCode());
				map.put("itemCodeName", balance.getItemName());
				map.put("organName",balance.getOrganName());
				map.put("sendQty2", balance.getSendQty());
				map.put("purchasePrice2", balance.getPurchasePrice());
				map.put("materialPrice1", balance.getMaterialPrice());
				map.put("sendAmount2", balance.getSendAmount());
			}
			rowMapList.add(map);
		}
		footerMap.put("sendQty", allFooterQty);
		footerMap.put("sendAmount", allFooterAmount);
		footerMap.put("sendQty1", allFooterQty1);
		footerMap.put("sendAmount1", allFooterAmount1);
		footerMap.put("sendQty2", allFooterQty2);
		footerMap.put("sendAmount2", allFooterAmount2);
		BigDecimal deductionAmount = printMapper.selectDeductionAmountByBalanceNo(balanceNo);
		footerMap.put("deductionAmount", deductionAmount);
		footerMapList.add(footerMap);
		Map<String, Object> lastFooterMap = new HashMap<String, Object>();
		BigDecimal balanceAmount = allFooterAmount.subtract(allFooterAmount1).subtract(deductionAmount).add(allFooterAmount2);
		lastFooterMap.put("balanceAmount", balanceAmount);
		footerMapList.add(lastFooterMap);
		resultMap.put("rowList", rowMapList);
		resultMap.put("footerList", footerMapList);
		return resultMap;
	}

	private List<Map<String, Object>> getYearColumnList(){
		List<Map<String, Object>> columnList = new ArrayList<Map<String, Object>>();
		Map<String, Object> genderMap = new HashMap<String, Object>();
		genderMap.put("field", "title");
		genderMap.put("title", "类别");
		genderMap.put("width", 65);
		columnList.add(genderMap);
		Map<String, Object> sendQtyMap = new HashMap<String, Object>();
		sendQtyMap.put("field", "sendQty");
		sendQtyMap.put("title", "累计进货数量");
		sendQtyMap.put("width", 80);
		columnList.add(sendQtyMap);
		Map<String, Object> sendAmountMap = new HashMap<String, Object>();
		sendAmountMap.put("field", "sendAmount");
		sendAmountMap.put("title", "累计进货金额");
		sendAmountMap.put("width", 80);
		columnList.add(sendAmountMap);
		Map<String, Object> costMap = new HashMap<String, Object>();
		costMap.put("field", "cost");
		costMap.put("title", "累计平均单价");
		costMap.put("width", 150);
		columnList.add(costMap);
		Map<String, Object> customReturnQtyMap = new HashMap<String, Object>();
		customReturnQtyMap.put("field", "customReturnQty");
		customReturnQtyMap.put("title", "累计客残数量");
		customReturnQtyMap.put("width", 70);
		columnList.add(customReturnQtyMap);
		Map<String, Object> customReturnAmountMap = new HashMap<String, Object>();
		customReturnAmountMap.put("field", "customReturnAmount");
		customReturnAmountMap.put("title", "累计客残金额");
		customReturnAmountMap.put("width", 70);
		columnList.add(customReturnAmountMap);
		Map<String, Object> balanceQtyMap = new HashMap<String, Object>();
		balanceQtyMap.put("field", "balanceQty");
		balanceQtyMap.put("title", "累计应付数量");
		balanceQtyMap.put("width", 80);
		columnList.add(balanceQtyMap);
		Map<String, Object> balanceAmountMap = new HashMap<String, Object>();
		balanceAmountMap.put("field", "balanceAmount");
		balanceAmountMap.put("title", "累计应付金额");
		balanceAmountMap.put("width", 80);
		columnList.add(balanceAmountMap);
		return columnList;
	}
	
	private List<Map<String, Object>> getColumnList(){
		List<Map<String, Object>> columnList = new ArrayList<Map<String, Object>>();
		Map<String, Object> genderMap = new HashMap<String, Object>();
		genderMap.put("field", "genderName");
		genderMap.put("title", "类别");
		genderMap.put("width", 50);
		columnList.add(genderMap);
		Map<String, Object> indexMap = new HashMap<String, Object>();
		indexMap.put("field", "ck");
		indexMap.put("title", "序号");
		indexMap.put("width", 30);
		columnList.add(indexMap);
		Map<String, Object> codeMap = new HashMap<String, Object>();
		codeMap.put("field", "salerNo");
		codeMap.put("title", "厂家代号");
		codeMap.put("width", 70);
		columnList.add(codeMap);
		Map<String, Object> nameMap = new HashMap<String, Object>();
		nameMap.put("field", "salerName");
		nameMap.put("title", "厂家名称");
		nameMap.put("width", 150);
		columnList.add(nameMap);
		Map<String, Object> sendQtyMap = new HashMap<String, Object>();
		sendQtyMap.put("field", "sendQty");
		sendQtyMap.put("title", "进货数量");
		sendQtyMap.put("width", 50);
		columnList.add(sendQtyMap);
		Map<String, Object> sendAmountMap = new HashMap<String, Object>();
		sendAmountMap.put("field", "sendAmount");
		sendAmountMap.put("title", "进货金额");
		sendAmountMap.put("width", 70);
		columnList.add(sendAmountMap);
		Map<String, Object> costMap = new HashMap<String, Object>();
		costMap.put("field", "cost");
		costMap.put("title", "平均单价");
		costMap.put("width", 50);
		columnList.add(costMap);
		Map<String, Object> customReturnQtyMap = new HashMap<String, Object>();
		customReturnQtyMap.put("field", "customReturnQty");
		customReturnQtyMap.put("title", "客残数量");
		customReturnQtyMap.put("width", 50);
		columnList.add(customReturnQtyMap);
		Map<String, Object> customReturnAmountMap = new HashMap<String, Object>();
		customReturnAmountMap.put("field", "customReturnAmount");
		customReturnAmountMap.put("title", "客残金额");
		customReturnAmountMap.put("width", 60);
		columnList.add(customReturnAmountMap);
		Map<String, Object> deductionAmountMap = new HashMap<String, Object>();
		deductionAmountMap.put("field", "deductionAmount");
		deductionAmountMap.put("title", "其他扣项");
		deductionAmountMap.put("width", 60);
		columnList.add(deductionAmountMap);
		Map<String, Object> balanceQtyMap = new HashMap<String, Object>();
		balanceQtyMap.put("field", "balanceQty");
		balanceQtyMap.put("title", "应付数量");
		balanceQtyMap.put("width", 80);
		columnList.add(balanceQtyMap);
		Map<String, Object> balanceAmountMap = new HashMap<String, Object>();
		balanceAmountMap.put("field", "balanceAmount");
		balanceAmountMap.put("title", "应付金额");
		balanceAmountMap.put("width", 80);
		columnList.add(balanceAmountMap);
		return columnList;
	}
}