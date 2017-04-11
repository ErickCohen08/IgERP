USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[usp_CRUD_DataComun]    Script Date: 04/04/2017 09:57:44 a.m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[usp_CRUD_DataComun]
(
	@pTipoOpe			char(1),
	@pIdDatoComun		int = NULL,
	@pCodigoTabla		int = NULL,
	@pCodigoFila		int = NULL,
	@pDescripcionCorta	varchar(100) = NULL,
	@pDescripcionLarga	varchar(300) = NULL,
	@pValorTexto1		varchar(500) = NULL,
	@pValorTexto2		varchar(500) = NULL,
	@pEstado			char(1) = NULL,
	@pUsuario			varchar(50) = NULL
)
AS
BEGIN
	
	IF @pTipoOpe = 'S' 
	BEGIN
		SELECT	IdDatoComun,
				CodigoTabla,
				CodigoFila,
				DescripcionCorta,
				DescripcionLarga,
				ValorTexto1,
				ValorTexto2,
				Estado
		FROM	[dbo].[TDatoComun]
		WHERE	(@pIdDatoComun IS NULL OR IdDatoComun = @pIdDatoComun) AND
				(@pCodigoTabla IS NULL OR CodigoTabla = @pCodigoTabla) AND
				(@pCodigoFila IS NULL OR CodigoFila = @pCodigoFila) AND
				(@pValorTexto1 IS NULL OR ValorTexto1 = @pValorTexto1)
	END

	IF @pTipoOpe = 'R' 
	BEGIN
		SELECT	IdDatoComun,
				CodigoTabla,
				CodigoFila,
				DescripcionCorta,
				DescripcionLarga,
				ValorTexto1,
				ValorTexto2,
				Estado
		FROM	[dbo].[TDatoComun]
		WHERE	(CodigoTabla = @pCodigoTabla) and 
				(CodigoFila > 0) and 
				(@pDescripcionCorta IS NULL OR DescripcionCorta = @pDescripcionCorta) and
				(@pDescripcionLarga IS NULL OR DescripcionLarga = @pDescripcionLarga) and
				(@pValorTexto1 IS NULL OR ValorTexto1 = @pValorTexto1) and
				(@pValorTexto2 IS NULL OR ValorTexto2 = @pValorTexto2) and
				(Estado = 'A')
	END

	IF @pTipoOpe = 'I' 
	BEGIN
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
			@pUsuario)
	END

	IF @pTipoOpe = 'D' 
	BEGIN
		DELETE FROM [dbo].[TDatoComun]
		WHERE IdDatoComun = @pIdDatoComun
	END

	IF @pTipoOpe = 'U' 
	BEGIN
		UPDATE [dbo].[TDatoComun] SET 
		DescripcionCorta = @pDescripcionCorta, 
		DescripcionLarga = @pDescripcionLarga, 
		ValorTexto1 = @pValorTexto1, 
		ValorTexto2 = @pValorTexto2,
		Estado = @pEstado
		WHERE IdDatoComun = @pIdDatoComun
	END

END