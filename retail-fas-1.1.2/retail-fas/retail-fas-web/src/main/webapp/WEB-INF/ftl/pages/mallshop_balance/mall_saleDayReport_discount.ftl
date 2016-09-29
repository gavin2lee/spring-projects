<div class="easyui-layout" data-options="fit:true">
	<div data-options="region:'center',border:false">
		<@p.datagrid id="dataGridDiv_discount"  loadUrl="" saveUrl=""   defaultColumn="" title=""
			isHasToolBar="false" onClickRowEdit="false" pagination="true" pageSize="20" rownumbers="true" 
			showFooter="true"
			rowStyler="function(index,row){
					     if (row.shop_name.indexOf('小计') != -1){
					         return 'background-color:#FFFFE0;';
					     }
					  }"
			frozenColumns="[
				{field:'shop_no',title:'店铺编码',width:80,sortable:true,align:'center'},
				{field:'shop_name',title:'店铺名称',width:150,sortable:true,align:'left',halign:'center'},
				{field:'out_date',title : '销售日期',width : 120,align:'center'},
				{field:'brand_no',title:'品牌编号',width:80,sortable:true,hidden:true},
				{field:'brand_name',title:'品牌名称',width:80,sortable:true},
				{field:'discount_code',title:'活动代码',width:60,sortable:true},
				{field:'discount',title:'扣率',width:50,sortable:true,align:'right',halign:'center',exportType:'number'},
				{field:'amount',title:'销售金额',width:80,align:'right',halign:'center',exportType:'number'}
			]"
        />
	</div>
</div>