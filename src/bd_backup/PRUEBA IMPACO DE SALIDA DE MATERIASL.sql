compu 10
laptop 10

SELECT sm.id_detalle_salida_material, sm.id_salida_material, sm.id_empresa, sm.CantidadSalida, sm.CantidadRetorno, s.EstadoAbierto, p.cantidad, p.id_producto, p.descripcion
FROM TDetalle_salida_material sm
INNER JOIN TProducto p on p.id_producto = sm.id_producto
INNER JOIN TSalida_material s on s.IdSalidaMaterial = sm.id_salida_material





delete from TDetalle_salida_material
delete from TSalida_material

select * from 


4.00
3.00
25.00
12.00


3.00
2.00
25.00
12.00