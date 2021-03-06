USE [bd_ig-projet]
GO
/****** Object:  Table [dbo].[TStand]    Script Date: 03/06/2018 17:35:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[TStand](
	[IdStand] [int] IDENTITY(1,1) NOT NULL,
	[IdAlmacen] [int] NOT NULL,
	[Nombre] [varchar](50) NOT NULL,
	[UsuarioInserta] [varchar](50) NOT NULL,
	[FechaInserta] [datetime] NOT NULL,
	[UsuarioModifica] [varchar](50) NULL,
	[FechaModifica] [datetime] NULL,
	[id_empresa] [int] NULL,
 CONSTRAINT [PK_TStand] PRIMARY KEY CLUSTERED 
(
	[IdStand] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
ALTER TABLE [dbo].[TStand] ADD  CONSTRAINT [DF_TStand_FechaInserta]  DEFAULT (getdate()) FOR [FechaInserta]
GO
ALTER TABLE [dbo].[TStand]  WITH CHECK ADD  CONSTRAINT [FK_TStand_TDatoComun] FOREIGN KEY([IdAlmacen])
REFERENCES [dbo].[TDatoComun] ([IdDatoComun])
GO
ALTER TABLE [dbo].[TStand] CHECK CONSTRAINT [FK_TStand_TDatoComun]
GO
ALTER TABLE [dbo].[TStand]  WITH CHECK ADD  CONSTRAINT [FK_TStand_TEmpresa] FOREIGN KEY([id_empresa])
REFERENCES [dbo].[TEmpresa] ([id_empresa])
GO
ALTER TABLE [dbo].[TStand] CHECK CONSTRAINT [FK_TStand_TEmpresa]
GO
