/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.stockfacil.dao;

import com.stockfacil.config.Conexion;
import com.stockfacil.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author User
 */
public class UsuarioDAO {

    private static final String SELECT_BASE
            = "SELECT U.*, R.NOMBRE AS ROL FROM USUARIO U INNER JOIN ROL R ON R.ID_ROL = U.ID_ROL ";

    public Usuario buscarPorUsuario(String usuario) throws SQLException {
        String sql = SELECT_BASE + "WHERE U.USUARIO = ?";

        try (Connection con = Conexion.obtener(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setIdUsuario(rs.getInt("id_usuario"));
        u.setNombre(rs.getString("nombre"));
        u.setUsuario(rs.getString("usuario"));
        u.setClave(rs.getString("clave"));
        u.setIdRol(rs.getInt("id_rol"));
        u.setRol(rs.getString("ROL"));
        u.setActivo(rs.getBoolean("activo"));
        return u;
    }
}
