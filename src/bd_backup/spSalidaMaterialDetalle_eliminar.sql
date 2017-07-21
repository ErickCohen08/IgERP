USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[spSalidaMaterialDetalle_eliminar]    Script Date: 17/07/2017 02:20:17 p.m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

ALTER PROCEDURE [dbo].[spSalidaMaterialDetalle_eliminar]
@Id_salida_material	int
AS
BEGIN
	DECLARE
	@vid_producto					INT,
	@vCantidadSalida				NUMERIC(18,2)

	--Obtenemos la lista de productos
	DECLARE cProducto CURSOR FOR
	SELECT id_producto, CantidadSalida
	FROM TDetalle_salida_material WHERE id_salida_material = @Id_salida_material;

	OPEN cProducto
	FETCH cProducto 
	INTO  @vid_producto, @vCantidadSalida

	WHILE (@@FETCH_STATUS = 0 )
	BEGIN
		--Actualizamos
		UPDATE TProducto SET
		cantidad = cantidad + @vCantidadSalida
		WHERE 
		id_producto = @vid_producto;

		FETCH cProducto 
		INTO  @vid_producto, @vCantidadSalida
	END
	
	CLOSE cProducto
	DEALLOCATE cProducto

	--Eliminamos 
	DELETE FROM TDetalle_salida_material 
	WHERE id_salida_material = @Id_salida_material;
END

