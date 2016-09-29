package cn.wonhigh.retail.fas.web.controller;

import cn.wonhigh.retail.fas.common.model.TicketCollectDtl;
import cn.wonhigh.retail.fas.manager.TicketCollectDtlManager;
import com.yougou.logistics.base.web.controller.BaseCrudController;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 请写出类的用途 
 * @author yu.y
 * @date  2014-11-13 15:44:38
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
@RequestMapping("/ticket_collect_dtl")
public class TicketCollectDtlController extends BaseCrudController<TicketCollectDtl> {
    @Resource
    private TicketCollectDtlManager ticketCollectDtlManager;

    @Override
    public CrudInfo init() {
        return new CrudInfo("ticket_collect_dtl/",ticketCollectDtlManager);
    }
}