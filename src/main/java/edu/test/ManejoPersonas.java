package edu.test;

import edu.datos.Conexion;
import edu.datos.UsuarioDAO;
import edu.datos.UsuarioDAOImpl;
import edu.datos.PersonaJDBC;
import edu.domain.Persona;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class ManejoPersonas {

    public static void main(String[] args) {

        // Definimos la variable conexión
        Connection conexion = null;
        try {
            conexion = Conexion.getConnection();

            // Autocommit por defecto es true, lo pasamos a false
            if (conexion.getAutoCommit()) {
                conexion.setAutoCommit(false);
            }

            PersonaJDBC personaJdbc = new PersonaJDBC(conexion);
            UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

            Scanner sc = new Scanner(System.in);
            //Crear usuario en tabla usuario
//            UsuarioDAOImpl usuarioDAOImpl = new UsuarioDAOImpl();
//            usuarioDAOImpl.insertUsuario("Luis", "powerdax");
//            conexion.commit();
//            System.out.println("Se ha hecho commit de la transacción");

            // Pedimos al usuario que ingrese su nombre de usuario y contraseña
            System.out.print("Ingrese su nombre de usuario: ");
            String username = sc.nextLine();
            System.out.print("Ingrese su contraseña: ");
            String contrasena = sc.nextLine();

            // Validamos el usuario y contraseña
            if (usuarioDAO.validarUsuario(username, contrasena)) {
                System.out.println("Usuario válido. Puede continuar.");

//                 Continuar con las operaciones de PersonaJDBC si el usuario es válido
//                 Por ejemplo, insertar o actualizar personas
//                 Persona nuevaPersona = new Persona();
//                 nuevaPersona.setNombre("Luis");
//                 nuevaPersona.setApellido("García");
//                 personaJdbc.insert(nuevaPersona);
//                 conexion.commit();
//                 System.out.println("Se ha hecho commit de la transacción");

                // Actualizar datos de personas
                Persona cambioPersona = new Persona();
                cambioPersona.setId_persona(1);
                cambioPersona.setNombre("Luis");
                cambioPersona.setApellido("García");
                cambioPersona.setEmail("nuevocorreo@gmail.com");
                cambioPersona.setTelefono("555555555");
                personaJdbc.update(cambioPersona);
                conexion.commit();
                System.out.println("Se ha hecho commit de la transacción");



            } else {
                System.out.println("Usuario o contraseña inválidos.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
            System.out.println("Entramos al rollback");
            try {
                if (conexion != null) {
                    conexion.rollback();
                }
            } catch (SQLException ex1) {
                ex1.printStackTrace(System.out);
            }
        } finally {
            // Cerrar la conexión
            Conexion.close(conexion);
        }
    }
}
