USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[usp_Producto_Read]    Script Date: 03/04/2017 23:05:19 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[usp_Moneda_Read]
(
	@pid_moneda			int = NULL
)
AS
BEGIN
	
	SELECT
		m.id_moneda,
		dbo.TRIM(m.nombre) nombre,
		dbo.TRIM(m.simbolo) simbolo,
		dbo.TRIM(m.moneda_local) moneda_local
	FROM TMoneda M
	WHERE
		(@pid_moneda = 0 OR m.id_moneda = @pid_moneda)
	ORDER BY m.moneda_local desc;
	
END