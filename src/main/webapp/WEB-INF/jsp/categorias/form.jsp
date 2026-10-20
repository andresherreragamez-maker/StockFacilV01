<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../comun/encabezado.jsp" %>
<h1 class="h3 mb-3">${categoria.idCategoria == 0 ? 'Nueva categoría' : 'Editar categoría'}</h1>
<c:if test="${not empty errores}">
    <div class="alert alert-danger">
        <c:forEach var="e" items="${errores}"><div><c:out value="${e}"/></div></c:forEach>
        </div>
</c:if>
<form method="post" action="categorias" class="card card-body" style="max-width: 560px">
    <input type="hidden" name="accion" value="guardar">
    <input type="hidden" name="idCategoria" value="${categoria.idCategoria}">
    <label class="form-label">Nombre</label>
    <input class="form-control mb-3" name="nombre" maxlength="60" required
           value="<c:out value='${categoria.nombre}'/>">
    
    <label class="form-label">Descripción</label>
    <input class="form-control mb-3" name="descripcion" maxlength="200"
           value="<c:out value='${categoria.descripcion}'/>">
    
    <c:if test="${categoria.idCategoria != 0}">
        <div class="form-check mb-3">
            <input class="form-check-input" type="checkbox" name="activo" id="activo"
                   ${categoria.activo ? 'checked' : ''}>
            <label class="form-check-label" for="activo">Activa</label>
        </div>
    
    </c:if>
    <div>
        <button class="btn btn-primary">Guardar</button>
        <a class="btn btn-link" href="categorias">Cancelar</a>
    </div>
</form>
<%@ include file="../comun/pie.jsp" %>
