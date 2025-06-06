package com.dialca.recommender.ui.controller;

import java.io.FileInputStream;
import java.io.InputStream;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Insets;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

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
            ImageView backgroundImage = new ImageView();
            backgroundImage.setFitWidth(800);
            backgroundImage.setFitHeight(600);
            backgroundImage.setPreserveRatio(false);
            Image image = null;
            InputStream is = getClass().getResourceAsStream("/com/dialca/recommender/assets/homeBg.jpg");
            if (is != null) {
                image = new Image(is);
            } else {
                try {
                    String relativePath = "./src/com/dialca/recommender/assets/homeBg.jpg";
                    image = new Image(new FileInputStream(relativePath));
                } catch (Exception e) {
                    System.err.println("No se pudo cargar la imagen desde ruta relativa: " + e.getMessage());
                }
            }
            if (image != null) {
                backgroundImage.setImage(image);
                root.getChildren().add(0, backgroundImage);

                Rectangle overlay = new Rectangle(800, 600, Color.rgb(0, 0, 0, 0.5));
                root.getChildren().add(1, overlay);
            } else {
                Rectangle fallbackBackground = new Rectangle(800, 600, Color.rgb(20, 20, 40, 1.0));
                root.getChildren().add(0, fallbackBackground);
                System.err.println("No se pudo cargar la imagen de fondo - usando color alternativo");
            }
            ImageView logoImg = new ImageView();
            logoImg.setFitWidth(200);
            logoImg.setFitHeight(100);
            logoImg.setPreserveRatio(true);
            Image logoImage = null;
            InputStream logoStream = getClass().getResourceAsStream("/com/dialca/recommender/assets/logo.png");
            if (logoStream != null) {
                logoImage = new Image(logoStream);
                logoImg.setImage(logoImage);
                root.getChildren().add(logoImg);
                StackPane.setAlignment(logoImg, javafx.geometry.Pos.TOP_LEFT);
                StackPane.setMargin(logoImg, new Insets(20, 0, 0, 20));
            } else {
                try {
                    String relativeLogoPath = "./src/com/dialca/recommender/assets/logo.png";
                    logoImage = new Image(new FileInputStream(relativeLogoPath));
                } catch (Exception e) {
                    System.err.println("No se pudo cargar la imagen del logo desde ruta relativa: " + e.getMessage());
                }
            }
            setupButtonHandlers();
        } catch (Exception e) {
            System.err.println("Error en inicialización: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupButtonHandlers() {
        btnLogin.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dialca/recommender/ui/view/LoginView.fxml"));
                Parent loginRoot = loader.load();
                Stage loginStage = new Stage();
                loginStage.initModality(Modality.APPLICATION_MODAL);
                loginStage.initStyle(StageStyle.DECORATED);
                loginStage.setTitle("Iniciar Sesión - DialcaFlix");
                loginStage.setScene(new Scene(loginRoot));
                loginStage.setResizable(false);
                loginStage.showAndWait();
            } catch (Exception e) {
                System.err.println("Error al cargar la vista de inicio de sesión: " + e.getMessage());
                e.printStackTrace();
            }
        });

        btnRegister.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dialca/recommender/ui/view/RegisterView.fxml"));
                Parent registerRoot = loader.load();
                Stage registerStage = new Stage();
                registerStage.initModality(Modality.APPLICATION_MODAL);
                registerStage.initStyle(StageStyle.DECORATED);
                registerStage.setTitle("Registro - DialcaFlix");
                registerStage.setScene(new Scene(registerRoot));
                registerStage.setResizable(false);
                registerStage.showAndWait();
            } catch (Exception e) {
                System.err.println("Error al cargar la vista de registro: " + e.getMessage());
                e.printStackTrace();
            }
        });
        
        btnStart.setOnAction(event -> {
            btnLogin.fire();
        });
    }
}
