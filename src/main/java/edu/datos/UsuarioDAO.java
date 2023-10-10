package edu.datos;

import edu.domain.Usuario;

public interface UsuarioDAO {
    boolean validarUsuario(String username, String contrasena);
    int insertUsuario(String username, String contrasena);
    String encriptarContrasena(String contrasena);
    // Otros métodos para CRUD de usuarios según tus necesidades
}