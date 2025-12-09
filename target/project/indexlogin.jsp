<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <title>DOgami - Usuario</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="icon" href="Image/Logo DOgami.png">
        <link rel="stylesheet" href="css/index.css">
    </head>
    <body>
        <main>
            <header>
                <div class="header-container">
                    <div class="vacio-container"></div>
                    <div class="titulo-container">
                        <img src="Image/Logo DOgami.png" alt="Icono Dogami">
                        <h1>DOgami</h1>
                    </div>
                    <div class="botones-login-container">
                        <%
                            // Lógica para mostrar nombre y botón Cerrar Sesión
                            String nombreUsuario = (String) session.getAttribute("loggedInUser");
                            if (nombreUsuario != null) {
                        %>
                            <div style="display: flex; flex-direction: column; align-items: center;">
                                <span style="font-size: 12px; margin-bottom: 2px;">Hola, <%= nombreUsuario %></span>
                                <a href="${pageContext.request.contextPath}/cerrarsesion" class="botones-sesion-login" style="background-color: #d9534f;">
                                    Cerrar Sesión
                                </a>
                            </div>
                        <%
                            } else {
                        %>
                            <a href="iniciosesion.jsp" class="botones-sesion-login">
                                Iniciar sesión
                            </a>
                        <%
                            }
                        %>
                    </div>
                </div>
            </header>
            
            <div class="prototipo-container">
                <img src="Image/Prototipo.png" alt="Imagen Software">
                <div class="texto-descripción-container">
                    <p>DOgami es un software diseñado para realizar diagramas de origami, con él podrás avanzar en tu camino como Origamista.</p>
                    
                    <%-- Mostrar contenido diferente según la licencia si quieres --%>
                    
                    <a href="licencia.jsp" class="descargaboton">Descarga DOgami</a>
                </div>
            </div>
        </main>
        <footer>
            <div class="mensaje-final">
                <h2>Tu software amigo en el mundo del Origami</h2>
            </div>
        </footer>
    </body>
</html>