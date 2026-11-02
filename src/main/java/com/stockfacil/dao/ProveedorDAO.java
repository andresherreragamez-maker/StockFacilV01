package com.stockfacil.dao;

import com.stockfacil.config.Conexion;
import com.stockfacil.model.Proveedor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO {

    // Mostrar todos los proveedores
    public List<Proveedor> listar() throws SQLException {

        String sql = "SELECT * FROM proveedor ORDER BY nombre";

        List<Proveedor> lista = new ArrayList<>();

        try (Connection con = Conexion.obtener();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }

        return lista;
    }

    // Buscar proveedor por ID
    public Proveedor buscarPorId(int id) throws SQLException {

        String sql = "SELECT * FROM proveedor WHERE id_proveedor = ?";

        try (Connection con = Conexion.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }

        return null;
    }

    // Verificar si la cédula jurídica ya existe
    public boolean existeCedula(String cedula, int idExcluir)
            throws SQLException {

        String sql = "SELECT COUNT(*) FROM proveedor "
                   + "WHERE cedula_juridica = ? "
                   + "AND id_proveedor <> ?";

        try (Connection con = Conexion.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cedula);
            ps.setInt(2, idExcluir);

            try (ResultSet rs = ps.executeQuery()) {

                rs.next();

                return rs.getInt(1) > 0;
            }
        }
    }

    // Registrar proveedor
    public void insertar(Proveedor p) throws SQLException {

        String sql = "INSERT INTO proveedor "
                   + "(nombre, cedula_juridica, telefono, correo) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection con = Conexion.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getCedulaJuridica());
            ps.setString(3, p.getTelefono());
            ps.setString(4, p.getCorreo());

            ps.executeUpdate();
        }
    }

    // Actualizar proveedor
    public void actualizar(Proveedor p) throws SQLException {

        String sql = "UPDATE proveedor SET "
                   + "nombre = ?, "
                   + "cedula_juridica = ?, "
                   + "telefono = ?, "
                   + "correo = ?, "
                   + "activo = ? "
                   + "WHERE id_proveedor = ?";

        try (Connection con = Conexion.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getCedulaJuridica());
            ps.setString(3, p.getTelefono());
            ps.setString(4, p.getCorreo());
            ps.setBoolean(5, p.isActivo());
            ps.setInt(6, p.getIdProveedor());

            ps.executeUpdate();
        }
    }

    // Desactivar proveedor sin eliminarlo
    public void desactivar(int id) throws SQLException {

        String sql = "UPDATE proveedor SET activo = 0 "
                   + "WHERE id_proveedor = ?";

        try (Connection con = Conexion.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            ps.executeUpdate();
        }
    }

    // Convertir los datos de SQL en un objeto Proveedor
    private Proveedor mapear(ResultSet rs) throws SQLException {

        Proveedor p = new Proveedor();

        p.setIdProveedor(rs.getInt("id_proveedor"));
        p.setNombre(rs.getString("nombre"));
        p.setCedulaJuridica(rs.getString("cedula_juridica"));
        p.setTelefono(rs.getString("telefono"));
        p.setCorreo(rs.getString("correo"));
        p.setActivo(rs.getBoolean("activo"));

        return p;
    }
}
