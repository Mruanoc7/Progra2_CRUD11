package edu.datos;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAOImpl implements UsuarioDAO {

    private Connection conexionTransaccional; // Incluye el constructor según tus necesidades

    @Override
    public boolean validarUsuario(String username, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean esValido = false;

        try {
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
            String sql = "SELECT password FROM usuario WHERE username = ?"; // Usar "password" en lugar de "contrasena"
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String contrasenaAlmacenada = rs.getString("password"); // Usar "password" en lugar de "contrasena"
                String contrasenaEncriptada = encriptarContrasena(password);

                // Compara la contraseña ingresada con la almacenada en la base de datos
                esValido = contrasenaAlmacenada.equals(contrasenaEncriptada);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            // Cierra recursos (stmt, rs) y la conexión si no es transaccional
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (this.conexionTransaccional == null && conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return esValido;
    }

    @Override
    public int insertUsuario(String username, String contrasena) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            conn = this.conexionTransaccional != null ? this.conexionTransaccional : Conexion.getConnection();
            String sql = "INSERT INTO usuario (username, password) VALUES (?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            // Encripta la contraseña antes de insertarla en la base de datos
            String contrasenaEncriptada = encriptarContrasena(contrasena);
            stmt.setString(2, contrasenaEncriptada);

            rows = stmt.executeUpdate();
            System.out.println("Usuario insertado exitosamente.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            Conexion.close(stmt);
            if (this.conexionTransaccional == null) {
                Conexion.close(conn);
            }
        }

        return rows;
    }

    @Override
    public String encriptarContrasena(String contrasena) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(contrasena.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al encriptar la contraseña.");
        }
    }

    // Implementa otros métodos para CRUD de usuarios según tus necesidades
}
