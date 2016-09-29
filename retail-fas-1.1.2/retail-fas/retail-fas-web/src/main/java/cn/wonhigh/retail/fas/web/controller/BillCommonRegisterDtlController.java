package cn.wonhigh.retail.fas.web.controller;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.wonhigh.retail.fas.common.model.BillCommonRegisterDtl;
import cn.wonhigh.retail.fas.manager.BillCommonRegisterDtlManager;

import com.yougou.logistics.base.common.enums.CommonOperatorEnum;
import com.yougou.logistics.base.common.exception.ManagerException;

/**
 * 请写出类的用途 
 * @author chen.mj
 * @date  2014-11-10 14:40:44
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
@RequestMapping("/bill_common_register_dtl")
public class BillCommonRegisterDtlController extends BaseController<BillCommonRegisterDtl> {
    @Resource
    private BillCommonRegisterDtlManager billCommonRegisterDtlManager;
    
    
   

    @Override
    public CrudInfo init() {
        return new CrudInfo("bill_common_register_dtl/",billCommonRegisterDtlManager);
    }
    
    /**
	 * 转换成泛型列表
	 * @param mapper
	 * @param list
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<BillCommonRegisterDtl> convertListWithTypeReference(ObjectMapper mapper,List<Map> list) throws JsonParseException, JsonMappingException, JsonGenerationException, IOException{
		Class<BillCommonRegisterDtl> entityClass = (Class<BillCommonRegisterDtl>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]; 
		List<BillCommonRegisterDtl> tl=new ArrayList<BillCommonRegisterDtl>(list.size());
		for (int i = 0; i < list.size(); i++) {
			BillCommonRegisterDtl type=mapper.readValue(mapper.writeValueAsString(list.get(i)),entityClass);
			tl.add(type);
		}
		return tl;
	}
	
    @SuppressWarnings("rawtypes")
	@RequestMapping(value = "/save")
	public ResponseEntity<Map<String, Boolean>> save(HttpServletRequest req) throws JsonParseException, JsonMappingException, IOException,
			ManagerException {
		Map<String, Boolean> flag = new HashMap<String, Boolean>();

		String deletedList = StringUtils.isEmpty(req.getParameter("deleted")) ? "" : req.getParameter("deleted");
		String upadtedList = StringUtils.isEmpty(req.getParameter("updated")) ? "" : req.getParameter("updated");
		String insertedList = StringUtils.isEmpty(req.getParameter("inserted")) ? "" : req.getParameter("inserted");
		ObjectMapper mapper = new ObjectMapper();
		Map<CommonOperatorEnum, List<BillCommonRegisterDtl>> params = new HashMap<CommonOperatorEnum, List<BillCommonRegisterDtl>>();
		if (StringUtils.isNotEmpty(deletedList)) {
			List<Map> list = mapper.readValue(deletedList, new TypeReference<List<Map>>(){});
			List<BillCommonRegisterDtl> oList=convertListWithTypeReference(mapper,list);
			params.put(CommonOperatorEnum.DELETED, oList);
		}
		if (StringUtils.isNotEmpty(upadtedList)) {
			List<Map> list = mapper.readValue(upadtedList, new TypeReference<List<Map>>(){});
			List<BillCommonRegisterDtl> oList=convertListWithTypeReference(mapper,list);
			params.put(CommonOperatorEnum.UPDATED, oList);
		}
		if (StringUtils.isNotEmpty(insertedList)) {
			List<Map> list = mapper.readValue(insertedList, new TypeReference<List<Map>>(){});
			List<BillCommonRegisterDtl> oList=convertListWithTypeReference(mapper,list);
			params.put(CommonOperatorEnum.INSERTED, oList);
		}
		
		if (params.size() > 0) {
			billCommonRegisterDtlManager.saveAll(params);
		}
		flag.put("success", true);
		return new ResponseEntity<Map<String, Boolean>>(flag, HttpStatus.OK);
	}
   
	/**
     * 根据发票单据号查询发票明细的记录数
     * @param req
     * @return 记录数 total
     * @throws ManagerException
     */
    @RequestMapping(value = "/getDtlTotalByBillNo")
	public ResponseEntity<Integer> getDtlTotalByBillNo(HttpServletRequest req) throws ManagerException {
		String billNo = req.getParameter("billNo") == null ? "" : req.getParameter("billNo");
		int total = 0;
		if(StringUtils.isNotBlank(billNo)){
			Map<String, Object> params = new HashMap<String,Object>();
			params.put("billNo", billNo);
			total = this.billCommonRegisterDtlManager.findCount(params);
		}
		return new ResponseEntity<Integer>(total, HttpStatus.OK);
	}
}