USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[usp_Producto_Read]    Script Date: 06/08/2017 12:07:38 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[spProductoDetalle_Read]
(
	@pid_producto			int = NULL,
	@pid_empresa			int = NULL	
)
AS
BEGIN
	
	SELECT 
		com.NumeroCompra numeroCompra,
		ISNULL(FORMAT(com.FechaCompra, 'd', 'en-gb'),'') fechaCompra,
		DBO.TRIM(prov.razon_social) razonsocialProveedor,
		DBO.TRIM(prov.ruc) rucProveedor,
		ISNULL(DBO.TRIM(dc2.ValorTexto1), '')	desMoneda,
		dcom.PrecioUnitario
	FROM TDetalle_compra dcom
		INNER JOIN TCompra		com		on (com.IdCompra = dcom.IdCompra and com.id_empresa = dcom.id_empresa)
		INNER JOIN TProveedor	prov	on prov.id_proveedor = com.id_proveedor
		INNER JOIN TProducto	prod	on prod.id_producto = dcom.id_producto
		INNER JOIN TDatoComun   dc2		on (dc2.IdDatoComun = com.id_moneda)
	WHERE
		dcom.id_producto = @pid_producto and
		dcom.id_empresa = @pid_empresa
	ORDER BY com.FechaCompra desc

END