<div class="easyui-layout" data-options="fit:true,plain:true">
	<div data-options="region:'center',border:false">
	   <@p.datagrid id="returnDtl"  
		loadUrl="" saveUrl=""   defaultColumn="" 
 		 isHasToolBar="false" onClickRowEdit="false" pagination="true" selectOnCheck="true" 
  		 checkOnSelect="true"  rownumbers="true" singleSelect="true"  showFooter="true"
			columnsJsonList="[
				{field : 'billNo',title : '单据编号',width : 150,align:'left',halign:'center',
			    	formatter:function(value,row){
              		if(row.buyerName=='合计'){
              			return '合计';
              		}	
              		    return value;
              		}},
			    {field : 'brandUnitName', title : '品牌部', width : 80,align:'center'},
				{field : 'brandName',title : '品牌',width : 100,align:'center'},
				{field : 'oneLevelCategoryName', title : '大类', width : 60,align:'center'},	
			    {field : 'twoLevelCategoryName', title : '二级大类', width : 60,align:'center'},	
				{field : 'itemCode',title : '商品编码',width : 150,align:'left',halign:'center'},	
				{field : 'itemName',title : '商品名称',width : 180,align:'left',halign:'center'},	
				{field : 'cost',title : '单价',width : 80,align:'right',halign:'center'},
				{field : 'sendQty',title : '数量',width : 80,align:'right',halign:'center'},				
				{field : 'sendAmount',title : '金额',width : 100,align:'right',halign:'center'},
				{field : 'sendDate',title : '单据日期',width : 100,align:'center'},	
				{field : 'orderUnitName', title : '货管单位', width : 80,align:'center',halign:'center'},
			    {field : 'receiveStoreName', title : '退货仓', width : 120,align:'left',halign:'center'},
			    {field : 'organName', title : '管理城市', width : 80,align:'center',halign:'center'},
				{field : 'billTypeName',title : '单据类型',width : 100,align:'center'}
			]" 
		/>
		</div>
</div>
