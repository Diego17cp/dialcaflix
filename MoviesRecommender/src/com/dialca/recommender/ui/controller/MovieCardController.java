package com.dialca.recommender.ui.controller;

import com.dialca.recommender.model.Movie;
import com.dialca.recommender.model.Users;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class MovieCardController {

    @FXML
    private ImageView imgPoster;
    @FXML
    private Label lblCategory;
    @FXML
    private Label lblTitle;
    @FXML
    private Button btnWatch;
    @FXML
    private ProgressIndicator loadingIndicator;
    @FXML
    private StackPane hoverOverlay;

    private Movie movie;
    private Runnable onWatchClicked;

    @FXML
    public void initialize() {
        btnWatch.setOnAction(event -> {
            if (onWatchClicked != null && movie != null) {
                onWatchClicked.run();
            }
        });
    }

    public void setMovie(Movie movie) {
        this.movie = movie;

        if (movie != null) {
            lblTitle.setText(movie.getTitle());
            lblCategory.setText(movie.getGenre());
            loadPosterImage(movie.getPosterUrl());
        }
    }

    public void setOnWatchClicked(Movie movie, Users currentUser) {
        btnWatch.setOnAction(event -> {
            try {
                // Corregir la ruta al archivo FXML - el nombre era incorrecto
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dialca/recommender/ui/view/components/MovieDetails.fxml"));
                Parent detailsRoot = loader.load();
                
                MovieDetailsController detailsController = loader.getController();
                detailsController.setMovie(movie, currentUser);
                
                Stage detailsStage = new Stage();
                detailsStage.initModality(Modality.APPLICATION_MODAL);
                detailsStage.initStyle(StageStyle.DECORATED); // Cambiado a DECORATED para tener barra de título
                detailsStage.setTitle(movie.getTitle() + " - DialcaFlix");
                detailsStage.setScene(new Scene(detailsRoot));
                
                Stage primaryStage = (Stage) btnWatch.getScene().getWindow();
                detailsStage.initOwner(primaryStage);
                
                detailsStage.showAndWait();
            }
            catch (Exception e) {
                System.err.println("Error al cargar la vista de detalles de la película: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
    private void loadPosterImage(String posterUrl) {
        if (loadingIndicator != null) {
            loadingIndicator.setVisible(true);
        }
        Image defaultImage = loadDefaultImage();
        imgPoster.setImage(defaultImage);
        if (posterUrl == null || posterUrl.isEmpty()) {
            if (loadingIndicator != null) {
                loadingIndicator.setVisible(false);
            }
            return;
        }
        try {
            Image posterImage = new Image(posterUrl, true);
            posterImage.progressProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal.doubleValue() == 1.0) {
                    if (!posterImage.isError()) {
                        imgPoster.setImage(posterImage);
                    }
                    if (loadingIndicator != null) {
                        loadingIndicator.setVisible(false);
                    }
                }
            });
            posterImage.errorProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    imgPoster.setImage(defaultImage);
                    if (loadingIndicator != null) {
                        loadingIndicator.setVisible(false);
                    }
                }
            });
        } catch (Exception e) {
            System.err.println("Error al intentar cargar imagen: " + e.getMessage());
            imgPoster.setImage(defaultImage);
            if (loadingIndicator != null) {
                loadingIndicator.setVisible(false);
            }
        }
    }
    private Image loadDefaultImage() {
        try {
            InputStream is = getClass().getResourceAsStream("/com/dialca/recommender/assets/no-poster.jpg");
            if (is != null) {
                return new Image(is);
            }
            File file = new File("./src/com/dialca/recommender/assets/no-poster.jpg");
            if (file.exists()) {
                return new Image(new FileInputStream(file));
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar la imagen por defecto: " + e.getMessage());
        }
        return createColoredPlaceholder();
    }
    private Image createColoredPlaceholder() {
        int width = 220;
        int height = 280;
        WritableImage image = new WritableImage(width, height);
        PixelWriter pixelWriter = image.getPixelWriter();
        Color color = Color.rgb(45, 45, 65);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixelWriter.setColor(x, y, color);
            }
        }
        return image;
    }

    @FXML
    private void showOverlay() {
        hoverOverlay.setVisible(true);
    }

    @FXML
    private void hideOverlay() {
        hoverOverlay.setVisible(false);
    }
}
