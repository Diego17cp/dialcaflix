package com.dialca.recommender.ui.controller;

import java.io.FileInputStream;
import java.io.InputStream;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class HomeController {

    @FXML
    private StackPane root;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnRegister;

    @FXML
    private Button btnStart;

    @FXML
    public void initialize() {
        try {
            // Crear imagen de fondo programáticamente
            ImageView backgroundImage = new ImageView();
            backgroundImage.setFitWidth(800);
            backgroundImage.setFitHeight(600);
            backgroundImage.setPreserveRatio(false);

            // Cargar imagen usando rutas relativas (portables)
            Image image = null;

            // Intento 1: Desde el classpath (funciona independientemente del sistema)
            InputStream is = getClass().getResourceAsStream("/com/dialca/recommender/assets/homeBg.jpg");
            if (is != null) {
                image = new Image(is);
            } else {
                // Intento 2: Relativa al directorio del proyecto
                try {
                    String relativePath = "./src/com/dialca/recommender/assets/homeBg.jpg";
                    image = new Image(new FileInputStream(relativePath));
                } catch (Exception e) {
                    System.err.println("No se pudo cargar la imagen desde ruta relativa: " + e.getMessage());
                }
            }

            // Si se cargó la imagen, configurar el ImageView
            if (image != null) {
                backgroundImage.setImage(image);
                root.getChildren().add(0, backgroundImage);

                // Añadir overlay semitransparente para mejorar legibilidad del texto
                Rectangle overlay = new Rectangle(800, 600, Color.rgb(0, 0, 0, 0.5));
                root.getChildren().add(1, overlay);
            } else {
                // Si no hay imagen, usar un fondo oscuro como alternativa
                Rectangle fallbackBackground = new Rectangle(800, 600, Color.rgb(20, 20, 40, 1.0));
                root.getChildren().add(0, fallbackBackground);
                System.err.println("No se pudo cargar la imagen de fondo - usando color alternativo");
            }

            // Configurar manejadores de eventos para los botones
            setupButtonHandlers();

        } catch (Exception e) {
            System.err.println("Error en inicialización: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupButtonHandlers() {
        btnLogin.setOnAction(event -> {
            System.out.println("Botón de login presionado");
            // Aquí irá la lógica para mostrar la pantalla de login
        });

        btnRegister.setOnAction(event -> {
            System.out.println("Botón de registro presionado");
            // Aquí irá la lógica para mostrar la pantalla de registro
        });
        
        btnStart.setOnAction(event -> {
            System.out.println("Botón comenzar presionado");
            // Este botón podría hacer lo mismo que registro o mostrar una pantalla de destacados
        });
    }
}
