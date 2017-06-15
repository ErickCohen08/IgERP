USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[usp_Producto_insert]    Script Date: 11/06/2017 11:20:09 ******/
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
	@vId_Personal int = null,
	@vId_Obra int;
	
	--Obtenemos Id Personal
	IF @DesPersonal IS NOT NULL
	BEGIN
		SELECT @vId_Personal = id_personal
		FROM TPersonal
		WHERE
		(DBO.TRIM(DBO.TRIM(ISNULL(Nombre, '')) +' '+DBO.TRIM(ISNULL(Apellido, '')))) = DBO.TRIM(@DesPersonal);

		IF @vId_Personal IS NULL
		BEGIN
			RAISERROR('El personal seleccionado no existe.',16,1)
		END
	END

	
	--Obtenemos la Obra
	IF @DesObra IS NOT NULL
	BEGIN
		SELECT @vId_Obra = IdObra
		FROM TObras
		WHERE
		UPPER(DBO.TRIM(Descripcion)) = UPPER(DBO.TRIM(@DesObra))
	
		IF @vId_Obra is null
		BEGIN 
			RAISERROR('La Obra seleccionada no existe.',16,1)
		END	
	END

	
	IF @TipoOperacion = 0
	BEGIN
		--Guardar
		INSERT INTO TSalida_material(
			FechaSalida,
			id_personal,
			id_obra,
			Direccion,
			Motivo,
			id_empresa,
			UsuarioInserta
			)
		VALUES (
			@FechaSalida,
			@vId_Personal,
			@vId_Obra,
			@Direccion,
			@Motivo,
			@id_empresa,
			@Usuario
			);
	END

	
	IF @TipoOperacion = 1 AND @IdSalidaMaterial <> 0
	BEGIN
		--Modificar
		UPDATE TSalida_material SET 
			FechaSalida = @FechaSalida,
			id_personal = @vId_Personal,
			id_obra = @vId_Obra,
			Direccion = @Direccion,
			Motivo = @Motivo,
			UsuarioModifica = @usuario
		WHERE 
			IdSalidaMaterial = @IdSalidaMaterial and
			id_empresa = @id_empresa;
	END
	
	SELECT MAX(IdSalidaMaterial) id_salida FROM TSalida_material;

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
