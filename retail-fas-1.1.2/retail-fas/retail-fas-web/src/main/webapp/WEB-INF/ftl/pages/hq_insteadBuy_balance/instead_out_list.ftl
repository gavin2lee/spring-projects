<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<title>采购明细表</title>
	<#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="${resourcesUrl}/fas/resources/js/common/area_detail.js?version=${version}"></script>
	<script type="text/javascript" src="${resourcesUrl}/fas/resources/js/modules/hq_insteadOf_buy/insteadOfBuyOutDtl.js?version=${version}"></script>
	<script type="text/javascript" src="${resourcesUrl}/fas/resources/js/common/billNoMenuRedirect.js?version=${version}"></script>
</head>
<input type="hidden" name="sign" value="${sign}" id="sign" />
<body class="easyui-layout">
	<div data-options="region:'north',border:false" >
		 <@p.billToolBar type="hq_insteadOf_list"/>
	</div>
				
	<div  data-options="region:'center',border:false">
    	<div class="easyui-layout" data-options="fit:true" id="subLayout">
			<#--搜索start-->
			<div data-options="region:'north',border:false" >
				<div class="search-div">
			      	<form name="searchForm" id="searchForm" method="post">
						<table class="form-tb">
							<col width="100" />
							<col />
							<col width="100" />
							<col />
							<col width="100" />
							<col />	
							<col width="100" />
							<col />						
							<tbody>
								<tr>
									<th>单据编号： </th>
									<td>
										<input class="easyui-validatebox ipt"  name="billNo" id="billNo" style="width: 150px;"/>
									</td>
									<th>品&nbsp;&nbsp;牌&nbsp;&nbsp;部： </th>
									<td>
										<input class="easyui-brandunit ipt"   name="brandUnitName" id="brandUnitNameId" 
										data-options="multiple:true,inputNoField:'brandUnitNoId',inputNameField:'brandUnitNameId',inputWidth:160"/>
										<input type="hidden" name="brandUnitNoLike" id="brandUnitNoId"/>
									</td>
								  	<th>品&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;牌： </th>
									<td>
										<input class="easyui-brand ipt"  name="brandName" id="brandNameId" 
										data-options="multiple:true,inputNoField:'brandNoId',inputNameField:'brandNameId',inputWidth:160"/>
										<input type="hidden" name="brandNoLike" id="brandNoId"/>
									</td>
									<th>大&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;类： </th>
									<td>
										<input class="easyui-categorybox"   name="oneLevelCategoryNo" id="oneLevelCategoryNo" data-options="width:160"/>
									</td>
								</tr>
								<tr>
									<th>公&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;司： </th>
									<td>
										<input class="easyui-company ipt"  name="buyerName" id="companyNameId" 
										data-options="queryUrl: BasePath + '/base_setting/company/list.json?params=groupLeadRole',inputNoField:'buyerNoCondition',inputNameField:'companyNameId',inputWidth:160,isDefaultData : false"/>
										<input type="hidden" name="buyerNo" id="buyerNoCondition" />
									</td>
									<th>商品编码： </th>
									<td>
										<input class="easyui-item ipt"  name="itemName" id="itemNameId" data-options="inputCodeField:'itemCodeCondition',inputNameField:'itemNameId',inputWidth:160"/>
										<input type="hidden" name="itemCode" id="itemCodeCondition" />
									</td>
									<th>单据日期： </th>
									<td>
										<input class="easyui-datebox ipt"  name="sendDateStart" id="sendDateStart" data-options="required:true,maxDate:'sendDateEnd'" style="width:150px;" readonly="true" />
									</td>
									<th>至： </th>
									<td>
										<input class="easyui-datebox ipt"   name="sendDateEnd" id="sendDateEnd" data-options="required:true,minDate:'sendDateStart'" style="width:150px;"  readonly="true"/>
									</td>
								</tr>	
								<tr>
									<th>供&nbsp;&nbsp;应&nbsp;&nbsp;商： </th>
									<td>
										<input class="easyui-supplier ipt"  name="salerName" id="supplierNameId" data-options="inputNoField:'supplierNoCondition',inputNameField:'supplierNameId',inputWidth:160"/>
										<input type="hidden" name="salerNo" id="supplierNoCondition" />
									</td>
						    		<th>货管单位：</th>
									<td>
										<input class="easyui-orderUnit ipt"   name="orderUnitName" id="orderUnitNameId" 
										data-options="inputNoField:'orderUnitNoId',inputNameField:'orderUnitNameId',inputWidth:160"/>
										<input type="hidden" name="orderUnitNo" id="orderUnitNoId" />
									</td>
									<th>单据类型：</th>
						    		<td>
						    			<select class="easyui-combobox"  name="billType" id="billType" data-options="editable:false,valueField:'code',textField:'name',width:'auto',url:BasePath + '/area_balance_common/getBillType?showType=ASN,RETURNOWN'"/>
						    		</td>
						    		<th>结算单号： </th>
									<td>
										<input class="easyui-validatebox ipt"   name="balanceNo" id="balanceNo" style="width: 150px;"/>
									</td>
								</tr>
								<tr>
								    <th>管理城市：</th>
									<td>
									<input class="easyui-organ ipt"   name="organName" id="organNameId" 
										data-options="inputNoField:'organNoId',inputNameField:'organNameId',inputWidth:160"/>
										<input type="hidden" name="organNo" id="organNoId" />
									</td>
									<th>结算状态： </th>
									<td>
										<input class="easyui-combobox" name="balanceFlag" id="balanceFlag" 
		       		 					data-options="valueField:'label',textField:'value',data:[{label:'1',value:'已结算'},{label:'0',value:'未结算'}],width:160"/>			
		       		 				</td>							
									<th>单价为零：</th>
									<td>
										<input type="checkbox" name="zeroFlag" id="zeroFlag" />
									</td>
									<th></th>
									<td></td>
								</tr>
							</tbody>
						</table>
					</form>
				</div>
			</div>
			<#--列表-->
        	<div data-options="region:'center',border:false">
		      <@p.datagrid id="dataGridJG"  loadUrl="" saveUrl=""   defaultColumn="" 
		              isHasToolBar="false"    onClickRowEdit="false" pagination="true" 
			           rownumbers="true"   pageSize="20" showFooter="true"
			           columnsJsonList="[
			                  {field : 'zoneName', title : '大区', width : 80,align:'center',halign:'center'},
			                  {field : 'buyerName', title : '公司', width : 200,align:'left',halign:'center'},
			                  {field : 'salerName', title : '供应商', width : 200,align:'left',halign:'center'},
			                  {field : 'brandUnitName', title : '品牌部', width : 80,align:'center'},	
			                  {field : 'brandName', title : '品牌', width : 100,align:'center'},	
			                  {field : 'oneLevelCategoryName', title : '大类', width : 80,align:'center'},	
			                  {field : 'twoLevelCategoryName', title : '二级大类', width : 80,align:'center'},	
			                  {field : 'itemCode', title : '商品编码', width : 150,align:'left',halign:'center'},	
 							  {field : 'itemName', title : '商品名称', width : 180,align:'left',halign:'center'},
			                  {field : 'cost', title : '单价', width : 80,align:'right',halign:'center'},
			                  {field : 'sendQty', title : '数量', width : 80,align:'right',halign:'center'},
			                  {field : 'sendAmount', title : '金额', width : 100,align:'right',halign:'center'}	,
 							  {field : 'sendDate', title : '单据日期', width : 100,align:'center',sortField:'receive_date',sortable:true},
			                  {field : 'organName', title : '管理城市', width : 80,align:'center',halign:'center'},
			                  {field : 'orderUnitName', title : '货管单位', width : 80,align:'center',halign:'center'},
			                  {field : 'receiveStoreName', title : '收(退)货仓', width : 150,align:'left',halign:'center'},
 							  {field : 'billNo', title : '单据编号', width : 150,align:'left',halign:'center'},
			                  {field : 'billTypeName', title : '单据类型', width : 100,align:'center'},
 							  {field : 'createUser',title : '制单人',width : 80,align:'center',halign:'center'},
			                  {field : 'balanceNo', title : '结算单号', width : 150,align:'center',halign:'center', formatter: function(value,row,index){
								  var title;
								  if($('#sign').val() == 'hq') {
									  title = 'HP-结算单';
								  }else if($('#sign').val() == 'area') {
									  title = 'AP-结算单查询';
								  }
								  return billNoMenuRedirect.billNoMenuFormat(value,row,index,title);
							  }}
		                  ]" 
                 />
			</div>
	 	</div>
	</div>
	
</body>
</html>