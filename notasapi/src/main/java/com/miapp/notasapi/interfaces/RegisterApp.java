package com.miapp.notasapi.interfaces;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class RegisterApp extends JFrame {

    private static final String REGISTER_URL = "http://localhost:8080/api/v1/usuarios";

    public RegisterApp() {
        setTitle("Registro de Usuario");
        setSize(300, 200);
        setLayout(new GridLayout(4, 2));

        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField();
        JLabel lblEmail = new JLabel("Correo:");
        JTextField txtEmail = new JTextField();
        JLabel lblPassword = new JLabel("Contraseña:");
        JPasswordField txtPassword = new JPasswordField();
        JButton btnRegister = new JButton("Registrar");

        add(lblNombre);
        add(txtNombre);
        add(lblEmail);
        add(txtEmail);
        add(lblPassword);
        add(txtPassword);
        add(new JLabel());
        add(btnRegister);

        btnRegister.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String email = txtEmail.getText().trim();
            String password = new String(txtPassword.getPassword());
            String passwordHash = Utils.hashPassword(password);

            // Validaciones básicas
            if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No puede haber campos vacíos", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!Utils.validarCorreo(email)) {
                JOptionPane.showMessageDialog(this, "Correo no válido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (password.length() < 6) {
                JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 6 caracteres", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Ejecutar en un hilo aparte para no congelar la UI
            new Thread(() -> {
                try {
                    HttpClient client = HttpClient.newHttpClient();
                    String json = String.format("{\"nombre\":\"%s\", \"email\":\"%s\", \"passwordHash\":\"%s\"}",
                            nombre, email, passwordHash);

                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(REGISTER_URL))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                            .build();

                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                    SwingUtilities.invokeLater(() -> {
                        if (response.statusCode() == 201) {
                            JOptionPane.showMessageDialog(this, "Registro exitoso");
                            dispose();
                        } else if (response.statusCode() == 400) {
                            JOptionPane.showMessageDialog(this, "El usuario ya existe o datos inválidos", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "Error del servidor: " + response.statusCode(), "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    });
                } catch (IOException | InterruptedException ex) {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                            "Error de conexión con el servidor", "Error", JOptionPane.ERROR_MESSAGE));
                }
            }).start();
        });

        setVisible(true);
    }
}
