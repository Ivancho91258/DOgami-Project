<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <title>DOgami</title>
        <meta charset="UTF-8">
        <meta name="Software diagramas de Origami">
        <meta name="keywords" content="Origami, Diagramas, Diseño">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="icon" href="Image/Logo DOgami.png">
        <link rel="stylesheet" href="css/registrousuario.css">
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
                <h1 class="titulo2-container">Ingresa tus datos</h1>
            </div>
            <div class="elementos-internos">
                <button class="loginbotongoogle">
                    <img src="Image/google.png" alt="Icono google" class="google-container">
                    <div class="texto-interno1">Ingresa con google</div>
                </button>
            </div>
            <div class="texto-interno2">
                <h2>Inicia sesión con otro correo</h2>
            </div>
            <div class="elementos-internos1">
                <input type="text" placeholder="Nombres" name="Nombres"/>
                <input type="text" placeholder="Apellidos" name="Apellidos"/>
            </div>
            <div class="email-container">
                <input type="email" placeholder="Dirección de correo" name="correo" />
            </div>
            <div class="password-container">
                <input type="password" placeholder="Ingresa una contraseña, mínimo 8 caracteres" name="contraseña" />
            </div>
            <div class="texto-interno3">
                <h2>Usa al menos una letra mayúscula, un número y un simbolo especial</h2>
            </div>
            <div class="elementos-internos">
                <input type="submit" value="Crea tu cuenta" id="boton-crear-cuenta" class="cuentaboton">
            </div>
        </div>
    </div>
    </body>
    <footer>
        <div class="mensaje-final">
            <h2>Tu software amigo en el mundo del Origami</h2>
        </div>
    </footer>
</html>