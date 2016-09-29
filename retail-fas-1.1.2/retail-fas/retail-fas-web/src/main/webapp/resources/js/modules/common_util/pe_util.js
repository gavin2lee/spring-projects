var pe_util = {
		combogrid : {
		    company  : {idField:'code',
				textField:'name',
				url:BasePath + '/common_util/getCompany'
			}, 
			zone  : {idField:'code',
				textField:'name',
				url:BasePath + '/common_util/getZone'
			}, 
			supplier : {idField:'code',
						textField:'name',
						url:BasePath + '/common_util/getSupplier'
			}, 
			customer : {idField:'code',
				textField:'name',
				url:BasePath + '/common_util/getCustomer'
			}, 
			item 	 : {idField:'code',
						textField:'name',
						url:BasePath + '/common_util/getItem'
			},
			brand 	 : {idField:'code',
						textField:'name',
						url:BasePath + '/common_util/getPageBrand'
			}, 
			brandUnit : {idField:'code',
						 textField:'name',
						 url:BasePath + '/common_util/getPageBrandUnit'
			},
			category : {idField:'code',
						textField:'name',
						url:BasePath + '/common_util/getPageCategory?levelid=1'
			},
			organ : {idField:'code',
				textField:'name',
				url:BasePath + '/common_util/getPageOrgan?organLevel=1'
			},
			orderUnit : {idField:'code',
				textField:'name',
				url:BasePath + '/common_util/getPageOrderUnit'
			},
			years :{idField:'code',
			    textField:'name',
			    url:BasePath + '/common_util/getLookup?lookupId=5'
			},
			payTermType : {idField:'code',
				textField:'name',
				url:BasePath + '/common_util/getPayTermType'
			},
			payTerm : {idField:'code',
				textField:'code',
				url:BasePath + '/common_util/getPayTerm'
			}
		},
		combobox : {
			YesOrNo : {idField:'code',
				textField:'name',
				data:[{code:'0', name:'否'}, {code:'1', name:'是'}]
			},
			PeBizType : {idField:'code',
				textField:'name',
				data:[{code:'0', name:'总部到货'}, {code:'1', name:'总部收购'}]
			},
			billType : {idField:'code',
						textField:'name',
						url:BasePath + '/common_util/getBillType'
			},
			peBalanceStatus : {idField:'code',
				textField:'name',
				url:BasePath + '/common_util/getPeBalanceStatus'
			},
			gender : {idField:'code',
							textField:'name',
							url:BasePath + '/common_util/getLookup?lookupId=7'
			},
			payTermType : {idField:'code',
				textField:'name',
				url:BasePath + '/common_util/getPayTermType'
			}
		}
};

pe_util.getColumns = function(type){
	var defaultColumns = [[{field:'code',title:'编码',width:100,align:'left'},{field:'name',title:'名称',width:250,align:'left'}]];
	var itemColumns = [[{field:'code',title:'编码',width:100,align:'left'},{field:'extendField1',title:'品牌',width:100,align:'left'},{field:'name',title:'名称',width:250,align:'left'}]];
	var columns = defaultColumns;
	if(type == 'item'){
		columns = itemColumns;
	}
	return columns;
};

$.extend($.fn.datagrid.defaults.editors, {
		test_combogrid: {
	        init: function(container, options){
	            var input = $('<input type="text" class="datagrid-editable-input">').appendTo(container);
	            var obj = pe_util.combogrid[options.type];
	            if(obj){
	            	var columns = pe_util.getColumns(options.type);
	        		input.combogrid({
	        	        panelWidth:400,
	        		    panelHeight : 200,
	        	        mode:'remote',
	        	        idField:obj.idField,
	        	        textField: options.type == 'item'?obj.idField:obj.textField,
	        	        required : options.required,
	        	        url : '',
	    		        columns:columns,
		  		      onSelect:function(data){
		  		    	var selectFunction = options.onSelect;
		  		    	if(selectFunction){
		  		    		selectFunction(data);
		  		    	}
					  }, 
		  		      onHidePanel:function(newVal,oldVal){
	        	        	var row = input.combogrid('grid').datagrid('getSelected');
	        	        	var callback = options.callback;
	        	        	if(callback){
	        	        		callback(row);
	        	        	}else{
	        	        		if(row){
	        	        			pe_util.setEditorValue(options.datagridId, options.valueField, row.code);
	        	        		}else{
	        	        			pe_util.setEditorValue(options.datagridId, options.valueField, '');
	        	        		}
	        	        	}
	        	        
	        	        },
	        	        onShowPanel:function(){
	        	        	var onShowPanelFunction = options.onShowPanelFunction;
	        	        	if(typeof(onShowPanelFunction)=='function'){
	        	        		onShowPanelFunction(input);
	        	        	}else{
		        	        	var url = obj.url;
		        	        	if(typeof(options.queryUrl)!='undefined'){
		        	        		url = options.queryUrl;
		        	        	}
		        	        	pe_util.loadData(input,url,options);
	        	        	}
	        	        }
	        	    });
	            }
	            return input;
	        },
	        destroy: function(target){
	            $(target).combogrid('destroy');
	        },
	        getValue: function(target){
	            return $(target).combogrid('getText');
	        },
	        setValue: function(target, value){
	            $(target).combogrid('setValue', value);
	        },
	        resize: function(target, width){
	            $(target).combogrid('resize',width);
	        }
	    },
	    test_multi_combogrid: {
	        init: function(container, options){
	            var input = $('<input type="text" class="datagrid-editable-input">').appendTo(container);
	            var obj = pe_util.combogrid[options.type];
	            if(obj){
	            	var columns = pe_util.getColumns(options.type);
	        		input.combogrid({
	        	        panelWidth:400,
	        		    panelHeight : 200,
	    		        mode:'remote',
	    		        multiple:true,
	        	        idField:obj.idField,
	        	        textField: options.type == 'item'?obj.idField:obj.textField,
	        	        required : options.required,
	    		        url : '',
	    		        separator:',',
	    		        selectOnCheck : true,
	    		        checkOnSelect : true,
	    		        frozenColumns:[[{field:'ck',checkbox:true,width:30}]],
	    		        columns:columns,
	    		        onSelect:function(data){
			  		    	var selectFunction = options.onSelect;
			  		    	if(selectFunction){
			  		    		selectFunction(data);
			  		    	}
						  }, 
		  		      onHidePanel:function(newVal,oldVal){
	        	        	var rows = input.combogrid('grid').datagrid('getSelections');
	        	        	var callback = options.callback;
	        	        	if(callback){
	        	        		callback(rows);
	        	        	}else{
	        	        		var code = '';
	        	        		$.each(rows,function(index,item){
	        	        			code +=item.code+',';
	        	        		});	
	        	        		if(rows.length >0){
	        	        			pe_util.setEditorValue(options.datagridId, options.valueField, code.substring(0,code.length-1));
	        	        		}else{
	        	        			pe_util.setEditorValue(options.datagridId, options.valueField, '');
	        	        		}
	        	        	}
	        	        
	        	        },
	        	        onShowPanel:function(){
	        	        	var onShowPanelFunction = options.onShowPanelFunction;
	        	        	if(typeof(onShowPanelFunction)=='function'){
	        	        		onShowPanelFunction(input);
	        	        	}else{
		        	        	var url = obj.url;
		        	        	if(typeof(options.queryUrl)!='undefined'){
		        	        		url = options.queryUrl;
		        	        	}
		        	        	pe_util.loadData(input,url,options);
	        	        	}
	        	        }
	        	    });
	            }
	            return input;
	        },
	        destroy: function(target){
	            $(target).combogrid('destroy');
	        },
	        getValue: function(target){
	            return $(target).combogrid('getText');
	        },
	        setValue: function(target, value){
	            $(target).combogrid('setValue', value);
	        },
	        resize: function(target, width){
	            $(target).combogrid('resize',width);
	        }
	    }    
});

// 设置ID值
pe_util.setEditorValue = function(datagridId, valueField, value){
	if(datagridId && valueField){
		var editIndex = $("tr[class*=datagrid-row-editing]").attr("datagrid-row-index");
    	var edValue =$('#'+datagridId).datagrid('getEditor',{index:editIndex, field:valueField});
    	edValue.target.val(value);
	}
}

// 获取editor字段值
pe_util.getEditorFieldValue = function(datagridId, valueField){
	if(datagridId && valueField){
		var editIndex = $("tr[class*=datagrid-row-editing]").attr("datagrid-row-index");
    	var edValue =$('#'+datagridId).datagrid('getEditor',{index:editIndex, field:valueField});
    	return  edValue.target.val();
	}
	return '';
}

//初始化单选
pe_util.initSingleSearch = function(formId){
	var input = $('input[singleSearch]');
	if(formId){
		input = $('#'+formId).find('input[singleSearch]');
	}
	input.each(function(){
		var _this = $(this);
		var type = _this.attr("singleSearch");
		var obj = pe_util.combogrid[type];
		if(obj){
			var columns = pe_util.getColumns(type);
			var _next = _this.next();
			$(this).combogrid({
		        panelWidth:400,
			    panelHeight : 250,
		        mode:'remote',
		        idField:type == 'item'?obj.idField:obj.textField,
				textField:type == 'item'?obj.idField:obj.textField,
		        url : '',
		        columns:columns,
    	        onHidePanel:function(){
    	        	var row = _this.combogrid('grid').datagrid('getSelected');
		        	var callback = _this.combo('options').callback;
		        	if(callback){
		        		callback(row,_this,_next);
		        	}else{
		        		if(row){
					        _next.val(row.code);
			        	}else{
			        		_next.val('');
			        		_this.combogrid('clear');
			        	}
		        	}
    	        },
    	        onShowPanel:function(){
    	        	var onShowPanelFunction = _this.combo('options').onShowPanelFunction;
    	        	if(typeof(onShowPanelFunction)=='function'){
    	        		onShowPanelFunction(_this);
    	        	}else{
        	        	var url = obj.url;
        	        	if(typeof(_this.combo('options').queryUrl)!='undefined'){
        	        		url = _this.combo('options').queryUrl;
        	        	}
        	        	pe_util.loadData(_this,url);
    	        	}
    	        }
		    });
		}
	});
};

//载入数据
pe_util.loadData = function(obj,url,options){
    if(typeof(obj.attr("notGroupLeadRole")) != 'undefined' || (options && typeof(options.notGroupLeadRole) != 'undefined')){
    	if(url.indexOf('?')!= -1){
    		url += '&params=notGroupLeadRole';
    	}else{
    		url += '?params=notGroupLeadRole';
    	}
    }else if(typeof(obj.attr("groupLeadRole")) != 'undefined' || (options && typeof(options.groupLeadRole) != 'undefined')){
    	if(url.indexOf('?')!= -1){
    		url += '&params=groupLeadRole';
    	}else{
    		url += '?params=groupLeadRole';
    	}
    }
	var dg = obj.combogrid('grid');
	var queryParams = obj.combo('options').queryParams;
	dg.datagrid( 'options' ).queryParams = queryParams;
	dg.datagrid( 'options' ).url = url;
	dg.datagrid( 'load' );
}

//初始化多选
pe_util.initMultiSearch = function(formId){
	var input = $('input[multiSearch]');
	if(formId){
		input = $('#'+formId).find('input[multiSearch]');
	}
	input.each(function(){
		var _this = $(this);
		var _next = _this.next();
		var type = _this.attr("multiSearch");
		var obj = pe_util.combogrid[type];
		if(obj){
			var columns = pe_util.getColumns(type);
			$(this).combogrid({
				panelWidth:400,
			    panelHeight : 250,
		        mode:'remote',
		        multiple:true,
		        idField:type == 'item'?obj.idField:obj.textField,
		        textField:type == 'item'?obj.idField:obj.textField,
		        url : '',
		        separator:',',
		        selectOnCheck : true,
		        checkOnSelect : true,
		        frozenColumns:[[{field:'ck',checkbox:true,width:30}]],
		        columns:columns,
    	        onHidePanel:function(){
		        	var rows = _this.combogrid('grid').datagrid('getSelections');
		        	var callback = _this.combo('options').callback;
		        	if(callback){
		        		callback(rows,_this,_next);
		        	}else{
		        		if(rows.length > 0){
		        			var code = '';
		        			$.each(rows,function(index,item){
		        				code +=item.code+',';
		        			});
		        			_next.val(pe_util.multiFormat(code.substring(0,code.length-1)));
			        	}else{
			        		_next.val('');
			        		_this.combogrid('clear');
			        	}
		        	}
    	        },
    	        onShowPanel:function(){
    	        	var onShowPanelFunction = _this.combo('options').onShowPanelFunction;
    	        	if(typeof(onShowPanelFunction)=='function'){
    	        		onShowPanelFunction(_this);
    	        	}else{
        	        	var url = obj.url;
        	        	if(typeof(_this.combo('options').queryUrl)!='undefined'){
        	        		url = _this.combo('options').queryUrl;
        	        	}
        	        	pe_util.loadData(_this,url);
    	        	}
    	        },
		        validType:_this.attr('validType')
		    });
		}
	});
}; 

//初始化下拉框
pe_util.initCombobox = function(formId){
	var input = $('input[combobox]');
	if(formId){
		input = $('#'+formId).find('input[combobox]');
	}
	input.each(function(){
		var _this = $(this);
		var obj = pe_util.combobox[_this.attr("combobox")];
		if(obj){
			if(obj.data){
				$(this).combobox({
					data:obj.data,
					valueField:obj.idField,
					textField:obj.textField,
					editable:true
				});
			}else{
				var url = obj.url;
				if(_this.attr('showType')){
					url += '?showType='+_this.attr('showType');
				}
				if(_this.attr('fliterType')){
					url += '?fliterType='+_this.attr('fliterType');
				}
				$(this).combobox({
					url:url,
					valueField:obj.idField,
					textField:obj.textField,
					editable:true
				});
			}
		}
	});
};

//初始化页面input宽度
pe_util.initInputWidth = function(formId){
	var input = $('input[class*=ipt]');
	var default_width = 140;
	if(formId){
		input = $('#'+formId).find('input[class*=ipt]');
	}
	input.each(function(){
		var _this = $(this);
		if(_this.width() < default_width){
			if(typeof($(this).attr('singleSearch'))!='undefined' 
				|| typeof($(this).attr('multiSearch'))!='undefined'
					|| typeof($(this).attr('combobox'))!='undefined'){
				_this.width(140);
			}else{
				_this.width(130);
			}
		}
	});
}

// 提示框
pe_util.showTips = function(value){
	return '<span title="' + value + '">' + value + '</span>';
};

//A,B,C -> ('A','B','C')
pe_util.multiFormat = function(str){
	if(str){
		str = str.toString();
		if(str.indexOf("(") != 0 && str.indexOf(")") != str.length - 1){
			var reg=new RegExp(",","g"); 
			var reg1 = new RegExp("，","g"); 
			str = str.replace(reg,"','");
			str = str.replace(reg1,"','");
			str = "('" + str + "')";
		}
	}
	return str;
};

//('A','B','C')-> A,B,C 
pe_util.multiParse = function(str){
	if(str){
		str = str.toString();
		if(str.indexOf("(") == 0 && str.indexOf(")") == str.length - 1){
			var reg=new RegExp("'","g"); 
			str = str.replace(reg,"");
			str = str.substring(1,str.length - 1);
		}
	}
	return str;
};

/**
 * 导出
 * @param dataGridId  导出数据的表格ID
 * @param exportUrl 导出数据的URL  
 * @param excelTitle excel文件名
 * @param otherParams 可选参数
 * 
 */
pe_util.doExport = function (dataGridId, exportUrl, excelTitle, otherParams, countUrl){
	$("#exportExcelForm").remove();
	$("<form id='exportExcelForm'  method='post'></form>").appendTo("body"); 
	var fromObj=$('#exportExcelForm');
	var $dg = $('#'+dataGridId);
	var params=$dg.datagrid('options').queryParams;
	var columns=$dg.datagrid('options').columns;
	var v_pageNumber = $dg.datagrid('options').pageNumber;//当前页号
	var v_pageSize = $dg.datagrid('options').pageSize;//每页多少行记录
	var columnsNew = [];
	var columnsOld = new Array();
	columnsOld.push(columns[columns.length-1]);
	var Fcolumns= $dg.datagrid('options').frozenColumns;
	if(Fcolumns[0].length>0){
		columns=Fcolumns.concat(columns);
	}
	$.each(columnsOld,function(index,item){
		var dataArray = [];
		var i = 0;
		$.each(item,function(rowIndex,rowData){
			var v_object = {};
			v_object.field = rowData.field;
			v_object.title = rowData.title;
			v_object.width = rowData.width;
			if(rowData.notexport || rowData.hidden){
				return ;
			}
			dataArray[i] = v_object;
			i++;
		});
		columnsNew[index] = dataArray;
	});
	var exportColumns=JSON.stringify(columnsNew);
	var url=BasePath+exportUrl;
	var dataRow=$dg.datagrid('getRows');
	if(dataRow.length>0){
		if(typeof countUrl != 'undefined'){// 数量验证
			var flag = true;
			var queryParams = fromObj.form('getData');
			if(params!=null&&params!={}){
				$.each(params,function(i){
					queryParams[i]=params[i];
				});
			}
			if(otherParams!=null&&otherParams!={}){
				$.each(otherParams,function(i){
					queryParams[i]=otherParams[i];
				});
			}
			ajaxRequestAsync(countUrl,queryParams,function(count){
				if(count > 100000){
					flag = false;
				}
			});
			if(!flag){
				showError("数据量大于10万,请选择分批导出");
				return ;
			}
		}
	    fromObj.form('submit', {
			url: url,
			onSubmit: function(param){
				if(params!=null&&params!={}){
					$.each(params,function(i){
						param[i]=params[i];
					});
				}
				if(otherParams!=null&&otherParams!={}){
					$.each(otherParams,function(i){
						param[i]=otherParams[i];
					});
				}
				param.exportColumns=exportColumns;
				param.fileName=excelTitle;
				param.pageNumber = v_pageNumber;
				param.pageSize = v_pageSize;
				param.exportFlag = true;
				
			},
			success: function(){
				
		    }
	   });
	}else{
		alert('查询记录为空，不能导出!',1);
	}
}

/**
 * 导入
 * @param templateName  模版名称
 * @param url 导入url  
 * @param balanceType 结算类型
 * @param successFn 成功回调函数
 * @param errorFn 失败回调函数
 */
pe_util.doImport = function(templateName,url,balanceType,successFn,errorFn) {
	$.importExcel.open({
		'submitUrl' : BasePath + url,
		'templateName' : templateName,
		'balanceType' : balanceType,
		success : successFn,
		error : errorFn
	});
};
/************************************************初始化方法**************************************************/
//初始化页面组件
pe_util.initPage = function(formId){
	pe_util.initInputWidth(formId);
	pe_util.initSingleSearch(formId); 
	pe_util.initMultiSearch(formId); 
	pe_util.initCombobox(formId);
}

// 初始化日期
pe_util.initDate = function(formId) {
	var startDate = $('input[defaultValue=startDate]');
	var endDate = $('input[defaultValue=endDate]');
	var currentDate = $('input[defaultValue=currentDate]');
	var currentMonthFirstDayDate = $('input[defaultValue=currentMonthFirstDayDate]');
	var currentMonthLastDayDate = $('input[defaultValue=currentMonthLastDayDate]');
	if(formId){
		startDate = $('#'+formId).find('input[defaultValue=startDate]');
		endDate = $('#'+formId).find('input[defaultValue=endDate]');
		currentDate = $('#'+formId).find('input[defaultValue=currentDate]');
		currentMonthFirstDayDate = $('#'+formId).find('input[defaultValue=currentMonthFirstDayDate]');
		currentMonthLastDayDate = $('#'+formId).find('input[defaultValue=currentMonthLastDayDate]');
	}
	if(startDate.length ==0 && endDate.length ==0 && currentDate.length ==0 && currentMonthFirstDayDate.length==0 && currentMonthLastDayDate.length==0){
		return ;
	}
	var currDate = new Date();
	var year = currDate.getFullYear();
	var month = currDate.getMonth() + 1;
	var day = currDate.getDate();
	var startYear = year;
	var endYear = year;
	var startMonth = month-1;
	var endMonth = month;
	if(day <= 10){
		if(month == 1){
			startYear = year - 1;
			endYear = year - 1;
			startMonth = 11;
			endMonth = 12;
		}else if(month == 2){
			startYear = year - 1;
			endYear = year;
			startMonth = 12;
			endMonth = 1;
		}else{
			startMonth = month - 2;
			endMonth = month - 1;
		}
	}else{
		if(month == 1){
			startYear = year - 1;
			endYear = year;
			startMonth = 12;
			endMonth = 1;
		}
	}
	if(startMonth < 10){
		startMonth = '0' + startMonth;
	}
	if(endMonth < 10){
		endMonth = '0' + endMonth;
	}
	var currentMonthLastDay = 30;
	if(month==2){
		if(year%4==0){
			currentMonthLastDay = 29;
		}else{
			currentMonthLastDay = 28;
		}
	}else if(month==1||month==3||month==5||month==7||month==8||month==10||month==12){
		currentMonthLastDay = 31;
	}else {
		currentMonthLastDay = 30;
	}
	startDate.val(year +'-'+ month +'-01');
	endDate.val(year +'-'+ month +'-'+currentMonthLastDay);
	currentDate.val(new Date().format("yyyy-MM-dd"));
	currentMonthFirstDayDate.val(year +'-'+ month +'-01');
	currentMonthLastDayDate.val(year +'-'+ month +'-'+currentMonthLastDay);
};

/**
 * 初始化导入方法
 */
pe_util.initImportMethod = function(){
	var defaults = {
			submitUrl : '',
			templateName : '',
			params : {},
			success:function(){
				
			},
			error:function(){
				
			}
		};

		var importExcel = {
			opts : {},
			dialog : null,
			open : function(o) {
				opts = o;
				var $uploadForm = null;
				var $errorInfo = null;
				dialog = ygDialog({
					isFrame: true,
					cache : false,
					title : '导入',
					modal:true,
					showMask: false,
					width:'400',
					height:'160',
					href : BasePath + "/common_util/toImport?balanceType="+o.balanceType,
					buttons:[{
						text:'上传',
						iconCls:'icon-upload',
						handler:function(self){
							if($errorInfo.text().trim()!=""){
								return ;
							}
							$.messager.progress({
								title:'请稍后',
								msg:'正在处理...'
							}); 
							$uploadForm.form('submit',{
								url : o.submitUrl,
								success:function(data){
									$.messager.progress('close');
									if(data){
										if(typeof o.success=="function"){
											o.success(data);
										}else{
											showInfo("导入成功!");
										}
									}else{
										if(typeof o.error=="function"){
											o.error(data);
										}else{
											showError("导入失败!");
										}
									}
									self.close();
								}
							});
						}
					},{
						text:'下载模板',
						iconCls:'icon-download',
						handler:function(){
							window.location.href=BasePath + '/common_util/download?fileName='+ o.templateName;
						}
					}],
					onLoad:function(win,dialog){
						$errorInfo = dialog.$('#errorInfo');
						$uploadForm = dialog.$('#uploadForm');
					}
				});
			},
			close : function(){
				dialog.panel('close');
			},
			success : function(){
				opts.success.call();
			},
			error : function(){
				opts.error.call();
			}
		};

		$.importExcel = function(options) {
			$.fn.importExcel.open(options);
		};

		$.importExcel.open = function(options) {
			var opts = $.extend({}, defaults, options);
			importExcel.open(opts);
		};

		$.importExcel.success = function(){
			return importExcel.success();
		};
		
		$.importExcel.error = function(){
			return importExcel.error();
		};
		
		$.importExcel.colse = function(){
			return importExcel.close();
		};
}

//导入回调
pe_util.importCallBack = function(data){
	if(data.indexOf('"success":true')!=-1){
		var jsonData = JSON.parse(data);
		if(jsonData.success){
			var lstData = jsonData.rows;
			if(lstData.length > 0){
				$('#importDataGrid').datagrid({
					data: lstData,
					rowStyler: function(index,row){
						if (row.pass == 0){
							return 'background-color:#D4E6E7;';
						}
					}
				});
				ygDialog({
					title : '导入结果',
					target : $('#myFormPanel'),
					width :  850,
					height : 'auto',
					buttons : [{
						text : '取消',
						iconCls : 'icon-cancel',
						handler : function(dialog) {
							dialog.close();
						}
					}]
				});
				showSuc("操作成功!");
				return ;
			}
		}
	}
	showError("数据不合法,导入失败!");
}

/**
 * 初始化编辑器方法
 */
pe_util.initEditorMethod = function(){
    $.extend($.fn.datagrid.methods, {
       addEditor : function(jq, param) {
          if (param instanceof Array) {
              $.each(param, function(index, item) {
                  var e = $(jq).datagrid('getColumnOption', item.field);
                  e.editor = item.editor;
              });
          } else {
              var e = $(jq).datagrid('getColumnOption', param.field);
              e.editor = param.editor;
          }
      },
      removeEditor : function(jq, param) {
          if (param instanceof Array) {
              $.each(param, function(index, item) {
                  var e = $(jq).datagrid('getColumnOption', item);
                  e.editor = {};
              });
          } else {
              var e = $(jq).datagrid('getColumnOption', param);
              e.editor = {};
          }
      }
    });
}

$(function(){
	pe_util.initPage();
	pe_util.initDate();
	pe_util.initEditorMethod();
	pe_util.initImportMethod();
});








