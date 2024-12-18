package view;

import controller.UserController;
import model.User;
import controller.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
//
public class LoginView extends BorderPane {
    Scene scene;
    GridPane formContainer;
    TextField usernameField;
    PasswordField passwordField;
    Label usernameLbl, passwordLbl, loginLbl, intentRegisterLbl, errorLbl;
    Button loginBtn;

    public void init() {
        // Initialize components
        usernameField = new TextField();
        passwordField = new PasswordField();

        usernameLbl = new Label("Username:");
        passwordLbl = new Label("Password:");
        loginLbl = new Label("Login");
        intentRegisterLbl = new Label("Don't have an account? Register here.");
        loginBtn = new Button("Login");
        errorLbl = new Label();  // Initialize the error label

        // Style the label for registration navigation
        intentRegisterLbl.setStyle("-fx-text-fill: blue; -fx-underline: true; -fx-cursor: hand;");
        errorLbl.setStyle("-fx-text-fill: red;"); // Style the error message
    }

    public void initializeForm() {
        formContainer = new GridPane();
        formContainer.setHgap(20); // Horizontal spacing between columns
        formContainer.setVgap(20); // Vertical spacing between rows
        formContainer.setAlignment(Pos.CENTER); // Center the form
        formContainer.setPadding(new Insets(20, 20, 20, 20)); // Padding around the form

        // Add components to the form
        double inputWidth = 300;

        formContainer.add(usernameLbl, 0, 0);
        formContainer.add(usernameField, 1, 0);
        usernameField.setPrefWidth(inputWidth);

        formContainer.add(passwordLbl, 0, 1);
        formContainer.add(passwordField, 1, 1);
        passwordField.setPrefWidth(inputWidth);

        formContainer.add(loginBtn, 1, 2);
        loginBtn.setPrefWidth(100); // Smaller width for button
        loginBtn.setAlignment(Pos.CENTER);

        // Add error label below the password field
        formContainer.add(errorLbl, 1, 3); // Positioning errorLbl below the password field
    }

    public void initializeStage(Stage stage) {
        // Style top label
        loginLbl.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        VBox topContainer = new VBox(loginLbl);
        topContainer.setAlignment(Pos.CENTER);
        topContainer.setPadding(new Insets(20, 0, 0, 0)); // Reduced vertical padding
        this.setTop(topContainer);

        // Add form to the center
        this.setCenter(formContainer);

        // Add registration navigation at the bottom
        VBox bottomContainer = new VBox(10, loginBtn, intentRegisterLbl);
        bottomContainer.setAlignment(Pos.CENTER);
        bottomContainer.setPadding(new Insets(20, 10, 40, 10));
        this.setBottom(bottomContainer);
        loginBtn.setStyle(
                "-fx-background-color: #0078d7; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 14px;"
            );
        loginBtn.setPrefWidth(200); // Wider button

        // Set up the scene
        scene = new Scene(this, 900, 750); // Adjusted size for better proportions
        stage.setScene(scene);
        stage.setTitle("User Login");
        stage.centerOnScreen();
        stage.show();
    }

    public void setEvent(Stage stage) {
        intentRegisterLbl.setOnMouseClicked(e -> {
            // Navigate to RegisterView
            new RegisterView(stage);
        });

        loginBtn.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            // Pass inputs to the controller
            UserController userController = new UserController();
            User user = userController.loginUser(username, password);

            if (user != null) { // Successful login
                if ("Seller".equals(user.getRole())) {
                    // Navigate to SellerView
                    new SellerView(stage, user);
                } else if ("Buyer".equals(user.getRole())) {
                     
                    new BuyerView(stage, user);
                } 
                else if("admin".equals(user.getRole())) {
                	new AdminView(stage);
                }
                else {
                    errorLbl.setText("Unknown role. Please contact support.");
                }
            } else {
                // Display the error message
                errorLbl.setText("Invalid username or password.");
            }
        });
    }

    public LoginView(Stage stage) {
        init();
        initializeForm();
        initializeStage(stage);
        setEvent(stage);
    }
}