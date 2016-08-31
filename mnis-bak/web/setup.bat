@shift
@echo off
title ��Ӱҽ���ƶ�������Ϣϵͳ��װ��...
mode con cols=100 lines=9999
color 3f
cls
echo.
echo ��л��ʹ����Ӱ��Ϣ��ҽ������������ڰ�װ��Ӱ�ƶ�������Ϣϵͳ...
set sname_sql=localhost

:connsql
echo.
echo *******************************
echo ���������ӵ�SQL Server...
echo ��������ַ�ǣ�%sname_sql%
echo ��¼���ǣ�sa
echo *******************************
echo.
goto changesql 

:setsql
echo.
set /P sname_sql=�������������ӵ�SQL Server��������ַ��
goto checksql

:changesql
echo �Ƿ�Ҫ���ӵ�%sname_sql%��̨SQL Server���ݿ⣿
set /P csql=��y�����ӵ���̨SQL Server���ݿ⣬��n��������������ַ�����������ַ����˳�����[y/n] 
if "%csql%"=="y" (
goto checksql
) else (
if "%csql%"=="n" (
goto setsql ) else (
goto cend
)
)

:checksql
echo.
set /P pwdsa=������%sname_sql%���ݿ����Աsa�û������룺
osql -S%sname_sql% -Usa -P%pwdsa% -Q
if %ERRORLEVEL% == 0 (
echo ���Ѿ���ȷ���ӵ�%sname_sql%��̨SQL Server���ݿ�...��
goto runsql
) else (
goto errorend
)

:runsql
pause
osql -S%sname_sql% -Usa -P%pwdsa% -i "db\sql_script\create_login.sql"
osql -S%sname_sql% -Uuih -PUih888 -i "db\sql_script\create_db.sql"
osql -S%sname_sql% -Uuih -PUih888 -d "MNIS_V2" -i "db\sql_script\create_struc.sql"
osql -S%sname_sql% -Uuih -PUih888 -d "MNIS_V2" -i "db\sql_script\0_crebas_nursedoc.sql"
:osql -S%sname_sql% -Uuih -PUih888 -d "MNIS_V2" -i "db\sql_script\1_system_basedata_nursedoc.sql"
echo.

echo �����ݿ�ṹ��װ��ɣ���

echo �����潫����ϵͳ�ĳ�ʼ������...��

bcp MNIS_V2.dbo.dict_drug        	in db\data\dbo.dict_drug        -S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r"**&**" -E
bcp MNIS_V2.dbo.dict_undrug      	in db\data\dbo.dict_undrug      -S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r"**&**" -E
bcp MNIS_V2.dbo.item_price       	in db\data\dbo.item_price       -S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r"**&**" -E
bcp MNIS_V2.dbo.bed              	in db\data\dbo.bed              -S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r"**&**"
bcp MNIS_V2.dbo.dict_body_sign   	in db\data\dbo.dict_body_sign   -S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r"**&**"
bcp MNIS_V2.dbo.dict_freq        	in db\data\dbo.dict_freq        -S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r"**&**"
bcp MNIS_V2.dbo.dict_order_class 	in db\data\dbo.dict_order_class -S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r"**&**"
bcp MNIS_V2.dbo.dict_usage       	in db\data\dbo.dict_usage       -S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r"**&**"
bcp MNIS_V2.dbo.dict_diag               in db\data\dbo.dict_diag               -S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.dict_lab_sample         in db\data\dbo.dict_lab_sample         -S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.dict_measure_note       in db\data\dbo.dict_measure_note       -S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.dict_measure_rule       in db\data\dbo.dict_measure_rule       -S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.dict_nurse_item         in db\data\dbo.dict_nurse_item         -S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.dict_nurse_record       in db\data\dbo.dict_nurse_record       -S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.dict_nurse_record_value in db\data\dbo.dict_nurse_record_value -S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.dict_ward               in db\data\dbo.dict_ward               -S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_dept            	in db\data\dbo.sys_dept            -S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_employee        	in db\data\dbo.sys_employee        -S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_employee_serve  	in db\data\dbo.sys_employee_serve  -S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_group           	in db\data\dbo.sys_group           -S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**" -E
bcp MNIS_V2.dbo.sys_module          	in db\data\dbo.sys_module          -S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**" -E
bcp MNIS_V2.dbo.sys_operate         	in db\data\dbo.sys_operate         -S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_permission      	in db\data\dbo.sys_permission      -S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**" -E
bcp MNIS_V2.dbo.sys_role            	in db\data\dbo.sys_role            -S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**" -E
bcp MNIS_V2.dbo.sys_role_group      	in db\data\dbo.sys_role_group      -S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_role_permission 	in db\data\dbo.sys_role_permission -S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_user            	in db\data\dbo.sys_user            -S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**" -E
bcp MNIS_V2.dbo.sys_user_group      	in db\data\dbo.sys_user_group      -S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_user_role       	in db\data\dbo.sys_user_role       -S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_user_session 	in db\data\dbo.sys_user_session  	-S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_user_finger 	in db\data\dbo.sys_user_finger  	-S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_skin_test 		in db\data\dbo.rec_skin_test  		-S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_pat_event 		in db\data\dbo.rec_pat_event  		-S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_nurse_shift 	in db\data\dbo.rec_nurse_shift  	-S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_nurse_record_mas 	in db\data\dbo.rec_nurse_record_mas  	-S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_nurse_record_detail in db\data\dbo.rec_nurse_record_detail  -S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_nurse_item_mas 	in db\data\dbo.rec_nurse_item_mas  	-S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_nurse_item_detail 	in db\data\dbo.rec_nurse_item_detail  	-S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_lab_test_mas 	in db\data\dbo.rec_lab_test_mas  	-S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_lab_test_detail 	in db\data\dbo.rec_lab_test_detail  	-S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_inspection_mas 	in db\data\dbo.rec_inspection_mas  	-S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_inspection_detail 	in db\data\dbo.rec_inspection_detail  	-S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_infu_monitor 	in db\data\dbo.rec_infu_monitor  	-S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_diag 		in db\data\dbo.rec_diag  		-S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_body_sign_mas 	in db\data\dbo.rec_body_sign_mas  	-S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_body_sign_detail 	in db\data\dbo.rec_body_sign_detail  	-S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_allergy 		in db\data\dbo.rec_allergy  		-S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.poc_bindings 		in db\data\dbo.poc_bindings  		-S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.phone_bindings 		in db\data\dbo.phone_bindings  		-S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.pat_hosp 		in db\data\dbo.pat_hosp  		-S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.pat_base 		in db\data\dbo.pat_base  		-S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.order_item 		in db\data\dbo.order_item  		-S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.order_group 		in db\data\dbo.order_group  		-S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.order_exec 		in db\data\dbo.order_exec  		-S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.item_price 		in db\data\dbo.item_price  		-S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.criticalvalue_info 	in db\data\dbo.criticalvalue_info  	-S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.ass_nurse_record_dept 	in db\data\dbo.ass_nurse_record_dept  	-S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.ass_nurse_item_dept 	in db\data\dbo.ass_nurse_item_dept  	-S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.ward_patrol 		in db\data\dbo.ward_patrol  		-S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.vital_sign_info 	in db\data\dbo.vital_sign_info  	-S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.task_nurse_attention 	in db\data\dbo.task_nurse_attention  	-S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.hosp_duty 		in db\data\dbo.hosp_duty  		-S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"

bcp MNIS_V2.dbo.DATA_SOURCE 		in db\data\dbo.DATA_SOURCE  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.DEPARTMENT_TEMPALTE 	in db\data\dbo.DEPARTMENT_TEMPALTE  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.DOC_ITEM_OPTION 	in db\data\dbo.DOC_ITEM_OPTION  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.DOC_TABLE_ITEM 		in db\data\dbo.DOC_TABLE_ITEM  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.DOC_TABLE_ITEM_OPTION 	in db\data\dbo.DOC_TABLE_ITEM_OPTION  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.DOC_TABLE_PATTERN 	in db\data\dbo.DOC_TABLE_PATTERN  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.DOC_TABLE_PATTERN_ITEM 	in db\data\dbo.DOC_TABLE_PATTERN_ITEM  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.METADATA 		in db\data\dbo.METADATA  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.METADATA_OPTION 	in db\data\dbo.METADATA_OPTION  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.NURSE_DOCUMENT 		in db\data\dbo.NURSE_DOCUMENT  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.NURSE_DOC_DATA 		in db\data\dbo.NURSE_DOC_DATA  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.NURSE_DOC_DATA_HIS 	in db\data\dbo.NURSE_DOC_DATA_HIS  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.NURSE_DOC_ITEM 		in db\data\dbo.NURSE_DOC_ITEM  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.NURSE_DOC_OPERATE 	in db\data\dbo.NURSE_DOC_OPERATE  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.OPERATE 		in db\data\dbo.OPERATE  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.PAGER 			in db\data\dbo.PAGER  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.TABLE_TEMPLATE 		in db\data\dbo.TABLE_TEMPLATE  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.TABLE_TEMPLATE_ITEM 	in db\data\dbo.TABLE_TEMPLATE_ITEM  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.TABLE_TEMPLATE_PATTERN 	in db\data\dbo.TABLE_TEMPLATE_PATTERN  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.TABLE_TEMPLATE_PATTERN_ITEM in db\data\dbo.TABLE_TEMPLATE_PATTERN_ITEM  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.TEMPLATE in db\data\dbo.TEMPLATE  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.TEMPLATE_ITEM 		in db\data\dbo.TEMPLATE_ITEM  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.TEMPLATE_OPERATE 	in db\data\dbo.TEMPLATE_OPERATE  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.VERIFY_POLICY 		in db\data\dbo.VERIFY_POLICY  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"

:bcp MNIS_V2.dbo.order_group           	in db\data\test.order_group             -S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
:bcp MNIS_V2.dbo.order_item           	in db\data\test.order_item              -S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
:bcp MNIS_V2.dbo.order_exec           	in db\data\test.order_exec              -S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
:bcp MNIS_V2.dbo.pat_base           	in db\data\test.pat_base               	-S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"
:bcp MNIS_V2.dbo.pat_hosp           	in db\data\test.pat_hosp              	-S%sname_sql% -Uuih -PUih888 -c -t"**$**" -r "**&**"

set pwdsa=
echo.
goto succend

:succend
echo ����ɰ�װ��
echo ��л��ѡ����Ӱҽ�������
echo.
goto end
:errorend
echo ��װ����û����ȷ���У��鿴������ʾ�������԰�����ʾ�����εİ�װ���⣬���������Ȼ���ڣ��뷢�ʹ�����Ϣ��zehua.xing@united-imaging.com����Ӱҽ�ƽ��ṩרҵ�ļ���֧�֡�
echo ��װ���򼴽��˳�...
echo.
goto end
:cend
echo ��Ч�����룡
echo �����˳���װ����...
echo.
goto end

:end
pause
