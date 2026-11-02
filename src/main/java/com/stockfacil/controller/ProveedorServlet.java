package com.stockfacil.controller;

import com.stockfacil.dao.ProveedorDAO;
import com.stockfacil.model.Proveedor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/proveedores")
public class ProveedorServlet extends HttpServlet {

    private final ProveedorDAO dao = new ProveedorDAO();

    // Mostrar lista, formulario nuevo o formulario de edición
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        String accion = req.getParameter("accion");

        try {

            if ("nuevo".equals(accion)) {

                Proveedor p = new Proveedor();
                p.setActivo(true);

                req.setAttribute("proveedor", p);

                mostrar("proveedores/form", req, resp);

            } else if ("editar".equals(accion)) {

                int id = Integer.parseInt(req.getParameter("id"));

                Proveedor p = dao.buscarPorId(id);

                if (p == null) {
                    resp.sendRedirect(
                        req.getContextPath() + "/proveedores"
                    );
                    return;
                }

                req.setAttribute("proveedor", p);

                mostrar("proveedores/form", req, resp);

            } else {

                req.setAttribute("proveedores", dao.listar());

                mostrar("proveedores/lista", req, resp);
            }

        } catch (SQLException | NumberFormatException e) {
            throw new ServletException(e);
        }
    }

    // Registrar, actualizar o desactivar proveedor
    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String accion = req.getParameter("accion");

        try {

            // Desactivar proveedor
            if ("desactivar".equals(accion)) {

                int id = Integer.parseInt(req.getParameter("id"));

                dao.desactivar(id);

                resp.sendRedirect(
                    req.getContextPath() + "/proveedores"
                );

                return;
            }

            // Obtener datos del formulario
            Proveedor p = new Proveedor();

            String id = req.getParameter("idProveedor");

            if (id == null || id.trim().isEmpty()) {
                p.setIdProveedor(0);
            } else {
                p.setIdProveedor(Integer.parseInt(id));
            }

            p.setNombre(valor(req, "nombre"));
            p.setCedulaJuridica(valor(req, "cedulaJuridica"));
            p.setTelefono(valor(req, "telefono"));
            p.setCorreo(valor(req, "correo"));

            p.setActivo(
                req.getParameter("activo") != null
                || p.getIdProveedor() == 0
            );

            // Lista de errores
            List<String> errores = new ArrayList<>();

            // Validar nombre
            if (p.getNombre().isEmpty()) {
                errores.add("El nombre es obligatorio.");
            }

            // Validar cédula jurídica
            if (p.getCedulaJuridica().isEmpty()) {

                errores.add("La cédula jurídica es obligatoria.");

            } else if (dao.existeCedula(
                    p.getCedulaJuridica(),
                    p.getIdProveedor())) {

                errores.add(
                    "Ya existe un proveedor con esa cédula jurídica."
                );
            }

            // Validar teléfono
            if (!p.getTelefono().isEmpty()
                    && !p.getTelefono().matches("\\d{8}")) {

                errores.add("El teléfono debe tener 8 dígitos.");
            }

            // Validar correo
            if (!p.getCorreo().isEmpty()
                    && !p.getCorreo().contains("@")) {

                errores.add("El correo no es válido.");
            }

            // Si existen errores, regresar al formulario
            if (!errores.isEmpty()) {

                req.setAttribute("errores", errores);
                req.setAttribute("proveedor", p);

                mostrar("proveedores/form", req, resp);

                return;
            }

            // Registrar o actualizar
            if (p.getIdProveedor() == 0) {

                dao.insertar(p);

            } else {

                dao.actualizar(p);
            }

            // Regresar a la lista
            resp.sendRedirect(
                req.getContextPath() + "/proveedores"
            );

        } catch (SQLException | NumberFormatException e) {
            throw new ServletException(e);
        }
    }

    // Obtener un dato del formulario evitando valores nulos
    private String valor(HttpServletRequest req, String nombre) {

        String dato = req.getParameter(nombre);

        return dato == null ? "" : dato.trim();
    }

    // Mostrar las páginas JSP
    private void mostrar(String vista,
                         HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher(
            "/WEB-INF/jsp/" + vista + ".jsp"
        ).forward(req, resp);
    }
}
