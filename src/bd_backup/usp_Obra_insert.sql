USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[usp_Producto_insert]    Script Date: 30/04/2017 14:40:57 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
alter PROCEDURE [dbo].[usp_Obra_insert]
(
	@descripcion			varchar(500) = NULL,
    @descliente				varchar(200) = NULL,
    @direccion				varchar(200) = NULL,
    @id_empresa				int = NULL,
	@usuario				varchar(50) = NULL
)
AS
BEGIN
SET NOCOUNT ON
BEGIN TRY
	BEGIN TRANSACTION

	DECLARE 
	@vError varchar(100),
	@vid_cliente int;

	--Obtenemos Id Moneda
	IF @descliente IS NOT NULL
	BEGIN
		SELECT @vid_cliente = id_cliente
		FROM TCliente
		WHERE	
		UPPER(DBO.TRIM(razon_social)) = UPPER(DBO.TRIM(@descliente))

		IF @vid_cliente is null
		BEGIN 
			RAISERROR('El cliente ingresado no existe.',16,1)
		END
	END

	
		--Guardamos el producto
	INSERT INTO TObras (
		Descripcion, 
		Direccion,
		id_cliente,
		id_empresa,
		UsuarioInserta,
		EstadoActivo
		)
	VALUES (
		@descripcion, 
		@direccion, 
		@vid_cliente, 
		@id_empresa,
		@usuario,
		'A');
	
	
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
