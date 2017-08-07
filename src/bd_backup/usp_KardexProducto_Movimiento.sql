USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[usp_SalidaMaterial_ConfirmarRetorno]    Script Date: 06/08/2017 16:55:30 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[usp_KardexProducto_Movimiento]
(
	@IdProducto			int = NULL,
	@IdOperacion		int = NULL,
	@IdEmpresa			int = NULL,
	@UsuarioInserta		varchar(50) = NULL,
	@Cantidad			numeric(18,2) = NULL,
	@Accion				int
)
AS
BEGIN
	/*@Accion
	=========
	1 = Crear Material
	2 = Compra
	3 = Salida Material
	4 = Salida Material*/
	
	IF @Accion = 1
	BEGIN
		INSERT INTO TKardexProducto (
		IdProducto,
		IdTipoOperacion,
		IdOperacion,
		IdEmpresa,
		UsuarioInserta,
		FechaInserta,
		Cantidad
		) 
		VALUES (
		@IdProducto,
		120001,
		@IdOperacion,
		@IdEmpresa,
		@UsuarioInserta,
		GETDATE(),
		@Cantidad
		)
	END

	IF @Accion = 2
	BEGIN
		INSERT INTO TKardexProducto (
		IdProducto,
		IdTipoOperacion,
		IdOperacion,
		IdEmpresa,
		UsuarioInserta,
		FechaInserta,
		Cantidad
		)
		SELECT 
		dcom.id_producto,
		120002,
		dcom.IdCompra,
		dcom.id_empresa,
		@UsuarioInserta,
		GETDATE(),
		dcom.Cantidad
		FROM TDetalle_compra dcom
		WHERE 
		dcom.IdCompra = @IdOperacion and
		dcom.id_empresa = @IdEmpresa		
	END

	IF @Accion = 3
	BEGIN
		INSERT INTO TKardexProducto (
		IdProducto,
		IdTipoOperacion,
		IdOperacion,
		IdEmpresa,
		UsuarioInserta,
		FechaInserta,
		Cantidad
		)
		SELECT 
		dsam.id_producto,
		120003,
		id_salida_material,
		id_empresa,
		@UsuarioInserta,
		GETDATE(),
		dsam.CantidadSalida
		FROM TDetalle_salida_material dsam
		WHERE 
		dsam.id_salida_material = @IdOperacion and
		dsam.id_empresa = @IdEmpresa
	END

	IF @Accion = 4
	BEGIN
		INSERT INTO TKardexProducto (
		IdProducto,
		IdTipoOperacion,
		IdOperacion,
		IdEmpresa,
		UsuarioInserta,
		FechaInserta,
		Cantidad
		)
		SELECT 
		dsam.id_producto,
		120004,
		id_salida_material,
		id_empresa,
		@UsuarioInserta,
		GETDATE(),
		dsam.CantidadRetorno
		FROM TDetalle_salida_material dsam
		WHERE 
		dsam.id_salida_material = @IdOperacion and
		dsam.id_empresa = @IdEmpresa
	END
END