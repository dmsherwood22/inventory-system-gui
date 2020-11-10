
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;


/**
 * Interface controller class used for Add Part interface.
 */
public class AddPartController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private RadioButton inHouseRBtn;

    @FXML
    private ToggleGroup partTG;

    @FXML
    private RadioButton outsourceRBtn;

    @FXML
    private Label partClassLabel;

    @FXML
    private TextField partMaxTxt;

    @FXML
    private TextField partMinTxt;

    @FXML
    private TextField partIdTxt;

    @FXML
    private TextField partNameTxt;

    @FXML
    private TextField partInvTxt;

    @FXML
    private TextField partPriceTxt;

    @FXML
    private TextField partClassTxt;

    /** Cancels adding part and returns to main form.
     * Asks user to confirm that they would like to return to main form and warns them that part will not be saved.
     * Method is called when user clicks on Cancel button.
     * @param event a click on Cancel button
     * @throws IOException If an input or output exception occur
     */
    @FXML
    void onActionReturnToMain(ActionEvent event) throws IOException {
        
        Alert exitConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        exitConfirm.setContentText("This will clear all text field values, do you wish to continue?");
        
        Optional<ButtonType> result = exitConfirm.showAndWait();
        
        if(result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        
    }

    /** Saves new part and adds product to inventory.
     * Then returns user to Main Form interface.
     * This method is called when user clicks Save button.
     * @param event a click on Save button
     * @throws IOException If an input or output exception occur, including incorrect data being entered.
     */
    @FXML
    void onActionSavePart(ActionEvent event) throws IOException {
    try {
        
            int id = Inventory.generatePartId();
            String name = partNameTxt.getText();
            int stock = Integer.parseInt(partInvTxt.getText());
            double price = Double.parseDouble(partPriceTxt.getText());
            int min = Integer.parseInt(partMinTxt.getText());
            int max = Integer.parseInt(partMaxTxt.getText());
            
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
                
                // Add In House part object, if In House radio button selected
                if (inHouseRBtn.isSelected()) {
                    int machineId = Integer.parseInt(partClassTxt.getText());
                    Inventory.addPart(new InHouse(machineId, id, name, price, stock, min, max));
                }
                // Add outsourced part object, if outsourced radio button selected
                else { 
                    String companyName = partClassTxt.getText();
                    Inventory.addPart(new Outsourced(companyName, id, name, price, stock, min, max));
                }

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
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // EventHandler<ActionEvent> event = null;
        inHouseRBtn.setOnAction(event ->
        {
            partClassLabel.setText("Machine ID");
        });
        outsourceRBtn.setOnAction(event ->
        {
            partClassLabel.setText("Company Name");
        });
    }    
    
}
