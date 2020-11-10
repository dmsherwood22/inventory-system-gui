
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
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
import model.Part;

/**
 * Interface controller class used for Modify Part interface.
 */
public class ModifyPartController implements Initializable {

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

    /** Cancels modification of part and returns user to Main Form.
     * Asks user to confirm that they would like to return to main form and warns them that part will not be saved.
     * Method is called when user clicks on Cancel button.
     * @param event a click on Cancel button
     * @throws IOException If an input or output exception occur
     */
    @FXML
    void onActionReturnToMain(ActionEvent event) throws IOException {
        Alert exitConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        exitConfirm.setContentText("Any modifications made will not be saved, do you wish to continue?");
        
        Optional<ButtonType> result = exitConfirm.showAndWait();
        
        if(result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }

    }

    /** Saves all modifications made to part. 
     * Also updates the modified part within inventory, then returns user to the main form. 
     * This method is called when user clicks Save button.
     * @param event a click on Save button
     * @throws IOException If an input or output exception occur, including incorrect data being entered.
     */
    @FXML
    void onActionSavePart(ActionEvent event) throws IOException {
        try {
            int id = Integer.parseInt(partIdTxt.getText());
            String name = partNameTxt.getText();
            int stock = Integer.parseInt(partInvTxt.getText());
            double price = Double.parseDouble(partPriceTxt.getText());
            int max = Integer.parseInt(partMaxTxt.getText());
            int min = Integer.parseInt(partMinTxt.getText());
            
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
                Part partToMod = Inventory.lookupPart(id);

                int partIndex = Inventory.getPartIndex(partToMod);

                if (partToMod instanceof InHouse && outsourceRBtn.isSelected()) {
                    Inventory.updatePart(partIndex, new Outsourced(partClassTxt.getText(), id, name, price, stock, min, max));
                }
                else if (partToMod instanceof Outsourced && inHouseRBtn.isSelected()) {
                    Inventory.updatePart(partIndex, new InHouse(Integer.parseInt(partClassTxt.getText()), id, name, price, stock, min, max));
                }

                else {

                    partToMod.setName(name);
                    partToMod.setStock(stock);
                    partToMod.setPrice(price);
                    partToMod.setMin(min);
                    partToMod.setMax(max);

                    if (partToMod instanceof InHouse) {
                        ((InHouse) partToMod).setMachineId(Integer.parseInt(partClassTxt.getText()));
                    }
                    else {
                        ((Outsourced) partToMod).setCompanyName(partClassTxt.getText());
                    }
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
    
    /** Receives part selected in Main Form and populates part information in text fields.
     * Method is called when user selects a part and clicks Modify on Main Form interface.
     * @param part selected part from Main Form
     */
    public void partToModify(Part part) {
       if (part instanceof InHouse) {
           inHouseRBtn.setSelected(true);
           partClassLabel.setText("Machine ID");
           partClassTxt.setText(String.valueOf(((InHouse) part).getMachineId()));
           
       }
       else if (part instanceof Outsourced) {
           outsourceRBtn.setSelected(true);
           partClassLabel.setText("Company Name");
           partClassTxt.setText(((Outsourced) part).getCompanyName());
       }
       
        partIdTxt.setText(String.valueOf(part.getId()));
        partNameTxt.setText(part.getName());
        partInvTxt.setText(String.valueOf(part.getStock()));
        partPriceTxt.setText(String.valueOf(part.getPrice()));
        partMaxTxt.setText(String.valueOf(part.getMax()));
        partMinTxt.setText(String.valueOf(part.getMin()));
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
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
