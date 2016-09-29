package cn.wonhigh.retail.fas.web.controller;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wonhigh.retail.fas.common.model.AssistProjectType;
import cn.wonhigh.retail.fas.manager.AssistProjectManager;
import cn.wonhigh.retail.fas.manager.AssistProjectTypeManager;

import com.yougou.logistics.base.common.annotation.ModuleVerify;
import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;


/**
 * 辅助项目类型
 * @author ouyang.zm
 * @date  2014-08-27 09:17:07
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
@RequestMapping("/base_setting/assist_project_type")
@ModuleVerify("30100002")
public class AssistProjectTypeController extends BaseController<AssistProjectType> {
    @Resource
    private AssistProjectTypeManager assistProjectTypeManager;
    @Resource
    private AssistProjectManager assistProjectManager;
    
    @Override
    public CrudInfo init() {
        return new CrudInfo("base_setting/assist_project_type/",assistProjectTypeManager);
    }
    
    /**
     * 校验编码是否重复
     * @param financialAccount
     * @return
     * @throws ManagerException 
     */
    @ResponseBody
    @RequestMapping(value="/check_Repect", method=RequestMethod.POST)
    public boolean checkrepeatData(@ModelAttribute("model") AssistProjectType model) throws ManagerException{
    	Map<String,Object> params=new HashMap<String, Object>();
    	String queryCondition = "AND ( A.TYPE_CODE = '"+model.getTypeCode()+"'  OR A.TYPE_NAME = '"+model.getTypeName()+"')";
    	params.put("queryCondition", queryCondition);
    	List<AssistProjectType> list=assistProjectTypeManager.findByBiz(model, params);
    	if (list != null && list.size() > 0) {
			for (AssistProjectType m : list) {
				if (!m.getId().equals(model.getId())) {
					return true;
				}
			}
		}
		return false;
    }
    
    /**
     * 查询是否包含辅助项目
     * @param codeList
     * @return
     */
    @RequestMapping("/searchData")
    @ResponseBody
    public Integer searchData(@RequestParam("codeList")String[] codeList){
    	int i=0;
    	i=assistProjectManager.findAssistProjects(codeList);
    	if(i>0){
    		return i;
    	}
		return i;
    }
    
    /**
     * 删除辅助项目类型
     * @param codeList
     * @return
     * @throws ServiceException 
     */
    @RequestMapping("/delData")
    @ResponseBody
    public Integer removeData(@RequestParam("codeList")String[] codeList) throws ServiceException{
    	int i=0;
    	i=assistProjectTypeManager.removeAssistProjectType(codeList);
    	if(i>0){
    		return i;
    	}
		return i;
    }
    
}