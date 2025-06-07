package com.miapp.notasapi.interfaces;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;

public class NotesApp extends JFrame {
    private JTextField titleField;
    private JTextArea contentArea;
    private DefaultListModel<Note> noteListModel;
    private JList<Note> noteList;
    private JTextField searchField;
    
    private String userFolder;
    private File notesDir; // Carpeta donde se guardan los archivos de cada nota
    
    // Constructor que recibe el email del usuario y el HashMap de usuarios (opcional para reabrir el login)
    public NotesApp(String userEmail, String tocken) {
        // Define la carpeta del usuario y la subcarpeta para notas
        userFolder = "usuarios/" + userEmail;
        File userDir = new File(userFolder);
        if (!userDir.exists()) {
            userDir.mkdirs();
        }
        notesDir = new File(userFolder, "notas");
        if (!notesDir.exists()) {
            notesDir.mkdirs();
        }
        
        setTitle("Notas de " + userEmail);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Panel superior: Campo para ingresar el título de la nota
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new JLabel("Título:"), BorderLayout.WEST);
        titleField = new JTextField();
        topPanel.add(titleField, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
        
        // Panel central: Lista de notas y área de contenido en un split pane
        noteListModel = new DefaultListModel<>();
        noteList = new JList<>(noteListModel);
        noteList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScroll = new JScrollPane(noteList);
        
        contentArea = new JTextArea();
        JScrollPane contentScroll = new JScrollPane(contentArea);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScroll, contentScroll);
        splitPane.setDividerLocation(250);
        add(splitPane, BorderLayout.CENTER);
        
        // Panel inferior: Botones y campo de búsqueda
        JPanel bottomPanel = new JPanel();
        JButton btnGuardar = new JButton("Guardar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");
        searchField = new JTextField(10);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnLogout = new JButton("Cerrar Sesión");
        
        bottomPanel.add(btnGuardar);
        bottomPanel.add(btnEditar);
        bottomPanel.add(btnEliminar);
        bottomPanel.add(btnLimpiar);
        bottomPanel.add(new JLabel("Buscar:"));
        bottomPanel.add(searchField);
        bottomPanel.add(btnBuscar);
        bottomPanel.add(btnLogout);
        
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Cargar notas existentes leyendo los archivos de la carpeta de notas
        loadNotes();
        
        // Al seleccionar una nota, se muestran sus datos en los campos de título y contenido
        noteList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Note selected = noteList.getSelectedValue();
                if (selected != null) {
                    titleField.setText(selected.getTitle());
                    contentArea.setText(selected.getContent());
                }
            }
        });
        
        // Botón Guardar: Crea un archivo para la nueva nota
        btnGuardar.addActionListener(e -> {
            String title = titleField.getText().trim();
            String content = contentArea.getText().trim();
            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El título no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Crear archivo con el título (sanitizado) y extensión .txt
            File noteFile = new File(notesDir, sanitizeFilename(title) + ".txt");
            if (noteFile.exists()) {
                JOptionPane.showMessageDialog(this, "Ya existe una nota con ese título. Usa 'Editar' para modificarla.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(noteFile))) {
                writer.write(content);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            Note note = new Note(title, content);
            noteListModel.addElement(note);
            clearFields();
        });
        
        // Botón Editar: Actualiza el archivo de la nota seleccionada
        btnEditar.addActionListener(e -> {
            Note selected = noteList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Selecciona una nota para editar.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String newTitle = titleField.getText().trim();
            String newContent = contentArea.getText().trim();
            if (newTitle.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El título no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Archivos: antiguo y nuevo (si el título cambió)
            File oldFile = new File(notesDir, sanitizeFilename(selected.getTitle()) + ".txt");
            File newFile = new File(notesDir, sanitizeFilename(newTitle) + ".txt");
            if (!oldFile.exists()) {
                JOptionPane.showMessageDialog(this, "El archivo de la nota no existe.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Si se cambia el título y ya existe otro archivo con ese nombre, error.
            if (!selected.getTitle().equals(newTitle) && newFile.exists()) {
                JOptionPane.showMessageDialog(this, "Ya existe una nota con el nuevo título.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                // Si el título cambió, eliminamos el antiguo y creamos uno nuevo
                if (!selected.getTitle().equals(newTitle)) {
                    oldFile.delete();
                    newFile.createNewFile();
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(newFile))) {
                        writer.write(newContent);
                    }
                } else {
                    // Si no cambió el título, actualizamos el contenido en el mismo archivo
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(oldFile))) {
                        writer.write(newContent);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Actualizamos la nota en la lista
            selected.setTitle(newTitle);
            selected.setContent(newContent);
            noteList.repaint();
            clearFields();
        });
        
        // Botón Eliminar: Borra la nota seleccionada y su archivo asociado
        btnEliminar.addActionListener(e -> {
            int index = noteList.getSelectedIndex();
            if (index != -1) {
                Note noteToDelete = noteListModel.get(index);
                File fileToDelete = new File(notesDir, sanitizeFilename(noteToDelete.getTitle()) + ".txt");
                if (fileToDelete.exists()) {
                    fileToDelete.delete();
                }
                noteListModel.remove(index);
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona una nota para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Botón Limpiar: Elimina todas las notas y sus archivos
        btnLimpiar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar todas las notas?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                File[] files = notesDir.listFiles();
                if (files != null) {
                    for (File f : files) {
                        f.delete();
                    }
                }
                noteListModel.clear();
                clearFields();
            }
        });
        
        // Botón Buscar: Selecciona la primera nota cuyo título contenga el texto ingresado
        btnBuscar.addActionListener(e -> {
            String query = searchField.getText().trim().toLowerCase();
            if (query.isEmpty()) return;
            for (int i = 0; i < noteListModel.getSize(); i++) {
                Note note = noteListModel.getElementAt(i);
                if (note.getTitle().toLowerCase().contains(query)) {
                    noteList.setSelectedIndex(i);
                    noteList.ensureIndexIsVisible(i);
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "No se encontró ninguna nota con ese título.", "Buscar", JOptionPane.INFORMATION_MESSAGE);
        });
        
        // Botón Cerrar Sesión: Guarda (ya se guardan en cada acción) y cierra la ventana
        btnLogout.addActionListener(e -> {
            dispose();
            // Aquí podrías reabrir la ventana de login, por ejemplo:
            // new LoginApp(users);
        });
        
        setVisible(true);
    }
    
    // Método auxiliar para limpiar los campos de entrada
    private void clearFields() {
        titleField.setText("");
        contentArea.setText("");
    }
    
    // Método auxiliar para sanitizar el nombre del archivo (elimina caracteres no válidos)
    private String sanitizeFilename(String name) {
        return name.replaceAll("[\\\\/:*?\"<>|]", "_");
    }
    
    // Carga las notas leyendo todos los archivos .txt de la carpeta de notas
    private void loadNotes() {
        File[] files = notesDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
        if (files != null) {
            for (File f : files) {
                String fileName = f.getName();
                String title = fileName.substring(0, fileName.length() - 4); // eliminar .txt
                String content = "";
                try {
                    content = new String(Files.readAllBytes(f.toPath()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                Note note = new Note(title, content);
                noteListModel.addElement(note);
            }
        }
    }
    
    // Clase interna para representar una nota
    private static class Note {
        private String title;
        private String content;
        
        public Note(String title, String content) {
            this.title = title;
            this.content = content;
        }
        public String getTitle() { return title; }
        public String getContent() { return content; }
        public void setTitle(String title) { this.title = title; }
        public void setContent(String content) { this.content = content; }
        @Override
        public String toString() { return title; }
    }
}
