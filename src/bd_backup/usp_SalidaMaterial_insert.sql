USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[usp_Producto_insert]    Script Date: 21/05/2017 12:02:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[usp_SalidaMaterial_insert]
(
	@IdSalidaMaterial		int = NULL,
    @FechaSalida			datetime = NULL,
    @Direccion				varchar(500) = NULL,

    @DesPersonal			varchar(500) = NULL,
	@DesObra				varchar(500) = NULL,
	
	@id_empresa				int = NULL,
	@usuario				varchar(50) = NULL,
	@TipoOperacion			int = NULL

)
AS
BEGIN
SET NOCOUNT ON
BEGIN TRY
	BEGIN TRANSACTION

	DECLARE 
	@vError varchar(100),
	@vId_moneda int = null,
	@vId_unidad int,
	
	
	--Validamos Codigo
	IF @codigo IS NOT NULL
	BEGIN
		IF EXISTS(
			SELECT id_producto FROM TProducto
			WHERE	
			UPPER(DBO.TRIM(codigo)) = UPPER(DBO.TRIM(@codigo)) AND
			(@id_producto IS NULL OR id_producto <> @id_producto)
		)RAISERROR('El codigo ingresado ya existe.',16,1);
	END

	
		
	--Obtenemos Id Moneda
	print @Desmoneda
	IF @Desmoneda IS NOT NULL
	BEGIN
		SELECT @vId_moneda = IdDatoComun
		FROM TDatoComun
		WHERE	
		UPPER(DBO.TRIM(DescripcionCorta)) = UPPER(DBO.TRIM(@Desmoneda)) AND 
		CodigoTabla = 2

		PRINT @vId_moneda
	
		IF @vId_moneda is null
		BEGIN 
			RAISERROR('La moneda seleccionada no existe.',16,1)
		END
	END

	
	--Obtenemos la unidad
	IF @Desunidad IS NOT NULL
	BEGIN
		SELECT @vId_unidad = IdDatoComun
		FROM TDatoComun
		WHERE	
		UPPER(DBO.TRIM(DescripcionCorta)) = UPPER(DBO.TRIM(@Desunidad)) AND 
		CodigoTabla = 1
	
		IF @vId_unidad is null
		BEGIN 
			RAISERROR('La unidad de medida seleccionada no existe.',16,1)
		END	
	END

	
	--Obtenemos la categoria
	IF @Desproductotipo IS NOT NULL
	BEGIN
		SELECT @vId_productotipo = IdDatoComun
		FROM TDatoComun
		WHERE	
		UPPER(DBO.TRIM(DescripcionCorta)) = UPPER(DBO.TRIM(@Desproductotipo)) AND 
		CodigoTabla = 6
	
		IF @vId_productotipo is null
		BEGIN 
			RAISERROR('La categoría seleccionada no existe.',16,1)
		END
	END

	--Obtenemos el almacen
	IF @DesAlmacen IS NOT NULL
	BEGIN
		SELECT @vId_Almacen = IdDatoComun
		FROM TDatoComun
		WHERE	
		UPPER(DBO.TRIM(DescripcionCorta)) = UPPER(DBO.TRIM(@DesAlmacen)) AND 
		CodigoTabla = 3
	
		IF @vId_Almacen is null
		BEGIN 
			RAISERROR('El almacén seleccionado no existe.',16,1)
		END
	END

	
	--Obtenemos la referencia del precio
	IF @DesReferencia_precio IS NOT NULL
	BEGIN
		SELECT @vId_Referencia_precio = IdDatoComun
		FROM TDatoComun
		WHERE
		UPPER(DBO.TRIM(DescripcionCorta)) = UPPER(DBO.TRIM(@DesReferencia_precio)) AND 
		CodigoTabla = 4
	
		IF @vId_Referencia_precio is null
		BEGIN 
			RAISERROR('La referencia de precio seleccionado no existe.',16,1)
		END
	END
		

	IF @TipoOperacion = 0
	BEGIN
		--Guardamos el producto
		INSERT INTO TProducto (
			codigo, 
			descripcion, 
			modelo, 
			marca, 
			descripcion_coloquial, 
			peso, 
			precio_promedio, 
			cantidad,
			id_moneda,
			id_unidad,
			id_productotipo,
			id_empresa,
			id_Almacen,
			id_Referencia_precio,
			UsuarioInserta)
		VALUES (
			@codigo, 
			@descripcion, 
			@modelo, 
			@marca, 
			@descripcion_coloquial, 
			@peso, 
			@precio_promedio, 
			@cantidad,
			@vId_moneda,
			@vId_unidad,
			@vId_productotipo,
			@id_empresa,
			@vId_Almacen,
			@vId_Referencia_precio,
			@usuario);
	END

	
	IF @TipoOperacion = 1 AND @id_producto <> 0
	BEGIN
		--Modificar
		UPDATE TProducto SET 
			codigo = @codigo, 
			descripcion = @descripcion, 
			modelo = @modelo, 
			marca = @marca, 
			descripcion_coloquial = @descripcion_coloquial, 
			peso = @peso, 
			precio_promedio = @precio_promedio, 
			cantidad = @cantidad,
			id_moneda = @vId_moneda,
			id_unidad = @vId_unidad,
			id_productotipo = @vId_productotipo,
			id_empresa = @id_empresa,
			id_Almacen = @vId_Almacen,
			id_Referencia_precio = @vId_Referencia_precio,
			UsuarioModifica = @usuario
		WHERE 
			id_producto = @id_producto;
	END
	
	SELECT MAX(id_producto) id_producto FROM TProducto;

	COMMIT TRANSACTION
END TRY
BEGIN CATCH
	IF @@TRANCOUNT > 0
	BEGIN
		ROLLBACK TRANSACTION
	END
	EXEC usp_RetornarError
END CATCH
SET NOCOUNT OFF
END
