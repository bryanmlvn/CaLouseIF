package view;

import java.sql.ResultSet;
import java.sql.SQLException;

import controller.ItemController;
import controller.WishlistController;
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
import model.User;
import repository.ItemRepository;
import repository.WishlistRepository;

public class BuyerView extends BorderPane {
	private Stage stage;
    private Scene scene;
    private TableView<Item> table;
    private VBox box;
    private HBox btnBox;
    private GridPane grid;
    private Button logoutBtn, buyBtn, makeOfferBtn, addToWishlistBtn, goToWishlistBtn, goToPurchaseHistoryBtn  ;
    private String tempId = null;
    private ItemController itemController;
    private WishlistController wishlistController;
    private Label errorLbl;
    private User user;
	
    public BuyerView(Stage stage, User user) {
    	this.stage = stage;
    	this.user = user;
    	itemController = new ItemController(new ItemRepository());
    	this.wishlistController = new WishlistController(new WishlistRepository());
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
        buyBtn = new Button("Buy");
        makeOfferBtn = new Button("Make offer");
        addToWishlistBtn = new Button("Add to wishlist");
        goToWishlistBtn = new Button("View wishlist");
        goToPurchaseHistoryBtn = new Button("View History");
        
        table = new TableView<>();
        setTableColumns();
        
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setSpacing(10);
        btnBox.getChildren().addAll(buyBtn, makeOfferBtn, addToWishlistBtn, goToWishlistBtn, goToPurchaseHistoryBtn, logoutBtn);
    }
    private void initializeStage(Stage stage) {
        stage.setScene(scene);
        stage.setTitle("Buyer Page");
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
        
        TableColumn<Item, String> offerPriceCol = new TableColumn<>("Last Offer Price");
        offerPriceCol.setCellValueFactory(new PropertyValueFactory<>("offerPrice"));
        offerPriceCol.setMinWidth(150);
        
       
        
        table.getColumns().addAll(nameCol, categoryCol, sizeCol, priceCol, offerPriceCol);
        refreshTable();
    }
    private void refreshTable() {
        table.getItems().clear();
        ResultSet rs = itemController.viewItemsForBuyers();  // Get items from the controller
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
        buyBtn.setOnAction(e -> handleBuy());
        makeOfferBtn.setOnAction(e -> handleMakeOffer());
        addToWishlistBtn.setOnAction(e -> handleAddToWishlist());
        goToWishlistBtn.setOnAction(e -> handleGoToWishlist());
        goToPurchaseHistoryBtn.setOnAction(e -> handleGoToPurchaseHistory());
        
        table.setOnMouseClicked(e -> handleTableSelection());
        
    }
    private void handleTableSelection() {
        Item selectedItem = table.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            tempId = selectedItem.getId();  // Store the selected item's Item_id
        }
    }
    private void handleLogout() {
    	new LoginView(stage);
    }
    private void handleBuy() {
        Item selectedItem = table.getSelectionModel().getSelectedItem();
        String userId = user.getUserId();
        if (selectedItem == null) {
            errorLbl.setText("Please select an item to purchase.");
            errorLbl.setTextFill(Color.RED); // Ensure the color is red
            return;
        }
        try {
            itemController.purchaseItem(tempId, userId); // Perform the purchase operation
            refreshTable(); // Refresh the table first to reflect updates
            errorLbl.setText("Purchase successful! Item " + selectedItem.getName() + " has been sold.");
            errorLbl.setTextFill(Color.GREEN); // Set success message color
        } catch (Exception e) {
            errorLbl.setText("An error occurred while processing the purchase.");
            errorLbl.setTextFill(Color.RED); // Set error message color
            e.printStackTrace();
        }
    }
    private void handleMakeOffer() {
        Item selectedItem = table.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            errorLbl.setText("Please select an item to make an offer.");
            errorLbl.setTextFill(Color.RED);
            return;
        }

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Make an Offer");

        Label currentPriceLabel = new Label("Current Offer Price: " + selectedItem.getOfferPrice());
        Label newPriceLabel = new Label("New Offer Price:");
        TextField newPriceField = new TextField();
        Label validationLabel = new Label();
        validationLabel.setTextFill(Color.RED);

        Button confirmButton = new Button("Confirm");
        confirmButton.setDisable(true);

        newPriceField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                double newOfferPrice = Double.parseDouble(newValue);
                double currentOfferPrice = selectedItem.getOfferPrice();
                if (newOfferPrice > currentOfferPrice) {
                    validationLabel.setText("");
                    confirmButton.setDisable(false);
                } else {
                    validationLabel.setText("Offer price must be higher than the current price.");
                    confirmButton.setDisable(true);
                }
            } catch (NumberFormatException e) {
                validationLabel.setText("Please enter a valid number.");
                confirmButton.setDisable(true);
            }
        });

        confirmButton.setOnAction(e -> {
            try {
                double newOfferPrice = Double.parseDouble(newPriceField.getText());
                itemController.updateOfferPrice(selectedItem.getId(), newOfferPrice, user.getUserId());
                errorLbl.setText("Offer updated successfully for item: " + selectedItem.getName());
                errorLbl.setTextFill(Color.GREEN);
                dialogStage.close();
                refreshTable();
            } catch (Exception ex) {
                errorLbl.setText("An error occurred while updating the offer.");
                errorLbl.setTextFill(Color.RED);
                ex.printStackTrace();
            }
        });

        VBox dialogLayout = new VBox(10);
        dialogLayout.setPadding(new Insets(10));
        dialogLayout.setAlignment(Pos.CENTER);
        dialogLayout.getChildren().addAll(currentPriceLabel, newPriceLabel, newPriceField, validationLabel, confirmButton);

        Scene dialogScene = new Scene(dialogLayout, 300, 200);
        dialogStage.setScene(dialogScene);
        dialogStage.showAndWait();
    }

    private void handleAddToWishlist() {
        Item selectedItem = table.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            errorLbl.setText("Please select an item to add to your wishlist.");
            errorLbl.setTextFill(Color.RED);
            return;
        }

        try {
            String userId = user.getUserId();
            String itemId = selectedItem.getId();
            wishlistController.addItemToWishlist(itemId, userId);

            errorLbl.setText("Item " + selectedItem.getName() + " added to your wishlist!");
            errorLbl.setTextFill(Color.GREEN);
        } catch (Exception e) {
            errorLbl.setText("An error occurred while adding the item to your wishlist.");
            errorLbl.setTextFill(Color.RED);
            e.printStackTrace();
        }
    }
    
    private void handleGoToPurchaseHistory() {
    	new HistoryView(stage, user);
    }
    
    private void handleGoToWishlist() {
    	new WishlistView(stage, user);
    }
    
    
}
