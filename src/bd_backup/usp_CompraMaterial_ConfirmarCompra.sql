USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[usp_SalidaMaterial_ConfirmarSalida]    Script Date: 05/08/2017 8:56:47 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[usp_CompraMaterial_ConfirmarCompra]
(
	@pIdCompra		int = NULL,
	@pid_empresa	int = NULL,
	@Usuario		varchar(50) = NULL
    	
)
AS
BEGIN
	DECLARE @vEstadoAbierto int;

	SELECT @vEstadoAbierto = EstadoAbierto 
	FROM TCompra
	WHERE IdCompra = @pIdCompra AND id_empresa = @pid_empresa
	
	IF @vEstadoAbierto = 100001
	BEGIN
		UPDATE TCompra SET
		EstadoAbierto = 100002,
		UsuarioModifica = @Usuario,
		FechaModifica = GETDATE()
		WHERE IdCompra = @pIdCompra AND id_empresa = @pid_empresa

		UPDATE A SET 
		A.cantidad = A.cantidad + ISNULL(B.Cantidad,0)
		FROM
		TProducto A
		INNER JOIN TDetalle_compra B ON (B.id_producto = A.id_producto AND B.IdCompra = @pIdCompra AND B.id_empresa = @pid_empresa)
	
	END		
END