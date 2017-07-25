USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[usp_SalidaMaterial_GetData]    Script Date: 25/07/2017 10:22:46 a.m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[usp_SalidaMaterial_GetData]
(
	@pid_salida_material	int = NULL
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
		DBO.TRIM(DBO.TRIM(ISNULL(p.Nombre, '')) +' '+DBO.TRIM(ISNULL(p.Apellido, ''))) DesPersonal
	FROM TSalida_material sm
		LEFT JOIN TPersonal p on p.id_personal = sm.id_personal
		LEFT JOIN TObras o on o.IdObra = sm.id_obra
	WHERE 
		(@pid_salida_material = 0 OR sm.IdSalidaMaterial = @pid_salida_material)
END