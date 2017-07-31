USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[spSalidaMaterialDetalle_RetornarMaterial]    Script Date: 28/07/2017 11:55:43 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[spSalidaMaterialDetalle_RetornarMaterial]
(
	@id_detalle_salida_material	int = NULL,
    @CantidadRetorno			numeric(18,2) = NULL,
	@ComentarioRetorno			varchar(1000) = NULL,
    @Usuario					varchar(50) = NULL
)
AS
BEGIN
SET NOCOUNT ON
BEGIN TRY
	BEGIN TRANSACTION

	UPDATE TDetalle_salida_material SET
	CantidadRetorno = @CantidadRetorno,
	ComentarioRetorno = @ComentarioRetorno,
	UsuarioEntrega = @Usuario
	WHERE 
	id_detalle_salida_material = @id_detalle_salida_material;

	UPDATE A SET 
	A.cantidad = A.cantidad + B.CantidadRetorno
	FROM
	TProducto A
	INNER JOIN TDetalle_salida_material B ON A.id_producto = B.id_producto
	WHERE B.id_detalle_salida_material = @id_detalle_salida_material
			
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
