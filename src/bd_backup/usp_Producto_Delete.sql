USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[usp_Producto_Delete]    Script Date: 09/04/2017 19:46:43 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

ALTER PROCEDURE [dbo].[usp_Producto_Delete]
@id_producto	int
AS
UPDATE TProducto SET
estado = 70002
WHERE 
id_producto=@id_producto;