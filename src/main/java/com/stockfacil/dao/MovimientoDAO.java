
package com.stockfacil.dao;

import com.stockfacil.config.Conexion;
import com.stockfacil.model.Movimiento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovimientoDAO {

    public List<Movimiento> listarRecientes(int cantidad) throws SQLException {

        String sql = "SELECT M.*, P.NOMBRE AS NOMBRE_PRODUCTO, "
                + "U.NOMBRE AS NOMBRE_USUARIO "
                + "FROM MOVIMIENTO M "
                + "INNER JOIN PRODUCTO P "
                + "ON P.ID_PRODUCTO = M.ID_PRODUCTO "
                + "INNER JOIN USUARIO U "
                + "ON U.ID_USUARIO = M.ID_USUARIO "
                + "ORDER BY M.FECHA DESC, M.ID_MOVIMIENTO DESC "
                + "LIMIT ?";

        List<Movimiento> lista = new ArrayList<>();

        try (Connection con = Conexion.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cantidad);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }

        return lista;
    }
    
    public void registrar(Movimiento m) throws SQLException, StockInsuficienteException {
        Connection con = Conexion.obtener();
        try {
           con.setAutoCommit(false);                       // 1. Empieza la transacción
            // 2. Leer el stock actual (FOR UPDATE bloquea la fila hasta el commit)
            int stockActual;
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT stock FROM producto WHERE id_producto = ? FOR UPDATE")) {
                ps.setInt(1, m.getIdProducto());
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) throw new SQLException("El producto no existe.");
                    stockActual = rs.getInt("stock");
                }
            }
            // 3. Calcular el nuevo stock y validar la regla
            int nuevoStock = "ENTRADA".equals(m.getTipo())
                    ? stockActual + m.getCantidad()
                    : stockActual - m.getCantidad();
            if (nuevoStock < 0) {
                throw new StockInsuficienteException(
                        "Stock insuficiente: hay " + stockActual + " unidades y se quieren sacar " + m.getCantidad() + ".");
            }
            // 4. Guardar el movimiento
            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO movimiento (id_producto, tipo, cantidad, id_usuario, nota) VALUES (?, ?, ?, ?, ?)")) {
                ps.setInt(1, m.getIdProducto());
                ps.setString(2, m.getTipo());
                ps.setInt(3, m.getCantidad());
                ps.setInt(4, m.getIdUsuario());
                ps.setString(5, m.getNota());
                ps.executeUpdate();
            }
            // 5. Actualizar el stock del producto
            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE producto SET stock = ? WHERE id_producto = ?")) {
                ps.setInt(1, nuevoStock);
                ps.setInt(2, m.getIdProducto());
                ps.executeUpdate();
            }
            con.commit();                                   // 6. Todo bien: se confirma
        } catch (SQLException | StockInsuficienteException e) {
            con.rollback();                                 // 6. Algo falló: se deshace todo
            throw e;
        } finally {
            con.setAutoCommit(true);
            con.close();
        }
    }

    private Movimiento mapear(ResultSet rs) throws SQLException {

        Movimiento m = new Movimiento();

        m.setIdMovimiento(rs.getInt("id_movimiento"));
        m.setIdProducto(rs.getInt("id_producto"));
        m.setNombreProducto(rs.getString("nombre_producto"));
        m.setTipo(rs.getString("tipo"));
        m.setCantidad(rs.getInt("cantidad"));
        m.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
        m.setIdUsuario(rs.getInt("id_usuario"));
        m.setNombreUsuario(rs.getString("nombre_usuario"));
        m.setNota(rs.getString("nota"));

        return m;
    }
}

