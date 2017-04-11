USE [bd_ig-projet]
GO
/****** Object:  Table [dbo].[TKardex]    Script Date: 10/04/2017 09:28:09 a.m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[TKardex](
	[IdKardex] [int] NOT NULL,
	[IdAccion] [int] NOT NULL,
	[IdProducto] [int] NULL,
	[IdOperacion] [int] NOT NULL,
	[Cantidad] [numeric](18, 2) NULL,
	[UsuarioInserta] [varchar](50) NULL,
	[FechaInserta] [datetime] NULL,
 CONSTRAINT [PK_Kardex] PRIMARY KEY CLUSTERED 
(
	[IdKardex] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
