package cn.wonhigh.retail.fas.web.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.wonhigh.retail.fas.common.model.RegionPriceRule;
import cn.wonhigh.retail.fas.manager.RegionPriceRuleManager;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.exception.ManagerException;

/**
 * 请写出类的用途 
 * @author wang.xy1
 * @date  2014-09-01 09:25:14
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
@RequestMapping("/region_price_rule")
@ModuleVerify("30120014")
public class RegionPriceRuleController extends BaseController<RegionPriceRule> {
    @Resource
    private RegionPriceRuleManager regionPriceRuleManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("region_price_rule/",regionPriceRuleManager);
    }
    
    @RequestMapping(value = "/check_rule_refered.json")
	public ResponseEntity<Integer> checkRuleRefered(HttpServletRequest req,Model model) throws ManagerException {
		
    	Map<String, Object> params = builderParams(req, model);
		
		int total= regionPriceRuleManager.checkIsRuleRefered(params);
		
		return new ResponseEntity<Integer>(total, HttpStatus.OK);
	}
    
    /**
     * 校验加价规则唯一性
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return 总部价格维护对象
     * @throws ManagerException 异常
     */
    @RequestMapping(value = "/get_unique_count.json")
	public ResponseEntity<Integer> getCheckUniqueCount(HttpServletRequest req,Model model)throws ManagerException{
		Map<String, Object> params = builderParams(req, model);
		int total= regionPriceRuleManager.findCount(params);
		return new ResponseEntity<Integer>(total, HttpStatus.OK);
	}
}