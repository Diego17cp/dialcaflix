package com.dialca.recommender.ui.controller;

import com.dialca.recommender.model.Users;
import com.dialca.recommender.controller.MovieController;
import com.dialca.recommender.controller.RatingController;
import com.dialca.recommender.model.Movie;
import java.util.ArrayList;
import com.dialca.recommender.ia.*;
import com.dialca.recommender.ui.controller.MovieCardController;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.util.List;
import java.io.FileInputStream;
import java.io.InputStream;
import javafx.geometry.Rectangle2D;
import javafx.event.ActionEvent;
import java.util.Arrays;

public class MainViewController {
    @FXML
    private ImageView imgLogo;
    @FXML
    private TextField txtSearch;
    @FXML
    private Button btnSearch;
    @FXML
    private Label lblUserName;

    @FXML
    private Button btnHome;
    @FXML
    private TitledPane categoriesPane;
    @FXML
    private Button btnNewReleases;
    @FXML
    private Button btnFavorites;
    @FXML
    private Button btnWatchLater;
    @FXML
    private Button btnHistory;
    @FXML
    private Button btnLogout;

    @FXML
    private StackPane contentArea;
    @FXML
    private FlowPane recommendedMoviesContainer;
    @FXML
    private FlowPane trendingMoviesContainer;
    @FXML
    private Button btnCategoryAction;
    @FXML
    private Button btnCategoryComedy;
    @FXML
    private Button btnCategoryDrama;
    @FXML
    private Button btnCategoryScienceFiction;
    @FXML
    private Button btnCategoryHorror;
    @FXML
    private Button btnCategoryRomance;
    @FXML
    private Button btnCategoryDocumentary;
    @FXML
    private Button btnCategoryAnimation;
    @FXML
    private Label lblTitle;
    @FXML
    private Label lblSubTitle;

    private Users loggedInUser;
    private RecommenderEngine recommenderEngine = new RecommenderEngine();
    private String currentCategory = null;

    @FXML
    public void initialize() {
        loadLogo();
        setupEventHandlers();
        updateUserInterface();
        Platform.runLater(() -> {
            Stage stage = (Stage) imgLogo.getScene().getWindow();
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            double initialWidth = Math.min(1000, bounds.getWidth() * 0.8);
            double initialHeight = Math.min(700, bounds.getHeight() * 0.8);
            stage.setWidth(initialWidth);
            stage.setHeight(initialHeight);
            double centerX = bounds.getMinX() + (bounds.getWidth() - initialWidth) / 2;
            double centerY = bounds.getMinY() + (bounds.getHeight() - initialHeight) / 2;
            stage.setX(centerX);
            stage.setY(centerY);
            categoriesPane.setExpanded(false);
        });
    }

    public void setLoggedInUser(Users user) {
        this.loggedInUser = user;
        updateUserInterface();
    }

    private void updateUserInterface() {
        if (loggedInUser != null) {
            lblUserName.setText(loggedInUser.getName());
            btnFavorites.setVisible(true);
            btnWatchLater.setVisible(true);
            btnHistory.setVisible(true);
        } else {
            lblUserName.setText("Invitado");
            btnFavorites.setVisible(false);
            btnWatchLater.setVisible(false);
            btnHistory.setVisible(false);
        }
        loadRecommendedMovies();
        loadTrendingMovies();
    }

    private void loadLogo() {
        try {
            Image logoImage = null;
            InputStream logoStream = getClass().getResourceAsStream("/com/dialca/recommender/assets/logo.png");
            if (logoStream != null) {
                logoImage = new Image(logoStream);
            } else {
                try {
                    String relativeLogoPath = "./src/com/dialca/recommender/assets/logo.png";
                    logoImage = new Image(new FileInputStream(relativeLogoPath));
                } catch (Exception e) {
                    System.err.println("No se pudo cargar el logo: " + e.getMessage());
                }
            }

            if (logoImage != null) {
                imgLogo.setImage(logoImage);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar logo: " + e.getMessage());
        }
    }

    private void setupEventHandlers() {
        btnSearch.setOnAction(event -> handleSearch());

        btnHome.setOnAction(event -> showHome());
        btnNewReleases.setOnAction(event -> showNewReleases());
        btnFavorites.setOnAction(event -> showFavorites());
        btnWatchLater.setOnAction(event -> showWatchLater());
        btnHistory.setOnAction(event -> showHistory());
        btnLogout.setOnAction(event -> handleLogout());
    }

    private void handleSearch() {
        String searchQuery = txtSearch.getText().trim();
        if (!searchQuery.isEmpty()) {
            System.out.println("Buscando: " + searchQuery);
            // La implementación de búsqueda se realizará más adelante
        }
    }

    private void handleLogout() {
        Stage currentStage = (Stage) btnLogout.getScene().getWindow();
        currentStage.close();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dialca/recommender/ui/view/HomeView.fxml"));
            Parent homeRoot = loader.load();

            Stage homeStage = new Stage();
            homeStage.setTitle("DialcaFlix");
            homeStage.setScene(new Scene(homeRoot));
            homeStage.setResizable(false);
            homeStage.show();
        } catch (Exception e) {
            System.err.println("Error al cargar la vista de Home: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadRecommendedMovies() {
        recommendedMoviesContainer.getChildren().clear();
        List<Movie> recommendedMovies;
        if (loggedInUser != null) {
            recommendedMovies = recommenderEngine.getRecommendations(loggedInUser);
        } else {
            recommendedMovies = recommenderEngine.getBasicRecommendations();
        }
        List<Movie> moviesToShow = recommendedMovies.stream()
                .limit(16)
                .collect(Collectors.toList());
        loadMoviesIntoContainer(moviesToShow, recommendedMoviesContainer);
    }

    private void loadTrendingMovies() {
        trendingMoviesContainer.getChildren().clear();
        RatingController ratingController = new RatingController();
        List<Movie> trendingMovies = ratingController.getTopRatedMovies(12);
        if (trendingMovies.size() < 6) {
            MovieController movieController = new MovieController();
            List<Movie> additionalMovies = movieController.getAllMovies().stream()
                    .filter(movie -> !trendingMovies.contains(movie))
                    .limit(6 - trendingMovies.size())
                    .collect(Collectors.toList());
            trendingMovies.addAll(additionalMovies);
        }
        List<Movie> moviesToShow = trendingMovies.stream()
                .limit(16)
                .collect(Collectors.toList());
        loadMoviesIntoContainer(moviesToShow, trendingMoviesContainer);
    }
    private void loadMoviesIntoContainer(List<Movie> movies, FlowPane container) {
        container.getChildren().clear();
        for (Movie movie : movies) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dialca/recommender/ui/view/components/MovieCard.fxml"));
                Parent movieCard = loader.load();
                MovieCardController controller = loader.getController();
                controller.setMovie(movie);
                controller.setOnWatchClicked(movie, loggedInUser);
                container.getChildren().add(movieCard);
            } catch (Exception e) {
                System.err.println("Error al cargar MovieCard: " + e.getMessage());
            }
        }
    }
    private void updateCategoryButtons(String selectedCategory) {
        List<Button> categoryButtons = Arrays.asList(
            btnCategoryAction, btnCategoryComedy, btnCategoryDrama, btnCategoryScienceFiction,
            btnCategoryHorror, btnCategoryRomance, btnCategoryDocumentary, btnCategoryAnimation
        );
        
        // Resetear todos los botones a su estilo normal
        for (Button btn : categoryButtons) {
            btn.getStyleClass().remove("category-button-selected");
            if (!btn.getStyleClass().contains("category-button")) {
                btn.getStyleClass().add("category-button");
            }
        }
        
        // Resaltar el botón seleccionado
        if (selectedCategory != null) {
            categoryButtons.forEach(btn -> {
                if (btn.getText().equals(selectedCategory)) {
                    btn.getStyleClass().remove("category-button");
                    btn.getStyleClass().add("category-button-selected");
                }
            });
        }
    }

    // Modificar el método handleCategorySelect para usar esta función
    @FXML
    private void handleCategorySelect(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        String category = sourceButton.getText();
        
        currentCategory = category;
        updateCategoryButtons(category);
        loadMoviesByCategory(category);
        lblTitle.setText("Categoría: " + category);
        lblSubTitle.setText("Explora las mejores películas de " + category);
    }
    private void loadMoviesByCategory(String category) {
        recommendedMoviesContainer.getChildren().clear();
        MovieController movieController = new MovieController();
        List<Movie> movies = movieController.getMoviesByGenre(category);
        if (movies.isEmpty()) {
            Label noMoviesLabel = new Label("No hay películas en esta categoría.");
            noMoviesLabel.getStyleClass().add("placeholder-text");
            recommendedMoviesContainer.getChildren().add(noMoviesLabel);
        } else {
            loadMoviesIntoContainer(movies, recommendedMoviesContainer);
        }
    }

    private void showHome() {
        currentCategory = null;
        updateCategoryButtons(null);
        updateUserInterface();
        lblTitle.setText("Bienvenido a DialcaFlix!");
        lblSubTitle.setText("Explora nuestras recomendaciones personalizadas y películas populares.");
    }

    private void showNewReleases() {
        lblTitle.setText("Nuevos Lanzamientos");
        lblSubTitle.setText("Descubre las últimas películas lanzadas.");
        recommendedMoviesContainer.getChildren().clear();
        MovieController movieController = new MovieController();
        List<Movie> newReleases = movieController.getNewReleases(16);
        if (newReleases != null && !newReleases.isEmpty()) {
            loadMoviesIntoContainer(newReleases, recommendedMoviesContainer);
        } else {
            Label noMoviesLabel = new Label("No hay nuevos lanzamientos disponibles.");
            noMoviesLabel.getStyleClass().add("placeholder-text");
            recommendedMoviesContainer.getChildren().add(noMoviesLabel);
        }
    }

    private void showFavorites() {
        // Implementación futura - Mostrar favoritos del usuario
    }

    private void showWatchLater() {
        // Implementación futura - Mostrar películas para ver más tarde
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Funcionalidad en desarrollo");
        alert.setHeaderText(null);
        alert.setContentText("La funcionalidad 'Ver más tarde' aún no está implementada.");
        alert.showAndWait();
    }

    private void showHistory() {
        // Implementación futura - Mostrar historial de visualización del usuario
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Funcionalidad en desarrollo");
        alert.setHeaderText(null);
        alert.setContentText("La funcionalidad 'Historial' aún no está implementada.");
        alert.showAndWait();
    }
}
