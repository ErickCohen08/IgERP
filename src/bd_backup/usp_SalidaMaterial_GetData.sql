USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[usp_SalidaMaterial_GetData]    Script Date: 30/07/2017 13:54:27 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[usp_SalidaMaterial_GetData]
(
	@pid_salida_material	int = NULL,
	@pid_empresa			int = NULL
)
AS
BEGIN
	SELECT
		sm.FechaSalida,
		sm.Direccion,
		sm.id_personal,
		sm.id_obra,
		sm.id_empresa,
		sm.Motivo,
		o.Descripcion DesObra,
		DBO.TRIM(DBO.TRIM(ISNULL(p.Nombre, '')) +' '+DBO.TRIM(ISNULL(p.Apellido, ''))) DesPersonal,
		sm.EstadoAbierto
	FROM TSalida_material sm
		LEFT JOIN TPersonal p on p.id_personal = sm.id_personal
		LEFT JOIN TObras o on o.IdObra = sm.id_obra
	WHERE 
		(@pid_salida_material = 0 OR sm.IdSalidaMaterial = @pid_salida_material) AND
		(@pid_empresa	= 0 OR sm.id_empresa = @pid_empresa) 
END