/**  
* 项目名称：retail-fas-web  
* 类名称：AreaPrivatePurchaseBalanceController  
* 类描述：地区自购结算
* 创建人：ouyang.zm  
* 创建时间：2014-9-25 下午6:00:34  
* @version       
*/ 
package cn.wonhigh.retail.fas.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.wonhigh.retail.fas.common.enums.AreaPurchaseBalanceStatusEnums;
import cn.wonhigh.retail.fas.common.enums.OperateLogEnums.OperateModule;
import cn.wonhigh.retail.fas.common.model.BillBalance;
import cn.wonhigh.retail.fas.common.model.BillBuyBalance;
import cn.wonhigh.retail.fas.common.model.BillSaleBalance;
import cn.wonhigh.retail.fas.common.model.OtherDeduction;
import cn.wonhigh.retail.fas.common.utils.DateUtil;
import cn.wonhigh.retail.fas.manager.AreaPrivatePurchaseBalanceManager;
import cn.wonhigh.retail.fas.manager.AreaPrivatePurchaseManager;
import cn.wonhigh.retail.fas.web.utils.AreaPurchaseeExportUtils;
import cn.wonhigh.retail.fas.web.utils.HqInsteadOfBuyExportUtils;
import cn.wonhigh.retail.fas.web.utils.OperateLogHelper;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.contains.PublicContains;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.model.SystemUser;
import com.yougou.logistics.base.common.utils.SimplePage;
import com.yougou.logistics.base.web.controller.BaseCrudController;

@Controller
@RequestMapping("/area_private_purchase_balance")
@ModuleVerify("30134003")
public class AreaPrivatePurchaseBalanceController extends BaseCrudController<BillBalance> {
	@Resource
	private AreaPrivatePurchaseManager areaPrivatePurchaseManager;
	@Resource
	private AreaPrivatePurchaseBalanceManager areaPrivatePurchaseBalanceManager;
	@Resource
	private	OperateLogHelper operateLogHelper;
	@Override
	public CrudInfo init() {
		return new CrudInfo("area_private_purchase_balance/", areaPrivatePurchaseBalanceManager);
	}

	/**
	 * 单据明细
	 * @return
	 */
	@RequestMapping(value = "/balance_bill")
	public ModelAndView listTab(HttpServletRequest req) {
		ModelAndView mav = new ModelAndView();
		String billNoMenu = req.getParameter("billNoMenu");
		if(StringUtils.isNotBlank(billNoMenu)){
			mav.addObject("billNoMenu", billNoMenu);
		}
		String warnPostUrl = req.getParameter("warnPostUrl");
		if(StringUtils.isNotBlank(warnPostUrl)){
			mav.addObject("warnPostUrl", warnPostUrl);
		}
		mav.setViewName("area_purchase_balance/list");
		return mav;
	}

	/**
	 * 单据列表
	 * @return
	 */
	@RequestMapping(value = "/list_tabMain")
	public String mainTab() {
		return "area_purchase_balance/list_tabMain";
	}
	
	/**
	 * 入库明细tab页
	 * @return
	 */
	@RequestMapping(value = "/transferInTab")
	public String toStorageList() {
		return "area_purchase_balance/storage_dtl";
	}
	
	/**
	 * 请款信息tab页
	 * @return
	 */
	@RequestMapping(value = "/paymentInfo")
	public String toPaymentInfoList() {
		return "area_purchase_balance/payment_info";

	}
	
	/**
	 * 其他扣项tab页
	 * @return
	 */
	@RequestMapping(value = "/otherDeduction")
	public String toOtherDeduction() {
		return "area_purchase_balance/deduction_info";
		
	}

	/**
	 * 发票信息tab页
	 * @return
	 */
	@RequestMapping(value = "/invoiceInfo")
	public String toInvoiceInfoList() {
		return "area_purchase_balance/invoice_info";

	}
	
	/**
	 * 查询
	 */
	@Override
	@RequestMapping(value = "/list.json")
	@ResponseBody
	public Map<String, Object> queryList(HttpServletRequest req, Model model)
			throws ManagerException {
		Map<String, Object> obj = super.queryList(req, model);
		String balanceType = StringUtils.isEmpty(req.getParameter("balanceType")) ? "": String.valueOf(req.getParameter("balanceType"));
		Map<String, Object> params = builderParams(req, model);
		params.put("balanceType", balanceType);
		List<BillBalance> totalList=areaPrivatePurchaseBalanceManager.findTotalRow(params);
		if(totalList.size()>0){
			obj.put("footer", totalList);
		}
		return obj;
	}
	
	/**
	 * 判断是否能生成结算单
	 * @throws ManagerException 
	 * @throws Exception 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@RequestMapping("check_data")
	@ResponseBody
	public ResponseEntity<BillBalance> getAmount(HttpServletRequest req,BillBalance formData) throws ManagerException, JsonParseException, JsonMappingException, Exception{
		BillBalance billBalance =areaPrivatePurchaseBalanceManager.findBalanceBill(formData);
		if(billBalance == null) {
			billBalance = new BillBalance();
		}
		return new ResponseEntity<BillBalance>(billBalance, HttpStatus.OK);
	}
	
    /**
     * 生成结算单
     * @throws Exception 
     * @throws IOException 
     * @throws JsonMappingException 
     */
	@RequestMapping(value = "/save_bill")
	@ResponseBody
	public ResponseEntity<BillBalance> add(HttpServletRequest req,BillBalance billBalance)throws JsonMappingException, IOException, Exception {
		SystemUser loginUser = (SystemUser) req.getSession().getAttribute(PublicContains.SESSION_USER);//获取登录用户
		billBalance.setCreateUser(loginUser.getUsername());
		billBalance =areaPrivatePurchaseBalanceManager.addBalanceBill(billBalance);
		if(null != billBalance){
			operateLogHelper.addVerifyPost(req, billBalance.getBillNo(), OperateModule.JSD.getModuleNo(), new Integer(0),
					"制单", "制单");//添加日志
			billBalance = areaPrivatePurchaseBalanceManager.findById(billBalance);
		}
		return new ResponseEntity<BillBalance>(billBalance, HttpStatus.OK);
	}
	
	/**
	 * 跳转到选单关联界面
	 */
	@RequestMapping("/to_select_bill")
	public ModelAndView toSelectBill(HttpServletRequest req,Model model) {
		String params = StringUtils.isEmpty(req.getParameter("params")) ? "" : String.valueOf(req.getParameter("params"));
		ModelAndView view=new ModelAndView();
		view.setViewName("area_purchase_balance/select_bill");
		view.addObject("params", params);
		return view;
	}
	
	/**
     * 生成结算单(选单)
     */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/select_save_bill")
	@ResponseBody
	public Map<String,Object> addByCreate(HttpServletRequest req,BillBalance billBalance)throws Exception {
		SystemUser loginUser = (SystemUser) req.getSession().getAttribute(PublicContains.SESSION_USER);//获取登录用户
		billBalance.setCreateUser(loginUser.getUsername());
		ObjectMapper mapper = new ObjectMapper();
		Map<String,Object> resultMap=new HashMap<String, Object>();
		if (StringUtils.isNotBlank(req.getParameter("checkedRows"))) {
			List<Map> outList = mapper.readValue(req.getParameter("checkedRows"), new TypeReference<List<Map>>() {});
			List<Object> lstItem = convertListWithTypeReference(mapper, outList, BillBuyBalance.class);
			resultMap = areaPrivatePurchaseBalanceManager.addBalanceBillBySelect(lstItem, billBalance);
		}
		BillBalance bill=(BillBalance) resultMap.get("bill"); 
		if (bill != null) {//添加日志
			operateLogHelper.addVerifyPost(req, bill.getBillNo(), OperateModule.JSD.getModuleNo(), new Integer(0),
					"制单", "制单");
		}
		return resultMap;
	}
	
	/**
	 * 扣项调整
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/deduction_adjust")
	@ResponseBody
	public ResponseEntity<BillBalance> deductionAdjust(HttpServletRequest req,BillBalance billBalance)throws Exception {
		SystemUser loginUser = (SystemUser) req.getSession().getAttribute(PublicContains.SESSION_USER);//获取登录用户
		billBalance.setCreateUser(loginUser.getUsername());
		ObjectMapper mapper = new ObjectMapper();
		BillBalance b = new BillBalance();
		if (StringUtils.isNotBlank(req.getParameter("checkedRows"))) {
			List<Map> outList = mapper.readValue(req.getParameter("checkedRows"), new TypeReference<List<Map>>() {
			});
			List<Object> lstItem = convertListWithTypeReference(mapper, outList, OtherDeduction.class);
			b = areaPrivatePurchaseBalanceManager.modifyDeducation(lstItem, billBalance);
			super.moditfy(b);
		}
		if (null != b) {
			b = areaPrivatePurchaseBalanceManager.findById(b);
		}
		return new ResponseEntity<BillBalance>(b, HttpStatus.OK);
	}
	
	/**
     * 批量生成结算单
	 * @throws Exception 
     */
	@RequestMapping(value = "/batch_save_bill")
	@ResponseBody
	public Map<String,Object> batchSave(HttpServletRequest req,BillBalance fromPage)throws ManagerException, Exception {
		SystemUser loginUser = (SystemUser) req.getSession().getAttribute(PublicContains.SESSION_USER);// 获取登录用户
		String salerNo = fromPage.getSalerNo();
		String buyerNo = fromPage.getBuyerNo();
		BillBalance balance = new BillBalance();
		balance.setCreateUser(loginUser.getUsername());
		balance.setSalerNo(salerNo); //卖方
		balance.setBuyerNo(buyerNo);//买方
		balance.setBalanceStartDate(fromPage.getBalanceStartDate());
		balance.setBalanceEndDate(fromPage.getBalanceEndDate());
		balance.setBalanceDate(fromPage.getBalanceDate());
		balance.setBrandUnitNo(fromPage.getBrandUnitNo()); //品牌部
		balance.setBrandUnitName(fromPage.getBrandUnitName());
		Map<String,Object> resultMap = areaPrivatePurchaseBalanceManager.addBalanceBillByBatch(balance);
		String dataNo = resultMap.get("billNos").toString()
				.substring(1, resultMap.get("billNos").toString().length() - 1);
		operateLogHelper.addVerifyPost(req, dataNo,OperateModule.JSD.getModuleNo(), new Integer(0),
				"制单", "制单");
		return resultMap;
	}
	
	/**
	 * 修改结算单
	 * @throws Exception 
	 * @throws IOException 
	 * @throws JsonMappingException 
	 */
	@RequestMapping(value = "/update_bill")
	@ResponseBody
	public ResponseEntity<BillBalance> moditfy(HttpServletRequest req,BillBalance billBalance)
			throws JsonMappingException, IOException, Exception {
		SystemUser loginUser = (SystemUser) req.getSession().getAttribute(PublicContains.SESSION_USER);//获取登录用户
		billBalance.setUpdateUser(loginUser.getUsername());
		billBalance.setUpdateTime(DateUtil.getCurrentDateTime());
		areaPrivatePurchaseBalanceManager.modifyById(billBalance);
		if(null != billBalance){
			billBalance = areaPrivatePurchaseBalanceManager.findById(billBalance);
		}
		return new ResponseEntity<BillBalance>(billBalance, HttpStatus.OK);
	}
	
	/**
	 * 删除结算单
	 * @return
	 * @throws ManagerException  
	 */
	@RequestMapping(value = "/delete_bill")
	@ResponseBody
	public Integer removeBillBalance(@RequestParam("idList")String[] ids) 
			throws ManagerException {
		return areaPrivatePurchaseBalanceManager.deleteBalanceBill(ids);
	}
	
	/**
	 * 查询入库明细
	 * @param req
	 * @param model
	 * @return
	 * @throws ManagerException
	 */
	@RequestMapping(value = "/transferInList")
	@ResponseBody
	public Map<String, Object> getBillDtl(HttpServletRequest req,Model model)
			throws ManagerException {
		int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1: Integer.parseInt(req.getParameter("page"));
		int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10: Integer.parseInt(req.getParameter("rows"));
		String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "": String.valueOf(req.getParameter("sort"));
		String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "": String.valueOf(req.getParameter("order"));
		String balanceNo = StringUtils.isEmpty(req.getParameter("balanceNo")) ? "": String.valueOf(req.getParameter("balanceNo"));
		Map<String, Object> params = builderParams(req, model);
		params.put("balanceNo", balanceNo);//结算单编号
		int total = this.areaPrivatePurchaseManager.findCount(params);
		SimplePage page = new SimplePage(pageNo, pageSize, (int) total);
		List<BillBuyBalance> list = this.areaPrivatePurchaseManager.findByPage(page,sortColumn, sortOrder, params);
		List<BillBuyBalance> totalList=this.areaPrivatePurchaseManager.findTotalRow(params);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("total", total);
		obj.put("rows", list);
		if(totalList.size()>0){
			obj.put("footer", totalList);
		}
		return obj;
	}
	
	/**
	 *  批量业务、财务、打回操作
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/batch_audit")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> BatchAuditConfirm(HttpServletRequest req) throws Exception {
		SystemUser loginUser = (SystemUser) req.getSession().getAttribute(PublicContains.SESSION_USER);//获取登录用户
		String idLists = req.getParameter("idList");
		String[] ids=idLists.split(",");
		String operStatus = req.getParameter("status");
		Map<String, Object> map = new HashMap<String, Object>();
		String operStatusName = AreaPurchaseBalanceStatusEnums.getTypeName(Integer.parseInt(operStatus));
		map.put("statusName", operStatusName);
		map.put("status", operStatus);
		map.put("auditor", loginUser.getUsername());
		map.put("auditTime", DateUtil.getCurrentDateTime());
		areaPrivatePurchaseBalanceManager.modifyBillStatus(ids,map);
		operateLogHelper.addVerifyPost(req,idLists,OperateModule.JSD.getModuleNo(),Integer.parseInt(operStatus),operStatusName,operStatusName);
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}

	/**
	 * 单个业务、财务、打回操作
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/audit")
	@ResponseBody
	public ResponseEntity<BillBalance> AuditConfirm(HttpServletRequest req,@RequestParam("billNo") String[] billNo,@RequestParam("operStatus")String operStatus) throws Exception {
		SystemUser loginUser = (SystemUser) req.getSession().getAttribute(PublicContains.SESSION_USER);//获取登录用户
		Map<String, Object> map = new HashMap<String, Object>();
		String operstatusName = AreaPurchaseBalanceStatusEnums.getTypeName(Integer.parseInt(operStatus));
		map.put("statusName", operstatusName);
		map.put("status", operStatus);
		map.put("auditor", loginUser.getUsername());
		map.put("auditTime", DateUtil.getCurrentDateTime());
		areaPrivatePurchaseBalanceManager.modifyBillStatus(billNo,map);
		operateLogHelper.addVerifyPost(req, OperateModule.JSD.getModuleNo(),operstatusName,operstatusName);
		String no=billNo[0];
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("billNo", no);
		//查询结算单
		BillBalance balance=new BillBalance();
		List<BillBalance> list=areaPrivatePurchaseBalanceManager.findByBiz(null, params);
		balance=list.get(0);
		Integer statusNo=balance.getStatus();
		balance.setStatusName(AreaPurchaseBalanceStatusEnums.getTypeName(statusNo));
		return new ResponseEntity<BillBalance>(balance, HttpStatus.OK);
	}

	/**
	 * 导出
	 * @param req
	 * @param model
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/export")
	public void export(HttpServletRequest req,Model model, HttpServletResponse response) throws Exception{
		String fileName = req.getParameter("fileName");
		String exportColumns = req.getParameter("exportColumns");
		String formData = req.getParameter("formData");
//		int pageNumber = Integer.parseInt(req.getParameter("pageNumber") == null? "0" : req.getParameter("pageNumber"));
//		int pageSize = Integer.parseInt(req.getParameter("pageSize") == null? "0" : req.getParameter("pageSize"));
		String balanceNo = StringUtils.isEmpty(req.getParameter("balanceNo")) ? "": String.valueOf(req.getParameter("balanceNo"));
		Map<String, Object> params = builderParams(req, model);
		params.put("balanceNo", balanceNo);//结算单编号
		int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1: Integer.parseInt(req.getParameter("page"));
//		int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10: Integer.parseInt(req.getParameter("rows"));
		String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "": String.valueOf(req.getParameter("sort"));
		String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "": String.valueOf(req.getParameter("order"));
		int total = this.areaPrivatePurchaseManager.findCount(params);
		SimplePage page = new SimplePage(pageNo, total, (int) total);
		List<BillBuyBalance> dataList = this.areaPrivatePurchaseManager.findByPage(page,sortColumn, sortOrder, params);
		AreaPurchaseeExportUtils.doExport(formData,fileName, exportColumns, dataList, response,0);
	}
	
	/**
	 * 列表导出
	 * @param req
	 * @param model
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/list_export")
	public void listExport(HttpServletRequest req,Model model, HttpServletResponse response) throws Exception{
		String fileName = req.getParameter("fileName");
		String exportColumns = req.getParameter("exportColumns");
		int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
		String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
		String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
		Map<String, Object> params = builderParams(req, model);
		int total = this.areaPrivatePurchaseBalanceManager.findCount(params);
		SimplePage page = new SimplePage(pageNo, total, (int) total);
		List<BillSaleBalance> dataList = this.areaPrivatePurchaseBalanceManager.findByPage(page, sortColumn, sortOrder, params);
		AreaPurchaseeExportUtils.doExport(null,fileName, exportColumns, dataList, response,1);
	}
	
	/**
	 * 明细导出
	 * @param req
	 * @param model
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/dtl_export")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void dtlExport(HttpServletRequest req,Model model, HttpServletResponse response) throws Exception{
		String fileName = req.getParameter("fileName");
		String exportColumns = req.getParameter("exportColumns");
		String billNo = req.getParameter("billNos");
		String[] billNos=billNo.split(",");
		Map<String, Object> params = builderParams(req, model);
		List<Map> dataList=new ArrayList<Map>();
		for(String no:billNos){
			BillBalance billBalance=new BillBalance();
			Map<String, Object> map = new HashMap<String, Object>();
			params.clear();
			params.put("billNo", no);//结算单编号
			billBalance=(BillBalance) areaPrivatePurchaseBalanceManager.findByBiz(model, params).get(0);
			params.clear();
			params.put("balanceNo", no);
			int total = this.areaPrivatePurchaseManager.findCount(params);
			SimplePage page = new SimplePage(0, total, (int) total);
			List dtlList = this.areaPrivatePurchaseManager.findByPage(page,"", "", params);
			List<Map> dtlMapList = HqInsteadOfBuyExportUtils.getDataList(dtlList);
			map.put("bill", billBalance);
			map.put("dtlList", dtlMapList);
			dataList.add(map);
			
		}
		AreaPurchaseeExportUtils.doFasExport(fileName, exportColumns, dataList, response);
	}
	
	/**
	 * 转换成泛型列表
	 * @param mapper
	 * @param list
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 * @throws IOException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<Object> convertListWithTypeReference(ObjectMapper mapper, List<Map> list, Class clazz)
			throws JsonParseException, JsonMappingException, JsonGenerationException, IOException {
		List<Object> tl = new ArrayList<Object>(list.size());
		if(!CollectionUtils.isEmpty(list)){
			for (int i = 0; i < list.size(); i++) {
				Object type = mapper.readValue(mapper.writeValueAsString(list.get(i)), clazz);
				tl.add(type);
			}
		}
		return tl;
	}
}
