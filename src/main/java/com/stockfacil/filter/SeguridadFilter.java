package com.stockfacil.filter;

import com.stockfacil.model.Usuario;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Semana 10: toda página exige sesión, menos /login. (En la semana 11 se
 * agregan los permisos por rol.)
 */
@WebFilter("/*")
public class SeguridadFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        String ruta = req.getServletPath();
        // 1. El login es público
        if (ruta.equals("/login")) {
            chain.doFilter(req, resp);
            return;
        }
        // 2. ¿Hay alguien con sesión?
        HttpSession sesion = req.getSession(false);
        Usuario usuario = (sesion == null) ? null : (Usuario) sesion.getAttribute("usuario");
        if (usuario == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        chain.doFilter(req, resp);
    }
}
