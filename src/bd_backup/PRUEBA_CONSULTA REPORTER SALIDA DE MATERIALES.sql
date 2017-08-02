	SELECT
		RIGHT('0000000000' + dbo.TRIM(sm.IdSalidaMaterial),10) IdSalidaMaterial,
		ISNULL(CONVERT(nvarchar(MAX), sm.FechaSalida, 101),'') FechaSalida,
		ISNULL(CONVERT(nvarchar(MAX), sm.FechaRetorno, 101),'') FechaRetorno,
		sm.Direccion,
		ISNULL(sm.Motivo, '') Motivo,
		o.Descripcion Obra,
		DBO.TRIM(DBO.TRIM(ISNULL(p.Nombre, '')) +' '+DBO.TRIM(ISNULL(p.Apellido, ''))) Personal,
		dc9.DescripcionCorta EstadoAbierto
	FROM TSalida_material sm
		LEFT JOIN TPersonal p on p.id_personal = sm.id_personal
		LEFT JOIN TObras o on o.IdObra = sm.id_obra
		LEFT JOIN TDatoComun dc9 on dc9.IdDatoComun = sm.EstadoAbierto
	WHERE 
		sm.IdSalidaMaterial = 1 AND
		sm.id_empresa = 1