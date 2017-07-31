USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[usp_SalidaMaterial_insert]    Script Date: 28/07/2017 12:04:04 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[usp_SalidaMaterial_RetornoMaterial]
(
	@IdSalidaMaterial	int = NULL,
    @FechaRetorno		DateTime = NULL,    
	@id_empresa			int = NULL,
    @Usuario			varchar(50) = NULL
    
)
AS
BEGIN
SET NOCOUNT ON
BEGIN TRY
	BEGIN TRANSACTION

	UPDATE TSalida_material SET 
		FechaRetorno = @FechaRetorno, 
		UsuarioModifica = @Usuario		
	WHERE 
		IdSalidaMaterial = @IdSalidaMaterial and 
		id_empresa = @id_empresa;
	
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
