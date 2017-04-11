USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[usp_Producto_Read]    Script Date: 08/04/2017 18:06:40 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[usp_Producto_Read]
(
	@pid_producto			int = NULL,
	
	@pcodigo				varchar(20) = NULL,
	@pdescripcion			varchar(500) = NULL,
	@pmodelo				varchar(100) = NULL,
	@pmarca					varchar(100) = NULL,
	@pdescripcion_coloquial varchar(500) = NULL,
	
	@pid_empresa			int = NULL,
	
	@pDesmoneda				varchar(500) = NULL,
	@pDesunidad				varchar(500) = NULL,
	@pDesproductotipo		varchar(500) = NULL,
	@pDesAlmacen			varchar(500) = NULL,
	@pDesReferencia_precio	varchar(500) = NULL
)
AS
BEGIN
	
	SELECT 
		ROW_NUMBER() OVER (ORDER BY FechaInserta) AS fila,
		id_producto,
		codigo,
		descripcion,
		Desunidad,
		cantidad,
		Desmoneda,
		precio_promedio,
		marca,
		modelo,
		FechaInserta
	FROM 
	(
		SELECT		
			ISNULL(p.id_producto, 0) id_producto, 
			dbo.TRIM(ISNULL(p.codigo, '')) codigo,
			dbo.TRIM(ISNULL(p.descripcion, '')) descripcion, 
			DBO.TRIM(ISNULL(unid.DescripcionCorta, '')) Desunidad,
			ISNULL(p.cantidad,0) cantidad, 
			DBO.TRIM(ISNULL(mone.ValorTexto1, '')) Desmoneda,
			ISNULL(p.precio_promedio, 0) precio_promedio,
			dbo.TRIM(ISNULL(p.marca, '')) marca,
			dbo.TRIM(ISNULL(p.modelo, '')) modelo,
			p.FechaInserta
		FROM TProducto p
			LEFT JOIN TDatoComun mone on p.id_moneda = mone.IdDatoComun
			LEFT JOIN TDatoComun unid on p.id_unidad = unid.IdDatoComun
			LEFT JOIN TDatoComun cate on p.id_productotipo = cate.IdDatoComun
			LEFT JOIN TDatoComun alma on p.id_Almacen = alma.IdDatoComun
			LEFT JOIN TDatoComun refe on p.id_Referencia_precio = refe.IdDatoComun
		WHERE 
			(@pcodigo					IS NULL OR UPPER(p.codigo) LIKE '%'+UPPER(@pcodigo)+'%' ) AND 
			(@pdescripcion				IS NULL OR UPPER(p.descripcion) LIKE '%'+UPPER(@pdescripcion)+'%' ) AND 
			(@pmodelo					IS NULL OR UPPER(p.modelo) LIKE '%'+UPPER(@pmodelo)+'%') AND
			(@pmarca					IS NULL OR UPPER(p.marca) LIKE '%'+UPPER(@pmarca)+'%') AND
			(@pdescripcion_coloquial	IS NULL OR UPPER(p.descripcion_coloquial) LIKE '%'+UPPER(@pdescripcion_coloquial)+'%') AND
		
			(@pid_producto	= 0 OR p.id_producto = @pid_producto) AND
			(@pid_empresa	= 0 OR p.id_empresa = @pid_empresa) AND
		
			(@pDesmoneda				IS NULL OR UPPER(mone.DescripcionCorta) LIKE '%'+UPPER(@pDesmoneda)+'%') AND 
			(@pDesunidad				IS NULL OR UPPER(unid.DescripcionCorta) LIKE '%'+UPPER(@pDesunidad)+'%') AND 
			(@pDesproductotipo			IS NULL OR UPPER(cate.DescripcionCorta) LIKE '%'+UPPER(@pDesproductotipo)+'%') AND 
			(@pDesAlmacen				IS NULL OR UPPER(alma.DescripcionCorta) LIKE '%'+UPPER(@pDesAlmacen)+'%') AND 
			(@pDesReferencia_precio		IS NULL OR UPPER(refe.DescripcionCorta) LIKE '%'+UPPER(@pDesReferencia_precio)+'%') AND 
			(p.estado = 70001)		
	) as t
	ORDER BY t.FechaInserta desc;
	

END