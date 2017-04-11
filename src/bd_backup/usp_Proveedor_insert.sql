USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[usp_CRUD_Cliente]    Script Date: 06/04/2017 10:11:13 a.m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
alter PROCEDURE [dbo].[usp_Proveedor_insert]
(
	@razon_social	varchar(200) = NULL,
	@ruc			varchar(11) = NULL,
	@direccion		varchar(200) = NULL,
	@telefono		varchar(20) = NULL,
	@celular		varchar(20) = NULL,
	@correo			varchar(50) = NULL,
	@id_empresa		int,
	@id_usuario		int 
)
AS
BEGIN
SET NOCOUNT ON
BEGIN TRY
	BEGIN TRANSACTION

	DECLARE 
	@vError varchar(100);

	--Insertamos en la tabla de clientes (Gestion/Clientes)
	--Validamos si la razon social existe
	IF EXISTS(
		SELECT id_proveedor FROM TProveedor
		WHERE	
		razon_social = @razon_social
	)RAISERROR('La razón social ingresada ya existe.',16,1)
		
	--Validamos si la razon social existe
	IF EXISTS(
		SELECT id_proveedor FROM TProveedor
		WHERE	
		ruc = @ruc
	)RAISERROR('El R.U.C. ingresado ya existe.',16,1)
		
		
	--Guardamos el cliente
	INSERT INTO TProveedor(razon_social,ruc,direccion,telefono,celular,correo,id_empresa,f_creacion,id_usuario)
	VALUES (@razon_social,@ruc,@direccion,@telefono,@celular,@correo,@id_empresa,getdate(),@id_usuario)
	
	SELECT id_proveedor from TProveedor where ruc = @ruc;
	
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
