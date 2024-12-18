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

public class HistoryView extends BorderPane {
	private Stage stage;
    private Scene scene;
    private TableView<Item> table;
    private VBox box;
    private HBox btnBox;
    private GridPane grid;
    private Button backBtn;
    private String tempId = null;
    private ItemController itemController;
    private WishlistController wishlistController;
    private Label errorLbl;
    private User user;
    
    public HistoryView(Stage stage, User user) {
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
        
        backBtn = new Button("Back");
        
        table = new TableView<>();
        setTableColumns();
        
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setSpacing(10);
        btnBox.getChildren().addAll(backBtn);
    }
    
    private void initializeStage(Stage stage) {
        stage.setScene(scene);
        stage.setTitle("History Page");
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
        
        TableColumn<Item, String> offerPriceCol = new TableColumn<>("Status");
        offerPriceCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        offerPriceCol.setMinWidth(150);
        
       
        
        table.getColumns().addAll(nameCol, categoryCol, sizeCol, priceCol, offerPriceCol);
        refreshTable();
    }
    
    private void refreshTable() {
        table.getItems().clear();
        ResultSet rs = itemController.viewHistory(user.getUserId());  // Get items from the controller
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
    	backBtn.setOnAction(e -> handleBack());
    }
    
    private void handleBack() {
    	new BuyerView(stage, user);
    }

}
