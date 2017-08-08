SELECT
/*Satos de la salida*/
('COMPRA DE MATERIAL N° ' + RIGHT('0000000000' + dbo.TRIM(com.IdCompra),10))  codigoCompra,
ISNULL(FORMAT(com.FechaCompra, 'd', 'en-gb'),'') fechaCompra,
ISNULL(dbo.TRIM(com.NumeroCompra),'') NumeroCompra,
ISNULL(com.CalculoIgv,0) CalculoIgv,
ISNULL(com.SubTotal,0) SubTotal,
ISNULL(com.Total,0) Total,
ISNULL(dbo.TRIM(com.TotalLetras),'') TotalLetras,

ISNULL(DBO.TRIM(pro.razon_social), '')		desProveedor,
ISNULL(DBO.TRIM(pro.ruc), '')				rucProveedor,
ISNULL(DBO.TRIM(pro.direccion), '')			direccionProveedor,
ISNULL(DBO.TRIM(dc2.DescripcionCorta), '')	desMoneda,
ISNULL(DBO.TRIM(dc2.ValorTexto1), '')		simboloMoneda,
ISNULL(DBO.TRIM(igv.igv), '')				desIgv,
ISNULL(DBO.TRIM(obr.Descripcion), '')		desObra,
ISNULL(DBO.TRIM(dc11.DescripcionCorta), '')	desDocumento,
ISNULL(DBO.TRIM(dc10.DescripcionCorta), '')	desEstadoAbierto,
DBO.TRIM(com.UsuarioModifica) usuarioImprime,
DBO.TRIM(u.nombre) usuarioNombre,

/*Datos del detalle de salida*/
(DBO.TRIM(prod.descripcion) + ISNULL(' Marca ' + DBO.TRIM(prod.marca), '') + ISNULL(' Modelo ' + DBO.TRIM(prod.modelo), '') )  nombreMaterial,
ISNULL(DBO.TRIM(dc1.ValorTexto1), '') unidadMaterial,
ISNULL(dcom.Cantidad, 0) cantidad,
ISNULL(dcom.PrecioUnitario, 0) precioUnitario,
ISNULL(dcom.PrecioTotal, 0) precioTotal,

/*Datos de la empresa*/
DBO.TRIM(e.razon_social) as empresa,
DBO.TRIM(e.ruc) as ruc_empresa,
DBO.TRIM(e.direccion) as direccion_empresa,
DBO.TRIM(e.telefono) as telefono_empresa,
DBO.TRIM(e.celular) as celular_empresa,
DBO.TRIM(e.pagina_web) as pagina_web_empresa,
DBO.TRIM(e.correo) as correo_empresa,
DBO.TRIM(e.logo) as logo,
DBO.TRIM(e.qr_pagina_web) as qr_pagina

FROM TCompra com
LEFT JOIN TProveedor	pro	 on (pro.id_proveedor = com.id_proveedor and pro.id_empresa = com.id_empresa)
LEFT JOIN TObras		obr	 on (obr.IdObra = com.id_obra and obr.id_empresa = com.id_empresa) 
LEFT JOIN TDatoComun	dc10 on (dc10.IdDatoComun = com.EstadoAbierto)
LEFT JOIN TDatoComun	dc11 on (dc11.IdDatoComun = com.id_documento)
LEFT JOIN TDatoComun    dc2  on (dc2.IdDatoComun = com.id_moneda)
LEFT JOIN TIgv			igv  on (igv.id_igv = com.id_igv and igv.id_empresa = com.id_empresa)

LEFT JOIN TUsuario u on (u.alias = com.UsuarioModifica and u.id_empresa = com.id_empresa)
LEFT JOIN TEmpresa e on e.id_empresa = com.id_empresa

LEFT JOIN TDetalle_compra dcom on (dcom.IdCompra = com.IdCompra and dcom.id_empresa = com.id_empresa)
LEFT JOIN TProducto prod	on (prod.id_producto = dcom.id_producto)
LEFT JOIN TDatoComun dc1 on (dc1.IdDatoComun = prod.id_unidad)


WHERE 
com.IdCompra = 2 AND
com.id_empresa = 1
