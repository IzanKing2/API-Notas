package com.miapp.notasapi;

import java.util.HashMap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.miapp.notasapi.interfaces.LoginApp;

@SpringBootApplication
public class NotasapiApplication {

	public static void main(String[] args) {
		// Cargar usuarios desde el archivo antes de iniciar la app
        HashMap<String, String> users = new HashMap<>();
        
        // Iniciar la ventana de login
        new LoginApp();

		SpringApplication.run(NotasapiApplication.class, args);
	}

}
