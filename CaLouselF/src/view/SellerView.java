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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Item;
import model.User;
import repository.ItemRepository;

public class SellerView extends BorderPane {
	private Stage stage;
    private Scene scene;
    private TableView<Item> table;
    private Label nameLbl, categoryLbl, sizeLbl, priceLbl, errorLbl;
    private VBox box;
    private FlowPane flow;
    private HBox btnBox;
    private TextField nameField, categoryField, sizeField, priceField;
    private GridPane grid;
    private Button addBtn, updateBtn, deleteBtn, logoutBtn, acceptOfferBtn, declineOfferBtn;
    private String tempId = null;
    private ItemController itemController;
    private User user;
    

    public SellerView(Stage stage, User user) {
    	this.stage = stage;
    	itemController = new ItemController(new ItemRepository());
    	this.user = user;
        init();
        initPosition();
        initializeStage(stage);
        setEventHandlers();
    }

    private void init() {
    	
        // Initialize containers and components as before
//        container = new BorderPane();
        grid = new GridPane();
        box = new VBox();
        btnBox = new HBox();
        flow = new FlowPane();
        scene = new Scene(this, 900, 750);

        nameLbl = new Label("Name:");
        categoryLbl = new Label("Category:");
        sizeLbl = new Label("Size:");
        priceLbl = new Label("Price:");
        
        errorLbl = new Label();
        errorLbl.setTextFill(Color.RED);

        nameField = new TextField();
        categoryField = new TextField();
        sizeField = new TextField();
        priceField = new TextField();

        addBtn = new Button("Add");
        updateBtn = new Button("Update");
        deleteBtn = new Button("Delete");
        logoutBtn = new Button("Logout");
        acceptOfferBtn = new Button("Accept Offer");
        declineOfferBtn = new Button("Decline Offer");
        
        table = new TableView<>();
        // Define Table columns
        setTableColumns();

        btnBox.setAlignment(Pos.CENTER);
        btnBox.setSpacing(10);
        btnBox.getChildren().addAll(addBtn, updateBtn, deleteBtn, acceptOfferBtn, declineOfferBtn, logoutBtn);
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
        
        TableColumn<Item, String> offerPriceCol = new TableColumn<>("Last Offer");
        offerPriceCol.setCellValueFactory(new PropertyValueFactory<>("offerPrice"));
        offerPriceCol.setMinWidth(150);
        
        table.getColumns().addAll(nameCol, categoryCol, sizeCol, priceCol, offerStatusCol, offerPriceCol);
        refreshTable();
    }

    private void initPosition() {
        // Set the alignment of the GridPane to center
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Horizontal gap between components
        grid.setVgap(10); // Vertical gap between components

        // Add components to the grid
        grid.add(nameLbl, 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(categoryLbl, 0, 1);
        grid.add(categoryField, 1, 1);

        grid.add(sizeLbl, 0, 2);
        grid.add(sizeField, 1, 2);

        grid.add(priceLbl, 0, 3);
        grid.add(priceField, 1, 3);
        
        grid.add(errorLbl, 0, 4, 2, 1);

        // Add margin to the GridPane (to create space between the table and the form)
        GridPane.setMargin(grid, new Insets(20, 0, 0, 0)); // Add top margin

        // Add the grid to the VBox
        box.getChildren().add(grid);

        // Add padding to the VBox to give space around the form
        box.setPadding(new Insets(20)); // Add padding around the VBox

        // Set up the BorderPane
        this.setTop(table);
        this.setCenter(box);

        // Center buttons at the bottom
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setSpacing(10);
        btnBox.setPadding(new Insets(10,10,30,10)); // Padding around the buttons
        this.setBottom(btnBox);
        BorderPane.setAlignment(btnBox, Pos.CENTER); 
    }

    private void setEventHandlers() {
        addBtn.setOnAction(e -> handleAdd());
        updateBtn.setOnAction(e -> handleUpdate());
        deleteBtn.setOnAction(e -> handleDelete());
        logoutBtn.setOnAction(e -> handleLogout());
        table.setOnMouseClicked(e -> handleTableSelection());
        acceptOfferBtn.setOnAction(e -> handleAcceptOffer());
        declineOfferBtn.setOnAction(e -> handleDeclineOffer());
    }
    
    private void handleAcceptOffer() {
    	Item selectedItem = table.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            // Optional: Show an error message
            errorLbl.setText("Please select an item to accept the offer.");
            return;
        }
        else if(selectedItem.getOfferPrice().equals("") || selectedItem.getOfferPrice().equals(null)){
       	 errorLbl.setText("Item you selected has no offer");
            return;
       }
        else {
        	//TODO: handle logic to database ya nanti
        	itemController.acceptOffer(tempId);
        	refreshTable();
        	errorLbl.setText("Offer accepted");
        	errorLbl.setTextFill(Color.GREEN); 
        }
    	
    }
    private void handleDeclineOffer() {
        // Check if an item is selected
        Item selectedItem = table.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            // Optional: Show an error message
            errorLbl.setText("Please select an item to decline the offer.");
            errorLbl.setTextFill(Color.RED);  // Red for error
            return;
        } else if (selectedItem.getOfferPrice() == null || selectedItem.getOfferPrice().equals("")) {
            // Check if the selected item has an offer
            errorLbl.setText("Item you selected has no offer.");
            errorLbl.setTextFill(Color.RED);
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

        // Create a VBox to hold the components
        VBox dialogLayout = new VBox(10);
        dialogLayout.setPadding(new Insets(10));
        dialogLayout.setAlignment(Pos.CENTER);

        // Add components to the layout
        dialogLayout.getChildren().addAll(reasonLabel, reasonField, confirmButton);

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
                System.out.println("Decline Reason: " + reason + " for item: " + selectedItem.getName());
                dialogStage.close();

                // Handle the logic to decline the offer in the database
                itemController.declineOffer(selectedItem.getId(), reason);

                // Refresh the table to reflect changes
                refreshTable();

                // Show success message
                errorLbl.setText("Offer for item " + selectedItem.getName() + " has been declined.");
                errorLbl.setTextFill(Color.GREEN);  // Green for success
            }
        });

        // Set up the scene and show the dialog
        Scene dialogScene = new Scene(dialogLayout, 300, 150);
        dialogStage.setScene(dialogScene);
        dialogStage.showAndWait();
    }


    private void handleTableSelection() {
        Item selectedItem = table.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            nameField.setText(selectedItem.getName());
            categoryField.setText(selectedItem.getCategory());
            sizeField.setText(selectedItem.getSize());
            priceField.setText(selectedItem.getPrice().toString());
            tempId = selectedItem.getId();  // Store the selected item's Item_id
        }
    }

    private void handleAdd() {
        String name = nameField.getText();
        String category = categoryField.getText();
        String size = sizeField.getText();
        String price = priceField.getText();
        String sellerId = user.getUserId();

        handleFormAction(name, category, size, price, sellerId, false);
    }

    private void handleUpdate() {
        String name = nameField.getText();
        String category = categoryField.getText();
        String size = sizeField.getText();
        String price = priceField.getText();
        String sellerId = user.getUserId();

        handleFormAction(name, category, size, price, sellerId,true);  
        
    }
    
    private void handleLogout() {
    	new LoginView(stage);
    }
    
    private void handleFormAction(String name, String category, String size, String price, String sellerId, boolean isUpdate) {
        // Validate the form using the itemController method
        if (!itemController.validateFormSeller(name, category, size, price)) {
            errorLbl.setText("All fields are required.");
            return; // Do nothing if the form is invalid
        }

        // Proceed with the corresponding action if the form is valid
        if (isUpdate) {
            itemController.editItem(tempId, name, category, size, price);
        } else {
            itemController.uploadItem(name, category, size, price, sellerId);  // Pass sellerId
        }

        refreshTable();
        errorLbl.setText(""); // Clear the error message after success
    }

    private void handleDelete() {
    	itemController.deleteItem(tempId);
        refreshTable();
    }


    private void refreshTable() {
        table.getItems().clear();
        ResultSet rs = itemController.viewItemsBySellerId(user.getUserId());  // Get items from the controller
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

    private void initializeStage(Stage stage) {
        stage.setScene(scene);
        stage.setTitle("Seller Page");
        stage.centerOnScreen();
        stage.show();
    }
}