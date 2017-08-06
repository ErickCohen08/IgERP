USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[usp_CompraMaterial_Read]    Script Date: 05/08/2017 18:04:39 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[usp_CompraMaterial_Read]
(
	@pIdCompra		int = NULL,
	@pFechaCompra	DateTime = NULL,
	@pNumeroCompra	varchar(15) = NULL,
    @pDesProveedor	varchar(500) = NULL,
    @pDesMoneda		varchar(500) = NULL,
    @pDesObra		varchar(500) = NULL,
    @pDesDocumento	varchar(500) = NULL,
	@pDesEstadoAbierto	varchar(500) = NULL,
    @pId_empresa		int = NULL    
)
AS
BEGIN
	
	SELECT 
		ROW_NUMBER() OVER (ORDER BY IdCompra DESC) AS fila,
		IdCompra,
		FechaCompra,
		NumeroCompra,
		Total,
		DesProveedor,
		DesMoneda,
		DesDocumento,
		DesEstadoAbierto
	FROM 
	(
		SELECT 
			com.IdCompra,
			com.FechaCompra,
			com.NumeroCompra,
			com.Total,
			DBO.TRIM(ISNULL(pro.razon_social, ''))		DesProveedor,
			DBO.TRIM(ISNULL(dc2.DescripcionCorta, ''))	DesMoneda,
			DBO.TRIM(ISNULL(dc11.DescripcionCorta, ''))	DesDocumento,			
			DBO.TRIM(ISNULL(dc10.DescripcionCorta, ''))	DesEstadoAbierto
		FROM TCompra com
			LEFT JOIN TProveedor	pro	 on (pro.id_proveedor = com.id_proveedor and pro.id_empresa = com.id_empresa)
			LEFT JOIN TObras		obr	 on (obr.IdObra = com.id_obra and obr.id_empresa = com.id_empresa) 
			LEFT JOIN TDatoComun	dc10 on (dc10.IdDatoComun = com.EstadoAbierto)
			LEFT JOIN TDatoComun	dc11 on (dc11.IdDatoComun = com.id_documento)
			LEFT JOIN TDatoComun    dc2  on (dc2.IdDatoComun = com.id_moneda)
		WHERE			
			(@pIdCompra		= 0 OR com.IdCompra = @pIdCompra) AND
			(@pid_empresa	= 0 OR com.id_empresa = @pid_empresa) AND
			
			(@pFechaCompra IS NULL OR CAST(com.FechaCompra AS date) = CAST(@pFechaCompra AS date)) and
			
			(@pNumeroCompra		IS NULL OR UPPER(DBO.TRIM(com.NumeroCompra))		like '%'+UPPER(DBO.TRIM(@pNumeroCompra))+'%')		and
			(@pDesProveedor		IS NULL OR UPPER(DBO.TRIM(pro.razon_social))		like '%'+UPPER(DBO.TRIM(@pDesProveedor))+'%' )		and 
			(@pDesMoneda		IS NULL OR UPPER(DBO.TRIM(dc2.DescripcionCorta))	like '%'+UPPER(DBO.TRIM(@pDesMoneda))+'%')			and 
			(@pDesObra			IS NULL OR UPPER(DBO.TRIM(obr.Descripcion))			like '%'+UPPER(DBO.TRIM(@pDesObra))+'%')			and
			(@pDesDocumento		IS NULL OR UPPER(DBO.TRIM(dc11.DescripcionCorta))	like '%'+UPPER(DBO.TRIM(@pDesDocumento))+'%')		and
			(@pDesEstadoAbierto	IS NULL OR UPPER(DBO.TRIM(dc10.DescripcionCorta))	like '%'+UPPER(DBO.TRIM(@pDesEstadoAbierto))+'%')	and
			
			com.EstadoActivo = 70001
	) as t
	ORDER BY t.IdCompra desc;
	
END