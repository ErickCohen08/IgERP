USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[usp_Producto_GetData]    Script Date: 02/06/2018 11:11:49 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[usp_Producto_GetData]
(
	@pid_producto			int = NULL
)
AS
BEGIN
	
		SELECT
			ISNULL(p.id_producto, 0) id_producto, 
			dbo.TRIM(ISNULL(p.codigo, '')) codigo,
			dbo.TRIM(ISNULL(p.descripcion, '')) descripcion, 
			dbo.TRIM(ISNULL(p.descripcion_coloquial, '')) descripcion_coloquial,
			DBO.TRIM(ISNULL(mone.DescripcionCorta, '')) Desmoneda,
			ISNULL(p.precio_promedio, 0) precio_promedio,
			dbo.TRIM(ISNULL(p.marca, '')) marca,
			dbo.TRIM(ISNULL(p.modelo, '')) modelo,
			ISNULL(p.peso, 0) peso,
			DBO.TRIM(ISNULL(unid.DescripcionCorta, '')) Desunidad,
			DBO.TRIM(ISNULL(alma.DescripcionCorta, '')) DesAlmacen,
			DBO.TRIM(ISNULL(refe.DescripcionCorta, '')) DesReferencia_precio,
			DBO.TRIM(ISNULL(cate.DescripcionCorta, '')) Desproductotipo,
			ISNULL(p.cantidad,0) cantidad,
			p.id_Almacen,
			p.IdStand,
			p.IdNivel,
			DBO.TRIM(ISNULL(stan.Nombre, '')) DesStand,
			DBO.TRIM(ISNULL(nive.Nombre, '')) DesNivel
		FROM TProducto p
			LEFT JOIN TDatoComun mone on p.id_moneda = mone.IdDatoComun
			LEFT JOIN TDatoComun unid on p.id_unidad = unid.IdDatoComun
			LEFT JOIN TDatoComun cate on p.id_productotipo = cate.IdDatoComun
			LEFT JOIN TDatoComun alma on p.id_Almacen = alma.IdDatoComun
			LEFT JOIN TDatoComun refe on p.id_Referencia_precio = refe.IdDatoComun
			LEFT JOIN TStand stan on stan.IdStand = p.IdStand
			LEFT JOIN TNivel nive on nive.IdNivel = p.IdNivel
		WHERE 
			(@pid_producto	= 0 OR p.id_producto = @pid_producto)	
END