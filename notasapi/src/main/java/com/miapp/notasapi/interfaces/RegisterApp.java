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
        // Ajustes generales de la pantalla
        setTitle("Registro de Usuario");
        setSize(300, 200);
        setLayout(new GridLayout(4, 2));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana

        // Componentes de la interfaz
        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField();
        JLabel lblEmail = new JLabel("Correo:");
        JTextField txtEmail = new JTextField();
        JLabel lblPassword = new JLabel("Contraseña:");
        JPasswordField txtPassword = new JPasswordField();
        JButton btnRegister = new JButton("Registrar");
        // Estilo de los componentes
        lblNombre.setFont(new Font("Arial", Font.PLAIN, 14));
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 14));
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        btnRegister.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegister.setBackground(new Color(0, 123, 255));
        btnRegister.setForeground(Color.WHITE);
        // Bordes de los botones
        btnRegister.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        // Añadimos padding a los campos de texto
        txtNombre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        // Añadimos separación entre los componentes
        setLayout(new GridLayout(5, 2, 10, 10));
        // Modificamos el cursor de los botones
        btnRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // Añadimos hover a los botones
        btnRegister.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRegister.setBackground(new Color(0, 105, 217));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRegister.setBackground(new Color(0, 123, 255));
            }
        });

        // Añadimos los componentes al JFrame
        add(lblNombre);
        add(txtNombre);
        add(lblEmail);
        add(txtEmail);
        add(lblPassword);
        add(txtPassword);
        add(new JLabel());
        add(btnRegister);

        
        // Boton de registro
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
