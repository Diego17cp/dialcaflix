package com.dialca.recommender.ui.controller;

import com.dialca.recommender.model.Movie;
import com.dialca.recommender.model.Users;
import com.dialca.recommender.controller.RatingController;
import com.dialca.recommender.dao.RatingDao;
import com.dialca.recommender.model.Rating;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.InputStream;

public class MovieDetailsController {

    @FXML private ImageView imgPoster;
    @FXML private Label lblTitle;
    @FXML private Label lblYear;
    @FXML private Label lblGenre;
    @FXML private Text txtSynopsis;
    @FXML private Label lblAverageRating;
    @FXML private ProgressIndicator loadingIndicator;
    @FXML private HBox averageRatingContainer;
    @FXML private HBox userRatingContainer;
    @FXML private Button btnStar1;
    @FXML private Button btnStar2;
    @FXML private Button btnStar3;
    @FXML private Button btnStar4;
    @FXML private Button btnStar5;
    @FXML private Button btnAddToFavorites;
    @FXML private Button btnWatchLater;
    @FXML private Button btnClose;
    
    private Movie movie;
    private Users currentUser;
    private RatingController ratingController;
    private int currentUserRating = 0;
    
    private Image starFilledImage;
    private Image starEmptyImage;
    
    @FXML
    public void initialize() {
        ratingController = new RatingController();
        
        // Cargar las imágenes de estrellas desde los recursos
        try {
            starFilledImage = new Image(getClass().getResourceAsStream(
                "/com/dialca/recommender/assets/star-filled.png"));
            starEmptyImage = new Image(getClass().getResourceAsStream(
                "/com/dialca/recommender/assets/star-empty.png"));
            
            // Establecer imágenes iniciales (todas vacías)
            setupStarImages();
        } catch (Exception e) {
            System.err.println("Error al cargar imágenes de estrellas: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void setMovie(Movie movie, Users currentUser) {
        this.movie = movie;
        this.currentUser = currentUser;
        
        if (movie != null) {
            loadMovieData();
        }
        boolean isLoggedIn = (currentUser != null);
        btnAddToFavorites.setVisible(isLoggedIn);
        btnWatchLater.setVisible(isLoggedIn);
        boolean canRate = isLoggedIn;
        for (Button btn : new Button[]{btnStar1, btnStar2, btnStar3, btnStar4, btnStar5}) {
            btn.setDisable(!canRate);
        }
        if (isLoggedIn && movie != null) {
            loadUserRating();
        }
    }
    
    private void loadUserRating() {
        try {
            Rating userRating = new RatingDao().findByUserAndMovie(currentUser, movie);
            if (userRating != null) {
                currentUserRating = userRating.getRate();
                updateStarButtons(currentUserRating);
            } else {
                currentUserRating = 0;
                updateStarButtons(0);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar la calificación del usuario: " + e.getMessage());
        }
    }
    
    private void setupStarImages() {
        Button[] stars = {btnStar1, btnStar2, btnStar3, btnStar4, btnStar5};
        for (Button star : stars) {
            ImageView starView = new ImageView(starEmptyImage);
            starView.setFitWidth(24);
            starView.setFitHeight(24);
            star.setGraphic(starView);
            star.setText("");
            if (!star.getStyleClass().contains("star-button")) {
                star.getStyleClass().add("star-button");
            }
        }
    }
    
    private void updateStarButtons(int rating) {
        Button[] stars = {btnStar1, btnStar2, btnStar3, btnStar4, btnStar5};
        for (int i = 0; i < stars.length; i++) {
            ImageView starView = (ImageView) stars[i].getGraphic();
            if (starView != null) {
                if (i < rating) {
                    starView.setImage(starFilledImage);
                } else {
                    starView.setImage(starEmptyImage);
                }
            } else {
                starView = new ImageView(i < rating ? starFilledImage : starEmptyImage);
                starView.setFitWidth(24); 
                starView.setFitHeight(24);
                stars[i].setGraphic(starView);
                stars[i].setText(""); 
            }
        }
    }
    
    private boolean saveRating(int rating) {
        if (currentUser != null && movie != null) {
            try {
                boolean success = ratingController.updateRating(currentUser, movie, rating);
                if (success) {
                    double avgRating = ratingController.getAverageRatingForMovie(movie.getId());
                    lblAverageRating.setText(String.format("%.1f", avgRating));
                }
                return success;
            } catch (Exception e) {
                System.err.println("Error al guardar la calificación: " + e.getMessage());
                return false;
            }
        }
        return false;
    }
    
    private void loadMovieData() {
        lblTitle.setText(movie.getTitle());
        lblYear.setText(String.valueOf(movie.getYear()));
        lblGenre.setText(movie.getGenre());
        try {
            String description = movie.getDescription();
            txtSynopsis.setText(description);
        } catch (Exception e) {
            txtSynopsis.setText("No hay descripción disponible para esta película.");
        }
        loadPosterImage(movie.getPosterUrl());
        try {
            double avgRating = ratingController.getAverageRatingForMovie(movie.getId());
            if (avgRating > 0) {
                lblAverageRating.setText(String.format("%.1f", avgRating));
            } else {
                lblAverageRating.setText("Sin calificaciones");
            }
        } catch (Exception e) {
            lblAverageRating.setText("--");
        }
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
                if (newVal.doubleValue() >= 1.0) {
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
                    System.err.println("Error cargando imagen: " + posterUrl);
                    if (loadingIndicator != null) {
                        loadingIndicator.setVisible(false);
                    }
                }
            });
            
        } catch (Exception e) {
            System.err.println("Error al cargar póster: " + e.getMessage());
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
        } catch (Exception e) {
            System.err.println("No se pudo cargar la imagen por defecto: " + e.getMessage());
        }
        return null;
    }
    
    @FXML
    private void handleAddToFavorites() {
        showAlert("Esta función estará disponible próximamente");
    }
    
    @FXML
    private void handleAddToWatchLater() {
        showAlert("Esta función estará disponible próximamente");
    }
    
    @FXML
    private void handleStar1Click() {
        int rating = 1;
        if (currentUserRating == rating) {
            rating = 0;
        }
        if (saveRating(rating)) {
            currentUserRating = rating;
            updateStarButtons(rating);
        }
    }
    
    @FXML
    private void handleStar2Click() {
        int rating = 2;
        if (saveRating(rating)) {
            currentUserRating = rating;
            updateStarButtons(rating);
        }
    }
    
    @FXML
    private void handleStar3Click() {
        int rating = 3;
        if (saveRating(rating)) {
            currentUserRating = rating;
            updateStarButtons(rating);
        }
    }
    
    @FXML
    private void handleStar4Click() {
        int rating = 4;
        if (saveRating(rating)) {
            currentUserRating = rating;
            updateStarButtons(rating);
        }
    }
    
    @FXML
    private void handleStar5Click() {
        int rating = 5;
        if (saveRating(rating)) {
            currentUserRating = rating;
            updateStarButtons(rating);
        }
    }
    
    @FXML
    private void handleClose() {
        // Cerrar la ventana
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("DialcaFlix");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
