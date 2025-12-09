<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <title>DOgami - Administrador</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="icon" href="Image/Logo DOgami.png">
        <link rel="stylesheet" href="css/administrador.css">
    </head>
    <body>
        <header>
            <div class="header-container">
                <div class="vacio-container"></div>
                <div class="titulo-container">
                    <img src="Image/Logo DOgami.png" alt="Icono Dogami">
                    <h1>DOgami Admin</h1>
                </div>
                <div class="botones-login-container">
                    <%
                        String nombreUsuario = (String) session.getAttribute("loggedInUser");
                        if (nombreUsuario != null) {
                    %>
                        <span style="color: white; margin-right: 10px; font-weight: bold;">Hola, <%= nombreUsuario %></span>
                        <a href="${pageContext.request.contextPath}/cerrarsesion" class="botones-sesion-login btn-logout">
                            Cerrar Sesión
                        </a>
                    <%
                        } else {
                    %>
                        <a href="index.jsp" class="botones-sesion-login">Iniciar sesión</a>
                    <%
                        }
                    %>
                </div>
            </div>
        </header>

        <main>
            <div class="tabla-estilo">
                <h1>Gestión de Usuarios</h1>
                
                <c:if test="${not empty mensajeExito}">
                    <div style="color: green; text-align: center; margin-bottom: 10px; font-weight: bold;">${mensajeExito}</div>
                </c:if>
                <c:if test="${not empty mensajeError}">
                    <div style="color: red; text-align: center; margin-bottom: 10px; font-weight: bold;">${mensajeError}</div>
                </c:if>

                <table>
                    <thead>
                        <tr>
                            <th style="width: 5%;">ID</th>
                            <th style="width: 20%;">Nombre</th>
                            <th style="width: 30%;">Correo</th>
                            <th style="width: 20%;">Licencia</th>
                            <th style="width: 25%;">Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="u" items="${usuarios}">
                            <tr>
                                <form action="${pageContext.request.contextPath}/actualizarusuario" method="post">
                                    <td>
                                        ${u.id}
                                        <input type="hidden" name="id" value="${u.id}">
                                    </td>
                                    <td>${u.nombre}</td>
                                    <td>
                                        <input type="email" name="correo" value="${u.correo}" class="input-tabla" required>
                                    </td>
                                    <td>
                                        <select name="tipo_de_licencia" class="select-tabla">
                                            <option value="Gratuita" ${u.tipo_de_licencia == 'Gratuita' ? 'selected' : ''}>Gratuita</option>
                                            <option value="Premium" ${u.tipo_de_licencia == 'Premium' ? 'selected' : ''}>Premium</option>
                                        </select>
                                    </td>
                                    <td class="acciones-cell">
                                        <button type="submit" class="btn-accion btn-guardar">Actualizar</button>
                                </form> 
                                        <form action="${pageContext.request.contextPath}/eliminarusuario" method="post" onsubmit="return confirm('¿Estás seguro de eliminar a este usuario?');" style="display:inline;">
                                            <input type="hidden" name="id" value="${u.id}">
                                            <button type="submit" class="btn-accion btn-eliminar">Eliminar</button>
                                        </form>
                                    </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </main>

        <footer>
            <div class="mensaje-final">
                <h2>Tu software amigo en el mundo del Origami</h2>
            </div>
        </footer>
    </body>
</html>