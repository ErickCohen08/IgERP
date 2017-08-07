USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[usp_SalidaMaterial_ConfirmarRetorno]    Script Date: 06/08/2017 22:55:27 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[usp_SalidaMaterial_ConfirmarRetorno]
(
	@pIdSalidaMaterial		int = NULL,
	@pid_empresa			int = NULL,
	@Usuario				varchar(50) = NULL	
)
AS
BEGIN
	DECLARE @vEstadoAbierto int;

	SELECT @vEstadoAbierto = EstadoAbierto 
	FROM TSalida_material
	WHERE IdSalidaMaterial = @pIdSalidaMaterial AND id_empresa = @pid_empresa
	
	IF @vEstadoAbierto = 90003
	BEGIN
		UPDATE TSalida_material SET
		EstadoAbierto = 90004,
		UsuarioModifica = @Usuario,
		FechaModifica = GETDATE()
		WHERE IdSalidaMaterial = @pIdSalidaMaterial AND id_empresa = @pid_empresa

		UPDATE A SET 
		A.cantidad = A.cantidad + ISNULL(B.CantidadRetorno,0)
		FROM
		TProducto A
		INNER JOIN TDetalle_salida_material B ON (B.id_producto = A.id_producto AND B.id_salida_material = @pIdSalidaMaterial AND B.id_empresa = @pid_empresa)
		
		/*Ingresamos al kardex*/
		EXECUTE usp_KardexProducto_Movimiento NULL, @pIdSalidaMaterial, @pid_empresa, @Usuario, NULL, 4;
	END		
END