package com.dogami.servlet;

import com.dogami.TablasDB.Usuario;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet({"/login", "/register", "/listausuarios"})
public class UsuarioServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String path = request.getServletPath();

        if ("/register".equals(path)) {
            handleRegister(request, response);
        } else if ("/login".equals(path)) {
            handleLogin(request, response);
        } else if ("/listausuarios".equals(path)) {
            handleListaUsuarios(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Error: URL no encontrada.");
        }
    }

    //Metodo para manejar el inicio de sesión
    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String correo = request.getParameter("correo");
        String contraseña = request.getParameter("contraseña");
        
        try (Connection conn = DBConnection.getConnection()) {

            // Inicio de sesión como administrador
            String sqlAdmin = "SELECT nombre FROM administrador WHERE correo = ? AND contraseña = ?";
            try (PreparedStatement pstmtAdmin = conn.prepareStatement(sqlAdmin)) {
                pstmtAdmin.setString(1, correo);
                pstmtAdmin.setString(2, contraseña);
                try (ResultSet rsAdmin = pstmtAdmin.executeQuery()) {
                    if (rsAdmin.next()) {
                        String nombre = rsAdmin.getString("nombre");
                        HttpSession session = request.getSession();
                        session.setAttribute("loggedInUser", nombre);
                        response.sendRedirect(request.getContextPath() + "/administrador.jsp");
                        return;
                    }
                }
            }

            // Inicio de sesión como usuario normal
            String sqlUser = "SELECT nombre FROM usuario WHERE correo = ? AND contraseña = ?";
            try (PreparedStatement pstmtUser = conn.prepareStatement(sqlUser)) {
                pstmtUser.setString(1, correo);
                pstmtUser.setString(2, contraseña);
                try (ResultSet rsUser = pstmtUser.executeQuery()) {
                    if (rsUser.next()) {
                        String nombre = rsUser.getString("nombre");
                        HttpSession session = request.getSession();
                        session.setAttribute("loggedInUser", nombre);
                        session.setAttribute("loggedInUserCorreo", correo);
                        response.sendRedirect(request.getContextPath() + "/indexlogin.jsp");
                        return;
                    }
                }
            }

            request.setAttribute("mensajeError", "Correo electrónico o contraseña incorrectos.");
            request.getRequestDispatcher("iniciosesion.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("mensajeError", "Error de base de datos. Intente más tarde.");
            request.getRequestDispatcher("iniciosesion.jsp").forward(request, response);
        }
    }

    // Metodo para manejar el registro de usuarios
    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
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
            
            if (pstmt.executeUpdate() > 0) {
                request.setAttribute("mensajeExito", "¡Cuenta creada! Por favor, inicia sesión.");
                request.getRequestDispatcher("iniciosesion.jsp").forward(request, response);
            } else {
                request.setAttribute("mensajeError", "No se pudo crear la cuenta. Inténtalo de nuevo.");
                request.getRequestDispatcher("registrousuario.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                request.setAttribute("mensajeError", "El correo electrónico ya está registrado.");
            } else {
                e.printStackTrace();
                request.setAttribute("mensajeError", "Error de base de datos. Intente más tarde.");
            }
            request.getRequestDispatcher("registrousuario.jsp").forward(request, response);
        }
    }
    private void handleListaUsuarios(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT id, nombre, correo, tipo_de_licencia FROM usuario";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String correo = rs.getString("correo");
                String tipoDeLicencia = rs.getString("tipo_de_licencia");
                
                Usuario usuarioObj = new Usuario(id, nombre, correo, tipoDeLicencia);
                usuarios.add(usuarioObj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("mensajeError", "Error al recuperar la lista de usuarios.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }
        
        request.setAttribute("usuarios", usuarios);
        request.getRequestDispatcher("/listausuarios.jsp").forward(request, response);
    }
}