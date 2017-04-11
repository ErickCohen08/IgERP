USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[usp_Proveedor_insert]    Script Date: 06/04/2017 15:18:24 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[usp_DataComun_create]
(
	@pIdDatoComun		int = NULL,
	@pCodigoTabla		int = NULL,
	@pCodigoFila		int = NULL,
	@pDescripcionCorta	varchar(100) = NULL,
	@pDescripcionLarga	varchar(300) = NULL,
	@pValorTexto1		varchar(500) = NULL,
	@pValorTexto2		varchar(500) = NULL,
	@pUsuario			varchar(50) = NULL
)
AS
BEGIN
SET NOCOUNT ON
BEGIN TRY
	BEGIN TRANSACTION

	IF @pCodigoTabla <> 0 --inserta fila
		BEGIN
			SELECT @pCodigoFila = ISNULL(MAX(CodigoFila),0) + 1
			FROM [dbo].[TDatoComun]
			WHERE CodigoTabla = @pCodigoTabla
		END
		ELSE --inserta tabla
		BEGIN
			SELECT @pCodigoTabla = ISNULL(MAX(CodigoTabla),0) + 1, @pCodigoFila = 0
			FROM [dbo].[TDatoComun]			
		END

		SELECT @pIdDatoComun = @pCodigoTabla * 10000 + @pCodigoFila
		
		INSERT INTO [dbo].[TDatoComun] (
			IdDatoComun,
			CodigoTabla,
			CodigoFila,
			DescripcionCorta,
			DescripcionLarga,
			ValorTexto1,
			ValorTexto2,
			UsuarioInserta)
		VALUES (
			@pIdDatoComun,
			@pCodigoTabla,
			@pCodigoFila,
			@pDescripcionCorta,
			@pDescripcionLarga,
			@pValorTexto1,
			@pValorTexto2,
			@pUsuario);

		select @pIdDatoComun as IdDatoComun;
	
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