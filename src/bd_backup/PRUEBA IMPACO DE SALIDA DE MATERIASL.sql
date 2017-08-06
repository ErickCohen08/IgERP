compu 10
laptop 10

delete from TDetalle_salida_material
delete from TSalida_material


SELECT sm.id_detalle_salida_material, sm.id_salida_material, sm.id_empresa, sm.CantidadSalida, sm.CantidadRetorno, s.EstadoAbierto, p.cantidad, p.id_producto, p.descripcion
FROM TDetalle_salida_material sm
INNER JOIN TProducto p on p.id_producto = sm.id_producto
INNER JOIN TSalida_material s on s.IdSalidaMaterial = sm.id_salida_material


delete from TDetalle_compra
delete from TCompra



SELECT sm.IdDetalleCompra, s.IdCompra, sm.id_empresa, sm.Cantidad, s.EstadoAbierto, p.cantidad, p.id_producto, p.descripcion
FROM TDetalle_compra sm
INNER JOIN TProducto p on p.id_producto = sm.id_producto
INNER JOIN TCompra s on s.IdCompra = sm.IdCompra

select * from TCompra
select * from TDetalle_compra

select igv from tigv where id_empresa='1' order by predeterminado desc
            
		
3.00
2.00