USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[spProductoDetalle_eliminar]    Script Date: 09/04/2017 9:35:26 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

ALTER PROCEDURE [dbo].[spProductoDetalle_eliminar]
@id_producto	int
AS
DELETE FROM TProducto_detalle WHERE id_producto=@id_producto