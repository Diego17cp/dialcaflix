package com.dialca.recommender.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.InputStream;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import com.dialca.recommender.model.Users;
import com.dialca.recommender.controller.UserController;

public class RegisterController {

    @FXML
    private TextField txtFullName;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private CheckBox chkTerms;

    @FXML
    private Button btnRegister;

    @FXML
    private Button btnCancel;

    @FXML
    private Hyperlink linkLogin;

    @FXML
    private ImageView imgLogo;

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

    private void setupEventHandlers() {
        btnRegister.setOnAction(event -> handleRegister());
        btnCancel.setOnAction(event -> handleCancel());
        linkLogin.setOnAction(event -> navigateToLogin());
    }

    private void handleRegister() {
        if (!validateForm()) {
            return;
        }
        try {
            String fullName = txtFullName.getText().trim();
            String email = txtEmail.getText().trim();
            String password = txtPassword.getText();

            UserController userController = new UserController();
            boolean newUser = userController.register(fullName, email, password);

            if (newUser) {
                showAlert(Alert.AlertType.INFORMATION, "Registro exitoso",
                        "¡Bienvenido a DialcaFlix, " + fullName + "!\n" +
                                "Tu cuenta ha sido creada correctamente.");
                closeWindow();
                navigateToLogin();
            } else {
                showAlert(Alert.AlertType.WARNING, "Usuario ya registrado",
                        "El email ingresado ya está asociado a una cuenta existente.");
                return;
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error de registro",
                    "No se pudo completar el registro: " + e.getMessage());
        }
    }

    private boolean validateForm() {
        String fullName = txtFullName.getText().trim();
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();
        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Campos incompletos",
                    "Por favor completa todos los campos del formulario.");
            return false;
        }
        if (!fullName.matches("^[a-zA-Z\\s]+$")) {
            showAlert(Alert.AlertType.WARNING, "Nombre inválido",
                "El nombre solo debe contener letras y espacios.");
            return false;
        }

        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showAlert(Alert.AlertType.WARNING, "Email inválido",
                    "Por favor ingresa un email válido.");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.WARNING, "Contraseñas no coinciden",
                    "Las contraseñas ingresadas no coinciden.");
            return false;
        }
        if (password.length() < 6) {
            showAlert(Alert.AlertType.WARNING, "Contraseña débil",
                    "La contraseña debe tener al menos 6 caracteres.");
            return false;
        }
        if (!chkTerms.isSelected()) {
            showAlert(Alert.AlertType.WARNING, "Términos no aceptados",
                    "Debes aceptar los términos y condiciones para continuar.");
            return false;
        }

        return true;
    }

    private void handleCancel() {
        closeWindow();
    }

    private void navigateToLogin() {
        try {
            closeWindow();
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/dialca/recommender/ui/view/LoginView.fxml"));
            Parent loginRoot = loader.load();

            Stage loginStage = new Stage();
            loginStage.setTitle("Iniciar Sesión - DialcaFlix");
            loginStage.setScene(new Scene(loginRoot));
            loginStage.setResizable(false);
            loginStage.show();
        } catch (Exception e) {
            System.err.println("Error al abrir la ventana de login: " + e.getMessage());
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
