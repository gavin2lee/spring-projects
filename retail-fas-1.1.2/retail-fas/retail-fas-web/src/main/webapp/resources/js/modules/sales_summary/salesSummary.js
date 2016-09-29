var salesSummary = new Object();

//当前用户
salesSummary.currentUser;

// 主表模块路径
salesSummary.modulePath = BasePath + '/sales_summary';

//清空
salesSummary.clear = function() {
	$('#searchForm').form("clear");
	$('#searchForm').find("input[name!=balanceType]").val("");
};

//查询
salesSummary.search = function() {
	var fromObj = $('#searchForm');
	
	var validateForm = fromObj.form('validate');
	// 1.校验必填项
	if (validateForm == false) {
		return;
	}
	var params = $('#searchForm').form('getData');
	var businessType = $("#businessType").combobox("getValues");
	if(businessType){
		params.businessType = businessType.join();
    }
	var url = salesSummary.modulePath + '/getSalesSummary';
    $('#dtlDataGrid').datagrid('options').queryParams= params;
    $('#dtlDataGrid').datagrid('options').url= url;
    $('#dtlDataGrid').datagrid('load');
};
//导出销售分类汇总信息
salesSummary.exportTotal = function() {
	fas_common.exportExcel({
		dataGridId : "dtlDataGrid",
		url : "/sales_summary/do_fas_export",
		title : "销售分类汇总信息"
	});
};

// 初始化
$(function(){
	var businessTypeArray = 
		[
		 {'value' : '1' , 'text' : '门店'},
		 {'value' : '2' , 'text' : '批发'},
		 {'value' : '3' , 'text' : '调货'},
		 {'value' : '4' , 'text' : '内购'},
		 {'value' : '99' , 'text' : '其他'}
       ];
	$('#businessType').combobox({
		data : businessTypeArray,
		valueField : 'value',
		textField : 'text'
	});
	var date = new Date();
	var year = date.getFullYear();
	var month = date.getMonth() + 1 > 9 ? date.getMonth() + 1 : '0' + (date.getMonth() + 1);
	$('#saleStartDate').datebox('setValue', year + '-' + month + '-01');
	var  day = new Date(year,month,0);
	$('#saleEndDate').datebox('setValue', year + '-' + month + '-' + day.getDate());
	
	//绑定店铺通用查询
//	$("#shopName").filterbuilder({
//        type:'organ',
//        organFlag: 2,
//        roleType:'bizCity', 
//        onSave : function(result) { 
//        	var value = $(this).filterbuilder('getValue');
//        	$("#shopNo").val(value);
//        }
//    });
});
