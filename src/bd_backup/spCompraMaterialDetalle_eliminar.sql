USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[spSalidaMaterialDetalle_eliminar]    Script Date: 05/08/2017 8:54:23 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[spCompraMaterialDetalle_eliminar]
@IdCompra	int,
@Id_empresa	int
AS
BEGIN
	--Eliminamos 
	DELETE FROM TDetalle_compra
	WHERE IdCompra = @IdCompra and id_empresa = @Id_empresa;
END

