USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[usp_CompraMaterial_insert]    Script Date: 05/08/2017 17:16:24 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[usp_CompraMaterial_insert]
(
	@IdCompra		int = NULL,
	@FechaCompra	DateTime = NULL,
	@NumeroCompra	varchar(15) = NULL,
    @CalculoIgv		numeric(18,2) = NULL, 
    @SubTotal		numeric(18,2) = NULL,
    @Total			numeric(18,2) = NULL,
    @TotalLetras	varchar(500) = NULL,
    @DesProveedor	varchar(500) = NULL,
    @DesMoneda		varchar(500) = NULL,
    @DesIgv			varchar(500) = NULL,
    @DesObra		varchar(500) = NULL,
    @DesDocumento	varchar(500) = NULL,
    @Id_empresa		int = NULL,
    @Usuario		varchar(50) = NULL,
    @TipoOperacion	int = NULL

)
AS
BEGIN
SET NOCOUNT ON
BEGIN TRY
	BEGIN TRANSACTION

	DECLARE 
	@vError varchar(100),
	@vId_proveedor	int = NULL,
    @vId_moneda		int = NULL,
    @vId_Igv		int = NULL,
    @vId_obra		int = NULL,
    @vId_documento	int = NULL
    
	--Validamos Proveedor
	IF @DesProveedor IS NOT NULL
	BEGIN
		SELECT @vId_proveedor = id_proveedor FROM TProveedor
		WHERE	
		UPPER(DBO.TRIM(@DesProveedor)) = UPPER(DBO.TRIM(DBO.TRIM(ISNULL(razon_social, '')))) and 
		id_empresa = @Id_empresa

		IF @vId_proveedor is null
		BEGIN 
			RAISERROR('Seleccione un proveedor de la lista.',16,1)
		END
	END

	--Validamos Moneda
	IF @Desmoneda IS NOT NULL
	BEGIN
		SELECT @vId_moneda = IdDatoComun
		FROM TDatoComun
		WHERE	
		UPPER(DBO.TRIM(DescripcionCorta)) = UPPER(DBO.TRIM(@Desmoneda)) AND 
		CodigoTabla = 2

		IF @vId_moneda is null
		BEGIN 
			RAISERROR('Seleccione una moneda de la lista.',16,1)
		END
	END

	--Validamos Obra
	IF @DesObra IS NOT NULL
	BEGIN
		SELECT @vId_obra = IdObra
		FROM TObras
		WHERE	
		UPPER(DBO.TRIM(Descripcion)) = UPPER(DBO.TRIM(@DesObra)) AND 
		id_empresa = @Id_empresa
	
		IF @vId_obra is null
		BEGIN 
			RAISERROR('Seleccione una obra de la lista.',16,1)
		END
	END

	--Validamos IGV
	IF @DesIgv IS NOT NULL
	BEGIN
		SELECT @vId_Igv = id_igv
		FROM TIgv
		WHERE	
		UPPER(DBO.TRIM(igv)) = UPPER(DBO.TRIM(@DesIgv)) and 
		id_empresa = @Id_empresa
		
		IF @vId_Igv is null
		BEGIN 
			RAISERROR('Seleccione un IGV de la lista.',16,1)
		END
	END
	
	--Validamos Documento
	IF @DesDocumento IS NOT NULL
	BEGIN
		SELECT @vId_documento = IdDatoComun
		FROM TDatoComun
		WHERE	
		UPPER(DBO.TRIM(DescripcionCorta)) = UPPER(DBO.TRIM(@DesDocumento)) AND 
		CodigoTabla = 11

		IF @DesDocumento is null
		BEGIN 
			RAISERROR('Seleccione un Documento de la lista.',16,1)
		END
	END
	

	IF @TipoOperacion = 0
	BEGIN
		DECLARE @vIdCompra int = null
		SELECT @vIdCompra = ISNULL(MAX(IdCompra) + 1, 1) FROM TCompra where id_empresa = @id_empresa

		--Guardamos la salida
		INSERT INTO TCompra(
			IdCompra,
			FechaCompra,
			NumeroCompra,
			CalculoIgv,
			SubTotal,
			Total,
			TotalLetras,
			id_proveedor,
			id_moneda,
			id_igv,
			id_obra,
			id_documento,
			id_empresa,
			UsuarioInserta			
			)
		VALUES (
			@vIdCompra,
			@FechaCompra,
			@NumeroCompra,
			@CalculoIgv,
			@SubTotal,
			@Total,
			@TotalLetras,
			@vId_proveedor,
			@vId_moneda,
			@vId_igv,
			@vId_obra,
			@vId_documento,
			@Id_empresa,
			@Usuario
			);

		SELECT MAX(IdCompra) IdCompra FROM TCompra where id_empresa = @id_empresa;
	END

	
	IF @TipoOperacion = 1 AND @IdCompra <> 0
	BEGIN
		--Modificar
		UPDATE TCompra SET 
			FechaCompra = @FechaCompra,
			NumeroCompra = @NumeroCompra,
			CalculoIgv = @CalculoIgv,
			SubTotal = @SubTotal,
			Total = @Total,
			TotalLetras = @TotalLetras,
			id_proveedor = @vId_proveedor,
			id_moneda = @vId_moneda,
			id_igv = @vId_Igv,
			id_obra = @vId_obra,
			id_documento = @vId_documento,
			id_empresa = @Id_empresa,
			UsuarioModifica = @Usuario,
			FechaModifica = GETDATE()			
		WHERE 
			IdCompra = @IdCompra and 
			id_empresa = @id_empresa;

		SELECT IdCompra FROM TCompra where IdCompra = @IdCompra and id_empresa = @id_empresa; 
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