var report_inventory = new Object();

//查询
report_inventory.search = function() {
	if($('#searchForm').form('validate')){
		var params = $('#searchForm').form('getData');
		var url = BasePath + '/report/report_inventory.json';
	    $('#dtlDataGrid').datagrid('options').queryParams= params;
	    $('#dtlDataGrid').datagrid('options').url= url;
	    $('#dtlDataGrid').datagrid('load');
	}
};

//清空
report_inventory.clear = function() {
	$('#searchForm').form("clear");
	common_util.initDate(); 
};

//初始化
$(function(){
	toolSearch({
        appendTo:$('#top_bar'), //添加位置，默认为$('#toolbar')
        target:$('#subLayout'), //控制对象，默认为$('#subLayout')
        collapsible:true //是否显示下拉箭头
	});
});