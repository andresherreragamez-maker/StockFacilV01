package com.stockfacil.controller;

import com.stockfacil.dao.UsuarioDAO;
import com.stockfacil.model.Usuario;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Semana 10. GET muestra el formulario; POST revisa usuario y contraseña.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UsuarioDAO dao = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String nombreUsuario = req.getParameter("usuario").trim();
        String clave = req.getParameter("clave");
        try {
            Usuario u = dao.buscarPorUsuario(nombreUsuario);
            // 1. ¿Existe, está activo (RN-07) y la clave coincide con el hash?
            if (u == null || !u.isActivo() || !BCrypt.checkpw(clave, u.getClave())) {
                req.setAttribute("error", "Usuario o contraseña incorrectos.");
                req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
                return;
            }
            // 2. Sesión nueva para evitar que alguien reutilice una vieja
            req.getSession().invalidate();
            HttpSession sesion = req.getSession(true);
            u.setClave(null);                         // el hash no se guarda en la sesión
            sesion.setAttribute("usuario", u);
            sesion.setMaxInactiveInterval(30 * 60);  // 30 minutos sin uso
            resp.sendRedirect(req.getContextPath() + "/");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
