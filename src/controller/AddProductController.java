
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
 * Interface controller class used for Add Product interface.
 */
public class AddProductController implements Initializable {

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
    
    /** Search available parts in inventory, not yet associated with this product.
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
     * Does not save part to product's associated parts until new product is saved.
     * This method is called when user clicks Add button.
     *  @param event a click on Add button
     */
    @FXML
    void onActionAddPartToProduct(ActionEvent event) {
        selectedParts.add(partSearchTable.getSelectionModel().getSelectedItem());
        
        assocPartTable.setItems(selectedParts);
    }

    /** Removes part from product's list of associated parts. 
     * Deletes parts from visible table list of parts associated with product. Confirms with user their action.
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

    /** Cancels adding product and returns to main form.
     * Asks user to confirm that they would like to return to main form and warns them that product will not be saved.
     * Method is called when user clicks on Cancel button.
     * @param event a click on Cancel button
     * @throws IOException If an input or output exception occur
     * 
     * <p> This method catches issues with user's entries including throwing errors and displaying
     * error message for any incompatible data types that are entered or if they fail to enter
     * data for any of these required fields.  These problems are caught with a "try/catch" statement
     * for exceptions.  </p>
     */
    @FXML
    void onActionReturnToMain(ActionEvent event) throws IOException {
        Alert exitConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        exitConfirm.setContentText("This product will not be saved and information entered will be lost, do you wish to continue?");
        
        Optional<ButtonType> result = exitConfirm.showAndWait();
        
        if(result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }

    }

    /** Saves new product and adds product to inventory.
     * Then returns user to Main Form interface.
     * This method is called when user clicks Save button.
     * @param event a click on Save button
     * @throws IOException If an input or output exception occur, including incorrect data being entered.
     */
    @FXML
    void onActionSaveProduct(ActionEvent event) throws IOException {
        try {
            int id = Inventory.generateProductId();
            String name = productNameTxt.getText();
            int stock = Integer.parseInt(productInvTxt.getText());
            double price = Double.parseDouble(productPriceTxt.getText());
            int min = Integer.parseInt(productMinTxt.getText());
            int max = Integer.parseInt(productMaxTxt.getText());
        
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

                Product newProduct = new Product(id, name, price, stock, min, max);

                for (Part p : selectedParts) {
                    newProduct.addAssociatedPart(p);
                }

                Inventory.addProduct(newProduct);

                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
    catch(NumberFormatException ex) {
            Alert dataError = new Alert(Alert.AlertType.ERROR);
            dataError.setTitle("ERROR DIALOG");
            dataError.setContentText("Please enter valid data for each field");
            dataError.showAndWait();
        }
        
    }

    /**
     * Initializes the controller class an sets up Table View information.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Add all parts to top table for searching
        partSearchTable.setItems(displayParts); 
        
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        assocPartTable.setItems(selectedParts);
        
        assocPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }    
   
    
}
