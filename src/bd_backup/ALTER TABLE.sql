--TPRODUCTO
ALTER TABLE dbo.TProducto ADD cantidad NUMERIC(18,2) NULL;
GO
ALTER TABLE dbo.TProducto ADD id_Almacen INT NULL;
GO
ALTER TABLE dbo.TProducto ADD id_Referencia_precio INT NULL;
GO
ALTER TABLE dbo.TProducto ADD id_tipo_producto INT NULL;
GO
ALTER TABLE dbo.TProducto ADD estado INT NULL;
GO
ALTER TABLE dbo.TProducto ADD cierreInventario INT NULL;
GO

ALTER TABLE dbo.TProducto ALTER COLUMN peso NUMERIC (18, 2) ;  
GO
ALTER TABLE dbo.TProducto ALTER COLUMN precio_promedio NUMERIC (18, 2);  
GO
ALTER TABLE dbo.TProducto_detalle ALTER COLUMN precio NUMERIC (18, 2);  
GO


ALTER TABLE dbo.TProducto ADD  CONSTRAINT DF_TProducto_precio_promedio  DEFAULT (0) FOR precio_promedio
GO
ALTER TABLE dbo.TProducto ADD  CONSTRAINT DF_TProducto_estado  DEFAULT (70001) FOR estado
GO
ALTER TABLE dbo.TProducto ADD  CONSTRAINT DF_TProducto_cierreInventario  DEFAULT (80001) FOR cierreInventario
GO
ALTER TABLE dbo.TProducto_detalle ADD  CONSTRAINT DF_TProductoDetalle_precio  DEFAULT (0) FOR precio
GO

update TProducto set estado = 70001;
update TProducto set cierreInventario = 80001; 

update TProducto set id_Referencia_precio = 40002 where  referencia_precio like '%REVISTA COSTOS%';
ALTER TABLE dbo.TProducto DROP COLUMN referencia_precio;


update TProducto set id_tipo_producto = 50001 where  tipo_producto like '%PRECIO DE MATERIAL%';
update TProducto set id_tipo_producto = 50002 where  tipo_producto like '%PRECIOS DE PARTIDA%';
ALTER TABLE dbo.TProducto DROP COLUMN tipo_producto;


update TProducto set id_moneda = 20001 where  id_moneda = 1;
update TProducto set id_moneda = 20002 where  id_moneda = 2;

ALTER TABLE dbo.TProducto DROP COLUMN guardado;

ALTER TABLE dbo.TProducto DROP CONSTRAINT FK_TProducto_TUnidad_producto;   
GO  

update TProducto set id_unidad = 10001 where id_unidad = 1;
update TProducto set id_unidad = 10002 where id_unidad = 2;
update TProducto set id_unidad = 10003 where id_unidad = 3;
update TProducto set id_unidad = 10004 where id_unidad = 4;
update TProducto set id_unidad = 10005 where id_unidad = 5;
update TProducto set id_unidad = 10007 where id_unidad = 7;
update TProducto set id_unidad = 10010 where id_unidad = 10;
update TProducto set id_unidad = 10011 where id_unidad = 11;
update TProducto set id_unidad = 10012 where id_unidad = 12;
update TProducto set id_unidad = 10014 where id_unidad = 14;
update TProducto set id_unidad = 10015 where id_unidad = 15;
update TProducto set id_unidad = 10016 where id_unidad = 16;
update TProducto set id_unidad = 10017 where id_unidad = 17;
update TProducto set id_unidad = 10018 where id_unidad = 18;
update TProducto set id_unidad = 10019 where id_unidad = 19;
update TProducto set id_unidad = 10020 where id_unidad = 22;
update TProducto set id_unidad = 10021 where id_unidad = 23;
update TProducto set id_unidad = 10022 where id_unidad = 24;
update TProducto set id_unidad = 10023 where id_unidad = 25;
update TProducto set id_unidad = 10024 where id_unidad = 26;
update TProducto set id_unidad = 10025 where id_unidad = 27;
update TProducto set id_unidad = 10027 where id_unidad = 29;
update TProducto set id_unidad = 10028 where id_unidad = 30;
update TProducto set id_unidad = 10029 where id_unidad = 31;

ALTER TABLE dbo.TProducto DROP CONSTRAINT FK_TProducto_TUsuario;
GO  
ALTER TABLE dbo.TProducto DROP COLUMN id_usuario;
GO
ALTER TABLE dbo.TProducto DROP COLUMN f_creacion;
GO
ALTER TABLE dbo.TProducto DROP COLUMN f_modificacion;
GO
ALTER TABLE dbo.TProducto ADD UsuarioInserta VARCHAR(50) NULL;
GO
ALTER TABLE dbo.TProducto ADD FechaInserta DATETIME NULL;
GO
ALTER TABLE dbo.TProducto ADD UsuarioModifica VARCHAR(50) NULL;
GO
ALTER TABLE dbo.TProducto ADD FechaModifica DATETIME NULL;
GO
ALTER TABLE dbo.TProducto ADD  CONSTRAINT DF_TProducto_FechaInserta  DEFAULT (getdate()) FOR FechaInserta
GO



update TProducto set id_productotipo = 60001 where id_productotipo = 9;
update TProducto set id_productotipo = 60002 where id_productotipo = 10;
update TProducto set id_productotipo = 60003 where id_productotipo = 11;
update TProducto set id_productotipo = 60004 where id_productotipo = 12;
update TProducto set id_productotipo = 60005 where id_productotipo = 13;
update TProducto set id_productotipo = 60006 where id_productotipo = 14;
update TProducto set id_productotipo = 60007 where id_productotipo = 15;
update TProducto set id_productotipo = 60008 where id_productotipo = 16;
update TProducto set id_productotipo = 60009 where id_productotipo = 17;
update TProducto set id_productotipo = 60010 where id_productotipo = 18;
update TProducto set id_productotipo = null where id_productotipo not in (60001, 60002, 60003, 60004, 60005, 60006, 60007, 60008, 60009, 60010);



ALTER TABLE [dbo].[TProducto]  WITH CHECK ADD  CONSTRAINT [FK_TProducto_TDatoComun] FOREIGN KEY([id_moneda])
REFERENCES [dbo].[TDatoComun] ([IdDatoComun])
GO
ALTER TABLE [dbo].[TProducto] CHECK CONSTRAINT [FK_TProducto_TDatoComun]
GO
ALTER TABLE [dbo].[TProducto]  WITH CHECK ADD  CONSTRAINT [FK_TProducto_TDatoComun1] FOREIGN KEY([id_unidad])
REFERENCES [dbo].[TDatoComun] ([IdDatoComun])
GO
ALTER TABLE [dbo].[TProducto] CHECK CONSTRAINT [FK_TProducto_TDatoComun1]
GO
ALTER TABLE [dbo].[TProducto]  WITH CHECK ADD  CONSTRAINT [FK_TProducto_TDatoComun2] FOREIGN KEY([id_productotipo])
REFERENCES [dbo].[TDatoComun] ([IdDatoComun])
GO
ALTER TABLE [dbo].[TProducto] CHECK CONSTRAINT [FK_TProducto_TDatoComun2]
GO
ALTER TABLE [dbo].[TProducto]  WITH CHECK ADD  CONSTRAINT [FK_TProducto_TDatoComun3] FOREIGN KEY([id_Almacen])
REFERENCES [dbo].[TDatoComun] ([IdDatoComun])
GO
ALTER TABLE [dbo].[TProducto] CHECK CONSTRAINT [FK_TProducto_TDatoComun3]
GO
ALTER TABLE [dbo].[TProducto]  WITH CHECK ADD  CONSTRAINT [FK_TProducto_TDatoComun4] FOREIGN KEY([id_Referencia_precio])
REFERENCES [dbo].[TDatoComun] ([IdDatoComun])
GO
ALTER TABLE [dbo].[TProducto] CHECK CONSTRAINT [FK_TProducto_TDatoComun4]
GO
ALTER TABLE [dbo].[TProducto] CHECK CONSTRAINT [FK_TProducto_TDatoComun5]
GO

ALTER TABLE dbo.TProducto DROP COLUMN id_tipo_producto;
GO
ALTER TABLE dbo.TProducto DROP COLUMN precio_manoobra;
GO
ALTER TABLE dbo.TProducto DROP COLUMN precio_material;
GO
ALTER TABLE dbo.TProducto DROP COLUMN precio_equipo;
GO