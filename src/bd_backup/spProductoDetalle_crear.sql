USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[spProductoDetalle_crear]    Script Date: 08/04/2017 18:04:58 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

ALTER PROCEDURE [dbo].[spProductoDetalle_crear]
@id_producto	int,
@rucProveedor	varchar(50),
@precio			numeric(18,2),
@id_empresa		int,
@id_usuario		int 

AS
BEGIN
SET NOCOUNT ON
BEGIN TRY
	BEGIN TRANSACTION

	DECLARE 
	@vError varchar(100),
	@vId_proveedor int = null

	--Validamos ruc del proveedor
	SELECT @vId_proveedor = id_proveedor 
	FROM TProveedor
	WHERE	
	UPPER(DBO.TRIM(ruc)) = UPPER(DBO.TRIM(@rucProveedor))


	IF @vId_proveedor IS NULL
	BEGIN
		RAISERROR('No existe el proveedor ingresado',16,1);
	END

	INSERT INTO TProducto_detalle(id_producto, id_proveedor, precio, id_empresa, f_creacion, id_usuario)
	VALUES (@id_producto,@vId_proveedor, @precio, @id_empresa, getdate(), @id_usuario);

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