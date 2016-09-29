<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<title>支付方式销售核对</title>
<#include  "/WEB-INF/ftl/common/header.ftl" >
<script type="text/javascript" src="${resourcesUrl}/fas/resources/js/modules/self_shop_bank_info/pay_sale_check/pay_sale_check.js?version=${version}"></script>
<script type="text/javascript" src="${resourcesUrl}/fas/resources/js/common/fas_common.js?version=${version}"></script>

<link type="text/css" rel="stylesheet" href="${staticFileUrl}/modules/filterbuilder/css/filterbuilder.css" /> 
<script type="text/javascript" src="${staticFileUrl}/assets/js/libs/sea.js?version=${version}"></script>
<script type="text/javascript" src="${staticFileUrl}/modules/filterbuilder/js/filterbuilder.js?version=${version}"></script>
</head>
<body class="easyui-layout">
<@p.commonSetting search_url="/pay_sale_check/list.json" 
					  datagrid_id="dataGridDiv" 
					  search_form_id="searchForm" 
					  export_url="/pay_sale_check/do_fas_export"
					  export_title="支付方式销售核对"
					  primary_key="id"/>
	
<div data-options="region:'north',border:false" class="toolbar-region">
	     <@p.toolbar id="toolbar" listData=[
			 {"id":"btn-search","title":"查询","iconCls":"icon-search", "type":0},
             {"id":"btn-remove","title":"清空","iconCls":"icon-remove", "type":0},
             {"id":"top_btn_aduit","title":"确认","iconCls":"icon-aduit","action":"paySaleCheck.batchOperate(1)","type":0},	
			 {"id":"top_btn_cancel","title":"反确认","iconCls":"icon-aduit","action":"paySaleCheck.batchOperate(0)","type":0},
             {"id":"btn-save","title":"保存","iconCls":"icon-save","type":0},
             {"id":"btn-export","title":"导出","iconCls":"icon-export","type":4}
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
						 <col width="50"/>
						 <col/>
						 <col width="50"/>
						 <col/>
						 <col width="50"/>
						 <col/>
						 <col width="50"/>
						 <col/>
		                 <tbody>
		                 	<tr>
								<td align="right" width="60">公司名称：</td>
								<td align="left" width="140">
									<input class="ipt easyui-company"  name="companyName" id="companyName" data-options="inputWidth:170,required:true"/>
									<input type="hidden"  name="companyNo" id="companyNo" 	/>	
								</td>
						     	<td align="right" width="80">店铺名称：</td>
								<td align="left" width="140">
								    <input id="shopName" class="easyui-shopCommon" disabled="disabled" data-options="callback:function(value){
						     			if($('#shopName').attr('disabled') == null){
						     				$('#shopNo').val(value);
						     			} else {
						     				showWarn('请先选择业务类型');
						     				$('#shopName').val('');
						     				$('#shopNo').val('');
						     			}
						     		}"/>
									<input type="hidden" name="shopNo" id="shopNo"/>
								</td>
						     	<td align="right" width="100">支付方式：</td>
						     	<td align="left" width="140">
						     		<input class="easyui-combobox ipt" style="width: 160px;"
										iptSearch="card" name="payCode" id="payCode"
										data-options="valueField:'id',textField:'text',data:[
											{id:'03',text:'现金券'},{id:'04',text:'银行卡'},
											{id:'05',text:'商场卡'},{id:'06',text:'其它'},
											{id:'08',text:'商场券'},{id:'09',text:'预收款'},
											{id:'10',text:'支票'},{id:'11',text:'减现额/接券额'},
											{id:'12',text:'国外银行卡'},{id:'13',text:'IC卡'},
											{id:'14',text:'公司折扣券'},{id:'15',text:'商场折扣券'},
											{id:'16',text:'美元'},{id:'17',text:'港币'},
											{id:'18',text:'联华卡'},{id:'19',text:'市民卡'},
											{id:'20',text:'商盟卡'},{id:'20151010',text:'支付宝(商场收银)'},
											{id:'21',text:'支付宝(当面付)'},{id:'22',text:'微信(当面付)'},
											{id:'23',text:'储值卡'},{id:'24',text:'其它外卡'},
											{id:'25',text:'商场现金券'},{id:'27',text:'商场会员卡'},
											{id:'28',text:'提货券'},{id:'29',text:'外币卡'},
											{id:'30',text:'刷卡退款'},{id:'31',text:'商场储值卡'},
											{id:'32',text:'百丽储值卡'},{id:'33',text:'支付宝'},
											{id:'35',text:'储值购物卡'},{id:'36',text:'微信(商场收银)'},
											{id:'37',text:'支付宝(第三方)'},{id:'38',text:'微信支付(第三方)'}]" />
								</td>
								<td align="right" width="100">终端号： </td>
								<td>
									<input class="ipt" style="width: 120px;" iptSearch="card"  name="terminalNumber" id="terminalNumber" />
								</td>
							</tr>
							<tr>
								<th align="right" width="60">商场名称：</th>
							  	<td align="left" width="140">
									<input class="easyui-mall" name="mallName" id="mallName" data-options="inputWidth:170"/>
									<input type="hidden" name="mallNo" id="mallNo"/>
								</td>
								<th align="right" width="100">销售订单编码： </th>
								<td align="left" width="140">
									<input class="ipt" style="width: 120px;" iptSearch="card"  name="orderNo" id="orderNo" />
								</td>
								<td align="right" width="60">终端绑定账号： </td>
								<td>
									<input class="ipt" style="width: 150px;" iptSearch="card"  name="creditCardAccount" id="cardNumber" />
								</td>
								<td align="right" width="80">商户编码： </td>
								<td>
									<input class="ipt" style="width: 120px;" iptSearch="card"  name="merchantsNo" id="merchantsNo" />
								</td>
								<input type="checkbox" name="s" id="s"/>
						     	<td align="right" width="100">仅显示财务确认:</td>
								<td>
									<input type="checkbox" value='1' name="status" id="status"/>
								</td>
							</tr>
		                 	<tr>
								
								<td align="right" width="80">销售日期：</td>
								<td align="left" colspan="3">
									<input class="easyui-datebox ipt"  name="startOutDate" id="startOutStart" data-options="maxDate:'endOutEnd',required:true"/>
									 - <input class="easyui-datebox ipt" name="endOutDate" id="endOutEnd" data-options="minDate:'startOutStart',required:true"/>
								</td>
							</tr>
						 </tbody>
						</table>
					</form>
				</div>
			</div>
			<!--列表-->
        	<div data-options="region:'center',border:false">
		      <@p.datagrid id="dataGridDiv" loadUrl="" saveUrl=""   defaultColumn="" 
		              isHasToolBar="false"    onClickRowEdit="false" pagination="true" 
			           rownumbers="true"  emptyMsg = "" pageSize="20" showFooter="true"
			           columnsJsonList="[
			                  	{field : 't',checkbox:true,width : 30,notexport:true},
				                {field : 'rowId',hidden : 'true',align:'center',notexport:true},
				                {field : 'operate',title : '操作',width : 80,notexport:true,align:'center',formatter: function(value,row,index){
				                	return paySaleCheck.operate(value,row,index);
				                }},
				                {field : 'shopNo',title : '店铺编码',width : 80,align:'left'},
								{field : 'shopName',title : '店铺名称',width : 120,align:'left'},
								{field : 'companyNo',title : '公司编码',width : 80,align:'left'},
								{field : 'companyName',title : '公司名称',width : 120,align:'left'},
				                {field : 'terminalNumber',title : '终端号',width : 120,align:'left',exportType:'text'},
				                {field : 'outDate',title : '销售日期',width : 100,align:'right'},
				                {field : 'payName',title : '支付方式',width : 100,align:'right'},
				                {field : 'amount',title : '销售金额',width : 100,align:'center',exportType:'number'},
				                {field : 'creditCardRate',title : '刷卡费率',width : 60,align:'center',formatter: function(value,row,index){
				                		if(row.rowId==undefined){return ''}else{if(value == null){return '0'}else{return value}}
				                		},exportType:'number'
				                },
				                {field : 'poundage',title : '手续费',width : 100,align:'right'
				                	,editor:{
				                  		type:'validatebox',
				                  		options:{
				                  			validType:['poundage','length[0,10]']
				                  		}
				                },exportType:'number'},
				                {field : 'paidinAmount',title : '实收金额',width : 100,align:'right',exportType:'number'},
				                {field : 'auditor',title : '确认人',width : 100,align:'left'}, 
				                {field : 'auditorTime',title : '确认时间',width : 150,align:'center'},
				                {field : 'status',title : '财务确认',width : 100,align:'left',formatter: function(value,row,index){if(row.rowId){if(value == '1'){return '是'}else{ return '否'}}}},
				                {field : 'orderNo',title : '销售订单编码',width : 150,align:'center'},
				                {field : 'merchantsNo',title : '商户编码',width : 150,align:'center',exportType:'text'},
				                {field : 'creditCardAccount',title : '终端绑定账号',width : 150,align:'center',exportType:'text'},
				                {field : 'createUser',title : '建档人',width : 100,align:'left'}, 
				                {field : 'createTime',title : '建档时间',width : 150,align:'center'}
				                
			              ]" 
				          jsonExtend='{
				          	onDblClickRow:function(rowIndex, rowData){
				          		if(rowData.rowId != undefined){
				          			if(rowData.status!=1){
										paySaleCheck.edit(rowIndex, rowData);
										return;
									}
									showWarn("已确认单据,系统不允许修改手续费!");
				          		}
			             	},
			             	onLoadSuccess:function(rowData){
			             		paySaleCheck.removeCheckbox(rowData);
							}
			             }' 
                 />
			</div>
	 	</div>
	</div>
   
</body>
</html>