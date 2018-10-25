select id_personal, count(id_personal) cant
from TSalida_material 
group by id_personal
order by cant desc;

select * from TProducto;
select * from TPersonal where id_personal = 8;

select
ISNULL(LTRIM(RTRIM(per.Nombre)), '') + ' ' + ISNULL(LTRIM(RTRIM(per.apellido)), '') as 'personal',
LTRIM(RTRIM(pro.descripcion)) as 'material', 
ISNULL(dc1.ValorTexto1, '') as 'unidad',
dsm.CantidadSalida as 'cantidad', 
RIGHT('00000000000' + Ltrim(Rtrim(sma.IdSalidaMaterial)),11) as 'NumeroSalida',
CONVERT(VARCHAR(10), sma.FechaSalida, 103) as 'fechaSalida', 
ISNULL(LTRIM(RTRIM(sma.Motivo)), '') as 'motivo',
ISNULL(LTRIM(RTRIM(obr.Descripcion)), '') as 'obra',
ISNULL(LTRIM(RTRIM(sma.Direccion)), '') as 'direccion',
ISNULL(LTRIM(RTRIM(cli.razon_social)), '') as 'cliente',
dc9.DescripcionCorta as 'estadoSalida',
ISNULL(dsm.CantidadRetorno, 0) as 'cantidadRetorno'
from TSalida_material sma
inner join TPersonal per on per.id_personal = sma.id_personal
inner join TDetalle_salida_material dsm on dsm.id_salida_material = sma.IdSalidaMaterial
inner join TProducto pro on pro.id_producto = dsm.id_producto
left join TDatoComun dc1 on dc1.IdDatoComun = pro.id_unidad
left join TObras obr on obr.IdObra = sma.id_obra
left join TCliente cli on cli.id_cliente = obr.id_cliente
left join TDatoComun dc9 on dc9.IdDatoComun = sma.EstadoAbierto
where
sma.id_personal = 8 and 
sma.id_empresa = 1 
order by sma.FechaSalida desc;
--3793

select * from TDetalle_salida_material;
select * from TPersonal;
select * from TCliente;
select * from TDatoComun where CodigoTabla = 7;
select * from TDatoComun where CodigoTabla = 9;

select * from TEmpresa;
select * from TSalida_material;
select * from TDetalle_salida_material;
select * from TDatoComun where CodigoFila > 0 AND CodigoTabla = 1;
select * from TDatoComun where CodigoFila > 0 and CodigoTabla = 6;
select * from TProducto;
select * from TDetalle_salida_material;
select * from TObras;




