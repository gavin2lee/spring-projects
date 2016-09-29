package cn.wonhigh.retail.fas.web.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cn.wonhigh.retail.fas.common.model.CashTransactionDtl;
import cn.wonhigh.retail.fas.common.model.SelfShopBankInfo;
import cn.wonhigh.retail.fas.common.model.SelfShopDepositAccount;
import cn.wonhigh.retail.fas.common.model.Shop;
import cn.wonhigh.retail.fas.common.model.UploadMessageModel;
import cn.wonhigh.retail.fas.manager.CashTransactionDtlManager;
import cn.wonhigh.retail.fas.manager.SelfShopDepositAccountManager;
import cn.wonhigh.retail.fas.manager.ShopManager;
import cn.wonhigh.retail.fas.service.SelfShopBankInfoService;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;

/**
 * 请写出类的用途 
 * @author zhouxm
 * @date  2014-10-13 17:36:27
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
@Controller
@RequestMapping("/cash_transaction_dtl")
@ModuleVerify("30170005")
public class CashTransactionDtlController extends ExcelImportController<CashTransactionDtl> {
    @Resource
    private CashTransactionDtlManager cashTransactionDtlManager;
    
    @Resource
    private SelfShopDepositAccountManager selfShopDepositAccountManager;
    
    @Resource
    private ShopManager shopManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("IndepShop_management/deal_dtl/",cashTransactionDtlManager);
    }

	@Override
	protected String[] getImportProperties() {
		return new String[]{"cardNumber","depositCashTime","depositAmount","depositSite"};
	}
	
	@RequestMapping(value = "/do_import")
	@ResponseBody
	public ModelAndView doImport(@RequestParam("fileData") MultipartFile file, HttpServletRequest request)throws Exception{
    	ModelAndView mv = new ModelAndView("/print/import");
    	
    	List<UploadMessageModel> msgList = this.doUpload(file.getInputStream(), CashTransactionDtl.class, request);
    	StringBuilder errorBuilder = new StringBuilder();
    	if(msgList != null && msgList.size() > 0){
	    	for(UploadMessageModel message : msgList){
	    		errorBuilder.append(message.getMessage() + "<br/>");
	    	}
	    	String errorInfo = " 以下错误行信息导入失败  <br />" + errorBuilder;
	    	mv.addObject("error", errorInfo);
    	}else{
    		mv.addObject("success","成功导入");
    	}
    	return mv;
	}

	@Override
	protected boolean doBatchAdd(List<CashTransactionDtl> list) throws ManagerException {
		try {
			if(list != null && list.size() > 0){
				initializationShopAndCompany(list);
				
				return cashTransactionDtlManager.uploadListTransactionDtl(list);
			}else{
				return false;
			}
		} catch (ManagerException e) {
			logger.error(e.getMessage(), e);
			throw new ManagerException(e.getMessage(), e);
		}
	}
	
	public void initializationShopAndCompany(List<CashTransactionDtl> cashTransactionDtlList) throws ManagerException{
		for (CashTransactionDtl cashTransactionDtl : cashTransactionDtlList) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("depositAccount", cashTransactionDtl.getCardNumber());
			List<SelfShopDepositAccount> list = selfShopDepositAccountManager.findByBiz(null, params);
			List<String> str = new ArrayList<String>();
			for(SelfShopDepositAccount self:list){
				if(self.getMallName() != null && !str.contains(self.getMallName())){
					str.add(self.getMallName());
				}
			}
			cashTransactionDtl.setMallName(str.toString());
		}
	}

	@Override
	protected UploadMessageModel validateModel(CashTransactionDtl model, int rowIndex) throws ManagerException, IOException {
		UploadMessageModel messageModel = null;
		try {
			messageModel = basicValidation(model,rowIndex);
		} catch (ManagerException e) {
			logger.error(e.getMessage(), e);
			throw new ManagerException(e.getMessage(), e);
		}
		
		if(messageModel != null){
			return messageModel;
		}
		
		return super.validateModel(model, rowIndex);
	}
	
	
	public UploadMessageModel basicValidation(CashTransactionDtl transactionDtl,int rowIndex) throws ManagerException{
		
		UploadMessageModel uploadMessageModel = null;
		StringBuffer sbf = new StringBuffer();
		
		rowIndex = rowIndex + 1;
		String cardNumber = transactionDtl.getCardNumber() != null ? transactionDtl.getCardNumber().trim() : null;
		BigDecimal depositAmount = transactionDtl.getDepositAmount();
		Date depositTime = transactionDtl.getDepositCashTime();
		
		if(cardNumber != null && !"".equals(cardNumber)){
		
			Pattern p = Pattern.compile("[0-9]{16,19}");
	        //进行匹配，并将匹配结果放在Matcher对象中
	        Matcher m = p.matcher(cardNumber);
	        if(!m.matches()){
//	        	sbf.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;").append("<br />");
	        	sbf.append(" 第"+rowIndex+"行 账号不合法").append("<br />");
	        }else{
	        	
	        	boolean flag = checkDepositCardExist(transactionDtl);
	        	if(!flag){
		        	sbf.append(" 第"+rowIndex+"行 存现账号没有在店铺存现账号管理中维护").append("<br />");
	        	}
	        }
		}else{
			sbf.append(" 第"+rowIndex+"行 账号不能为空").append("<br />");
		}
		
		if(depositAmount != null){
			
			if(depositAmount.compareTo(new BigDecimal(0)) <= 0){
//				sbf.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
	        	sbf.append(" 第"+rowIndex+"行请正确录入存现金额").append("<br />");
			}
			
		}else{
			sbf.append(" 第"+rowIndex+"行 存现金额不能为空").append("<br />");
		}
		
		if(depositTime != null && !"".equals(depositTime)){
			
		}else{
			sbf.append(" 第"+rowIndex+"行 存现日期不能为空").append("<br />");
		}
		
		if(!"".equals(sbf.toString())){
			uploadMessageModel = new UploadMessageModel();
			uploadMessageModel.setFlag(false);
			uploadMessageModel.setMessage(sbf.toString());
		}
		return uploadMessageModel;
	}
	
	//检查导入的存现账号是否已经维护
	public boolean checkDepositCardExist(CashTransactionDtl transactionDtl) throws ManagerException{
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("creditCardAccount", transactionDtl.getCardNumber());
		
		int count = selfShopDepositAccountManager.findCount(params);
		
		if(count > 0){
			return true;
		}
		return false;
	}
	
}