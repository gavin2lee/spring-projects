@shift
@echo off
title ��Ӱҽ���ƶ�������Ϣϵͳ��װ��...
mode con cols=100 lines=9999
color 3f
cls
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

echo �����潫����ϵͳ�ĳ�ʼ������...��

bcp MNIS_V2.dbo.sys_user_session out .\db\data_bak\dbo.sys_user_session  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_user_role out .\db\data_bak\dbo.sys_user_role  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_user_group out .\db\data_bak\dbo.sys_user_group  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_user_finger out .\db\data_bak\dbo.sys_user_finger  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_user out .\db\data_bak\dbo.sys_user  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_role_permission out .\db\data_bak\dbo.sys_role_permission  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_role_group out .\db\data_bak\dbo.sys_role_group  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_role out .\db\data_bak\dbo.sys_role  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_permission out .\db\data_bak\dbo.sys_permission  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_operate out .\db\data_bak\dbo.sys_operate  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_module out .\db\data_bak\dbo.sys_module  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_group out .\db\data_bak\dbo.sys_group  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_employee_serve out .\db\data_bak\dbo.sys_employee_serve  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_employee out .\db\data_bak\dbo.sys_employee  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.sys_dept out .\db\data_bak\dbo.sys_dept  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_skin_test out .\db\data_bak\dbo.rec_skin_test  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_pat_event out .\db\data_bak\dbo.rec_pat_event  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_nurse_shift out .\db\data_bak\dbo.rec_nurse_shift  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_nurse_record_mas out .\db\data_bak\dbo.rec_nurse_record_mas  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_nurse_record_detail out .\db\data_bak\dbo.rec_nurse_record_detail  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_nurse_item_mas out .\db\data_bak\dbo.rec_nurse_item_mas  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_nurse_item_detail out .\db\data_bak\dbo.rec_nurse_item_detail  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_lab_test_mas out .\db\data_bak\dbo.rec_lab_test_mas  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_lab_test_detail out .\db\data_bak\dbo.rec_lab_test_detail  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_inspection_mas out .\db\data_bak\dbo.rec_inspection_mas  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_inspection_detail out .\db\data_bak\dbo.rec_inspection_detail  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_infu_monitor out .\db\data_bak\dbo.rec_infu_monitor  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_diag out .\db\data_bak\dbo.rec_diag  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_body_sign_mas out .\db\data_bak\dbo.rec_body_sign_mas  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_body_sign_detail out .\db\data_bak\dbo.rec_body_sign_detail  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.rec_allergy out .\db\data_bak\dbo.rec_allergy  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.poc_bindings out .\db\data_bak\dbo.poc_bindings  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.phone_bindings out .\db\data_bak\dbo.phone_bindings  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.pat_hosp out .\db\data_bak\dbo.pat_hosp  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.pat_base out .\db\data_bak\dbo.pat_base  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.order_item out .\db\data_bak\dbo.order_item  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.order_group out .\db\data_bak\dbo.order_group  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.order_exec out .\db\data_bak\dbo.order_exec  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.item_price out .\db\data_bak\dbo.item_price  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.criticalvalue_info out .\db\data_bak\dbo.criticalvalue_info  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.bed out .\db\data_bak\dbo.bed  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.ass_nurse_record_dept out .\db\data_bak\dbo.ass_nurse_record_dept  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.ass_nurse_item_dept out .\db\data_bak\dbo.ass_nurse_item_dept  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.ward_patrol out .\db\data_bak\dbo.ward_patrol  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.vital_sign_info out .\db\data_bak\dbo.vital_sign_info  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.task_nurse_attention out .\db\data_bak\dbo.task_nurse_attention  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.hosp_duty out .\db\data_bak\dbo.hosp_duty  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"

bcp MNIS_V2.dbo.DATA_SOURCE out .\db\data_bak\dbo.DATA_SOURCE  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.DEPARTMENT_TEMPALTE out .\db\data_bak\dbo.DEPARTMENT_TEMPALTE  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.DOC_ITEM_OPTION out .\db\data_bak\dbo.DOC_ITEM_OPTION  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.DOC_TABLE_ITEM out .\db\data_bak\dbo.DOC_TABLE_ITEM  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.DOC_TABLE_ITEM_OPTION out .\db\data_bak\dbo.DOC_TABLE_ITEM_OPTION  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.DOC_TABLE_PATTERN out .\db\data_bak\dbo.DOC_TABLE_PATTERN  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.DOC_TABLE_PATTERN_ITEM out .\db\data_bak\dbo.DOC_TABLE_PATTERN_ITEM  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.METADATA out .\db\data_bak\dbo.METADATA  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.METADATA_OPTION out .\db\data_bak\dbo.METADATA_OPTION  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.NURSE_DOCUMENT out .\db\data_bak\dbo.NURSE_DOCUMENT  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.NURSE_DOC_DATA out .\db\data_bak\dbo.NURSE_DOC_DATA  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.NURSE_DOC_DATA_HIS out .\db\data_bak\dbo.NURSE_DOC_DATA_HIS  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.NURSE_DOC_ITEM out .\db\data_bak\dbo.NURSE_DOC_ITEM  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.NURSE_DOC_OPERATE out .\db\data_bak\dbo.NURSE_DOC_OPERATE  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.OPERATE out .\db\data_bak\dbo.OPERATE  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.PAGER out .\db\data_bak\dbo.PAGER  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.TABLE_TEMPLATE out .\db\data_bak\dbo.TABLE_TEMPLATE  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.TABLE_TEMPLATE_ITEM out .\db\data_bak\dbo.TABLE_TEMPLATE_ITEM  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.TABLE_TEMPLATE_PATTERN out .\db\data_bak\dbo.TABLE_TEMPLATE_PATTERN  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.TABLE_TEMPLATE_PATTERN_ITEM out .\db\data_bak\dbo.TABLE_TEMPLATE_PATTERN_ITEM  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.TEMPLATE out .\db\data_bak\dbo.TEMPLATE  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.TEMPLATE_ITEM out .\db\data_bak\dbo.TEMPLATE_ITEM  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.TEMPLATE_OPERATE out .\db\data_bak\dbo.TEMPLATE_OPERATE  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"
bcp MNIS_V2.dbo.VERIFY_POLICY out .\db\data_bak\dbo.VERIFY_POLICY  -S%sname_sql% -Usa -P%pwdsa% -c -t"**$**" -r "**&**"

set pwdsa=
echo.
goto succend

:succend
echo ����ɵ�����
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
