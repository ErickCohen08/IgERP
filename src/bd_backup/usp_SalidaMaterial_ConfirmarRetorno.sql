USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[usp_SalidaMaterial_ConfirmarSalida]    Script Date: 02/08/2017 12:42:20 p.m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[usp_SalidaMaterial_ConfirmarRetorno]
(
	@pIdSalidaMaterial		int = NULL,
	@pid_empresa			int = NULL	
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
		EstadoAbierto = 90004
		WHERE IdSalidaMaterial = @pIdSalidaMaterial AND id_empresa = @pid_empresa

		UPDATE A SET 
		A.cantidad = A.cantidad + ISNULL(B.CantidadSalida,0)
		FROM
		TProducto A
		INNER JOIN TDetalle_salida_material B ON (B.id_producto = A.id_producto AND B.id_salida_material = @pIdSalidaMaterial AND B.id_empresa = @pid_empresa)
	
	END		
END