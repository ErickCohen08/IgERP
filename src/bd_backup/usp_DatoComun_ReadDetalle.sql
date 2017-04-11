USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[usp_DatoComun_ReadDetalle]    Script Date: 06/04/2017 15:01:52 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[usp_DatoComun_ReadDetalle]
(
	@pCodigoTabla		int = NULL
)
AS
BEGIN
	
	SELECT	
		IdDatoComun,
		CodigoTabla,
		CodigoFila,
		DBO.TRIM(DescripcionCorta) DescripcionCorta,
		DBO.TRIM(DescripcionLarga) DescripcionLarga,
		DBO.TRIM(ValorTexto1) ValorTexto1,
		DBO.TRIM(ValorTexto2) ValorTexto2,
		DBO.TRIM(Estado) Estado
	FROM [dbo].[TDatoComun]
	WHERE	
		(CodigoTabla = @pCodigoTabla) and 
		(CodigoFila > 0) and 
		(Estado = 'A')
END