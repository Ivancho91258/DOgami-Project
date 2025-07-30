<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <title>DOgami/Administrador</title>
        <meta charset="UTF-8">
        <meta name="Software diagramas de Origami">
        <meta name="keywords" content="Origami, Diagramas, Diseño">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="icon" href="Image/Logo DOgami.png">
        <link rel="stylesheet" href="css/administrador.css">
    </head>
    <header>
        <div class="header-container">
            <div class="vacio-container"></div>
            <div class="titulo-container">
                <img src="Image/Logo DOgami.png" alt="Icono Dogami">
                <h1>DOgami</h1>
            </div>
            <div class="botones-login-container">
                <a href="index.jsp" class="botones-sesion-login">
                    <%
                    String nombreUsuario = (String) session.getAttribute("loggedInUser");
                    if (nombreUsuario != null) {
                        out.print(nombreUsuario);
                    } else {
                        out.print("Iniciar sesión");
                    }
                    %>
                </a>
                <%--Pendiente la configuración del cierre de sesión--%>
            </div>
        </div>
        <h1>Lista de usuarios</h1>
        <table border="1">
            <thead
            <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Correo</th>
                <th>Tipo de licencia</th>
            </tr>
            </thead>
            <tbody>
                <c:forEach var="usuario" items="${usuarios}">
                    <tr>
                        <td>${usuario.id}</td>
                        <td>${usuario.nombre}</td>
                        <td>${usuario.correo}</td>
                        <td>${usuario.tipo_de_licencia}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </header>
    <body>
    </body>
    <footer>
        <div class="mensaje-final">
            <h2>Tu software amigo en el mundo del Origami</h2>
        </div>
    </footer>

