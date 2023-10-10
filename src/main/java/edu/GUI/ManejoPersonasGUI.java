package edu.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import edu.datos.*;
import edu.domain.Persona;
import java.sql.Connection;
import java.sql.SQLException;

public class ManejoPersonasGUI extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public ManejoPersonasGUI() {
        super("Manejo de Personas");

        // Configuración de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);

        // Crear componentes
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Iniciar Sesión");

        // Crear panel para los componentes
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));
        panel.add(new JLabel("Nombre de Usuario:"));
        panel.add(usernameField);
        panel.add(new JLabel("Contraseña:"));
        panel.add(passwordField);
        panel.add(loginButton);

        // Agregar panel a la ventana
        add(panel);

        // Asociar acción al botón de inicio de sesión
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Validar el usuario y contraseña aquí
                UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
                boolean esValido = usuarioDAO.validarUsuario(username, password);

                if (esValido) {
                    // Realizar operaciones de actualización de datos aquí
                    Connection conexion = null;
                    try {
                        conexion = Conexion.getConnection();
                        if (conexion.getAutoCommit()) {
                            conexion.setAutoCommit(false);
                        }

                        PersonaJDBC personaJdbc = new PersonaJDBC(conexion);

                        // Actualizar datos de personas
                        Persona cambioPersona = new Persona();
                        cambioPersona.setId_persona(1);
                        cambioPersona.setNombre("Luis");
                        cambioPersona.setApellido("García");
                        cambioPersona.setEmail("nuevocorreo@gmail.com");
                        cambioPersona.setTelefono("555555555");
                        personaJdbc.update(cambioPersona);
                        conexion.commit();

                        JOptionPane.showMessageDialog(null, "Operación exitosa", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error en la operación", "Error", JOptionPane.ERROR_MESSAGE);
                        try {
                            if (conexion != null) {
                                conexion.rollback();
                            }
                        } catch (SQLException ex1) {
                            ex1.printStackTrace();
                        }
                    } finally {
                        Conexion.close(conexion);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Usuario o contraseña inválidos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ManejoPersonasGUI gui = new ManejoPersonasGUI();
                gui.setVisible(true);
            }
        });
    }
}
