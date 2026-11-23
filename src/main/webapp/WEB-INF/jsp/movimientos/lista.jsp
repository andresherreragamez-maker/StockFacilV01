<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../comun/encabezado.jsp" %>

<h1 class="h3 mb-3">Entradas y salidas</h1>

<c:if test="${param.ok == '1'}">
    <div class="alert alert-success">Movimiento registrado y stock actualizado.</div>
</c:if>
<c:if test="${not empty errores}">
    <div class="alert alert-danger">
        <c:forEach var="e" items="${errores}"><div><c:out value="${e}"/></div></c:forEach>
        </div>
</c:if>

<form method="post" action="movimientos" class="card card-body mb-4">
    <div class="row g-3 align-items-end">
        <div class="col-md-4">
            <label class="form-label">Producto</label>
            <select class="form-select" name="idProducto" required>
                <option value="">Seleccione…</option>
                <c:forEach var="p" items="${productos}">
                    <option value="${p.idProducto}" ${p.idProducto == movimiento.idProducto ? 'selected' : ''}>
                        <c:out value="${p.codigo} · ${p.nombre}"/> (stock ${p.stock})
                    </option>
                </c:forEach>
            </select>
        </div>
        <div class="col-md-2">
            <label class="form-label">Tipo</label>
            <select class="form-select" name="tipo">
                <option value="ENTRADA" ${movimiento.tipo == 'ENTRADA' ? 'selected' : ''}>Entrada</option>
                <option value="SALIDA" ${movimiento.tipo == 'SALIDA' ? 'selected' : ''}>Salida</option>
            </select>
        </div>
        <div class="col-md-2">
            <label class="form-label">Cantidad</label>
            <input class="form-control" name="cantidad" type="number" min="1" required
                   value="${movimiento.cantidad > 0 ? movimiento.cantidad : ''}">
        </div>
        <div class="col-md-3">
            <label class="form-label">Nota</label>
            <input class="form-control" name="nota" maxlength="200"
                   value="<c:out value='${movimiento.nota}'/>">
        </div>
        <div class="col-md-1">
            <button class="btn btn-primary w-100">Guardar</button>
        </div>
    </div>
</form>

<h2 class="h5">Últimos movimientos</h2>
<table class="table table-sm table-striped bg-white">
    <thead>
        <tr><th>Fecha</th><th>Producto</th><th>Tipo</th><th class="text-end">Cantidad</th><th>Usuario</th><th>Nota</th></tr>
    </thead>
    <tbody>
        <c:forEach var="m" items="${movimientos}">
            <tr>
                <td>${m.fechaTexto}</td>
                <td><c:out value="${m.nombreProducto}"/></td>
                <td><span class="badge ${m.tipo == 'ENTRADA' ? 'text-bg-success' : 'text-bg-secondary'}">${m.tipo}</span></td>
                <td class="text-end">${m.cantidad}</td>
                <td><c:out value="${m.nombreUsuario}"/></td>
                <td><c:out value="${m.nota}"/></td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<%@ include file="../comun/pie.jsp" %>
