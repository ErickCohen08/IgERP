USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[spSalidaMaterialDetalle_crear]    Script Date: 05/08/2017 9:58:21 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[spCompraMaterialDetalle_crear]
(
	@IdDetalleCompra			int = NULL,
    @IdCompra					int = NULL,
    @Id_producto				int = NULL,
    @Cantidad					numeric(18,2) = NULL,
    @PrecioUnitario				numeric(18,2) = NULL,
    @PrecioTotal				numeric(18,2) = NULL,    
	@Usuario					varchar(50) = NULL,
    @TipoOperacion				int = NULL,
	@Id_empresa					int = NULL
        
)
AS
BEGIN
SET NOCOUNT ON
BEGIN TRY
	BEGIN TRANSACTION

	--Guardamos
	INSERT INTO TDetalle_compra(
		IdCompra,
		id_producto,
		Cantidad,
		PrecioUnitario,
		PrecioTotal,
		UsuarioInserta,
		id_empresa)
	VALUES (
		@IdCompra, 
		@Id_producto, 
		@Cantidad, 
		@PrecioUnitario,
		@PrecioTotal,
		@Usuario,
		@Id_empresa);	
			
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