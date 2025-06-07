package com.miapp.notasapi.interfaces;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;

public class LoginApp extends JFrame {

    private static final String LOGIN_URL = "http://localhost:8080/api/v2/usuarios";

    public LoginApp() {
        new LoginFrame();
    }

    public class LoginFrame extends JFrame {
        // Ajustes generales de la pantalla
        public LoginFrame() {
            setTitle("Inicio de Sesión");
            setSize(300, 200);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new GridLayout(3, 2));
            setLocationRelativeTo(null); // Centrar la ventana
            setResizable(true); // Permitir redimensionar

            // Componentes de la interfaz
            JLabel lblEmail = new JLabel("Correo:");
            JTextField txtEmail = new JTextField();
            JLabel lblPassword = new JLabel("Contraseña:");
            JPasswordField txtPassword = new JPasswordField();
            JButton btnLogin = new JButton("Iniciar Sesión");
            JButton btnRegister = new JButton("Registrarse");

            // Estilo de los componentes
            lblEmail.setFont(new Font("Arial", Font.PLAIN, 14));
            lblPassword.setFont(new Font("Arial", Font.PLAIN, 14));
            txtEmail.setFont(new Font("Arial", Font.PLAIN, 14));
            txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
            btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
            btnRegister.setFont(new Font("Arial", Font.BOLD, 14));
            btnLogin.setBackground(new Color(0, 123, 255));
            btnLogin.setForeground(Color.WHITE);
            btnRegister.setBackground(new Color(108, 117, 125));
            btnRegister.setForeground(Color.WHITE);
            // Bordes de los botones
            btnLogin.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
            btnRegister.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
            // Añadimos padding a los campos de texto
            txtEmail.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.GRAY, 1),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            txtPassword.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.GRAY, 1),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            // Añadimos separación entre los componentes
            setLayout(new GridLayout(4, 2, 10, 10));
            // Modificamos el cursor de los botones
            btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            // Añadimos efecto hover a los botones
            btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btnLogin.setBackground(new Color(0, 105, 217));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btnLogin.setBackground(new Color(0, 123, 255));
                }
            });
            btnRegister.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btnRegister.setBackground(new Color(92, 102, 110));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btnRegister.setBackground(new Color(108, 117, 125));
                }
            });

            // Añadimos los componentes al frame
            add(lblEmail); add(txtEmail);
            add(lblPassword); add(txtPassword);
            add(btnLogin); add(btnRegister);

            // Acción del botón de inicio de sesión
            // Validación de campos y llamada a la API
            btnLogin.addActionListener(e -> {
                String email = txtEmail.getText().trim();
                String password = new String(txtPassword.getPassword());
                String passwordHash = Utils.hashPassword(password); // Asumimos que Utils.hashPassword() está implementado

                // Validación básica
                if (email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Rellena todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!Utils.validarCorreo(email)) {
                    JOptionPane.showMessageDialog(this, "Correo no válido", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Llamada a la API en segundo plano
                new Thread(() -> {
                    try {
                        HttpClient client = HttpClient.newHttpClient();
                        String json = String.format("{\"email\":\"%s\", \"passwordHash\":\"%s\"}", email, passwordHash);

                        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(LOGIN_URL))
                                .header("Content-Type", "application/json")
                                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                                .build();

                        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                        // Procesar la respuesta en el hilo de la UI
                        SwingUtilities.invokeLater(() -> {
                            if (response.statusCode() == 200) {
                                String responseBody = response.body();
                                String token = Utils.extractToken(responseBody); // implementamos esto abajo
                                if (token != null) {
                                    JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso");
                                    new NotesApp(email, token);
                                    dispose();
                                } else {
                                    JOptionPane.showMessageDialog(this, "Token no recibido", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            } else if (response.statusCode() == 401) {
                                JOptionPane.showMessageDialog(this, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(this, "Error del servidor: " + response.statusCode(), "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        });

                    } catch (IOException | InterruptedException ex) {
                        SwingUtilities.invokeLater(() -> 
                            JOptionPane.showMessageDialog(this, "Error de conexión con el servidor", "Error", JOptionPane.ERROR_MESSAGE)
                        );
                    }
                }).start();
            });

            btnRegister.addActionListener(e -> new RegisterApp()); // Abrir la ventana de registro

            setVisible(true);
        }
    }
}

