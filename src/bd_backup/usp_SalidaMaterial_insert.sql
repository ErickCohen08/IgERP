USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[usp_SalidaMaterial_insert]    Script Date: 30/07/2017 13:48:13 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[usp_SalidaMaterial_insert]
(
	@IdSalidaMaterial	int = NULL,
    @FechaSalida		DateTime = NULL,
    @DesPersonal		varchar(500) = NULL,
    @DesObra			varchar(500) = NULL,
    @Direccion			varchar(500) = NULL,
    @Motivo				varchar(100) = NULL,
	@id_empresa			int = NULL,
    @Usuario			varchar(50) = NULL,
    @TipoOperacion		int = NULL

)
AS
BEGIN
SET NOCOUNT ON
BEGIN TRY
	BEGIN TRANSACTION

	DECLARE 
	@vError varchar(100),
	@vId_personal int = null,
	@vId_obra int
	
	--Validamos Personal
	IF @DesPersonal IS NOT NULL
	BEGIN
		SELECT @vId_personal = id_personal FROM TPersonal
		WHERE	
		UPPER(DBO.TRIM(@DesPersonal)) = UPPER(DBO.TRIM(DBO.TRIM(ISNULL(Nombre, '')) +' '+DBO.TRIM(ISNULL(Apellido, ''))))
		
		IF @vId_personal is null
		BEGIN 
			RAISERROR('Seleccione un personal de la lista.',16,1)
		END
	END

	--Validamos Obra
	IF @DesObra IS NOT NULL
	BEGIN
		SELECT @vId_obra = IdObra
		FROM TObras
		WHERE	
		UPPER(DBO.TRIM(Descripcion)) = UPPER(DBO.TRIM(@DesObra))
	
		IF @vId_obra is null
		BEGIN 
			RAISERROR('Seleccione una obra de la lista.',16,1)
		END
	END

	
	IF @TipoOperacion = 0
	BEGIN
		DECLARE @vIdSalidaMaterial int = null
		SELECT @vIdSalidaMaterial = ISNULL(MAX(IdSalidaMaterial) + 1, 1) FROM TSalida_material

		--Guardamos la salida
		INSERT INTO TSalida_material(
			IdSalidaMaterial,
			FechaSalida, 
			Direccion, 
			id_personal, 
			id_obra,
			Motivo,
			EstadoAbierto, 
			id_empresa, 
			UsuarioInserta)
		VALUES (
			@vIdSalidaMaterial,
			@FechaSalida, 
			@Direccion, 
			@vId_personal, 
			@vId_obra, 
			@Motivo,
			90001, 
			@id_empresa, 
			@Usuario);

		SELECT MAX(IdSalidaMaterial) IdSalidaMaterial FROM TSalida_material where id_empresa = @id_empresa;
	END

	
	IF @TipoOperacion = 1 AND @IdSalidaMaterial <> 0
	BEGIN
		--Modificar
		UPDATE TSalida_material SET 
			FechaSalida = @FechaSalida, 
			Direccion = @Direccion, 
			id_personal = @vId_personal, 
			id_obra = @vId_obra, 
			Motivo = @Motivo, 
			EstadoAbierto = 90001,
			UsuarioModifica = @Usuario
		WHERE 
			IdSalidaMaterial = @IdSalidaMaterial and 
			id_empresa = @id_empresa;

		SELECT IdSalidaMaterial FROM TSalida_material where IdSalidaMaterial = @IdSalidaMaterial and id_empresa = @id_empresa; 
	END
	
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
