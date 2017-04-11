USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[usp_CRUD_DataComun]    Script Date: 03/04/2017 11:56:06 a.m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[usp_CRUD_Producto]
(
	@pTipoOpe			char(1),
	@pid_producto		int = NULL,
	@pcodigo			varchar(20) = NULL,
	@pdescripcion		varchar(500) = NULL,
	@pmodelo			varchar(100) = NULL,
	@ppeso				numeric(18, 2) = NULL,
	@pmarca				varchar(100) = NULL,
	@pid_unidad			int = NULL,
	@pid_productotipo	int = NULL,
	@pid_empresa		int = NULL,
	@pid_usuario		int = NULL,
	@pid_moneda			int = NULL,
	@pprecio_promedio	numeric(18, 2) = NULL,
	@pprecio_manoobra	numeric(18, 2) = NULL,
	@pprecio_material	numeric(18, 2) = NULL,
	@pprecio_equipo		numeric(18, 2) = NULL,
	@preferencia_precio varchar(500) = NULL,
	@pdescripcion_coloquial varchar(500) = NULL,
	@ptipo_producto		varchar(50) = NULL,
	@pcantidad			numeric(18, 2) = NULL
)
AS
BEGIN
	
	IF @pTipoOpe = 'S' 
	BEGIN
		SELECT 
		p.id_producto, 
		ISNULL(p.codigo, '') codigo, 
		DBO.TRIM(p.descripcion) descripcion, 
		up.codigo,
		ISNULL(p.cantidad,0) cantidad, 
		DBO.TRIM(m.simbolo) simbolo,
		p.precio_promedio 
		FROM TProducto p
		LEFT JOIN TUnidad_producto up on p.id_unidad = up.id_unidad
		LEFT JOIN TMoneda m on p.id_moneda = m.id_moneda
		WHERE
		(@pdescripcion IS NULL OR p.descripcion LIKE '%'+@pdescripcion+'%' ) AND 
		(@pcodigo IS NULL OR p.codigo LIKE '%'+@pcodigo+'%' ) AND 
		(@pmodelo IS NULL OR p.modelo LIKE '%'+@pmodelo+'%') AND
		(@pmarca IS NULL OR p.marca LIKE '%'+@pmarca+'%') AND
		(@preferencia_precio IS NULL OR p.referencia_precio LIKE '%'+@preferencia_precio+'%') AND
		(@pdescripcion_coloquial IS NULL OR p.descripcion_coloquial LIKE '%'+@pdescripcion_coloquial+'%') AND
		(@pid_producto IS NULL OR p.id_producto = @pid_producto) AND
		(@pid_moneda IS NULL OR p.id_moneda = @pid_moneda) AND
		(@pid_productotipo IS NULL OR p.id_productotipo = @pid_productotipo) AND
		(@pid_unidad IS NULL OR p.id_unidad = @pid_unidad) AND
		(@pid_empresa IS NULL OR p.id_empresa = @pid_empresa);
	END

	IF @pTipoOpe = 'I' 
	BEGIN
		INSERT INTO TProducto (
		codigo, descripcion,modelo,peso,marca,id_unidad,id_productotipo,id_empresa,id_usuario,id_moneda,
		precio_promedio,precio_manoobra,precio_material,precio_equipo,referencia_precio,descripcion_coloquial,
		tipo_producto,cantidad)
		VALUES (@pcodigo,@pdescripcion,@pmodelo,@ppeso,@pmarca,@pid_unidad,@pid_productotipo,@pid_empresa,@pid_usuario,@pid_moneda,
		@pprecio_promedio,@pprecio_manoobra,@pprecio_material,@pprecio_equipo,@preferencia_precio,@pdescripcion_coloquial,
		@ptipo_producto,@pcantidad);
	END

	IF @pTipoOpe = 'D' 
	BEGIN
		DELETE FROM TProducto
		WHERE id_producto = @pid_producto
	END

	IF @pTipoOpe = 'U' 
	BEGIN
		UPDATE TProducto SET 
		codigo = @pcodigo,
		descripcion = @pdescripcion,
		modelo = @pmodelo,
		peso = @ppeso,
		marca = @pmarca,
		id_unidad = @pid_unidad,
		id_productotipo = @pid_productotipo,
		id_empresa = @pid_empresa,
		id_usuario = @pid_usuario,
		id_moneda= @pid_moneda,
		precio_promedio = @pprecio_promedio,
		precio_manoobra = @pprecio_manoobra,
		precio_material = @pprecio_material,
		precio_equipo = @pprecio_equipo,
		referencia_precio = @preferencia_precio,
		descripcion_coloquial = @pdescripcion_coloquial,
		tipo_producto = @ptipo_producto,
		cantidad = @pcantidad
		WHERE id_producto = @pid_producto
	END

END