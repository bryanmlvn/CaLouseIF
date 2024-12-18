package view;

import java.sql.ResultSet;
import java.sql.SQLException;

import controller.ItemController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Item;
import repository.ItemRepository;
import util.Connect;

public class AdminView extends BorderPane {
	private Stage stage;
    private Scene scene;
    private TableView<Item> table;
    private VBox box;
    private HBox btnBox;
    private GridPane grid;
    private Button logoutBtn, approveBtn, declineBtn;
    private String tempId = null;
    private ItemController itemController;
    private Label errorLbl;
    
    public AdminView(Stage stage) {
    	this.stage = stage;
    	itemController = new ItemController(new ItemRepository());
    	init();
        initPosition();
        initializeStage(stage);
        setEventHandlers();
    }
    
    private void init() {
    	grid = new GridPane();
        box = new VBox();
        btnBox = new HBox();
        scene = new Scene(this, 900, 750);
        
        errorLbl = new Label();
        errorLbl.setTextFill(Color.RED);
        
        logoutBtn = new Button("Log out");
        approveBtn = new Button("Approve");
        declineBtn = new Button("Decline");
        
        table = new TableView<>();
        setTableColumns();
        
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setSpacing(10);
        btnBox.getChildren().addAll(approveBtn, declineBtn, logoutBtn);
        
    }
    private void initializeStage(Stage stage) {
        stage.setScene(scene);
        stage.setTitle("Admin Page");
        stage.centerOnScreen();
        stage.show();
    }
    
    private void setTableColumns() {
    	TableColumn<Item, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setMinWidth(150);

        TableColumn<Item, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryCol.setMinWidth(150);

        TableColumn<Item, String> sizeCol = new TableColumn<>("Size");
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
        sizeCol.setMinWidth(150);

        TableColumn<Item, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setMinWidth(150);
        
        TableColumn<Item, String> offerStatusCol = new TableColumn<>("Status");
        offerStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        offerStatusCol.setMinWidth(150);
        
        TableColumn<Item, String> statusReasonCol = new TableColumn<>("Reason");
        statusReasonCol.setCellValueFactory(new PropertyValueFactory<>("statusReason"));
        statusReasonCol.setMinWidth(150);
        
        table.getColumns().addAll(nameCol, categoryCol, sizeCol, priceCol, offerStatusCol, statusReasonCol);
        refreshTable();
    }
    
    private void refreshTable() {
        table.getItems().clear();
        ResultSet rs = itemController.viewItems();  // Get items from the controller
        try {
            while (rs.next()) {
                Item item = new Item(
                    rs.getString("Item_id"),
                    rs.getString("Item_name"),
                    rs.getString("Item_size"),
                    rs.getDouble("Item_price"),
                    rs.getString("Item_category"),
                    rs.getString("Item_status"),
                    rs.getString("Item_wishlist"),
                    rs.getString("Item_offer_status"),
                    rs.getString("seller_id"),
                    rs.getDouble("Item_offer_price"),
                    rs.getString("Item_status_reason"),
                    rs.getString(("Item_last_offerror"))
                    
                );
                table.getItems().add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        errorLbl.setText("");
    }
    
    private void initPosition() {
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Horizontal gap between components
        grid.setVgap(10);

        this.setTop(table);

        // Center buttons at the bottom
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setSpacing(10);
        btnBox.setPadding(new Insets(10, 10, 10, 10)); // Padding around the buttons

        // Add buttons and error label to a VBox
        VBox bottomBox = new VBox(10); // Vertical spacing of 10
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(10, 10, 30, 10)); // Additional padding
        bottomBox.getChildren().addAll(btnBox, errorLbl); // Add btnBox and errorLbl

        this.setBottom(bottomBox); // Set VBox as the bottom component
        BorderPane.setAlignment(bottomBox, Pos.CENTER);
    }
    
    private void setEventHandlers() {
        logoutBtn.setOnAction(e -> handleLogout());
        approveBtn.setOnAction(e -> handleApprove());
        declineBtn.setOnAction(e -> handleDecline());
        table.setOnMouseClicked(e -> handleTableSelection());
        
    }
    
    private void handleLogout() {
    	new LoginView(stage);
    }
    private void handleTableSelection() {
        Item selectedItem = table.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            tempId = selectedItem.getId();  // Store the selected item's Item_id
        }
    }
    
    private void handleApprove() {
        Item selectedItem = table.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            errorLbl.setText("Please select an item to approve.");
            return;
        }

        try {
            // Call the controller to approve the item
            itemController.approveItem(selectedItem.getId());

            // Refresh the table to reflect changes
            refreshTable();

            // Provide success feedback
            errorLbl.setTextFill(Color.GREEN);
            errorLbl.setText("Item approved successfully.");
        } catch (Exception e) {
            errorLbl.setTextFill(Color.RED);
            errorLbl.setText("Failed to approve the item.");
            e.printStackTrace();
        }
    }
    
    private void handleDecline() {
        // Check if an item is selected
        Item selectedItem = table.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            errorLbl.setText("Please select an item to decline.");
            return;
        }

        // Clear any previous error messages
        errorLbl.setText("");

        // Create a new Stage for the dialog
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Decline Offer");

        // Create the dialog components
        Label reasonLabel = new Label("Reason:");
        TextField reasonField = new TextField();
        Button confirmButton = new Button("Confirm");
        Button cancelButton = new Button("Cancel");

        // Create a VBox to hold the components
        VBox dialogLayout = new VBox(10);
        dialogLayout.setPadding(new Insets(10));
        dialogLayout.setAlignment(Pos.CENTER);

        // Add components to the layout
        HBox buttonBox = new HBox(10, confirmButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);
        dialogLayout.getChildren().addAll(reasonLabel, reasonField, buttonBox);

        // Disable confirm button initially
        confirmButton.setDisable(true);

        // Enable confirm button only when text is entered
        reasonField.textProperty().addListener((observable, oldValue, newValue) -> {
            confirmButton.setDisable(newValue.trim().isEmpty());
        });

        // Set the confirm button action
        confirmButton.setOnAction(e -> {
            String reason = reasonField.getText().trim();
            if (!reason.isEmpty()) {
                try {
                    // Call the controller to update the item status
                    itemController.declineItem(selectedItem.getId(), reason);

                    // Refresh the table to reflect changes
                    refreshTable();

                    // Provide user feedback
                    errorLbl.setTextFill(Color.GREEN);
                    errorLbl.setText("Item declined successfully.");

                    dialogStage.close();
                } catch (Exception ex) {
                    errorLbl.setTextFill(Color.RED);
                    errorLbl.setText("Failed to decline the item.");
                    ex.printStackTrace();
                }
            }
        });

        // Set the cancel button action
        cancelButton.setOnAction(e -> dialogStage.close());

        // Set up the scene and show the dialog
        Scene dialogScene = new Scene(dialogLayout, 300, 150);
        dialogStage.setScene(dialogScene);
        dialogStage.showAndWait();
    }

}
