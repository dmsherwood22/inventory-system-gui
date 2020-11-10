
package controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

/**
 * Interface controller class used for Main Form interface.
 */
public class MainFormController implements Initializable {

    Stage stage;
    Parent scene;
    
        @FXML
    private TextField partSearchTxt;

    @FXML
    private TableView<Part> partTableView;

    @FXML
    private TableColumn<Part, Integer> partIdCol;

    @FXML
    private TableColumn<Part, String> partNameCol;

    @FXML
    private TableColumn<Part, Integer> partInvCol;

    @FXML
    private TableColumn<Part, Double> partPriceCol;

    @FXML
    private TextField productSearchTxt;

    @FXML
    private TableView<Product> productTableView;

    @FXML
    private TableColumn<Product, Integer> productIdCol;

    @FXML
    private TableColumn<Product, String> productNameCol;

    @FXML
    private TableColumn<Product, Integer> productInvCol;

    @FXML
    private TableColumn<Product, Double> productPriceCol;
    
    /** Search for matching part(s) in inventory.
     * Searches for full or partial part name match and/or matching part ID number. 
     * This method is called when user clicks Search button.
     *  @param event a click on search parts button
     */
    @FXML
    void onActionSearchParts(ActionEvent event) {
        String search = partSearchTxt.getText();
       
        ObservableList<Part> searchPartDisplay = Inventory.lookupPart(search);
        
        if(searchPartDisplay.size() == 0) {
            try {
                int searchId = Integer.parseInt(search);
                Part searchPart = Inventory.lookupPart(searchId);

                if(searchPart != null) {
                    searchPartDisplay.add(searchPart);
                }
            }
            catch(NumberFormatException ex) {
                
            }
        }

        partTableView.setItems(searchPartDisplay);
        
        if(searchPartDisplay.size() == 0) {
            Alert dataError = new Alert(Alert.AlertType.ERROR);
            dataError.setTitle("ERROR");
            dataError.setContentText("No matching parts found");
            dataError.showAndWait();
        }
    }

    /** Search for matching product(s) in inventory.
     * Searches for full or partial product name match and/or matching product ID number. 
     * This method is called when user clicks Search button.
     *  @param event a click on search parts button
     */
    @FXML
    void onActionSearchProducts(ActionEvent event) {
        String search = productSearchTxt.getText();
       
        ObservableList<Product> searchProductDisplay = Inventory.lookupProduct(search);
        
        if(searchProductDisplay.size() == 0) {
            try {
                int searchId = Integer.parseInt(search);
                Product searchProduct = Inventory.lookupProduct(searchId);

                if(searchProduct != null) {
                    searchProductDisplay.add(searchProduct);
                }
            }
            catch(NumberFormatException ex) {
                
            }
        }

        productTableView.setItems(searchProductDisplay);
        
        if(searchProductDisplay.size() == 0) {
            Alert dataError = new Alert(Alert.AlertType.ERROR);
            dataError.setTitle("ERROR");
            dataError.setContentText("No matching product found");
            dataError.showAndWait();
        }
    }


    /** Deletes selected part from inventory.
     * Checks which part is selected, confirms that user would like to delete, then removes it from inventory.
     * Generates error message if no part is selected.
     *  @param event a click on Delete part button
     */
    @FXML
    void onActionDeletePart(ActionEvent event) {
        // Check if part is selected and confirm that user wants to delete part.
        if (partTableView.getSelectionModel().getSelectedItem() != null) {
            Alert deleteConfirm = new Alert(Alert.AlertType.CONFIRMATION);
            deleteConfirm.setContentText("This will permanently delete selected part. Are you sure you want to delete?");
        
            Optional<ButtonType> result = deleteConfirm.showAndWait();
        
            if(result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deletePart(partTableView.getSelectionModel().getSelectedItem());
                partTableView.setItems(Inventory.getAllParts()); 
            }
        }
        
        // Display error message if no item selected to delete
        else {
            Alert dataError = new Alert(Alert.AlertType.ERROR);
            dataError.setTitle("ERROR");
            dataError.setContentText("No part selected to delete.  Please select part and try again.");
            dataError.showAndWait();
        }
    
    }

    /** Deletes selected product from inventory.
     * Checks which product is selected, confirms that user would like to delete, then removes it from inventory.
     * If product has associated parts, cannot delete and warns user to remove associated parts first.
     * Generates error message if no product is selected.
     *  @param event a click on Delete product button
     */
    @FXML
    void onActionDeleteProduct(ActionEvent event) {
       // Check if product is selected 
        if (productTableView.getSelectionModel().getSelectedItem() != null) {
            
            // Check if product has any associated parts and throw error message if associated parts found
            if (productTableView.getSelectionModel().getSelectedItem().getAllAssociatedParts().size() > 0) {
                Alert dataError = new Alert(Alert.AlertType.ERROR);
                dataError.setTitle("ERROR");
                dataError.setContentText("Cannot delete product with associated parts.  If you wish to delete this product, please remove associated parts by using Modify option.");
                dataError.showAndWait();
            }
            
            // Confirm that user wishes to permanently delete product
            else {
                Alert deleteConfirm = new Alert(Alert.AlertType.CONFIRMATION);
                deleteConfirm.setContentText("This will permanently delete selected product. Are you sure you want to delete?");
        
                Optional<ButtonType> result = deleteConfirm.showAndWait();
            
                if(result.isPresent() && result.get() == ButtonType.OK) {
                    Inventory.deleteProduct(productTableView.getSelectionModel().getSelectedItem());
                    productTableView.setItems(Inventory.getAllProducts()); 
                }
            }
        }
        
        // Display error message if no item selected to delete
        else {
            Alert dataError = new Alert(Alert.AlertType.ERROR);
            dataError.setTitle("ERROR");
            dataError.setContentText("No product selected to delete.  Please select product and try again.");
            dataError.showAndWait();
        }

    }

    /** Exit Inventory System application. Closes user interface and shuts down program.
     *  @param event a click on Exit button
     */
    @FXML
    void onActionExit(ActionEvent event) {
        System.exit(0);
    }

    /** Opens up the Add Part interface.
     *  @param event a click on Add part button.
     */
    @FXML
    void onActionOpenAddPart(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Opens up the Add Product interface.
     *  @param event a click on Add part button.
     */
    @FXML
    void onActionOpenAddProduct(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /** Opens up the Modify Part interface and sends selected part information to Modify Part interface.
     *  @param event a click on Modify part button.
     */
    @FXML
    void onActionOpenModifyPart(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        
        loader.setLocation(getClass().getResource("/view/ModifyPart.fxml"));
        loader.load();
        
        ModifyPartController ModPart = loader.getController();
        ModPart.partToModify(partTableView.getSelectionModel().getSelectedItem());
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /** Opens up the Modify Product interface and sends selected product information to Modify Product interface.
     *  @param event a click on Modify product button.
     */
    @FXML
    void onActionOpenModifyProduct(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
        
            loader.setLocation(getClass().getResource("/view/ModifyProduct.fxml"));
            loader.load();
        
            ModifyProductController ModProduct = loader.getController();
            ModProduct.productToModify(productTableView.getSelectionModel().getSelectedItem());
        
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        // Fix/figure out catch type needed
        catch(Exception ex) {
            System.out.println("Please select a part to modify");
        }
    
    }

    /**
     * Initializes the controller class and sets up Table View information.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        partTableView.setItems(Inventory.getAllParts()); 
        
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        productTableView.setItems(Inventory.getAllProducts()); 
        
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }    
    
}
