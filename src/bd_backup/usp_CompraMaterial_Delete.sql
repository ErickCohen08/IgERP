USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[usp_SalidaMaterial_Delete]    Script Date: 05/08/2017 8:50:10 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[usp_CompraMaterial_Delete]
(
	@pIdCompra		int = NULL,
	@pid_empresa	int = NULL	
)
AS
BEGIN
	
	EXECUTE spCompraMaterialDetalle_eliminar @pIdCompra, @pid_empresa

	DELETE FROM TCompra
	WHERE 
	IdCompra = @pIdCompra AND
	id_empresa = @pid_empresa
		
END