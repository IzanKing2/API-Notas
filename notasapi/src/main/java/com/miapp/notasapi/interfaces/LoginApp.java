package com.miapp.notasapi.interfaces;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;

public class LoginApp extends JFrame {

    private static final String LOGIN_URL = "http://localhost:8080/api/auth/login";

    public LoginApp() {
        new LoginFrame();
    }

    public class LoginFrame extends JFrame {
        public LoginFrame() {
            setTitle("Inicio de Sesión");
            setSize(300, 200);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new GridLayout(3, 2));

            JLabel lblEmail = new JLabel("Correo:");
            JTextField txtEmail = new JTextField();
            JLabel lblPassword = new JLabel("Contraseña:");
            JPasswordField txtPassword = new JPasswordField();
            JButton btnLogin = new JButton("Iniciar Sesión");
            JButton btnRegister = new JButton("Registrarse");

            add(lblEmail); add(txtEmail);
            add(lblPassword); add(txtPassword);
            add(btnLogin); add(btnRegister);

            btnLogin.addActionListener(e -> {
                String email = txtEmail.getText().trim();
                String password = new String(txtPassword.getPassword());

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
                        String json = String.format("{\"email\":\"%s\", \"password\":\"%s\"}", email, password);

                        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(LOGIN_URL))
                                .header("Content-Type", "application/json")
                                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                                .build();

                        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

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

            btnRegister.addActionListener(e -> new RegisterApp());

            setVisible(true);
        }
    }
}

