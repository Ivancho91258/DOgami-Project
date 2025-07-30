<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html lang="es">
    <head>
        <title>DOgami/Inicio de sesión</title>
        <meta charset="UTF-8">
        <meta name="Software diagramas de Origami">
        <meta name="keywords" content="Origami, Diagramas, Diseño">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="icon" href="Image/Logo DOgami.png">
        <link rel="stylesheet" href="css/iniciosesion.css">
    </head>
    <body>
        <header>
            <div class="header-container">
                <div class="vacio-container"></div>
                <div class="titulo-container">
                    <img src="Image/Logo DOgami.png" alt="Icono DOgami"/>
                    <h1>DOgami</h1>
                </div>
                <div class="vacio-container"></div>
            </div>
        </header>
        <div class="cuadro1-container">
            <div class="elementos-internos">
                <h1 class="titulo2-container">Inicia sesión</h1>
            </div>
            <form id="Login-autenticación" action="login" method="post">
                <div class="elementos-internos">
                    <input type="email" id="correo" placeholder="Dirección de correo" name="correo"/>
                </div>
                <div class="elementos-internos">
                    <input type="password" id="contraseña" placeholder="Contraseña" name="contraseña"/>
                </div>
                <div class="error-container">
                    <p id="mensaje-error"></p>
                </div>
                <div class="elementos-internos">
                    <button type="submit" id="boton-inicio-sesión" class="loginboton">Iniciar sesión
                </div>
            </form>
            <div class="elementos-internos">
                <a href="#" class="olvidaste-contraseña">¿Olvidaste tu Contraseña?</a>
            </div>
        </div>
    </body>
    <footer>
        <div class="mensaje-final">
            <h2>Tu software amigo en el mundo del Origami</h2>
        </div>
    </footer>
</html>
