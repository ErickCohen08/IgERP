USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[spSalidaMaterialDetalle_crear]    Script Date: 17/07/2017 02:19:50 p.m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[spSalidaMaterialDetalle_crear]
(
	@Id_detalle_salida_material	int = NULL,
    @Id_salida_material			int = NULL,
    @Id_producto				int = NULL,
    @CantidadSalida				numeric(18,2) = NULL,
    @Usuario					varchar(50) = NULL,
    @TipoOperacion				int = NULL
)
AS
BEGIN
SET NOCOUNT ON
BEGIN TRY
	BEGIN TRANSACTION

	IF @TipoOperacion = 0
	BEGIN
		--Guardamos
		INSERT INTO TDetalle_salida_material(
			id_salida_material, 
			id_producto, 
			CantidadSalida,
			UsuarioSalida)
		VALUES (
			@Id_salida_material, 
			@Id_producto, 
			@CantidadSalida, 
			@Usuario);
	END

	IF @TipoOperacion = 1 AND @Id_detalle_salida_material <> 0
	BEGIN
		--Modificar
		UPDATE TDetalle_salida_material SET
			id_producto = @Id_producto,
			CantidadSalida = @CantidadSalida,
			UsuarioSalida = @Usuario
		WHERE 
			id_detalle_salida_material = @Id_detalle_salida_material;
	END

	UPDATE TProducto SET
	cantidad = cantidad - @CantidadSalida
	WHERE 
	id_producto = @Id_producto
			
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
