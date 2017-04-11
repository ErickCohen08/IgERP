USE [bd_ig-projet]
GO
/****** Object:  StoredProcedure [dbo].[usp_SalidaMaterial_Read]    Script Date: 11/04/2017 03:54:46 p.m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[usp_SalidaMaterial_Read]
(
	@pIdSalidaMaterial		int = NULL,
	@pid_empresa			int = NULL,
	@pFechaSalida			DateTime = NULL,
	@pDireccion				varchar(500) = NULL,
	@pMotivo				varchar(1000) = NULL,
	@pDesPersonal			varchar(500) = NULL,
	@pDesObra				varchar(500) = NULL	
)
AS
BEGIN
	
	SELECT 
		ROW_NUMBER() OVER (ORDER BY FechaInserta DESC) AS fila,
		IdSalidaMaterial,
		FechaSalida,
		DesPersonal,
		DesObra,
		Direccion,
		DesEstadoAbierto,
		FechaInserta
	FROM 
	(
		SELECT 
			sm.IdSalidaMaterial,
			sm.FechaSalida,
			DBO.TRIM(DBO.TRIM(ISNULL(p.Nombre, '')) +' '+ DBO.TRIM(ISNULL(p.Apellido, ''))) DesPersonal,
			DBO.TRIM(ISNULL(o.Descripcion, ''))			DesObra,
			DBO.TRIM(ISNULL(sm.Direccion,''))			Direccion,
			DBO.TRIM(ISNULL(dc.DescripcionCorta, ''))	DesEstadoAbierto,
			SM.FechaInserta
		FROM TSalida_material sm
			LEFT JOIN TPersonal p on (sm.id_personal = p.id_personal)
			LEFT JOIN TObras o on (sm.id_obra = o.IdObra) 
			LEFT JOIN TDatoComun dc on (sm.EstadoAbierto = dc.IdDatoComun)
		WHERE
			
			(@pIdSalidaMaterial	= 0 OR sm.IdSalidaMaterial = @pIdSalidaMaterial) AND
			(@pid_empresa	= 0 OR sm.id_empresa = @pid_empresa) AND
			
			(@pFechaSalida IS NULL OR CAST(sm.FechaSalida AS date) = CAST(@pFechaSalida AS date)) and
			
			(@pDireccion	IS NULL OR UPPER(DBO.TRIM(sm.Direccion))			like '%'+UPPER(DBO.TRIM(@pDireccion))+'%')		and
			(@pMotivo		IS NULL OR UPPER(DBO.TRIM(sm.Motivo))				like '%'+UPPER(DBO.TRIM(@pMotivo))+'%' )		and 
			(@pDesPersonal	IS NULL OR UPPER(DBO.TRIM(DBO.TRIM(ISNULL(p.Nombre, ''))+' '+DBO.TRIM(ISNULL(p.Apellido, ''))))	like '%'+UPPER(DBO.TRIM(@pDesPersonal))+'%')	and 
			(@pDesObra		IS NULL OR UPPER(DBO.TRIM(o.Descripcion))			like '%'+UPPER(DBO.TRIM(@pDesObra))+'%')		and
			
			sm.EstadoActivo = 70001			
	) as t
	ORDER BY t.FechaInserta desc;
	
END