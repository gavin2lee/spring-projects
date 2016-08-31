USE [MNIS_V3]GO/****** Object:  Table [dbo].[lx_report_template]    Script Date: 04/27/2015 17:58:59 ******/SET ANSI_NULLS ONGOSET QUOTED_IDENTIFIER ONGOSET ANSI_PADDING ONGOCREATE TABLE [dbo].[lx_report_template](	[rp_st_id] [varchar](30) NOT NULL,	[rp_st_name] [varchar](30) NOT NULL,	[doc_type] [varchar](10) NOT NULL,	[dept_code] [varchar](30) NOT NULL,	[show_index] [int] NOT NULL,	[using] [int] NOT NULL,	[memo] [varchar](50) NULL,	[template_show_name] [varchar](50) NULL,	[template_file_name] [varchar](50) NULL, CONSTRAINT [PK_lx_report_template] PRIMARY KEY CLUSTERED (	[rp_st_id] ASC)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]) ON [PRIMARY]GOSET ANSI_PADDING OFFGOEXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'文书模版名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'lx_report_template', @level2type=N'COLUMN',@level2name=N'rp_st_name'GOEXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'文书类型，0为病例类，1为护理记录（表格类），2为病程类' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'lx_report_template', @level2type=N'COLUMN',@level2name=N'doc_type'GOEXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'科室编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'lx_report_template', @level2type=N'COLUMN',@level2name=N'dept_code'GOEXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'左边显示排序' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'lx_report_template', @level2type=N'COLUMN',@level2name=N'show_index'GOEXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'0为起用状态，1为禁用状态' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'lx_report_template', @level2type=N'COLUMN',@level2name=N'using'GOEXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'模版文件名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'lx_report_template', @level2type=N'COLUMN',@level2name=N'template_show_name'GOALTER TABLE [dbo].[lx_report_template] ADD  CONSTRAINT [DF_lx_report_template_show_index]  DEFAULT ((0)) FOR [show_index]GOALTER TABLE [dbo].[lx_report_template] ADD  CONSTRAINT [DF_lx_report_template_using]  DEFAULT ((0)) FOR [using]GO/****** Object:  Table [dbo].[lx_report_template_item]    Script Date: 04/27/2015 17:59:26 ******/SET ANSI_NULLS ONGOSET QUOTED_IDENTIFIER ONGOSET ANSI_PADDING ONGOCREATE TABLE [dbo].[lx_report_template_item](	[rp_st_item_id] [varchar](30) NOT NULL,	[rp_st_id] [varchar](30) NOT NULL,	[rp_st_item_name] [varchar](30) NOT NULL,	[required] [bit] NOT NULL, CONSTRAINT [PK_lx_report_template_item] PRIMARY KEY CLUSTERED (	[rp_st_item_id] ASC)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]) ON [PRIMARY]GOSET ANSI_PADDING OFFGOEXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否必填，0为非必填，1为必填' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'lx_report_template_item', @level2type=N'COLUMN',@level2name=N'required'GOALTER TABLE [dbo].[lx_report_template_item] ADD  CONSTRAINT [DF_lx_report_template_item_required]  DEFAULT ((0)) FOR [required]GO