/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import database.AccesoDB;
import entity.ReporteSalidaMaterialBE;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import service.ReporteService;

/**
 *
 * @author vector
 */
public class ReporteDAO implements ReporteService {

    Connection cn = null;
    ResultSet rs = null;
    PreparedStatement ps = null;
    StringBuilder sql;

    @Override
    public List<ReporteSalidaMaterialBE> salidaMaterialesEntreFechas(String desde, String hasta, int empresa) throws Exception {
        List<ReporteSalidaMaterialBE> list = new ArrayList<>();
        try {
            //abrir conexion a la base de datos
            cn = AccesoDB.getConnection();
            sql = new StringBuilder();
            sql.append("select ");
            sql.append("RIGHT('00000000000' + Ltrim(Rtrim(sma.IdSalidaMaterial)),11) as 'numeroSalida', ");
            sql.append("CONVERT(VARCHAR(10), sma.FechaSalida, 103) as 'fechaSalida', ");
            sql.append("UPPER(ISNULL(LTRIM(RTRIM(per.Nombre)), '') + ' ' + ISNULL(LTRIM(RTRIM(per.apellido)), '')) as 'solicitante', ");
            sql.append("UPPER(ISNULL(LTRIM(RTRIM(sma.Motivo)), 'NO ESPEFICICADO')) as 'motivo', ");
            sql.append("UPPER(ISNULL(LTRIM(RTRIM(cli.razon_social)), 'NO ESPEFICICADO')) as 'cliente', ");
            sql.append("UPPER(ISNULL(LTRIM(RTRIM(obr.Descripcion)), 'NO ESPEFICICADO')) as 'obra', ");
            sql.append("UPPER(ISNULL(LTRIM(RTRIM(sma.Direccion)), 'NO ESPEFICICADO')) as 'direccion', ");
            sql.append("UPPER(ISNULL(LTRIM(RTRIM(dc9.DescripcionCorta)), 'NO ESPEFICICADO')) as 'estadoSalida', ");
            sql.append("ISNULL(dsm.CantidadRetorno, 0) as 'cantidadRetorno', ");
            sql.append("RIGHT('00000000000' + Ltrim(Rtrim(pro.id_producto)),11) as 'idProducto', ");
            sql.append("UPPER(ISNULL(LTRIM(RTRIM(pro.descripcion)), 'NO ESPEFICICADO')) as 'material', ");
            sql.append("UPPER(ISNULL(LTRIM(RTRIM(dc1.ValorTexto1)), 'NO ESPEFICICADO')) as 'unidad', ");
            sql.append("ISNULL(dsm.CantidadSalida, 0) as 'cantidad', ");
            sql.append("UPPER(ISNULL(LTRIM(RTRIM(pro.marca)), 'NO ESPEFICICADO')) as 'marca', ");
            sql.append("UPPER(ISNULL(LTRIM(RTRIM(dc6.DescripcionCorta)), 'NO ESPEFICICADO')) as 'categoria' ");
            sql.append("from TSalida_material sma ");
            sql.append("inner join TPersonal per on per.id_personal = sma.id_personal ");
            sql.append("inner join TDetalle_salida_material dsm on dsm.id_salida_material = sma.IdSalidaMaterial ");
            sql.append("inner join TProducto pro on pro.id_producto = dsm.id_producto ");
            sql.append("left join TDatoComun dc1 on dc1.IdDatoComun = pro.id_unidad ");
            sql.append("left join TObras obr on obr.IdObra = sma.id_obra ");
            sql.append("left join TCliente cli on cli.id_cliente = obr.id_cliente ");
            sql.append("left join TDatoComun dc9 on dc9.IdDatoComun = sma.EstadoAbierto ");
            sql.append("left join TDatoComun dc6 on dc6.IdDatoComun = pro.id_productotipo ");
            sql.append("where ");
            sql.append("sma.id_empresa = ");
            sql.append(empresa);
            sql.append(" ");
            sql.append("and cast(sma.FechaSalida as date) between '");
            sql.append(desde);
            sql.append("' and '");
            sql.append(hasta);
            sql.append("' ");
            sql.append("order by sma.FechaSalida desc, sma.IdSalidaMaterial desc ");
            System.out.print(sql.toString());
            ps = cn.prepareStatement(sql.toString());
            rs = ps.executeQuery();

            ReporteSalidaMaterialBE obj;

            while (rs.next()) {
                obj = new ReporteSalidaMaterialBE();
                obj.setNumeroSalida(rs.getString("numeroSalida"));
                obj.setFechaSalida(rs.getString("fechaSalida"));
                obj.setSolicitante(rs.getString("solicitante"));
                obj.setMotivo(rs.getString("motivo"));
                obj.setCliente(rs.getString("cliente"));
                obj.setObra(rs.getString("obra"));
                obj.setDireccion(rs.getString("direccion"));
                obj.setEstadoSalida(rs.getString("estadoSalida"));
                obj.setCantidadRetorno(rs.getDouble("cantidadRetorno"));
                obj.setIdProducto(rs.getInt("idProducto"));
                obj.setMaterial(rs.getString("material"));
                obj.setUnidad(rs.getString("unidad"));
                obj.setCantidad(rs.getDouble("cantidad"));
                obj.setMarca(rs.getString("marca"));
                obj.setCategoria(rs.getString("categoria"));
                list.add(obj);
            }

            rs.close();
            ps.close();
        } catch (IllegalAccessException | InstantiationException | SQLException e) {
            throw e;
        } finally {
            cn.close();
        }

        return list;
    }

    @Override
    public List<ReporteSalidaMaterialBE> salidaMaterialesAll(int empresa) throws Exception {
        List<ReporteSalidaMaterialBE> list = new ArrayList<>();
        try {
            //abrir conexion a la base de datos
            cn = AccesoDB.getConnection();
            sql = new StringBuilder();
            sql.append("select ");
            sql.append("RIGHT('00000000000' + Ltrim(Rtrim(sma.IdSalidaMaterial)),11) as 'numeroSalida', ");
            sql.append("CONVERT(VARCHAR(10), sma.FechaSalida, 103) as 'fechaSalida', ");
            sql.append("UPPER(ISNULL(LTRIM(RTRIM(per.Nombre)), '') + ' ' + ISNULL(LTRIM(RTRIM(per.apellido)), '')) as 'solicitante', ");
            sql.append("UPPER(ISNULL(LTRIM(RTRIM(sma.Motivo)), 'NO ESPEFICICADO')) as 'motivo', ");
            sql.append("UPPER(ISNULL(LTRIM(RTRIM(cli.razon_social)), 'NO ESPEFICICADO')) as 'cliente', ");
            sql.append("UPPER(ISNULL(LTRIM(RTRIM(obr.Descripcion)), 'NO ESPEFICICADO')) as 'obra', ");
            sql.append("UPPER(ISNULL(LTRIM(RTRIM(sma.Direccion)), 'NO ESPEFICICADO')) as 'direccion', ");
            sql.append("UPPER(ISNULL(LTRIM(RTRIM(dc9.DescripcionCorta)), 'NO ESPEFICICADO')) as 'estadoSalida', ");
            sql.append("ISNULL(dsm.CantidadRetorno, 0) as 'cantidadRetorno', ");
            sql.append("RIGHT('00000000000' + Ltrim(Rtrim(pro.id_producto)),11) as 'idProducto', ");
            sql.append("UPPER(ISNULL(LTRIM(RTRIM(pro.descripcion)), 'NO ESPEFICICADO')) as 'material', ");
            sql.append("UPPER(ISNULL(LTRIM(RTRIM(dc1.ValorTexto1)), 'NO ESPEFICICADO')) as 'unidad', ");
            sql.append("ISNULL(dsm.CantidadSalida, 0) as 'cantidad', ");
            sql.append("UPPER(ISNULL(LTRIM(RTRIM(pro.marca)), 'NO ESPEFICICADO')) as 'marca', ");
            sql.append("UPPER(ISNULL(LTRIM(RTRIM(dc6.DescripcionCorta)), 'NO ESPEFICICADO')) as 'categoria' ");
            sql.append("from TSalida_material sma ");
            sql.append("inner join TPersonal per on per.id_personal = sma.id_personal ");
            sql.append("inner join TDetalle_salida_material dsm on dsm.id_salida_material = sma.IdSalidaMaterial ");
            sql.append("inner join TProducto pro on pro.id_producto = dsm.id_producto ");
            sql.append("left join TDatoComun dc1 on dc1.IdDatoComun = pro.id_unidad ");
            sql.append("left join TObras obr on obr.IdObra = sma.id_obra ");
            sql.append("left join TCliente cli on cli.id_cliente = obr.id_cliente ");
            sql.append("left join TDatoComun dc9 on dc9.IdDatoComun = sma.EstadoAbierto ");
            sql.append("left join TDatoComun dc6 on dc6.IdDatoComun = pro.id_productotipo ");
            sql.append("where ");
            sql.append("sma.id_empresa = ");
            sql.append(empresa);
            sql.append(" ");
            sql.append("order by sma.FechaSalida desc, sma.IdSalidaMaterial desc ");
            System.out.print(sql.toString());
            ps = cn.prepareStatement(sql.toString());
            rs = ps.executeQuery();

            ReporteSalidaMaterialBE obj;

            while (rs.next()) {
                obj = new ReporteSalidaMaterialBE();
                obj.setNumeroSalida(rs.getString("numeroSalida"));
                obj.setFechaSalida(rs.getString("fechaSalida"));
                obj.setSolicitante(rs.getString("solicitante"));
                obj.setMotivo(rs.getString("motivo"));
                obj.setCliente(rs.getString("cliente"));
                obj.setObra(rs.getString("obra"));
                obj.setDireccion(rs.getString("direccion"));
                obj.setEstadoSalida(rs.getString("estadoSalida"));
                obj.setCantidadRetorno(rs.getDouble("cantidadRetorno"));
                obj.setIdProducto(rs.getInt("idProducto"));
                obj.setMaterial(rs.getString("material"));
                obj.setUnidad(rs.getString("unidad"));
                obj.setCantidad(rs.getDouble("cantidad"));
                obj.setMarca(rs.getString("marca"));
                obj.setCategoria(rs.getString("categoria"));
                list.add(obj);
            }

            rs.close();
            ps.close();
        } catch (IllegalAccessException | InstantiationException | SQLException e) {
            throw e;
        } finally {
            cn.close();
        }

        return list;
    }
}
