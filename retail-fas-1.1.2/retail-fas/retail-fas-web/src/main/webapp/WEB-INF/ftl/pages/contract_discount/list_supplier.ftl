<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<title>供应商合同折扣</title>
<#include  "/WEB-INF/ftl/common/header.ftl" >
<script type="text/javascript" src="${resourcesUrl}/fas/resources/js/modules/common_util/pe_util.js?version=${version}"></script>
<script type="text/javascript" src="${resourcesUrl}/fas/resources/js/modules/contract_discount/contractDiscountSupplier.js?version=${version}"></script>
</head>
<body class="easyui-layout">
	<div data-options="region:'north',border:false" class="toolbar-region">
	     <@p.toolbar id="toolbar" listData=[
			 {"id":"btn-search","title":"查询","iconCls":"icon-search","action":"contractDiscountSupplier.searchData()", "type":0},
             {"id":"btn-remove","title":"清空","iconCls":"icon-empty","action":"contractDiscountSupplier.searchClear()", "type":0},
             {"id":"btn-add","title":"新增","iconCls":"icon-add","action":"contractDiscountSupplier.add()", "type":1},
             {"id":"btn-del","title":"删除","iconCls":"icon-del","action":"contractDiscountSupplier.del()", "type":3},
             {"id":"btn-save","title":"保存","iconCls":"icon-save","action":"contractDiscountSupplier.save()", "type":7},
             {"id":"btn-import","title":"导入","iconCls":"icon-export","action":"pe_util.doImport('供应商合同折扣设置.xlsx','/contract_discount/do_import?type=1',16,pe_util.importCallBack)","type":6},
             {"id":"btn-export","title":"导出","iconCls":"icon-export","action":"pe_util.doExport('dtlDataGrid', '/contract_discount/export_data', '供应商合同折扣')","type":4}
           ]
		/>
	</div>

	<div  data-options="region:'center',border:false">
    	<div class="easyui-layout" data-options="fit:true">
			<!--搜索start-->
			<div data-options="region:'north',border:false" >
				<div class="search-div">
			      	<form name="searchForm" id="searchForm" method="post">
			      		<input type="hidden" name="contractDiscountType" value="1" id="contractDiscountType"/>
						<table class="form-tb">
							<col width="80" />
							<col />
							<col width="80" />
							<col />
							<col width="80" />
							<col />
							<col width="80" />
							<col />
							<tbody>
								<tr>
									<th>公司： </th>
									<td><input class="ipt" multiSearch="company"  /><input type="hidden" name="multiBuyerNo"></td>
									<th>供应商： </th>
									<td><input class="ipt" multiSearch="supplier"  /><input type="hidden" name="multiSalerNo"></td>
									<th>品牌： </th>
									<td><input class="ipt" multiSearch="brand"  /><input type="hidden" name="multiBrandNo"></td>
									<th>大类： </th> 
									<td><input class="ipt" multiSearch="category"  /><input type="hidden" name="multiCategoryNo"></td>
								</tr>
								<tr>
									<th>生效日： </th>
									<td ><input class="easyui-datebox ipt" name="effectiveDateStart" id="effectiveDateStart" data-options="maxDate:'effectiveDateEnd'"/></td>
									<th>&nbsp;&nbsp;&nbsp;&nbsp;- &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
									<td> <input class="easyui-datebox ipt" name="effectiveDateEnd" id="effectiveDateEnd"  data-options="minDate:'effectiveDateStart'"/></td>
								</tr>
							</tbody>
						</table>
					</form>
				</div>
			</div>
			<!--列表-->
        	<div data-options="region:'center',border:false">
		      <@p.datagrid id="dtlDataGrid"  loadUrl="" saveUrl=""   defaultColumn=""   rownumbers="true"
		              isHasToolBar="false"    onClickRowEdit="false" pagination="true"  emptyMsg = ""  pageSize="20" 
			           columnsJsonList="[
			           		  {field : 't', checkbox:true, width : 30, notexport:true,},
			           		  {field : 'buyerNo', title : '公司编号', hidden:'true'},
			           		  {field : 'salerNo', title : '供应商编号', hidden:'true'},
			           		  {field : 'brandNo', title : '品牌', width : 100, hidden:'true'},
			                  {field : 'categoryNo', title : '大类', width : 100, hidden:'true'},
			           		  {field : 'buyerName', title : '公司', width : 240},
			                  {field : 'salerName', title : '供应商', width : 240},
			                  {field : 'orderType', title : '订货类型', width : 100},
			                  {field : 'brandName', title : '品牌', width : 100},
			                  {field : 'categoryName', title : '大类', width : 100},
			                  {field : 'priceBasis', title : '定价依据', width : 120},
			                  {field : 'discount1', title : '折扣1', width : 100},
			                  {field : 'discount2', title : '折扣2', width : 100},
			                  {field : 'algorithm', title : '算法(含税单价)', width : 100 },
			                  {field : 'algorithmDesc', title : '算法描述(含税单价)', width : 180},
			                  {field : 'notTaxAlgorithm', title : '算法(不含税金额)', width : 120 },
			                  {field : 'notTaxAlgorithmDesc', title : '算法描述(不含税金额)', width : 180},
			                  {field : 'effectiveDate', title : '生效日', width : 120},
			                  {field : 'createUser', title : '创建人', width : 80},
			                  {field : 'createTime', title : '创建时间', width : 150}]" 
			                  jsonExtend='{
	                           onDblClickRow:function(rowIndex, rowData){
			                	  //双击方法
			                   	   contractDiscountSupplier.edit(rowIndex, rowData);
			              		}}'
                 />
			</div>
	 	</div>
	</div>
	<div id="myFormPanel" class="easyui-dialog" data-options="closed:true"> 
		<@p.datagrid id="importDataGrid"  loadUrl="" saveUrl=""  defaultColumn=""  
              isHasToolBar="false"    onClickRowEdit="false" pagination="false"  emptyMsg = ""
	           columnsJsonList="[
	                 {field : 'index', title : '行号', width : 30},
	           		 {field : 'pass',title:'是否导入成功',width:100,formatter: function(value,row,index){
							if (value == 0){
								return '否';
							}
							return '是'
						}
			         },
			         {field : 'errorInfo',title:'错误信息',width:200,formatter : pe_util.showTips},
                     {field : 'validateObj.buyerNo', title : '公司编码', width : 100},
                     {field : 'validateObj.salerNo', title : '供应商编码', width : 100},
                     {field : 'validateObj.orderType', title : '订货类型', width : 150},	
	 				 {field : 'validateObj.brandNo',title:'品牌编码',width:100},
	 				 {field : 'validateObj.categoryNo',title:'大类编码',width:100,align:'right'},
	 				 {field : 'validateObj.priceBasis',title:'定价依据',width:100,align:'right'},
	 				 {field : 'validateObj.discount1',title:'折扣1',width:100},
	 				 {field : 'validateObj.discount2',title:'折扣2',width:100,align:'right'},
	 				 {field : 'validateObj.algorithm',title:'采购价算法',width:100,align:'right'},
	 				 {field : 'validateObj.effectiveDate',title:'生效日',width:100,align:'right'}]"	
         />
     </div>	
</body>
</html>