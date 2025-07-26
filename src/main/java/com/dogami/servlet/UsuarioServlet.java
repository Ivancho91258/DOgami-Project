package com.dogami.servlet;

import com.dogami.dbconnection.DBConnection; // Clase para la conexión JDBC
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet({"/login", "/registerUser"})
public class UsuarioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String path = request.getRequestURI().substring(request.getContextPath().length());

        try {
            switch (path) {
                case "/login":
                    handleLogin(request, response);
                    break;
                case "/registerUser":
                    handleRegisterUser(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Acción POST no reconocida: " + path);
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error inesperado en UsuarioServlet (" + path + "): " + e.getMessage());
            e.printStackTrace();
            // Siempre redirige a un JSP para mostrar errores, no imprimas HTML directamente
            request.setAttribute("error", "Ocurrió un error inesperado. Por favor, intente de nuevo más tarde.");
            request.getRequestDispatcher("/error.jsp").forward(request, response); // Asegúrate de tener un error.jsp
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");

        String path = request.getRequestURI().substring(request.getContextPath().length());

        try {
            switch (path) {
                case "/login":
                    request.getRequestDispatcher("/iniciosesion.jsp").forward(request, response);
                    break;
                case "/registerUser":
                    request.getRequestDispatcher("/registrousuario.jsp").forward(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Acción GET no reconocida: " + path);
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error inesperado en UsuarioServlet (GET " + path + "): " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Ocurrió un error inesperado al procesar GET: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    // Métodos de Ayuda para cada operación CRUD

    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String correo = request.getParameter("correo");
        String contraseña = request.getParameter("contraseña");

        System.out.println("DEBUG (Login): Correo recibido: " + correo);
        System.out.println("DEBUG (Login): Contraseña recibida: " + contraseña);

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean loginExitoso = false;

        try {
            conn = DBConnection.getConnection();
            System.out.println("DEBUG (Login): Conexión a DB obtenida: " + (conn != null ? "EXITO" : "FALLO"));

            String sql = "SELECT COUNT(*) FROM usuario WHERE correo = ? AND contraseña = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, correo);
            pstmt.setString(2, contraseña);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                loginExitoso = (count == 1);
            }
            System.out.println("DEBUG (Login): Resultado de autenticación (loginExitoso): " + loginExitoso);

            if (loginExitoso) {
                request.setAttribute("correo", correo);
                request.getSession().setAttribute("loggedInUserCorreo", correo);
                System.out.println("DEBUG (Login): Inicio de sesión exitoso. Redirigiendo a indexlogin.jsp");
                response.sendRedirect(request.getContextPath() + "/indexlogin.jsp");
            } else {
                request.setAttribute("mensajeError", "Correo electrónico o contraseña incorrectos.");
                System.out.println("DEBUG (Login): Inicio de sesión fallido. Redirigiendo a iniciosesion.jsp");
                request.getRequestDispatcher("iniciosesion.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            System.err.println("ERROR SQL en Login: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("mensajeError", "Error de base de datos durante el inicio de sesión: " + e.getMessage());
            request.getRequestDispatcher("iniciosesion.jsp").forward(request, response);
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    // Método para registrar usuario
    private void handleRegisterUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String correo = request.getParameter("correo");
        String contraseña = request.getParameter("contraseña");
        String nombre = request.getParameter("nombre");

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnection.getConnection();
            String sql = "INSERT INTO usuario (correo, contraseña, nombre) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, correo);
            pstmt.setString(2, contraseña);
            pstmt.setString(3, nombre);

            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                request.setAttribute("mensajeExito", "Usuario registrado exitosamente.");
                response.sendRedirect(request.getContextPath() + "/iniciosesion.jsp");
            } else {
                request.setAttribute("mensajeError", "No se pudo registrar el usuario.");
                request.getRequestDispatcher("registrousuario.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            System.err.println("ERROR SQL en Registro: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("mensajeError", "Error de base de datos durante el registro: " + e.getMessage());
            request.getRequestDispatcher("registrousuario.jsp").forward(request, response);
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    // Método auxiliar para cerrar recursos JDBC
    private void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar ResultSet: " + e.getMessage());
            }
        }
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar PreparedStatement: " + e.getMessage());
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar Connection: " + e.getMessage());
            }
        }
    }
}