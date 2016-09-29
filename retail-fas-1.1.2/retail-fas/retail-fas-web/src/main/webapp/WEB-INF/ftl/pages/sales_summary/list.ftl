<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<title>分类汇总表</title>
<#include  "/WEB-INF/ftl/common/header.ftl" >
<script type="text/javascript" src="${resourcesUrl}/fas/resources/js/common/fas.2.0.js?version=${version}"></script>
<script type="text/javascript" src="${resourcesUrl}/fas/resources/js/common/fas_common.js?version=${version}"></script>
<script type="text/javascript" src="${resourcesUrl}/fas/resources/js/common/fas_util.js?version=${version}"></script>
<script type="text/javascript" src="${resourcesUrl}/fas/resources/js/modules/sales_summary/salesSummary.js?version=${version}"></script>

<link type="text/css" rel="stylesheet" href="${staticFileUrl}/modules/filterbuilder/css/filterbuilder.css" /> 
<script type="text/javascript" src="${staticFileUrl}/assets/js/libs/sea.js?version=${version}"></script>
<script type="text/javascript" src="${staticFileUrl}/modules/filterbuilder/js/filterbuilder.js?version=${version}"></script>
</head>
<body class="easyui-layout">
	<div data-options="region:'north',border:false" class="toolbar-region">
	     <@p.toolbar id="toolbar" listData=[
			 {"id":"btn-search","title":"查询","iconCls":"icon-search","action":"salesSummary.search()", "type":0},
             {"id":"btn-remove","title":"清空","iconCls":"icon-remove","action":"salesSummary.clear()", "type":0},
           	 {"title":"导出","iconCls":"icon-export","action":"salesSummary.exportTotal()","type":4}
           ]
        />
	</div>

	<div  data-options="region:'center',border:false">
    	<div class="easyui-layout" data-options="fit:true">
			<!--搜索start-->
			<div data-options="region:'north',border:false" >
				<div class="search-div">
			      	<form name="searchForm" id="searchForm" method="post">
						<table class="form-tb">
						 <col width="120"/>
						 <col/>
						 <col width="120"/>
						 <col/>
						 <col width="120"/>
						 <col/>
		                 <tbody>
		                 		<tr height='33'>
								<th><span class="ui-color-red">*</span>公司	： </th>
								<td>
									<input class="easyui-company" name="companyName" id="companyName" data-options="multiple:true,required:true,inputWidth:200"/>
						     		<input type="hidden"  name="companyNo" id="companyNo" />
								</td>
								<th>店&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;铺： </th>
								<td>
									<input class="easyui-shopCommon" id="shopName"/>
						     		<input type="hidden"  name="shopNo" id="shopNo" />
								</td>
								<th><span class="ui-color-red">*</span>业务日期： </th>
									<td>
										<input class="easyui-datebox ipt"   name="saleStartDate" id="saleStartDate" data-options="maxDate:'saleEndDate',required:true"  />
									</td>
									<th>&nbsp;&nbsp;— —&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
									<td>
										<input class="easyui-datebox ipt" name="saleEndDate" id="saleEndDate" data-options="minDate:'saleStartDate',required:true"  />
								</td>
							</tr>
							<tr>
								<th>品牌： </th>
								<td>
									<input class="easyui-brand  ipt"  name="brandName" id="brandName" data-options="inputWidth:200,multiple:true"/>
									<input type="hidden" name="brandNo" id="brandNo"/>
								</td>
								<th>业务类型： </th>
								<td>						
									<input class="easyui-combobox ipt" name="businessType" id="businessType" data-options="required:false,multiple:true" readonly/>		
								</td>
						<!--	<th>结算月：</th>
					       		<td>
					       		 	<input class="easyui-datebox ipt"  name="month" id="month" data-options="dateFmt:'yyyyMM'" /> 
					       		</td>-->	
					       		<th>&nbsp;&nbsp;&nbsp;&nbsp;商品编号： </th>
								<td>
									<input class="easyui-itemCommon" id="itemName" />
									<input type="hidden" name="itemSql" id="itemNo"/>
								</td>
								<th>管理城市： </th>
									<td>
									    <input class="easyui-organ ipt"  name="organName" id="organName" data-options="inputWidth:130,multiple:true"/>
								        <input type="hidden" name="organNo" id="organNo"/>
								    </td>
							</tr>
						 </tbody>
					 </table>
				</form>
			</div>
		</div>
			<#--列表-->
			<div data-options="region:'center'">
				<@p.datagrid id="dtlDataGrid" loadUrl="" saveUrl="" defaultColumn="" showFooter="true" 
					isHasToolBar="false" onClickRowEdit="false" pagination="true" pageSize="20" 
					rownumbers="true"  singleSelect="true" 
					columnsJsonList="[
						{field : 'companyName', title : '公司名称', width :180,align:'left',rowspan:'2'},
						{field : 'organName', title : '管理城市', width :90,align:'left',rowspan:'2'},
						{field : 'shopNo1', title : '店铺/客户编码', width : 90,rowspan:'2'},
						{field : 'shopNoReplace', title : '店铺替换编码', width : 100,rowspan:'2'},
						{field : 'shopName', title : '店铺/客户名称', width : 100,align:'left',halign:'center',rowspan:'2'},
						
						{field : 'bizType', title : '业务类型', width : 60,rowspan:'2'},
  						{field : 'brandUnitNo', title : '品牌部编码', width : 100,rowspan:'2'},
						{field : 'brandUnitName', title : '品牌部名称', width : 100,rowspan:'2'},
						{field : 'totalQty', title : '数量', width : 60, align:'right',rowspan:'2',exportType:'number'},
						{field : 'totalTagPrice', title : '牌价总金额', width : 80, align:'right',rowspan:'2',exportType:'number'},
						{field : 'totalAmount', title : '销售总金额', width : 80, align:'right',rowspan:'2',exportType:'number'},
						{field : 'totalAmountUnitCost', title : '单位成本总额',  align:'right',width : 90,rowspan:'2',exportType:'number'},
						{field : 'totalAmountRegionCost', title : '地区成本总额',  align:'right',width : 90,rowspan:'2',exportType:'number'},
						{field : 'totalAmountHeadquarterCost', title : '总部成本总和',  align:'right',width : 90,rowspan:'2',exportType:'number'},

						{title : '鞋',colspan:'6'},
		                {title : '童鞋',colspan:'6',organType:'U010101'},
		                {title : '服',colspan:'6'},
		                {title : '包饰:U010101,包:U010105',colspan:'6',organType:'U010101,U010105'},
		                {title : '护鞋用品:U010101,配:U010102,配:U010105',colspan:'6'},
		                {title : '物料',colspan:'6',organType:'U010101,U010105'},
		                {title : '其他',colspan:'6'}],
		                
						[{field : 'totalQty01', title : '数量', width : 60, align:'right',halign:'center',exportType:'number'},
						{field : 'totalTagPrice01', title : '牌价金额 ', width : 80, align:'right',halign:'center',exportType:'number'},
						{field : 'totalAmount01', title : '销售金额 ', width : 80, align:'right',halign:'center',exportType:'number'},
						{field : 'totalAmountUnitCost01', title : '单位成本', width :90, align:'right',halign:'center',exportType:'number'},                                                                                                                                                                                                                      
						{field : 'totalAmountRegionCost01', title : '地区成本', width : 90, align:'right',halign:'center',exportType:'number'},
						{field : 'totalAmountHeadquarterCost01', title : '总部成本', width : 90, align:'right',halign:'center',exportType:'number'},
					                                                                                                                                                                                                                   
						{field : 'totalQty02', title : '数量', width : 60, align:'right',halign:'center',exportType:'number',organType:'U010101'},
						{field : 'totalTagPrice02', title : '牌价金额 ', width : 80, align:'right',halign:'center',exportType:'number',organType:'U010101'},
						{field : 'totalAmount02', title : '销售金额 ', width : 80, align:'right',halign:'center',exportType:'number',organType:'U010101'},
						{field : 'totalAmountUnitCost02', title : '单位成本', width : 90, align:'right',halign:'center',exportType:'number',organType:'U010101'},                                                                                                                                                                                                                      
						{field : 'totalAmountRegionCost02', title : '地区成本', width : 90, align:'right',halign:'center',exportType:'number',organType:'U010101'},
						{field : 'totalAmountHeadquarterCost02', title : '总部成本', width : 90, align:'right',halign:'center',exportType:'number',organType:'U010101'},
						
						{field : 'totalQty03', title : '数量', width : 60, align:'right',halign:'center',exportType:'number'},
						{field : 'totalTagPrice03', title : '牌价金额 ', width : 80, align:'right',halign:'center',exportType:'number'},
						{field : 'totalAmount03', title : '销售金额 ', width : 80, align:'right',halign:'center',exportType:'number'},
						{field : 'totalAmountUnitCost03', title : '单位成本', width : 90, align:'right',halign:'center',exportType:'number'},                                                                                                                                                                                                                      
						{field : 'totalAmountRegionCost03', title : '地区成本', width : 90, align:'right',halign:'center',exportType:'number'},
						{field : 'totalAmountHeadquarterCost03', title : '总部成本', width : 90, align:'right',halign:'center',exportType:'number'},
						                                                                                                                                 
						{field : 'totalQty04', title : '数量', width : 60, align:'right',halign:'center',exportType:'number',organType:'U010101,U010105'},
						{field : 'totalTagPrice04', title : '牌价金额 ', width : 80, align:'right',halign:'center',exportType:'number',organType:'U010101,U010105'},
						{field : 'totalAmount04', title : '销售金额 ', width : 80, align:'right',halign:'center',exportType:'number',organType:'U010101,U010105'},
						{field : 'totalAmountUnitCost04', title : '单位成本', width : 90, align:'right',halign:'center',exportType:'number',organType:'U010101,U010105'},                                                                                                                                                                                                                      
						{field : 'totalAmountRegionCost04', title : '地区成本', width : 90, align:'right',halign:'center',exportType:'number',organType:'U010101,U010105'},
						{field : 'totalAmountHeadquarterCost04', title : '总部成本', width : 90, align:'right',halign:'center',exportType:'number',organType:'U010101,U010105'},
						
						{field : 'totalQty05', title : '数量', width : 60, align:'right',halign:'center',exportType:'number'},
						{field : 'totalTagPrice05', title : '牌价金额 ', width : 80, align:'right',halign:'center',exportType:'number'},
						{field : 'totalAmount05', title : '销售金额 ', width : 80, align:'right',halign:'center',exportType:'number'},
						{field : 'totalAmountUnitCost05', title : '单位成本', width : 90, align:'right',halign:'center',exportType:'number'},                                                                                                                                                                                                                      
						{field : 'totalAmountRegionCost05', title : '地区成本', width : 90, align:'right',halign:'center',exportType:'number'},
						{field : 'totalAmountHeadquarterCost05', title : '总部成本', width : 90, align:'right',halign:'center',exportType:'number'},
						
						{field : 'totalQty06', title : '数量', width : 60, align:'right',halign:'center',exportType:'number',organType:'U010101,U010105'},
						{field : 'totalTagPrice06', title : '牌价金额 ', width : 80, align:'right',halign:'center',exportType:'number',organType:'U010101,U010105'},
						{field : 'totalAmount06', title : '销售金额 ', width : 80, align:'right',halign:'center',exportType:'number',organType:'U010101,U010105'},
						{field : 'totalAmountUnitCost06', title : '单位成本', width : 90, align:'right',halign:'center',exportType:'number',organType:'U010101,U010105'},                                                                                                                                                                                                                      
						{field : 'totalAmountRegionCost06', title : '地区成本', width : 90, align:'right',halign:'center',exportType:'number',organType:'U010101,U010105'},
						{field : 'totalAmountHeadquarterCost06', title : '总部成本', width : 90, align:'right',halign:'center',exportType:'number',organType:'U010101,U010105'},
						
						{field : 'totalQty07', title : '数量', width : 60, align:'right',halign:'center',exportType:'number'},
						{field : 'totalTagPrice07', title : '牌价金额 ', width : 80, align:'right',halign:'center',exportType:'number'},
						{field : 'totalAmount07', title : '销售金额 ', width : 80, align:'right',halign:'center',exportType:'number'},
						{field : 'totalAmountUnitCost07', title : '单位成本', width : 90, align:'right',halign:'center',exportType:'number'},                                                                                                                                                                                                                      
						{field : 'totalAmountRegionCost07', title : '地区成本', width : 90, align:'right',halign:'center',exportType:'number'},
						{field : 'totalAmountHeadquarterCost07', title : '总部成本', width : 90, align:'right',halign:'center',exportType:'number'}]"  />
			</div>
		</div>
	</div>
	
</body>
</html>