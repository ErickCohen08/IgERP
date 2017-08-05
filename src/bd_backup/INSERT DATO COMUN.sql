--Insert Datos comunes
USE [bd_ig-projet]
go
--Unidad de medida
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (10000, 1, 0, 'Unidad de medida', null,		null,	null, 'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (10001, 1, 1, 'Unidad',			 null,		'UNID',	NULL,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (10002, 1, 2, 'Global',			 null,		'GLB',	NULL,'ERCO','A');          
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (10003, 1, 3, 'Metro cuadrado',	 null,		'M2',	NULL,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (10004, 1, 4, 'Metro cubico',	 null,		'M3',	NULL,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (10005, 1, 5, 'Metro lineal',	 null,		'ML',	NULL,'ERCO','A');    
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (10006, 1, 6, 'Centimetro',		 null,		'CM',	NULL,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (10007, 1, 7, 'Pieza',			 null,		'PZA',	NULL,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (10008, 1, 8, 'Hora hombre',		 null,		'HH',	NULL,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (10009, 1, 9, 'Hora maquina',	 null,		'HM',	NULL,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (10010, 1, 10, 'Pie cuadrado',	 null,		'P2',	NULL,'ERCO','A');    
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (10011, 1, 11, 'Galon',			 null,		'GAL',	NULL,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (10012, 1, 12, 'Litro',			 null,		'LT',	NULL,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (10013, 1, 13, 'Gramo',			 null,		'GR',	NULL,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (10014, 1, 14, 'Kilogramo',		 null,		'KG',	NULL,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (10015, 1, 15, 'Tonelada',		 null,		'TN',	NULL,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (10016, 1, 16, 'Conjunto',		 null,		'CTO',	NULL,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (10017, 1, 17, 'Rollo',			 null,		'RLL',	NULL,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (10018, 1, 18, 'Varilla',		 null,		'VAR',	NULL,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (10019, 1, 19, 'Bolsa',			 null,		'BLS',	NULL,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (10020, 1, 20, 'Mes',			 null,		'MES',	NULL,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (10021, 1, 21, 'Juego',			 null,		'JGO',	NULL,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (10022, 1, 22, 'Punto',			 null,		'PTO',	NULL,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (10023, 1, 23, 'Millar',			 null,		'MLL',	NULL,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (10024, 1, 24, 'Balde',			 null,		'BLD',	NULL,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (10025, 1, 25, 'Caja',			 null,		'CJA',	NULL,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (10026, 1, 26, 'Operacion',		 null,		'OP',	NULL,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (10027, 1, 27, 'Plancha',		 null,		'PL',	NULL,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (10028, 1, 28, 'Kilometro',		 null,		'KM',	NULL,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (10029, 1, 29, 'Par',			 null,		'PAR',	NULL,'ERCO','A');

--Monedad
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (20000, 2, 0, 'Moneda',				null,	null, null, 'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (20001, 2, 1, 'Soles',				null,	'S/',	'1','ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (20002, 2, 2, 'Dólares Americanos',	null,	'US$',	'0','ERCO','A');

--Almacen
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (30000, 3, 0, 'Almacen',	  null,	null, null, 'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (30001, 3, 1, 'Almacen 1', null,	'Direccion Almacen 1',	null,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (30002, 3, 2, 'Almacen 2', null,	'Direccion Almacen 2',	null,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (30003, 3, 3, 'Almacen 3', null,	'Direccion Almacen 3',	null,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (30004, 3, 4, 'Almacen 4', null,	'Direccion Almacen 4',	null,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (30005, 3, 5, 'Almacen 5', null,	'Direccion Almacen 5',	null,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (30006, 3, 6, 'Almacen 6', null,	'Direccion Almacen 6',	null,'ERCO','A');

--Precio Referencia
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (40000, 4, 0, 'Precio Referencia',	null,	null,	null,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (40001, 4, 1, 'Compras',				null,	null,	null,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (40002, 4, 2, 'Revista Costos',		null,	null,	null,'ERCO','A');


--Tipo Producto
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (50000, 5, 0, 'Tipo Producto',	null,	null,	null,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (50001, 5, 1, 'Material',				null,	null,	null,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (50002, 5, 2, 'Partidas/Servicios',		null,	null,	null,'ERCO','A');

--Categoria Producto
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (60000, 6, 0, 'Categoria de Producto',	null,	null,	null,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (60001, 6, 1, 'Tuberia',					null,	null,	null,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (60002, 6, 2, 'Gabinetes',				null,	null,	null,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (60003, 6, 3, 'Rociadores',				null,	null,	null,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (60004, 6, 4, 'Accesorios Roscados',		null,	null,	null,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (60005, 6, 5, 'Accesorios Ranurados',	null,	null,	null,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (60006, 6, 6, 'Colgadores Antisismicos',	null,	null,	null,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (60007, 6, 7, 'Válvulas',				null,	null,	null,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (60008, 6, 8, 'Mangueras',				null,	null,	null,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (60009, 6, 9, 'Suministros',				null,	null,	null,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (60010, 6,10, 'Equipos de Medición',		null,	null,	null,'ERCO','A');

--Estado
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (70000, 7, 0, 'Estado Registro',		null,	null,	null,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (70001, 7, 1, 'Activo',				null,	'A',	null,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (70002, 7, 2, 'Inactivo',			null,	'I',	null,'ERCO','A');
 
--Estado Inventario Material
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (80000, 8, 0, 'Estado Inventario',	null,	null,	null,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (80001, 8, 1, 'Abierto',				null,	'A',	null,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (80002, 8, 2, 'Cerrado',				null,	'C',	null,'ERCO','A');


--Estado Salida Material
DELETE FROM TDatoComun WHERE CodigoTabla = 9
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (90000, 9, 0, 'Estado Salida de Material',		null,	null,	null,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (90001, 9, 1, 'Por confirmar Salida',			null,	'S',	null,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (90002, 9, 2, 'Salida de Material Confirmado',	null,	'SC',	null,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (90003, 9, 3, 'Por confirmar Entrega',			null,	'E',	null,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (90004, 9, 4, 'Entrega de material confirmado',	null,	'EC',	null,'ERCO','A');

--Estado Compra de material
DELETE FROM TDatoComun WHERE CodigoTabla = 10
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (100000, 10, 0, 'Estado Compra de Material',		null,	null,	null,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (100001, 10, 1, 'Por confirmar Compra',			null,	'C',	null,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (100002, 10, 2, 'Compra de Material Confirmado',	null,	'CC',	null,'ERCO','A');

--Documentos de compra o venta
DELETE FROM TDatoComun WHERE CodigoTabla = 11
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (110000, 11, 0, 'Documento Compra Venta',	null,	null,	null,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (110001, 11, 1, 'Factura',					null,	'F',	null,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (110002, 11, 2, 'Boleta',					null,	'B',	null,'ERCO','A');
INSERT INTO TDatoComun (IdDatoComun, CodigoTabla, CodigoFila, DescripcionCorta, DescripcionLarga, ValorTexto1, ValorTexto2, UsuarioInserta, Estado) VALUES (110003, 11, 3, 'Ticket',					null,	'T',	null,'ERCO','A');
