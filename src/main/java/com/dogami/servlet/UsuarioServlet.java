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
import org.mindrot.jbcrypt.BCrypt;
import java.util.regex.Pattern;

@WebServlet({"/login", "/register", "/listausuarios", "/actualizarusuario", "/eliminarusuario", "/cerrarsesion"})
public class UsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
                doPost(request, response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String path = request.getServletPath();

        if ("/register".equals(path)) {
            handleRegister(request, response);
        } else if ("/login".equals(path)) {
            handleLogin(request, response);
        } else if ("/listausuarios".equals(path)) {
            handleListaUsuarios(request, response);
        } else if ("/cerrarsesion".equals(path)) {
            handlecerrarSesion(request, response);
        } else if ("/actualizarusuario".equals(path)) {
            handleactualizarUsuario(request, response);
        } else if ("/eliminarusuario".equals(path)) {
            handleeliminarUsuario(request, response);
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
                        request.getRequestDispatcher("/listausuarios").forward(request, response);
                        return;
                    }
                }
            }

            // Inicio de sesión como usuario normal

            String sqlUser = "SELECT nombre FROM usuario WHERE correo = ?";

                try (PreparedStatement pstmtUser = conn.prepareStatement(sqlUser)) {
                    pstmtUser.setString(1, correo);

                    try (ResultSet rsUser = pstmtUser.executeQuery()) {
                        if (rsUser.next()) {
                            // El usuario existe, ahora verificamos la contraseña
                            String hashguardado = rsUser.getString("contraseña");
                            String nombre = rsUser.getString("nombre");

                            if (!BCrypt.checkpw(contraseña, hashguardado)) {
                                
                                // Contraseña incorrecta
                                request.setAttribute("mensajeError", "Correo electrónico o contraseña incorrectos.");
                                request.getRequestDispatcher("iniciosesion.jsp").forward(request, response);
                                return;
                            }
                            // Contraseña correcta
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

    // Metodo para manejar el cierre de sesión
    private void handlecerrarSesion(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }

    // Metodo para manejar el registro de usuarios
    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String nombres = request.getParameter("nombres");
        String apellidos = request.getParameter("apellidos");
        String correo = request.getParameter("correo");
        String contraseña = request.getParameter("contraseña");
        String nombreCompleto = nombres + " " + apellidos;
    
    // Validación de la contraseña

        String regex  = "^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\\\-=\\\\[\\\\]{};':\\\"\\\\\\\\|,.<>\\\\/?]).{8,}$";
        
        if (contraseña == null || !Pattern.matches(regex, contraseña)) {
            request.setAttribute("mensajeError", "La contraseña debe tener al menos 8 caracteres, incluyendo un número y un carácter especial.");
            request.setAttribute("nombres", nombres);
            request.setAttribute("apellidos", apellidos);
            request.setAttribute("correo", correo);
            request.getRequestDispatcher("registrousuario.jsp").forward(request, response);
            return;
        }

    // Validación de si es administrador

    String sqlCheckAdmin = "SELECT 1 FROM administrador WHERE correo = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement pstmtCheckAdmin = conn.prepareStatement(sqlCheckAdmin)) {
            pstmtCheckAdmin.setString(1, correo);
            try (ResultSet rsCheckAdmin = pstmtCheckAdmin.executeQuery()) {
                if (rsCheckAdmin.next()) {
                    request.setAttribute("mensajeError", "El correo electrónico ya está registrado como administrador.");
                    request.setAttribute("nombres", nombres);
                    request.setAttribute("apellidos", apellidos);
                    request.setAttribute("correo", correo);
                    request.getRequestDispatcher("registrousuario.jsp").forward(request, response);
                    return;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("mensajeError", "Error de base de datos. Intente más tarde.");
            request.getRequestDispatcher("registrousuario.jsp").forward(request, response);
            return;
        }

        String hashcontraseña = BCrypt.hashpw(contraseña, BCrypt.gensalt());

    // Inserción del nuevo usuario en la base de datos

        String sql = "INSERT INTO usuario (nombre, correo, contraseña, tipo_de_licencia) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombreCompleto);
            pstmt.setString(2, correo);
            pstmt.setString(3, hashcontraseña);
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
    // Metodo para manejar la lista de usuarios
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
                String tipo_de_licencia = rs.getString("tipo_de_licencia");
                
                Usuario usuario = new Usuario(id, nombre, correo, tipo_de_licencia);
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("mensajeError", "Error al recuperar la lista de usuarios.");
            request.getRequestDispatcher("error.jsp").forward(request, response); //Pendiente la creacion de la pagina error.jsp
            return;
        }
        
        request.setAttribute("usuarios", usuarios);
        request.getRequestDispatcher("/administrador.jsp").forward(request, response);
    }

    // Metodo para manejar la actualización de usuarios, correo y licencia
        private void handleactualizarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
            String idStr = request.getParameter("id");
            String nuevoCorreo = request.getParameter("correo");
            String nuevaLicencia = request.getParameter("tipo_de_licencia");

            if (idStr != null && nuevoCorreo != null && nuevaLicencia != null) {
                int id = Integer.parseInt(idStr);

                String sql = "UPDATE usuario SET correo = ?, tipo_de_licencia = ? WHERE id = ?";

                try (Connection conn = DBConnection.getConnection();
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {

                    pstmt.setString(1, nuevoCorreo);
                    pstmt.setString(2, nuevaLicencia);
                    pstmt.setInt(3, id);

                    int filasActualizadas = pstmt.executeUpdate();
                    if (filasActualizadas > 0) {
                        response.sendRedirect(request.getContextPath() + "/listausuarios");
                    } else {
                        request.setAttribute("mensajeError", "No se encontró el usuario para actualizar.");
                        request.getRequestDispatcher("/listausuarios").forward(request, response);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    request.setAttribute("mensajeError", "Error al actualizar el usuario.");
                    request.getRequestDispatcher("/listausuarios").forward(request, response);
                }
            } else {
                request.setAttribute("mensajeError", "Datos incompletos para la actualización.");
                request.getRequestDispatcher("/listausuarios").forward(request, response);
            }
        }

        // Metodo para manejar la eliminación de usuarios
        private void handleeliminarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
            
            String idStr = request.getParameter("id");

            if (idStr != null) {
                int id = Integer.parseInt(idStr);

                String sql = "DELETE FROM usuario WHERE id = ?";

                try (Connection conn = DBConnection.getConnection();
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {

                    pstmt.setInt(1, id);

                    int filasEliminadas = pstmt.executeUpdate();
                    if (filasEliminadas > 0) {
                        response.sendRedirect(request.getContextPath() + "/listausuarios");
                    } else {
                        request.setAttribute("mensajeError", "No se encontró el usuario para eliminar.");
                        request.getRequestDispatcher("/listausuarios").forward(request, response);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    request.setAttribute("mensajeError", "Error al eliminar el usuario.");
                    request.getRequestDispatcher("/listausuarios").forward(request, response);
                }
            } else {
                request.setAttribute("mensajeError", "ID de usuario no proporcionado para la eliminación.");
                request.getRequestDispatcher("/listausuarios").forward(request, response);
        }
    }
}