<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="WEB-INF/jsp/comun/encabezado.jsp" %>

h1 class="h3">Bienvenido, <c:out value="${sessionScope.usuario.nombre}"/></h1>
<p class="text-muted">Elija una opción del menú o uno de estos botones.</p>

<a class="btn btn-primary" href="categorias">Ir a categorías</a>
<a class="btn btn-primary" href="proveedores">Ir a proveedores</a>
<a class="btn btn-primary" href="productos">Ir a productos</a>
<a class="btn btn-primary" href="movimientos">Ir a movimientos</a>

<%@ include file="WEB-INF/jsp/comun/pie.jsp" %>
