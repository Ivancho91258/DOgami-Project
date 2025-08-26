<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <title>DOgami/Usuario</title>
        <meta charset="UTF-8">
        <meta name="Software diagramas de Origami">
        <meta name="keywords" content="Origami, Diagramas, Diseño">
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
            </header>
        <div class="prototipo-container">
            <img src="Image/Prototipo.png" alt="Imagen Software" class="">
            <div class="texto-descripción-container">
                <p>DOgami es un software diseñado para realizar diagramas de origami, con el podrás avanzar en tu camino como Origamista.</p>
                <p>Puedes obtener la versión gratuita dando clic en descargar, o registrarte, obtener la licencia y acceder a distintos beneficios, ¡Bienvenidos!</p>
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