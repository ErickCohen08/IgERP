SELECT
/*Satos de la salida*/
('SALIDA DE MATERIAL N° ' + RIGHT('0000000000' + dbo.TRIM(sm.IdSalidaMaterial),10))  nroSalidaMaterial,
ISNULL(FORMAT(sm.FechaSalida, 'd', 'en-gb'),'') fechaSalida,
ISNULL(FORMAT(sm.FechaRetorno, 'd', 'en-gb'),'') fechaRetorno,
dbo.TRIM(sm.Direccion) direccion,
ISNULL(DBO.TRIM(sm.Motivo), '') motivo,
DBO.TRIM(o.Descripcion) obra,
DBO.TRIM(DBO.TRIM(ISNULL(p.Nombre, '')) +' '+DBO.TRIM(ISNULL(p.Apellido, ''))) personalResponsableSalida,
DBO.TRIM(dc9.DescripcionCorta) estadoAbierto,
DBO.TRIM(sm.UsuarioModifica) usuarioImprime,
DBO.TRIM(u.nombre) usuarioNombre,

/*Datos del detalle de salida*/
(DBO.TRIM(pro.descripcion) + ISNULL(' Marca ' + DBO.TRIM(pro.marca), '') + ISNULL(' Modelo ' + DBO.TRIM(pro.modelo), '') )  nombreMaterial,
ISNULL(DBO.TRIM(dc1.ValorTexto1), '') unidadMaterial,
ISNULL(dsm.CantidadSalida, 0) cantidadSalida,
ISNULL(dsm.CantidadRetorno, 0) cantidadRetorno,
ISNULL(DBO.TRIM(dsm.ComentarioRetorno), '') comentarioRetorno,

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

FROM TSalida_material sm
LEFT JOIN TPersonal p on p.id_personal = sm.id_personal
LEFT JOIN TObras o on o.IdObra = sm.id_obra
LEFT JOIN TDatoComun dc9 on dc9.IdDatoComun = sm.EstadoAbierto
LEFT JOIN TUsuario u on u.alias = sm.UsuarioModifica
LEFT JOIN TEmpresa e on e.id_empresa = sm.id_empresa
LEFT JOIN TDetalle_salida_material dsm on dsm.id_salida_material = sm.IdSalidaMaterial
LEFT JOIN TProducto pro on pro.id_producto = dsm.id_producto
LEFT JOIN TDatoComun dc1 on dc1.IdDatoComun = pro.id_unidad
WHERE 
sm.IdSalidaMaterial = 3 AND
sm.id_empresa = 1 AND 
U.id_empresa = 1 AND 
--o.id_empresa = 1 AND
dsm.id_empresa = 1

--SELECT * FROM TSalida_material

---select * from TProducto

/*select * from TSalida_material

delete from TDetalle_salida_material
delete from TSalida_material*/

--select * from TCotizacion
--select * from TEmpresa


/*
c.numero,
d.serie,
convert(varchar, c.fecha, 103) as fecha,
rtrim(m.nombre) as moneda,
rtrim(m.simbolo) as simbolo,
rtrim(tc.descripcion) as categoria,
rtrim(c.proyecto) as proyecto,
rtrim(c.ubicacion) as ubicacion,
rtrim(c.tiempo_duracion)  as duracion,
rtrim(cl.razon_social) as cliente,
rtrim(cl.ruc) as cliente_ruc,
rtrim(cl.direccion) as cliente_direccion,
rtrim(c.atencion) as atencion,
rtrim(fp.descripcion) as forma_pago,
c.costo_neto,
c.gasto_gen_por,
c.gasto_gen_monto,
c.utilidad_por,
c.utilidad_monto,
c.subtotal,
c.descuento_por,
c.descuento_monto,
c.subtotal_neto,
i.igv,
c.igv_monto,
c.total,
rtrim(c.total_letras) as total_letras,

rtrim(e.razon_social) as empresa,
rtrim(e.ruc) as ruc_empresa,
rtrim(e.direccion) as direccion_empresa,
rtrim(e.telefono) as telefono_empresa,
rtrim(e.celular) as celular_empresa,
rtrim(e.pagina_web) as pagina_web_empresa,
rtrim(e.correo) as correo_empresa,

rtrim(e.logo) as logo,
rtrim(e.qr_pagina_web) as qr_pagina,
rtrim(e.firma) as firma,

cd.id_cotizaciondetalle,
rtrim(cd.categ_padre) as categ_padre,
cd.item,
rtrim(cd.descripcion) as descripcion,
rtrim(up.codigo) as unidad,
cd.cantidad,
cd.precio_unitario,
cd.precio_total

from
TCotizacion c, TDocumentos d, TMoneda m, TTipoCotizacion tc, TCliente cl, TFormaPago fp, TIgv i, TEmpresa e, TCotizacion_detalle cd, TUnidad_producto up
where
c.id_documento = d.id_documento and
c.id_moneda = m.id_moneda and
c.id_tipocotizacion = tc.id_tipocotizacion and
c.id_cliente = cl.id_cliente and
c.id_formapago = fp.id_formapago and
c.id_igv = i.id_igv and
c.id_empresa = e.id_empresa and
c.id_cotizacion = cd.id_cotizacion and
c.id_cotizacion = $P{id_cotizacion} and
cd.id_unidad = up.id_unidad
order by cd.item
*/