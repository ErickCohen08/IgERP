USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[usp_Obra_Read]    Script Date: 06/05/2017 9:14:01 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[usp_Obra_Read]
(
	@pIdObra				int = NULL,
	@pDescripcion			varchar(500) = NULL,
	@pid_cliente			int = NULL,
	@pid_empresa			int = NULL
)
AS
BEGIN
	SELECT
		o.IdObra,
		dbo.trim(o.Descripcion) Descripcion,
		o.id_cliente,
		ISNULL(o.Direccion, C.direccion) direccion
	FROM TObras o
		LEFT JOIN TCliente c ON O.id_cliente = c.id_cliente
	WHERE 
		EstadoActivo = 'A' AND
		o.id_empresa = @pid_empresa AND
		(@pDescripcion IS NULL OR UPPER(o.Descripcion) LIKE '%'+UPPER(@pDescripcion)+'%') AND
		(@pIdObra = 0 OR o.IdObra = @pIdObra) AND
		(@pid_cliente = 0 OR o.id_cliente = @pid_cliente) AND
		(@pid_empresa = 0 OR o.id_empresa = @pid_empresa);
END