package com.stockfacil.controller;

import com.stockfacil.dao.MovimientoDAO;
import com.stockfacil.dao.ProductoDAO;
import com.stockfacil.dao.StockInsuficienteException;
import com.stockfacil.model.Movimiento;
import com.stockfacil.model.Usuario;
        
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Semana 9. Registrar entradas y salidas; la lista muestra los últimos 30.
 */
@WebServlet("/movimientos")
public class MovimientoServlet extends HttpServlet {
    
    private final MovimientoDAO dao = new MovimientoDAO();
    private final ProductoDAO productoDAO = new ProductoDAO();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        mostrar(req, resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        List<String> errores = new ArrayList<>();
        Movimiento m = new Movimiento();
        try {
            m.setIdProducto(Integer.parseInt(req.getParameter("idProducto")));
            m.setCantidad(Integer.parseInt(req.getParameter("cantidad")));
        } catch (NumberFormatException e) {
            errores.add("Seleccione un producto y escriba una cantidad entera.");
        }
        m.setTipo(req.getParameter("tipo"));
        m.setNota(req.getParameter("nota").trim());
        // Semana 9: el usuario es fijo (1 = admin).
        // En la semana 10 se toma del usuario que inició sesión.
        Usuario usuario = (Usuario) req.getSession().getAttribute("usuario");
        m.setIdUsuario(usuario.getIdUsuario());
        
        m.setIdUsuario(1);
        if (m.getCantidad() <= 0) {
            errores.add("La cantidad debe ser mayor que cero.");
        }
        if (!"ENTRADA".equals(m.getTipo()) && !"SALIDA".equals(m.getTipo())) {
            errores.add("Seleccione si es entrada o salida.");
        }
        if (errores.isEmpty()) {
            try {
                dao.registrar(m);
                resp.sendRedirect("movimientos?ok=1");
                return;
            } catch (StockInsuficienteException e) {
                errores.add(e.getMessage());                 // RN-03: mensaje para el usuario
            } catch (SQLException e) {
                throw new ServletException(e);
            }
        }
        req.setAttribute("errores", errores);
        req.setAttribute("movimiento", m);
        mostrar(req, resp);
    }

    /**
     * La página siempre necesita dos listas: productos para el combo y los
     * últimos movimientos.
     */
    private void mostrar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("productos", productoDAO.listarActivos());
            req.setAttribute("movimientos", dao.listarRecientes(30));
            req.getRequestDispatcher("/WEB-INF/jsp/movimientos/lista.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
