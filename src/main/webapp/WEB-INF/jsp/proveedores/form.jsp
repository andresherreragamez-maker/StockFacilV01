<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="../comun/encabezado.jsp" %>

<h1 class="h3 mb-3">

    <c:choose>

        <c:when test="${proveedor.idProveedor == 0}">
            Nuevo proveedor
        </c:when>

        <c:otherwise>
            Editar proveedor
        </c:otherwise>

    </c:choose>

</h1>

<!-- Mostrar errores -->
<c:if test="${not empty errores}">

    <div class="alert alert-danger">

        <ul class="mb-0">

            <c:forEach var="error" items="${errores}">

                <li>
                    <c:out value="${error}"/>
                </li>

            </c:forEach>

        </ul>

    </div>

</c:if>

<!-- Formulario -->
<form method="post"
      action="${pageContext.request.contextPath}/proveedores">

    <input type="hidden"
           name="idProveedor"
           value="${proveedor.idProveedor}">

    <!-- Nombre -->
    <div class="mb-3">

        <label class="form-label" for="nombre">
            Nombre
        </label>

        <input class="form-control"
               type="text"
               id="nombre"
               name="nombre"
               value="<c:out value='${proveedor.nombre}'/>">

    </div>

    <!-- Cédula jurídica -->
    <div class="mb-3">

        <label class="form-label" for="cedulaJuridica">
            Cédula jurídica
        </label>

        <input class="form-control"
               type="text"
               id="cedulaJuridica"
               name="cedulaJuridica"
               value="<c:out value='${proveedor.cedulaJuridica}'/>">

    </div>

    <!-- Teléfono -->
    <div class="mb-3">

        <label class="form-label" for="telefono">
            Teléfono
        </label>

        <input class="form-control"
               type="text"
               id="telefono"
               name="telefono"
               value="<c:out value='${proveedor.telefono}'/>">

    </div>

    <!-- Correo -->
    <div class="mb-3">

        <label class="form-label" for="correo">
            Correo
        </label>

        <input class="form-control"
               type="text"
               id="correo"
               name="correo"
               value="<c:out value='${proveedor.correo}'/>">

    </div>

    <!-- Estado: solamente al editar -->
    <c:if test="${proveedor.idProveedor != 0}">

        <div class="form-check mb-3">

            <input class="form-check-input"
                   type="checkbox"
                   id="activo"
                   name="activo"
                   value="true"
                   ${proveedor.activo ? 'checked' : ''}>

            <label class="form-check-label" for="activo">
                Activo
            </label>

        </div>

    </c:if>

    <button type="submit" class="btn btn-primary">
        Guardar
    </button>

    <a class="btn btn-secondary"
       href="${pageContext.request.contextPath}/proveedores">
        Cancelar
    </a>

</form>

<%@ include file="../comun/pie.jsp" %>
