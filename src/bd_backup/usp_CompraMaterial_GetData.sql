USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[usp_CompraMaterial_GetData]    Script Date: 05/08/2017 15:23:36 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[usp_CompraMaterial_GetData]
(
	@pIdCompra		int = NULL,
	@pid_empresa	int = NULL
)
AS
BEGIN
	SELECT
		com.IdCompra,
		com.FechaCompra,
		com.NumeroCompra,
        com.CalculoIgv,
        com.SubTotal,
        com.Total,
        com.TotalLetras,
        com.id_proveedor,
        com.id_moneda,
        com.id_igv,
        com.id_obra,
        com.id_documento,
        com.id_empresa,
        com.EstadoAbierto,

		DBO.TRIM(ISNULL(pro.razon_social, ''))		DesProveedor,
		DBO.TRIM(ISNULL(pro.ruc, ''))				RucProveedor,
		DBO.TRIM(ISNULL(dc2.DescripcionCorta, ''))	DesMoneda,
		DBO.TRIM(ISNULL(igv.igv, ''))				DesIgv,
		DBO.TRIM(ISNULL(obr.Descripcion, ''))		DesObra,
		DBO.TRIM(ISNULL(dc11.DescripcionCorta, ''))	DesDocumento,
		DBO.TRIM(ISNULL(dc10.DescripcionCorta, ''))	DesEstadoAbierto

	FROM TCompra com
		LEFT JOIN TProveedor	pro	 on (pro.id_proveedor = com.id_proveedor and pro.id_empresa = com.id_empresa)
		LEFT JOIN TObras		obr	 on (obr.IdObra = com.id_obra and obr.id_empresa = com.id_empresa) 
		LEFT JOIN TDatoComun	dc10 on (dc10.IdDatoComun = com.EstadoAbierto)
		LEFT JOIN TDatoComun	dc11 on (dc11.IdDatoComun = com.id_documento)
		LEFT JOIN TDatoComun    dc2  on (dc2.IdDatoComun = com.id_moneda)
		LEFT JOIN TIgv			igv  on (igv.id_igv = com.id_igv and igv.id_empresa = com.id_empresa)
	WHERE 
		(@pIdCompra = 0 OR com.IdCompra = @pIdCompra) AND
		(@pid_empresa = 0 OR com.id_empresa = @pid_empresa) 
END