/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
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
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;
import model.Product;

/**
 * Interface controller class used for Modify Product interface.
 */
public class ModifyProductController implements Initializable {

    Stage stage;
    Parent scene;
    
        @FXML
    private TextField productMaxTxt;

    @FXML
    private TextField productMinTxt;

    @FXML
    private TextField productIdTxt;

    @FXML
    private TextField productNameTxt;

    @FXML
    private TextField productInvTxt;

    @FXML
    private TextField productPriceTxt;
    
    @FXML
    private TextField partSearchTxt;

    @FXML
    private TableView<Part> partSearchTable;

    @FXML
    private TableColumn<Part, Integer> partIdCol;

    @FXML
    private TableColumn<Part, String> partNameCol;

    @FXML
    private TableColumn<Part, Integer> partInvCol;

    @FXML
    private TableColumn<Part, Double> partPriceCol;

    @FXML
    private TableView<Part> assocPartTable;

    @FXML
    private TableColumn<Part, Integer> assocPartIdCol;

    @FXML
    private TableColumn<Part, String> assocPartNameCol;

    @FXML
    private TableColumn<Part, Integer> assocPartInvCol;

    @FXML
    private TableColumn<Part, Double> assocPartPriceCol;
    
    ObservableList<Part> selectedParts = FXCollections.observableArrayList();
    
    ObservableList<Part> displayParts = Inventory.getAllParts();
    
    /** Search available parts list, not associated with product. 
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

        partSearchTable.setItems(searchPartDisplay);
        
        if(searchPartDisplay.size() == 0) {
            Alert dataError = new Alert(Alert.AlertType.ERROR);
            dataError.setTitle("ERROR");
            dataError.setContentText("No matching parts found");
            dataError.showAndWait();
        }
    }

    
    /** Add part to product's list of associated parts.
     * Adds parts to visible table list of parts associated with product.
     * Does not save part to product's associated parts until modified product is saved.
     * This method is called when user clicks Add button.
     *  @param event a click on Add button
     */
    @FXML
    void onActionAddPartToProduct(ActionEvent event) {
        selectedParts.add(partSearchTable.getSelectionModel().getSelectedItem());
        
        assocPartTable.setItems(selectedParts);
    }

    /** Removes part from product's list of associated parts. 
     * Deletes parts from visible table list of parts associated with product. Confirms action with user.
     * Does not remove part from product's associated parts until modified product is saved.
     * This method is called when user clicks Remove button.
     *  @param event a click on Remove button
     */
    @FXML
    void onActionRemovePartFromProduct(ActionEvent event) {
        Alert exitConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        exitConfirm.setContentText("Are you sure you want to remove this part from product?");
        
        Optional<ButtonType> result = exitConfirm.showAndWait();
        
        if(result.isPresent() && result.get() == ButtonType.OK) {
            selectedParts.remove(assocPartTable.getSelectionModel().getSelectedItem());

            assocPartTable.setItems(selectedParts);
        }
    }

    /** Cancels modification of product and returns to main form.
     * Asks user to confirm that they would like to return to main form and warns them that changes will not be saved.
     * Method is called when user clicks on Cancel button.
     * @param event a click on Cancel button
     * @throws IOException If an input or output exception occur
     */
    @FXML
    void onActionReturnToMain(ActionEvent event) throws IOException {
        Alert exitConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        exitConfirm.setContentText("Any modifications will not be saved, do you wish to continue?");
        
        Optional<ButtonType> result = exitConfirm.showAndWait();
        
        if(result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }

    }

    /** Saves all modifications made to product.
     * Saves all modifications to product, including text fields and any changes with associated parts.  
     * Also updates the modified product within inventory, then returns user to the main form. 
     * This method is called when user clicks Save button.
     * 
     * <p> In this method I originally encountered an error when I attempted to first clear out
     * the Product's previous associated parts list, then tried to add the updated list by 
     * looping through the "selectedParts" ObservableList (the one used to display associated parts)
     * and individually add the parts to the Product's associated parts list.  This gave odd behavior 
     * each time, clearing out the entire Product's part list.  After doing some debugging and research 
     * I realized that the "selectedParts" ObservableList is a reference to the Product's associated parts list,
     * which meant that every time I cleared it I also cleared out the entire "selectedParts" list. To avoid
     * this issue I did still use the selectedParts list but updated the Product in "allProducts" list with a
     * new Product object which made the process easier and avoided these type of errors. </p>
     * 
     * @param event a click on Save button
     * @throws IOException If an input or output exception occur, including incorrect data being entered.
     */
    @FXML
    public void onActionSaveProduct(ActionEvent event) throws IOException {
        try {
            int id = Integer.parseInt(productIdTxt.getText());
            String name = productNameTxt.getText();
            int stock = Integer.parseInt(productInvTxt.getText());
            double price = Double.parseDouble(productPriceTxt.getText());
            int max = Integer.parseInt(productMaxTxt.getText());
            int min = Integer.parseInt(productMinTxt.getText());
            
            if(max < min) {
               Alert dataError = new Alert(Alert.AlertType.ERROR);
                dataError.setTitle("ERROR");
                dataError.setContentText("Max must be larger than min value.  Please correct these values and click save again.");
                dataError.showAndWait();
            }
            else if (stock < min) {
                Alert dataError = new Alert(Alert.AlertType.ERROR);
                dataError.setTitle("ERROR");
                dataError.setContentText("Stock must be greater than or equal to min value.  Please correct these values and click save again.");
                dataError.showAndWait();
            }
            else if (stock > max) {
                Alert dataError = new Alert(Alert.AlertType.ERROR);
                dataError.setTitle("ERROR");
                dataError.setContentText("Stock must be less than or equal to max value.  Please correct these values and click save again.");
                dataError.showAndWait();
            }
            else {
                Product productToMod = Inventory.lookupProduct(Integer.parseInt(productIdTxt.getText()));
                int productIndex = Inventory.getProductIndex(productToMod);

                Product updatedProduct = new Product(id, name, price, stock, min, max);
                
                for (int j = 0; j < selectedParts.size(); ++j) {
                    updatedProduct.addAssociatedPart(selectedParts.get(j));
                }
                
                Inventory.updateProduct(productIndex, updatedProduct);

                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
            
            if(Integer.parseInt(productMaxTxt.getText()) > Integer.parseInt(productMinTxt.getText()) && Integer.parseInt(productInvTxt.getText()) >= Integer.parseInt(productMinTxt.getText()) && Integer.parseInt(productInvTxt.getText()) <= Integer.parseInt(productMaxTxt.getText())) {
                
            }
            else {
                Alert dataError = new Alert(Alert.AlertType.ERROR);
                dataError.setTitle("ERROR");
                dataError.setContentText("Max must be larger than min and inventory count must be between the min and max values.  Please correct these values and click save again.");
                dataError.showAndWait();
            }
        }
        
        catch(NumberFormatException ex) {
            Alert dataError = new Alert(Alert.AlertType.ERROR);
            dataError.setTitle("ERROR DIALOG");
            dataError.setContentText("Please enter valid data for each field");
            dataError.showAndWait();
 
        }
    }
    
    /** Receives product selected in Main Form and populates product information in text fields.
     * Sets up information of product to be modified, including list of associated parts.
     * Method is called when user selects a product and clicks Modify on Main Form interface.
     * @param product selected product from Main Form
     */
    public void productToModify(Product product) {
  
        // Set text fields with values from product to modify
        productIdTxt.setText(String.valueOf(product.getId()));
        productNameTxt.setText(product.getName());
        productInvTxt.setText(String.valueOf(product.getStock()));
        productPriceTxt.setText(String.valueOf(product.getPrice()));
        productMaxTxt.setText(String.valueOf(product.getMax()));
        productMinTxt.setText(String.valueOf(product.getMin()));
        
       // Set bottom associated parts table with parts associated with selected product
        selectedParts = product.getAllAssociatedParts();
                
        assocPartTable.setItems(selectedParts); 
        
        assocPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        partSearchTable.setItems(displayParts);
        
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
