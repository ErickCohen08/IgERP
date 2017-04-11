USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[usp_Producto_Read]    Script Date: 05/04/2017 04:07:02 p.m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
alter PROCEDURE [dbo].[usp_Proveedor_ReadTablaBuscar]
(
	@pBus			VARCHAR(500) = NULL
)
AS
BEGIN
	
	select
		DBO.TRIM(id_proveedor) id_proveedor,
        DBO.TRIM(ruc) ruc,
        DBO.TRIM(razon_social) razon_social
	from tproveedor
    where
		(@pBus is null or UPPER(DBO.TRIM(razon_social))	like '%'+UPPER(DBO.TRIM(@pBus))+'%') or
		(@pBus is null or UPPER(DBO.TRIM(ruc))			like '%'+UPPER(DBO.TRIM(@pBus))+'%') or
        (@pBus is null or UPPER(DBO.TRIM(direccion))	like '%'+UPPER(DBO.TRIM(@pBus))+'%') or
        (@pBus is null or UPPER(DBO.TRIM(telefono))		like '%'+UPPER(DBO.TRIM(@pBus))+'%') or
        (@pBus is null or UPPER(DBO.TRIM(celular))		like '%'+UPPER(DBO.TRIM(@pBus))+'%') or
        (@pBus is null or UPPER(DBO.TRIM(correo))		like '%'+UPPER(DBO.TRIM(@pBus))+'%')
	ORDER BY razon_social ASC;	

END