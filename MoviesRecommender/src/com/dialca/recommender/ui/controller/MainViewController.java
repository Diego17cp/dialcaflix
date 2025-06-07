package com.dialca.recommender.ui.controller;

import com.dialca.recommender.model.*;
import com.dialca.recommender.controller.MovieController;
import com.dialca.recommender.controller.RatingController;
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
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressIndicator;

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
    @FXML
    private StackPane loadingPane;

    private Users loggedInUser;
    private RecommenderEngine recommenderEngine = new RecommenderEngine();
    private String currentCategory = null;

    @FXML
    public void initialize() {
        if (loadingPane == null) {
            loadingPane = new StackPane();
            loadingPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
            ProgressIndicator progressIndicator = new ProgressIndicator();
            progressIndicator.setPrefSize(100, 100);
            progressIndicator.setStyle("-fx-progress-color: #e50914;");
            Label loadingLabel = new Label("Cargando contenido...");
            loadingLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
            VBox loadingContent = new VBox(15, progressIndicator, loadingLabel);
            loadingContent.setAlignment(Pos.CENTER);
            loadingPane.getChildren().add(loadingContent);
            loadingPane.setAlignment(Pos.CENTER);
            Platform.runLater(() -> {
                if (contentArea != null) {
                    contentArea.getChildren().add(loadingPane);
                    loadingPane.toFront();
                    loadingPane.setPrefSize(contentArea.getWidth(), contentArea.getHeight());
                }
            });
        }
        loadLogo();
        setupEventHandlers();
        setupSearchField();
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
        if (loadingPane == null) {
            System.err.println("Loading pane is null, cannot set user interface.");
        } else {
            loadingPane.setVisible(false);
        }
        if (user != null) {
            lblUserName.setText(user.getName());
            btnFavorites.setVisible(true);
            btnWatchLater.setVisible(true);
            btnHistory.setVisible(true);
        } else {
            lblUserName.setText("Invitado");
            btnFavorites.setVisible(false);
            btnWatchLater.setVisible(false);
            btnHistory.setVisible(false);
        }
        loadUIAsync();
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
            recommendedMoviesContainer.getChildren().clear();
            trendingMoviesContainer.getChildren().clear();
            lblTitle.setText("Resultados de búsqueda: \"" + searchQuery + "\"");
            lblSubTitle.setText("Explora las películas que coinciden con tu búsqueda.");
            MovieController movieController = new MovieController();
            List<Movie> searchResults = movieController.searchMovies(searchQuery);
            List<String> possibleGenres = findMatchingGenres(searchQuery);
            List<Movie> genreResults = new ArrayList<>();
            if (!possibleGenres.isEmpty()) {
                String bestMatchGenre = possibleGenres.get(0);
                genreResults = movieController.getMoviesByGenre(bestMatchGenre);
                genreResults = genreResults.stream()
                        .filter(movie -> !searchResults.contains(movie))
                        .collect(Collectors.toList());
            }
            List<Movie> combinedResults = new ArrayList<>(searchResults);
            combinedResults.addAll(genreResults);
            if (combinedResults.isEmpty()) {
                Label noResultsLabel = new Label("No se encontraron resultados para tu búsqueda.");
                noResultsLabel.getStyleClass().add("placeholder-text");
                recommendedMoviesContainer.getChildren().add(noResultsLabel);
            } else {
                loadMoviesIntoContainer(combinedResults, recommendedMoviesContainer);
                Label resultsLabel = new Label("Se encontraron " + combinedResults.size() + " resultados.");
                resultsLabel.getStyleClass().add("results-label");
                recommendedMoviesContainer.getChildren().add(resultsLabel);
            }
        }
    }

    private List<String> findMatchingGenres(String query) {
        final String queryLower = query.toLowerCase();
        List<String> allGenres = Arrays.asList("Acción", "Comedia", "Drama", "Ciencia Ficción",
                "Terror", "Romance", "Documental", "Animación");
        List<String> exactMatches = allGenres.stream()
                .filter(genre -> genre.toLowerCase().equals(queryLower))
                .collect(Collectors.toList());
        if (!exactMatches.isEmpty())
            return exactMatches;
        return allGenres.stream()
                .filter(genre -> genre.toLowerCase().contains(queryLower) ||
                        queryLower.contains(genre.toLowerCase()))
                .collect(Collectors.toList());
    }

    private void setupSearchField() {
        txtSearch.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                handleSearch();
            }
        });
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
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/com/dialca/recommender/ui/view/components/MovieCard.fxml"));
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
                btnCategoryHorror, btnCategoryRomance, btnCategoryDocumentary, btnCategoryAnimation);

        for (Button btn : categoryButtons) {
            btn.getStyleClass().remove("category-button-selected");
            if (!btn.getStyleClass().contains("category-button")) {
                btn.getStyleClass().add("category-button");
            }
        }
        if (selectedCategory != null) {
            categoryButtons.forEach(btn -> {
                if (btn.getText().equals(selectedCategory)) {
                    btn.getStyleClass().remove("category-button");
                    btn.getStyleClass().add("category-button-selected");
                }
            });
        }
    }

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
        lblTitle.setText("Mis Películas Calificadas");
        lblSubTitle.setText("Películas que has calificado");
        recommendedMoviesContainer.getChildren().clear();
        trendingMoviesContainer.getChildren().clear();

        if (loggedInUser != null) {
            RatingController ratingController = new RatingController();
            List<Rating> userRatings = ratingController.getRatingsByUser(loggedInUser);

            if (userRatings != null && !userRatings.isEmpty()) {
                List<Movie> ratedMovies = userRatings.stream()
                        .map(Rating::getMovie)
                        .distinct()
                        .collect(Collectors.toList());
                loadMoviesIntoContainer(ratedMovies, recommendedMoviesContainer);
                Label infoLabel = new Label("Has calificado " + ratedMovies.size() +
                        " películas. Tus calificaciones ayudan a mejorar tus recomendaciones.");
                infoLabel.getStyleClass().add("info-text");
                infoLabel.setWrapText(true);
                recommendedMoviesContainer.getChildren().add(0, infoLabel);
            } else {
                Label noMoviesLabel = new Label("Aún no has calificado películas.\n" +
                        "Calificar películas nos permite ofrecerte mejores recomendaciones.");
                noMoviesLabel.getStyleClass().add("placeholder-text");
                noMoviesLabel.setWrapText(true);
                recommendedMoviesContainer.getChildren().add(noMoviesLabel);
            }
        } else {
            Label loginPromptLabel = new Label("Inicia sesión para ver tus películas calificadas");
            loginPromptLabel.getStyleClass().add("placeholder-text");
            recommendedMoviesContainer.getChildren().add(loginPromptLabel);
        }
    }

    private void showWatchLater() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Funcionalidad en desarrollo");
        alert.setHeaderText(null);
        alert.setContentText("La funcionalidad 'Ver más tarde' aún no está implementada.");
        alert.showAndWait();
    }

    private void showHistory() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Funcionalidad en desarrollo");
        alert.setHeaderText(null);
        alert.setContentText("La funcionalidad 'Historial' aún no está implementada.");
        alert.showAndWait();
    }
    private void loadUIAsync() {
        showLoading(true);
        Thread dataLoadingThread = new Thread(() -> {
            try {
                List<Movie> recommendedMovies;
                List<Movie> trendingMovies;
                
                try {
                    if (loggedInUser != null) {
                        recommendedMovies = recommenderEngine.getRecommendations(loggedInUser);
                    } else {
                        recommendedMovies = recommenderEngine.getBasicRecommendations();
                    }
                    RatingController ratingController = new RatingController();
                    trendingMovies = ratingController.getTopRatedMovies(12);
                    
                    // Actualizar la UI con los datos cargados
                    final List<Movie> finalRecommended = recommendedMovies;
                    final List<Movie> finalTrending = trendingMovies;
                    
                    Platform.runLater(() -> {
                        try {
                            updateUIWithLoadedData(finalRecommended, finalTrending);
                        } catch (Exception e) {
                            System.err.println("Error al actualizar la UI: " + e.getMessage());
                            e.printStackTrace();
                        } finally {
                            hideLoadingPane();
                        }
                    });
                } catch (Exception e) {
                    System.err.println("Error al cargar datos: " + e.getMessage());
                    e.printStackTrace();

                    Platform.runLater(this::hideLoadingPane);
                }
            } catch (Exception e) {
                System.err.println("Error crítico en el hilo de carga: " + e.getMessage());
                e.printStackTrace();
                Platform.runLater(this::hideLoadingPane);
            }
        });
        
        dataLoadingThread.setDaemon(true);
        dataLoadingThread.start();
    }
    private void hideLoadingPane() {
        if (loadingPane != null) {
            loadingPane.setVisible(false);
            loadingPane.setManaged(false);
            System.out.println("Panel de carga ocultado correctamente");
        } else {
            System.err.println("El panel de carga es nulo, no se puede ocultar");
        }
    }

    private void showLoading(boolean show) {
        if (loadingPane != null) {
            Platform.runLater(() -> {
                loadingPane.setVisible(show);
                loadingPane.setManaged(show);
            });
        }
    }

    private void updateUIWithLoadedData(List<Movie> recommendedMovies, List<Movie> trendingMovies) {
        List<Movie> moviesToShowRecommended = recommendedMovies.stream()
                .limit(16)
                .collect(Collectors.toList());
        loadMoviesIntoContainer(moviesToShowRecommended, recommendedMoviesContainer);
        List<Movie> processedTrendingMovies = new ArrayList<>(trendingMovies);
        if (trendingMovies.size() < 6) {
            MovieController movieController = new MovieController();
            List<Movie> additionalMovies = movieController.getAllMovies().stream()
                    .filter(movie -> !trendingMovies.contains(movie))
                    .limit(6 - trendingMovies.size())
                    .collect(Collectors.toList());
            processedTrendingMovies.addAll(additionalMovies);
        }
        List<Movie> moviesToShowTrending = processedTrendingMovies.stream()
                .limit(16)
                .collect(Collectors.toList());
        loadMoviesIntoContainer(moviesToShowTrending, trendingMoviesContainer);
    }
}
