package com.dogami.servlet;

import com.dogami.dbconnection.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Anotación para manejar ambas URLs
@WebServlet({"/login", "/register"})
public class UsuarioServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Obtenemos la URL para saber qué acción tomar
        String path = request.getServletPath();

        // Decidimos qué método llamar
        if ("/register".equals(path)) {
            handleRegister(request, response);
        } else {
            handleLogin(request, response);
        }
    }


    //Maneja la lógica de inicio de sesión.

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String correo = request.getParameter("correo");
        String contraseña = request.getParameter("contraseña");
        
        String sql = "SELECT nombre FROM usuario WHERE correo = ? AND contraseña = ?";
        
        // Usamos try-with-resources para el manejo automático de recursos
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, correo);
            pstmt.setString(2, contraseña);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Login exitoso - llama el nombre del usuario para mostrarlo en la sesión
                    String nombre = rs.getString("nombre");
                    HttpSession session = request.getSession();
                    session.setAttribute("loggedInUser", nombre);
                    request.getSession().setAttribute("loggedInUserCorreo", correo);
                    //Inicio de sesión de administrador

                    if ("ivancho91258@gmail.com".equals(correo)) {
                        response.sendRedirect(request.getContextPath() + "/administrador.jsp");
                        return;
                    } else {
                        response.sendRedirect(request.getContextPath() + "/indexlogin.jsp");
                        return;
                    }

                } else {
                    // Inicio de sesión fallido
                    request.setAttribute("mensajeError", "Correo electrónico o contraseña incorrectos.");
                    request.getRequestDispatcher("iniciosesion.jsp").forward(request, response);
                    return;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("mensajeError", "Error de base de datos. Intente más tarde.");
            request.getRequestDispatcher("iniciosesion.jsp").forward(request, response);
            return;
        }
    }

    /**
     * Maneja la lógica de registro de nuevos usuarios.
     */
    private void handleRegister(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String nombres = request.getParameter("nombres");
        String apellidos = request.getParameter("apellidos");
        String correo = request.getParameter("correo");
        String contraseña = request.getParameter("contraseña");
        
        String nombreCompleto = nombres + " " + apellidos;
        
        String sql = "INSERT INTO usuario (nombre, correo, contraseña, tipo_de_licencia) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombreCompleto);
            pstmt.setString(2, correo);
            pstmt.setString(3, contraseña);
            pstmt.setString(4, "Gratuita");
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Registro exitoso, redirige al login
                System.out.println("Usuario creado exitosamente: " + nombreCompleto);
                request.setAttribute("mensajeExito", "¡Cuenta creada! Por favor, inicia sesión.");
                request.getRequestDispatcher("iniciosesion.jsp").forward(request, response);
            } else {
                System.err.println("Error al crear el usuario: filas afectadas = " + rowsAffected);
                request.setAttribute("mensajeError", "No se pudo crear la cuenta. Inténtalo de nuevo.");
                request.getRequestDispatcher("registrousuario.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            // Maneja error de correo duplicado (código para PostgreSQL: 23505)
            if ("23505".equals(e.getSQLState())) {
                request.setAttribute("mensajeError", "El correo electrónico ya está registrado.");
            } else {
                e.printStackTrace();
                request.setAttribute("mensajeError", "Error de base de datos. Intente más tarde.");
            }
            request.getRequestDispatcher("registrousuario.jsp").forward(request, response);
        }
    }
}