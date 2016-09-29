<!DOCTYPE html>
<html>
<head>
    <title>地区间(应收)结算打印</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<style>
		table,td,th {
			border-collapse: collapse,cellpadding:1;
			font-size:12px;
		}
		td{
			height:20px;
		}
		tr{
			line-height:20px;
		}
		span{
			width:30px;
		}
		.mgright{
			 margin-right:20px;
		};
	</style>
	<#include  "/WEB-INF/ftl/common/header.ftl" >
	<script type="text/javascript" src="${resourcesUrl}/fas/resources/common/js/LodopFuncs.js?version=${version}>"></script>
	<script type="text/javascript">
			function print_page(ygDialog){
				LODOP=getLodop(); 
				//=====判断Lodop插件是否安装过，没有安装或版本过低就提示下载安装:==========
				if ((LODOP == null) || (typeof (LODOP.VERSION) == "undefined")) {
					return false;
				}
				LODOP.PRINT_INIT("地区间(应收)结算单");
				LODOP.SET_PRINT_PAGESIZE(2,0,0,"A4");//第一个参数，0：打印机缺省设置,1：纵向，2：横向
				//表中
				//打印单据内容
				var strStyle="<style>.cell td,.cell th {border-width: 1px;border-style: solid;border-collapse: collapse;font-size:12px}</style>";
				LODOP.ADD_PRINT_HTM(10,10,"95%","95%",strStyle+$('#contentDiv').html());
				LODOP.SET_PRINT_STYLEA(0,"Vorient",3);
				LODOP.SET_SHOW_MODE ("LANDSCAPE_DEFROTATED",true);	
				LODOP.SET_PRINT_STYLE("FontSize",12);
				//表尾
				LODOP.PREVIEW();//预览
			}
	</script>
</head>
<body>
 <div id="contentDiv" style="page-break-after:always">
		<div id="printContentDiv" style="width:100%">
			<h2 align="center">地区间(应收)结算单</h2>
			<table  style="width:100%;font-size:14px;magin:auto;line-height:30px;" cellspacing="0" cellpadding="2" >
					<col width="6%" />
					<col width="24%" />
					<col width="6%" />
					<col width="14%" />
		            <col width="6%" />
		            <col width="14%" />
		            <col width="6%" />
		            <col width="14%" />
				
				<tr>
					<td>单据编号:</td> 
					<td id="billNo"></td> 
					<td>单据状态:</td> 
					<td id="statusName"></td> 
					<td>单据名称:</td> 
					<td id="billName"></td>
					<td>币&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别:</td> 
					<td id="currency"></td>
				</tr>
				<tr>
					<td>调出公司:</td> 
					<td id="salerName"></td> 
					<td>出库金额</td> 
					<td id="outAmount"></td> 
					<td>扣项金额:</td> 
					<td id="deductionAmount"></td>
					<td>本期应收:</td> 
					<td id="balanceAmount"></td>
				</tr>
				<tr>
					<td>调入公司:</td> 
					<td id="buyerName"></td>
					<td>结算期间:</td> 
					<td id="balanceStartDate"></td>  
					<td>至:</td> 
					<td id="balanceEndDate"></td> 
					<td>结&nbsp;&nbsp;算&nbsp;&nbsp;日:</td> 
					<td id="balanceDate"></td>
				</tr>
				<tr>
					<td>品&nbsp;&nbsp;牌&nbsp;&nbsp;部:</td> 
					<td id="brandUnitName"></td> 
					<td>备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注:</td> 
					<td colspan="3" id="remark"></td> 
					<td>开票单号:</td> 
					<td id="invoiceApplyNo"></td>
				</tr>
			</table>
		</div>
		<br/><br/><br/>
		
		<div id="centerContentDiv" style="width:100%">
			<table style="width:100%;margin-right:10px;border-collapse:collapse" border="1" cellspacing="0" cellpadding="1" width="100%" bordercolor="#333333">
				<thead id="tableHeader" style="font-size:13px;">
				</thead>    
				<tbody id="tableBody"  style="font-size:13px;">      
				</tbody>  
			</table>
		</div>
		<br/>
		
		<br/><br/><br/><br/><br/>
		<div id="printFoot" style="word-wrap:break-word;font-size:12px;width:100%"  align="top">
			<span style="font-size:16px;">制单:</span>
			<span style="margin-right:12%;font-size:16px;" id="createUser"></span>
			<span style="font-size:16px;">制单日期:</span>
			<span style="margin-right:12%;font-size:16px;" id="createTime"></span>
			<span style="font-size:16px;">审核:</span>
			<span style="margin-right:12%;font-size:16px;" id="auditor"></span>
			<span style="font-size:16px;">审核日期:</span>
			<span style="font-size:16px;" id="auditTime"></span>
		<br/><br/>	
			<span style="font-size:16px;margin-left:73%">打印时间:</span>
			<span style="font-size:16px;" id="printTime"></span>
		</div>
			
		</div>
	</div>
</body>
</html>