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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wonhigh.retail.fas.common.model.CompanyBrandSettlePeriod;
import cn.wonhigh.retail.fas.common.model.CompanySettlePeriod;
import cn.wonhigh.retail.fas.common.utils.FasUtil;
import cn.wonhigh.retail.fas.manager.CompanySettlePeriodManager;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.utils.SimplePage;

/**
 * 请写出类的用途 
 * @author wang.xy1
 * @date  2014-08-28 09:02:52
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
@RequestMapping("/company_settle_period")
@ModuleVerify("30120003")
public class CompanySettlePeriodController extends BaseController<CompanySettlePeriod> {
	
    @Resource
    private CompanySettlePeriodManager companySettlePeriodManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("company_settle_period/",companySettlePeriodManager);
    }
    
    @ResponseBody
    public Map<String, Object> queryList(HttpServletRequest req, Model model) throws ManagerException
    {
            int pageNo = StringUtils.isEmpty(req.getParameter("page")) ? 1 : Integer.parseInt(req.getParameter("page"));
            int pageSize = StringUtils.isEmpty(req.getParameter("rows")) ? 10 : Integer.parseInt(req.getParameter("rows"));
            String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
            String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
            Map<String, Object> params = builderParams(req, model);
            if (StringUtils.isNotEmpty(req.getParameter("companyNos"))) {
    			params.put("companyNos",FasUtil.formatInQueryCondition(req.getParameter("companyNos").toString()));
    		}
            int total = companySettlePeriodManager.findCount(params);
            SimplePage page = new SimplePage(pageNo, pageSize, total);
            List<CompanyBrandSettlePeriod> list = companySettlePeriodManager.findByPage(page, sortColumn, sortOrder, params);
            Map<String, Object> obj = new HashMap<String, Object>();
            obj.put("total", Integer.valueOf(total));
            obj.put("rows", list);
            return obj;
    }
    
    @SuppressWarnings("rawtypes")
	@RequestMapping("/validate_data")
	@ResponseBody
	public Map<String, Object> validateData(HttpServletRequest req) throws ManagerException, JsonParseException, JsonMappingException, IOException {

		Map<String, Object> result = new HashMap<String, Object>();
		
//		String upadtedList = StringUtils.isEmpty(req.getParameter("updated")) ? "" : req.getParameter("updated");
		String insertedList = StringUtils.isEmpty(req.getParameter("inserted")) ? "" : req.getParameter("inserted");
		ObjectMapper mapper = new ObjectMapper();
		List<CompanySettlePeriod> checkList = new ArrayList<CompanySettlePeriod>();

//		if (StringUtils.isNotEmpty(upadtedList)) {
//			List<Map> list = mapper.readValue(upadtedList, new TypeReference<List<Map>>(){});
//			checkList.addAll(convertCompanySettlePeriod(mapper,list));
//		}
		if (StringUtils.isNotEmpty(insertedList)) {
			List<Map> list = mapper.readValue(insertedList, new TypeReference<List<Map>>(){});
			checkList.addAll(convertCompanySettlePeriod(mapper,list));
		}
		if (!CollectionUtils.isEmpty(checkList)) {
			
			Map<String, String> duplicateMap = new HashMap<String, String>();
			Map<String, Object> checkParams = null;
			int count = 0;
			
			boolean duplicate = false;
			String duplicateShopGroup = "";
			
			for (CompanySettlePeriod settlePeriod : checkList) {
				
				checkParams = new HashMap<String, Object>();
				checkParams.put("companyNo", settlePeriod.getCompanyNo());
				count = this.companySettlePeriodManager.findCount(checkParams);
				if (count > 0) {
					duplicate = true;
					duplicateShopGroup = settlePeriod.getCompanyName();
					break;
				}
				
				if (duplicateMap.containsKey(settlePeriod.getCompanyNo())) {
					duplicate = true;
					duplicateShopGroup = settlePeriod.getCompanyName();
					break;
				}else {
					duplicateMap.put(settlePeriod.getCompanyNo(), settlePeriod.getCompanyName());
				}
			}
			
			if (duplicate) {
				result.put("success", false);
				result.put("message", "公司[" + duplicateShopGroup + "]已经维护了结账日！");
				return result;
			}
		}
		
		result.put("success", true);
		return result;
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
	private List<CompanySettlePeriod> convertCompanySettlePeriod(ObjectMapper mapper,List<Map> list) throws JsonParseException, JsonMappingException, JsonGenerationException, IOException{
		Class<CompanySettlePeriod> entityClass = (Class<CompanySettlePeriod>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]; 
		List<CompanySettlePeriod> tl=new ArrayList<CompanySettlePeriod>(list.size());
		for (int i = 0; i < list.size(); i++) {
			CompanySettlePeriod type=mapper.readValue(mapper.writeValueAsString(list.get(i)),entityClass);
			tl.add(type);
		}
		return tl;
	}
	
}