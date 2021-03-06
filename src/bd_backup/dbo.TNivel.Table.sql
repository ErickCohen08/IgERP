USE [bd_ig-projet]
GO
/****** Object:  Table [dbo].[TNivel]    Script Date: 03/06/2018 17:35:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[TNivel](
	[IdNivel] [int] IDENTITY(1,1) NOT NULL,
	[IdStand] [int] NOT NULL,
	[Nombre] [varchar](50) NOT NULL,
	[UsuarioInserta] [varchar](50) NOT NULL,
	[FechaInserta] [datetime] NOT NULL,
	[UsuarioModifica] [varchar](50) NULL,
	[FechaModifica] [datetime] NULL,
	[id_empresa] [int] NULL,
 CONSTRAINT [PK_TNivel] PRIMARY KEY CLUSTERED 
(
	[IdNivel] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
ALTER TABLE [dbo].[TNivel] ADD  CONSTRAINT [DF_TNivel_FechaInserta]  DEFAULT (getdate()) FOR [FechaInserta]
GO
ALTER TABLE [dbo].[TNivel]  WITH CHECK ADD  CONSTRAINT [FK_TNivel_TEmpresa] FOREIGN KEY([id_empresa])
REFERENCES [dbo].[TEmpresa] ([id_empresa])
GO
ALTER TABLE [dbo].[TNivel] CHECK CONSTRAINT [FK_TNivel_TEmpresa]
GO
ALTER TABLE [dbo].[TNivel]  WITH CHECK ADD  CONSTRAINT [FK_TNivel_TStand] FOREIGN KEY([IdStand])
REFERENCES [dbo].[TStand] ([IdStand])
GO
ALTER TABLE [dbo].[TNivel] CHECK CONSTRAINT [FK_TNivel_TStand]
GO
