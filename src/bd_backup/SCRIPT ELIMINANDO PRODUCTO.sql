select * from TDatoComun where CodigoTabla = 7

select descripcion, estado from TProducto where id_producto = 9909
SELECT * FROM TDetalle_compra WHERE id_producto = 9909

/*
9862 DISCO DE CORTE DE 4.5"
*/
SELECT * FROM TProducto
WHERE 
estado = 70002
ORDER BY descripcion

DELETE FROM TProducto WHERE id_producto = 9909
DELETE FROM TKardexProducto WHERE IdProducto = 9909
DELETE FROM TDetalle_compra WHERE Id_Producto = 9909
DELETE FROM TDetalle_salida_material WHERE Id_Producto = 9909

UPDATE TProducto SET estado = 70001 WHERE id_producto = 9850;
SELECT * FROM TProducto WHERE id_producto = 9850
SELECT * FROM TKardexProducto WHERE IdProducto = 9850
SELECT * FROM TDetalle_compra WHERE Id_Producto = 9850
SELECT * FROM TDetalle_salida_material WHERE Id_Producto = 9850

SELECT * FROM TProducto WHERE id_producto = 9909
SELECT * FROM TKardexProducto WHERE IdProducto = 9909
SELECT * FROM TDetalle_compra WHERE Id_Producto = 9909
SELECT * FROM TDetalle_salida_material WHERE Id_Producto = 9909



DELETE FROM TProducto WHERE id_producto = 9864
DELETE FROM TKardexProducto WHERE IdProducto = 9864
DELETE FROM TDetalle_compra WHERE Id_Producto = 9864
DELETE FROM TDetalle_salida_material WHERE Id_Producto = 9864

SELECT * FROM TProducto WHERE id_producto = 9864
SELECT * FROM TKardexProducto WHERE IdProducto = 9864
SELECT * FROM TDetalle_compra WHERE Id_Producto = 9864
SELECT * FROM TDetalle_salida_material WHERE Id_Producto = 9864





DELETE FROM TProducto WHERE id_producto = 9923
DELETE FROM TKardexProducto WHERE IdProducto = 9923
DELETE FROM TDetalle_compra WHERE Id_Producto = 9923
DELETE FROM TDetalle_salida_material WHERE Id_Producto = 9923

SELECT * FROM TProducto WHERE id_producto = 9923
SELECT * FROM TKardexProducto WHERE IdProducto = 9923
SELECT * FROM TDetalle_compra WHERE Id_Producto = 9923
SELECT * FROM TDetalle_salida_material WHERE Id_Producto = 9923



DELETE FROM TProducto WHERE id_producto = 8907
DELETE FROM TKardexProducto WHERE IdProducto = 8907
DELETE FROM TDetalle_compra WHERE Id_Producto = 8907
DELETE FROM TDetalle_salida_material WHERE Id_Producto = 8907

SELECT * FROM TProducto WHERE id_producto = 8907
SELECT * FROM TKardexProducto WHERE IdProducto = 8907
SELECT * FROM TDetalle_compra WHERE Id_Producto = 8907
SELECT * FROM TDetalle_salida_material WHERE Id_Producto = 8907







DELETE FROM TProducto WHERE id_producto = 9865
DELETE FROM TKardexProducto WHERE IdProducto = 9865
DELETE FROM TDetalle_compra WHERE Id_Producto = 9865
DELETE FROM TDetalle_salida_material WHERE Id_Producto = 9865
UPDATE TProducto SET estado = 70001, cantidad = 0 WHERE id_producto = 9863;
UPDATE TCompra SET EstadoAbierto = 100001 WHERE IdCompra = 8 AND id_empresa = 1
DELETE FROM TKardexProducto WHERE IdKardex = 1194
SELECT * FROM TProducto WHERE id_producto = 9863
SELECT * FROM TKardexProducto WHERE IdProducto = 9863
SELECT * FROM TDetalle_compra WHERE Id_Producto = 9863
SELECT * FROM TDetalle_salida_material WHERE Id_Producto = 9863
SELECT * FROM TCompra WHERE IdCompra = 8

UPDATE TProducto SET estado = 70001, cantidad = 0 WHERE id_producto = 9862;
DELETE FROM TKardexProducto WHERE IdKardex = 1193
SELECT * FROM TProducto WHERE id_producto = 9862
SELECT * FROM TKardexProducto WHERE IdProducto = 9862
SELECT * FROM TDetalle_compra WHERE Id_Producto = 9862
SELECT * FROM TDetalle_salida_material WHERE Id_Producto = 9862





SELECT * FROM TProducto WHERE id_producto = 9865
SELECT * FROM TKardexProducto WHERE IdProducto = 9865
SELECT * FROM TDetalle_compra WHERE Id_Producto = 9865
SELECT * FROM TDetalle_salida_material WHERE Id_Producto = 9865



DELETE FROM TProducto WHERE id_producto = 9847
DELETE FROM TKardexProducto WHERE IdProducto = 9847
DELETE FROM TDetalle_compra WHERE Id_Producto = 9847
DELETE FROM TDetalle_salida_material WHERE Id_Producto = 9847

SELECT * FROM TProducto WHERE id_producto = 9847
SELECT * FROM TKardexProducto WHERE IdProducto = 9847
SELECT * FROM TDetalle_compra WHERE Id_Producto = 9847
SELECT * FROM TDetalle_salida_material WHERE Id_Producto = 9847



DELETE FROM TProducto WHERE id_producto = 9927
DELETE FROM TKardexProducto WHERE IdProducto = 9927
DELETE FROM TDetalle_compra WHERE Id_Producto = 9927
DELETE FROM TDetalle_salida_material WHERE Id_Producto = 9927

SELECT * FROM TProducto WHERE id_producto = 9927
SELECT * FROM TKardexProducto WHERE IdProducto = 9927
SELECT * FROM TDetalle_compra WHERE Id_Producto = 9927
SELECT * FROM TDetalle_salida_material WHERE Id_Producto = 9927



DELETE FROM TProducto WHERE id_producto = 9928
DELETE FROM TKardexProducto WHERE IdProducto = 9928
DELETE FROM TDetalle_compra WHERE Id_Producto = 9928
DELETE FROM TDetalle_salida_material WHERE Id_Producto = 9928

SELECT * FROM TProducto WHERE id_producto = 9928
SELECT * FROM TKardexProducto WHERE IdProducto = 9928
SELECT * FROM TDetalle_compra WHERE Id_Producto = 9928
SELECT * FROM TDetalle_salida_material WHERE Id_Producto = 9928



DELETE FROM TProducto WHERE id_producto = 8906
DELETE FROM TKardexProducto WHERE IdProducto = 8906
DELETE FROM TDetalle_compra WHERE Id_Producto = 8906
DELETE FROM TDetalle_salida_material WHERE Id_Producto = 8906

SELECT * FROM TProducto WHERE id_producto = 8906
SELECT * FROM TKardexProducto WHERE IdProducto = 8906
SELECT * FROM TDetalle_compra WHERE Id_Producto = 8906
SELECT * FROM TDetalle_salida_material WHERE Id_Producto = 8906



DELETE FROM TProducto WHERE id_producto = 8905
DELETE FROM TKardexProducto WHERE IdProducto = 8905
DELETE FROM TDetalle_compra WHERE Id_Producto = 8905
DELETE FROM TDetalle_salida_material WHERE Id_Producto = 8905

SELECT * FROM TProducto WHERE id_producto = 8905
SELECT * FROM TKardexProducto WHERE IdProducto = 8905
SELECT * FROM TDetalle_compra WHERE Id_Producto = 8905
SELECT * FROM TDetalle_salida_material WHERE Id_Producto = 8905




DELETE FROM TProducto WHERE id_producto = 9866
DELETE FROM TKardexProducto WHERE IdProducto = 9866
DELETE FROM TDetalle_compra WHERE Id_Producto = 9866
DELETE FROM TDetalle_salida_material WHERE Id_Producto = 9866
UPDATE TProducto SET estado = 70001 WHERE id_producto = 9862;
SELECT * FROM TProducto WHERE id_producto = 9862
SELECT * FROM TKardexProducto WHERE IdProducto = 9862
SELECT * FROM TDetalle_compra WHERE Id_Producto = 9862
SELECT * FROM TDetalle_salida_material WHERE Id_Producto = 9862



DELETE FROM TProducto WHERE id_producto = 9912
DELETE FROM TKardexProducto WHERE IdProducto = 9912
DELETE FROM TDetalle_compra WHERE Id_Producto = 9912
DELETE FROM TDetalle_salida_material WHERE Id_Producto = 9912

SELECT * FROM TProducto WHERE id_producto = 9912
SELECT * FROM TKardexProducto WHERE IdProducto = 9912
SELECT * FROM TDetalle_compra WHERE Id_Producto = 9912
SELECT * FROM TDetalle_salida_material WHERE Id_Producto = 9912


DELETE FROM TProducto WHERE id_producto = 8911
DELETE FROM TKardexProducto WHERE IdProducto = 8911
DELETE FROM TDetalle_compra WHERE Id_Producto = 8911
DELETE FROM TDetalle_salida_material WHERE Id_Producto = 8911

SELECT * FROM TProducto WHERE id_producto = 9912
SELECT * FROM TKardexProducto WHERE IdProducto = 9912
SELECT * FROM TDetalle_compra WHERE Id_Producto = 9912
SELECT * FROM TDetalle_salida_material WHERE Id_Producto = 9912

