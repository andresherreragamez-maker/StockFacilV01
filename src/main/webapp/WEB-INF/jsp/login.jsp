<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="comun/encabezado.jsp" %>

<div class="card card-body mx-auto" style="max-width: 380px">
    <h1 class="h4 mb-3">Iniciar sesión</h1>
    
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>
        
    <form method="post" action="login">
        <label class="form-label">Usuario</label>
        <input class="form-control mb-3" name="usuario" required autofocus value="<c:out value='${param.usuario}'/>">
        
        <label class="form-label">Contraseña</label>
        <input class="form-control mb-3" name="clave" type="password" required>
        
        <button class="btn btn-primary w-100">Entrar</button>
    </form>
</div>
        
<%@ include file="comun/pie.jsp" %>
