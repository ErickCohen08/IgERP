USE [bd_ig-projet]
GO
/****** Object:  Table [dbo].[DatoComun]    Script Date: 03/04/2017 10:04:24 a.m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[TDatoComun](
	[IdDatoComun] [int] NOT NULL,
	[CodigoTabla] [int] NOT NULL,
	[CodigoFila] [int] NOT NULL,
	[DescripcionCorta] [varchar](100) NULL,
	[DescripcionLarga] [varchar](300) NULL,
	[ValorTexto1] [varchar](500) NULL,
	[ValorTexto2] [varchar](500) NULL,
	[UsuarioInserta] [varchar](50) NULL,
	[FechaInserta] [datetime] NOT NULL,
	[UsuarioModifica] [varchar](50) NULL,
	[FechaModifica] [datetime] NULL,
	[Estado] [char](1) NOT NULL,
	[id_empresa] [int] NULL,
 CONSTRAINT [PK_DatoComun] PRIMARY KEY CLUSTERED 
(
	[IdDatoComun] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
ALTER TABLE [dbo].[TDatoComun] ADD  CONSTRAINT [DF_DatoComun_FechaInserta]  DEFAULT (getdate()) FOR [FechaInserta]
GO
ALTER TABLE [dbo].[TDatoComun] ADD  CONSTRAINT [DF_DatoComun_Estado]  DEFAULT ('A') FOR [Estado]
GO
