package view;

import controller.UserController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegisterView extends BorderPane {
    Stage stage;
    Scene scene;
    GridPane formContainer;
    TextField usernameField, phonenumberField;
    PasswordField passwordField;
    TextArea addressField;
    ComboBox<String> roleBox;
    CheckBox checkBox;
    Label usernameLbl, passwordLbl, addressLbl, roleLbl, registerLbl, phonenumberLbl, errorLbl, intentloginLbl;
    Button registerBtn;

    public void init() {
        usernameField = new TextField();
        passwordField = new PasswordField();
        phonenumberField = new TextField();
        addressField = new TextArea();
        roleBox = new ComboBox<>();
        checkBox = new CheckBox("I agree with the terms and conditions");

        usernameLbl = new Label("Username:");
        passwordLbl = new Label("Password:");
        phonenumberLbl = new Label("Phone Number:");
        errorLbl = new Label();
        errorLbl.setStyle("-fx-text-fill: red;");
        addressLbl = new Label("Address:");
        roleLbl = new Label("Role:");
        intentloginLbl = new Label("Already have an account? Login here.");

        registerBtn = new Button("Register");

        formContainer = new GridPane();
    }

    public void initializeForm() {
        // Add items to ComboBox
        roleBox.getItems().addAll("Buyer", "Seller");
        roleBox.getSelectionModel().selectFirst();

        // Style GridPane for perfect symmetry
        formContainer.setHgap(20); // Horizontal gap
        formContainer.setVgap(20); // Vertical gap
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(20, 20, 20, 20)); // Symmetric padding

        // Consistent width for input fields
        double inputWidth = 350;

        // Add components with consistent positioning
        formContainer.add(usernameLbl, 0, 0);
        formContainer.add(usernameField, 1, 0);
        usernameField.setPrefWidth(inputWidth);

        formContainer.add(passwordLbl, 0, 1);
        formContainer.add(passwordField, 1, 1);
        passwordField.setPrefWidth(inputWidth);

        formContainer.add(phonenumberLbl, 0, 2);
        formContainer.add(phonenumberField, 1, 2);
        phonenumberField.setPrefWidth(inputWidth);

        formContainer.add(addressLbl, 0, 3);
        formContainer.add(addressField, 1, 3);
        addressField.setPrefWidth(inputWidth);
        addressField.setPrefHeight(120); // Slightly increased height

        formContainer.add(roleLbl, 0, 4);
        formContainer.add(roleBox, 1, 4);
        roleBox.setPrefWidth(inputWidth);

        // Add the checkbox in its own row, spanning two columns
        formContainer.add(checkBox, 0, 5, 2, 1);
        GridPane.setMargin(checkBox, new Insets(10, 0, 0, 0)); // Add some space above the checkbox
        checkBox.setAlignment(Pos.CENTER_LEFT);

        // Add the error label below the checkbox
        formContainer.add(errorLbl, 0, 6, 2, 1); // Span across two columns
        errorLbl.setStyle("-fx-text-fill: red;");
    }

    public void initializeStage() {
        // Center the GridPane in the BorderPane
        this.setCenter(formContainer);

        // Style top label
        registerLbl = new Label("Register");
        registerLbl.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        VBox topContainer = new VBox(registerLbl);
        topContainer.setAlignment(Pos.CENTER);
        topContainer.setPadding(new Insets(20, 0, 0, 0)); // Reduced vertical padding
        this.setTop(topContainer);

        // Style and add intentloginLbl
        intentloginLbl.setStyle("-fx-text-fill: blue; -fx-underline: true; -fx-cursor: hand;");

        // Add button and intentloginLbl at the bottom, centered with symmetrical padding
        VBox bottomContainer = new VBox(10, registerBtn, intentloginLbl);
        bottomContainer.setAlignment(Pos.CENTER);
        bottomContainer.setPadding(new Insets(20, 10, 40, 10));
        this.setBottom(bottomContainer);

        // Style button for consistency
        registerBtn.setStyle(
            "-fx-background-color: #0078d7; " +
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-font-size: 14px;"
        );
        registerBtn.setPrefWidth(200); // Wider button
    }

    public void setEvent() {
        intentloginLbl.setOnMouseClicked(e -> new LoginView(stage)); // Navigate to LoginView
        registerBtn.setOnAction(e -> {
            // Collect user inputs
            String username = usernameField.getText();
            String password = passwordField.getText();
            String phoneNumber = phonenumberField.getText();
            String address = addressField.getText();
            String role = roleBox.getValue();
            boolean termsAgreed = checkBox.isSelected();

            // Pass inputs to the controller
            UserController userController = new UserController();
            String message = userController.registerUser(username, password, phoneNumber, address, role, termsAgreed);

            // Display the message
            errorLbl.setText(message); // Replace with proper alert/dialog in a real application
            
            if(!message.equals("Please fill out all fields and agree to the terms.")) {
            	new LoginView(stage);
            }
            
        });
    }

    public RegisterView(Stage stage) {
        this.stage = stage; // Ensure stage is assigned first
        init();
        initializeForm();
        initializeStage();
        setEvent();
        // Create scene with centered content
        scene = new Scene(this, 900, 750); // Adjusted size for better proportions
        stage.setScene(scene);
        stage.setTitle("User Registration");
        stage.centerOnScreen(); // Center the stage on screen
        stage.show();
    }
}