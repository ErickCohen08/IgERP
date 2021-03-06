USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[usp_Producto_insert]    Script Date: 26/05/2018 23:56:06 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[usp_Producto_insert]
(
	@codigo					varchar(20) = NULL,
    @descripcion			varchar(500) = NULL,
    @modelo					varchar(100) = NULL,
    @marca					varchar(100) = NULL,
    @descripcion_coloquial	varchar(500) = NULL,
    @peso					numeric(18,2) = NULL,
    @precio_promedio		numeric(18,2) = NULL,
    @cantidad				numeric(18,2) = NULL,
        
    @Desmoneda				varchar(100) = NULL,
    @Desunidad				varchar(100) = NULL,
    @Desproductotipo		varchar(100) = NULL,
    @id_Almacen				int = NULL,
    @DesReferencia_precio	varchar(100) = NULL,

	@id_empresa				int = NULL,
	@usuario				varchar(50) = NULL,
	@TipoOperacion			int = NULL,
	@id_producto			int = NULL,
	@idStand				int = NULL,
	@idNivel				int = NULL
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
	@vId_productotipo int,
	@vId_Referencia_precio int,
	@vDescripcion varchar(500),
	@vId_Almacen int = null,
	@vIdStand int = null,
	@vIdNivel int = null;

	--Validamos el codigo de almacen
	IF @id_Almacen <> 0
	BEGIN
		set @vId_Almacen = @id_Almacen;
	END

	--Validamos el codigo de stand
	IF @idStand <> 0
	BEGIN
		set @vIdStand = @idStand;
	END

	--Validamos el codigo de nivel
	IF @idNivel <> 0
	BEGIN
		set @vIdNivel = @idNivel;
	END


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
		--Verificamos si ya existe otro producto con el mismo nombre
		IF @descripcion IS NOT NULL
		BEGIN
			SELECT @vDescripcion = descripcion
			FROM TProducto
			WHERE
			UPPER(DBO.TRIM(descripcion)) = UPPER(DBO.TRIM(@descripcion)) AND 
			id_empresa = @id_empresa and 
			estado = 70001;

			IF @vDescripcion is not null
			BEGIN 
				RAISERROR('Ya existe un material con el mismo nombre.',16,1)
			END
		END

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
			UsuarioInserta,
			IdStand,
			IdNivel
		)
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
			@usuario,
			@vIdStand,
			@vIdNivel
			);

			DECLARE 
			@vIdProducto int,
			@vCantidad numeric(18,2);

			SELECT @vIdProducto = MAX(id_producto) FROM TProducto;
			
			IF @cantidad IS NULL 
			BEGIN
				SET @vCantidad = 0
			END
			ELSE
			BEGIN
				SET @vCantidad = @cantidad
			END 


			EXECUTE usp_KardexProducto_Movimiento @vIdProducto, NULL, @id_empresa, @usuario, @vCantidad, 1;
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
			id_moneda = @vId_moneda,
			id_unidad = @vId_unidad,
			id_productotipo = @vId_productotipo,
			id_empresa = @id_empresa,
			id_Almacen = @vId_Almacen,
			id_Referencia_precio = @vId_Referencia_precio,
			UsuarioModifica = @usuario,
			IdStand = @vIdStand,
			IdNivel = @vIdNivel
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