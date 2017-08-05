USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[usp_CompraMaterial_Read]    Script Date: 05/08/2017 10:09:17 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[spCompraMaterial_Read]
(
	@pIdCompra		int = NULL,
	@pId_empresa		int = NULL    
)
AS
BEGIN
	
	SELECT 
		dcom.IdDetalleCompra,
		dcom.IdCompra,
		pro.id_producto,
		dcom.Cantidad,
		dcom.PrecioUnitario,
		dcom.PrecioTotal,
		pro.descripcion DesMaterial,
		dc.DescripcionCorta DesUnidad
	FROM TDetalle_compra dcom
		LEFT JOIN TProducto pro	on (pro.id_producto = dcom.id_producto)
		LEFT JOIN TDatoComun dc on (dc.IdDatoComun = pro.id_unidad)
	WHERE			
	(@pIdCompra		= 0 OR dcom.IdCompra = @pIdCompra) AND
	(@pid_empresa	= 0 OR dcom.id_empresa = @pid_empresa)
END