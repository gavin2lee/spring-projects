#   FAS ��ʼ������
#   version:GMS-0.1.1
#   MODIFIED				(MM/DD/YY)
#   yang.h						08/28/28
#   
#   
#   *********************FASϵͳ��ʼ��*******************
#  1.FAS��������    Insert		lookup,lookup_entry,lookup_rel��
#--------------------------------------------------------------


tee 5.dml.log

select
'FAS ��ʼ������'		as name,
'FAS-0.1.1'			as version \G

source 5.dml/5.1.fas_data.sql

notee