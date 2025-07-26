package com.dogami.servlet;

import com.dogami.dbconnection.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/login")

public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String correo = request.getParameter("correo");
        String contraseña = request.getParameter("contraseña");

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean loginExitoso = false;

        try {

            conn = DBConnection.getConnection();

            String sql = "SELECT COUNT(*) FROM usuario WHERE correo = ? AND contraseña = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, correo);
            pstmt.setString(2, contraseña);

            rs = pstmt.executeQuery();
    
            if (rs.next()) {
                int count = rs.getInt(1);
                if (count == 1) {
                    loginExitoso = true;
                }
            }

            if (loginExitoso) {

                out.println("<!DOCTYPE html>");
                out.println("<html><head><title>Inicio Exitoso</title></head><body>");
                out.println("<h1>¡Bienvenido, " + correo + "!</h1>");
                out.println("<p>Inicio de sesión exitoso.</p>");
                out.println("<p><a href='index.jsp'>Volver al inicio</a></p>");
                out.println("</body></html>");

            } else {

                out.println("<!DOCTYPE html>");
                out.println("<html><head><title>Error al iniciar</title></head><body>");
                out.println("<h1>Error de Inicio de Sesión</h1>");
                out.println("<p>Correo electrónico o contraseña incorrectos.</p>");
                out.println("<a href='iniciosesion.jsp'>Ingrese los datos de nuevo</a>");
                out.println("</body></html>");
            }

        } catch (SQLException e) {
  
            out.println("<h2>Error de base de datos durante el ingreso:</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
            e.printStackTrace(out);
            System.err.println("Error de SQL en LoginServlet: " + e.getMessage());
        } catch (Exception e) {
 
            out.println("<h2>Ocurrió un error inesperado:</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
            e.printStackTrace(out);
            System.err.println("Error inesperado en LoginServlet: " + e.getMessage());
        } finally {
     
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) DBConnection.closeConnection(conn);
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos en LoginServlet: " + e.getMessage());
            }
        }
    }
}