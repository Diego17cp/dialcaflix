package com.dialca.recommender.ui.controller;

import com.dialca.recommender.dao.UsersDao;
import com.dialca.recommender.model.Users;
import com.dialca.recommender.controller.UserController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.io.InputStream;
import java.io.FileInputStream;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class LoginController {

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnCancel;

    @FXML
    private Hyperlink linkForgotPassword;

    @FXML
    private ImageView imgLogo;

    @FXML
    private Hyperlink linkRegister;

    private UsersDao userDao = new UsersDao();
    private Users loggedInUser;
    private Stage homeStage;

    @FXML
    public void initialize() {
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
            System.err.println("Error al inicializar: " + e.getMessage());
        }
        setupEventHandlers();
    }
    public void setHomeStage(Stage homeStage) {
        this.homeStage = homeStage;
    }
    private void setupEventHandlers() {
        btnLogin.setOnAction(event -> handleLogin());
        btnCancel.setOnAction(event -> handleCancel());
        linkForgotPassword.setOnAction(event -> handleForgotPassword());
        linkRegister.setOnAction(event -> navigateToRegister());
    }

    private void handleLogin() {
        if (!validateForm()) {
            return;
        }
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText();

        UserController userController = new UserController();
        Users authenticatedUser = userController.login(email, password);

        if (authenticatedUser != null) {
            this.loggedInUser = authenticatedUser;
            closeWindow();
            if (homeStage != null) {
                homeStage.close();
            }
            navigateToMainPage();
        } else
            showAlert("Error de inicio de sesión", "Email o contraseña incorrectos.");
    }

    private boolean validateForm() {
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Campos incompletos", "Por favor complete todos los campos del formulario.");
            return false;
        }
        if (!email.matches("^[\\w-\\.]+@[\\w-]+\\.[a-zA-Z]{2,}$")) {
            showAlert("Email inválido", "Por favor ingrese un email válido.");
            return false;
        }
        if (password.length() < 6) {
            showAlert("Contraseña débil", "La contraseña debe tener al menos 6 caracteres.");
            return false;
        }
        return true;
    }

    private void handleCancel() {
        closeWindow();
    }
    private void navigateToMainPage() {
        try {
            closeWindow();
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/dialca/recommender/ui/view/MainView.fxml"));
            Parent mainRoot = loader.load();
            MainViewController mainController = loader.getController();
            mainController.setLoggedInUser(loggedInUser);
            Stage mainStage = new Stage();
            mainStage.initStyle(StageStyle.DECORATED);
            mainStage.setTitle("DialcaFlix - Página Principal");
            mainStage.setScene(new Scene(mainRoot));
            mainStage.setResizable(true);
            mainStage.show();
        } catch (Exception e) {
            System.err.println("Error al cargar la vista principal: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "No se pudo abrir la ventana principal.");
        }
    }
    private void navigateToRegister() {
        try {
            closeWindow();
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/dialca/recommender/ui/view/RegisterView.fxml"));
            Parent registerRoot = loader.load();

            Stage registerStage = new Stage();
            registerStage.initModality(Modality.APPLICATION_MODAL);
            registerStage.initStyle(StageStyle.DECORATED);
            registerStage.setTitle("Registro - DialcaFlix");
            registerStage.setScene(new Scene(registerRoot));
            registerStage.setResizable(false);
            registerStage.show();
        } catch (Exception e) {
            System.err.println("Error al cargar la vista de registro: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "No se pudo abrir la ventana de registro.");
        }
    }

    private void handleForgotPassword() {
        showAlert("Recuperar contraseña", "Se enviará un correo con instrucciones para recuperar tu contraseña.");
    }

    private void closeWindow() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public Users getLoggedInUser() {
        return loggedInUser;
    }
}
