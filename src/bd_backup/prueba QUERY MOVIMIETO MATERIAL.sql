SELECT 
/*Cabecera*/
kard.IdKardex,
(DBO.TRIM(prod.descripcion) + ISNULL(' Marca ' + DBO.TRIM(prod.marca), '') + ISNULL(' Modelo ' + DBO.TRIM(prod.modelo), '') )  nombreMaterial,
prod.cantidad stockActual,
dc1.DescripcionCorta unidadMaterial,

/*Detalle de movimiento*/
kard.Cantidad,
dc12.DescripcionCorta as descripcionOperacion,
ISNULL(FORMAT(kard.FechaInserta, 'd', 'en-gb'),'') fechaOperacion,
CASE  
	WHEN kard.IdTipoOperacion = 120002 THEN ('Código de Compra: ' + RIGHT('0000000000' + dbo.TRIM(com.IdCompra),10) + ', ' + DBO.TRIM(dc11.DescripcionCorta) + ' N° ' + DBO.TRIM(com.NumeroCompra))
	WHEN kard.IdTipoOperacion = 120003 THEN ('Código de Salida: ' + RIGHT('0000000000' + dbo.TRIM(smat.IdSalidaMaterial),10) )
	WHEN kard.IdTipoOperacion = 120004 THEN ('Código de Retorno: ' + RIGHT('0000000000' + dbo.TRIM(smat.IdSalidaMaterial),10) )
	ELSE ''
END AS operacion,
CASE 
	WHEN kard.IdTipoOperacion = 120001 THEN ('Registrado por: ' + DBO.TRIM(usuk.nombre))
	WHEN kard.IdTipoOperacion = 120002 THEN ('Registrado por: ' + DBO.TRIM(usuk.nombre))
	WHEN kard.IdTipoOperacion = 120003 THEN ('Material Solicitado por: ' + DBO.TRIM(DBO.TRIM(ISNULL(pers.Nombre, '')) +' '+DBO.TRIM(ISNULL(pers.Apellido, ''))))
	WHEN kard.IdTipoOperacion = 120004 THEN ('Registrado por: ' + DBO.TRIM(usuk.nombre))	
END AS responsable,

/*Datos de la empresa*/
DBO.TRIM(empr.razon_social) as empresa,
DBO.TRIM(empr.ruc) as ruc_empresa,
DBO.TRIM(empr.direccion) as direccion_empresa,
DBO.TRIM(empr.telefono) as telefono_empresa,
DBO.TRIM(empr.celular) as celular_empresa,
DBO.TRIM(empr.pagina_web) as pagina_web_empresa,
DBO.TRIM(empr.correo) as correo_empresa,
DBO.TRIM(empr.logo) as logo,
DBO.TRIM(empr.qr_pagina_web) as qr_pagina

FROM TKardexProducto kard
INNER JOIN TProducto		prod	ON (prod.id_producto = kard.IdProducto and prod.id_empresa = kard.IdEmpresa)
INNER JOIN TDatoComun		dc12	ON (dc12.IdDatoComun = kard.IdTipoOperacion)
LEFT JOIN TCompra			com		ON (com.IdCompra = kard.IdOperacion and com.id_empresa = kard.IdEmpresa)
LEFT JOIN TDatoComun		dc11	ON (dc11.IdDatoComun = com.id_documento)
LEFT JOIN TSalida_material	smat	ON (smat.IdSalidaMaterial = kard.IdOperacion and smat.id_empresa = kard.IdEmpresa)
LEFT JOIN TPersonal			pers	ON (pers.id_personal = smat.id_personal and pers.id_empresa = smat.id_empresa)
LEFT JOIN TUsuario			usuk	ON (upper(dbo.trim(usuk.alias)) = upper(kard.UsuarioInserta) and usuk.id_empresa = kard.IdEmpresa)
LEFT JOIN TEmpresa			empr	ON (empr.id_empresa = kard.IdEmpresa)
LEFT JOIN TDatoComun		dc1		ON (dc1.IdDatoComun = prod.id_unidad)

WHERE 
kard.IdEmpresa = 1 AND
kard.IdProducto =  8821
ORDER BY kard.IdKardex ASC


--select * from TProducto
--select * from TKardexProducto

--select * from TUsuario where id_empresa = 
--select * from TPersonal where id_empresa = 1
