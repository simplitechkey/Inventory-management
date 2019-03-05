/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProductEntry;

import Beans.ProductItem;
import DatabaseHelper.DBDAO;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author omkarkamate
 */
public class ProductEntryController implements Initializable {

    @FXML
    private TableView<ProductItem> tableProducts;
    @FXML
    private TextField fieldId;
    @FXML
    private TextField fieldPrice;
    @FXML
    private TextField fieldName;

    /**
     * Initializes the controller class.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void initialize(URL url, ResourceBundle rb) {
        try {
            TableColumn<ProductItem,String>productId=new TableColumn("productId");
            productId.setCellValueFactory(new PropertyValueFactory<>("productId"));
            
            TableColumn<ProductItem,String>productName=new TableColumn("productName");
            productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
            
            TableColumn<ProductItem,Double>productPrice=new TableColumn("productPrice");
            productPrice.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
            
            tableProducts.getColumns().addAll(productId,productName,productPrice);
            tableProducts.setItems(DBDAO.getAllProducts());
            
        } catch (Exception ex) {
            Logger.getLogger(ProductEntryController.class.getName()).log(Level.SEVERE, null, ex);
        }
           
    }    

    @FXML
    private void addNewProductAction(ActionEvent event) {
        if(!fieldId.getText().trim().isEmpty() && !fieldName.getText().trim().isEmpty() && !fieldName.getText().trim().isEmpty()){
            try {
                DBDAO.insertProduct(fieldId.getText().trim(), fieldName.getText().trim(), Double.parseDouble(fieldPrice.getText().trim()));
                tableProducts.setItems(DBDAO.getAllProducts());
                Alert alert=new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Product Added Successfully");
                alert.showAndWait();
            } catch (Exception ex) {
                Logger.getLogger(ProductEntryController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }else{
           Alert alert=new Alert(Alert.AlertType.ERROR);
           alert.setContentText("One of the Field is Empty");
        }
    }
    @FXML
    private void sellProductAction(ActionEvent event) {
        try {
            Stage stage2 = (Stage) fieldId.getScene().getWindow();
            stage2.close();
            AnchorPane root = FXMLLoader.load(getClass().getResource("/SellProduct/SellProduct.fxml"));
            Stage stage=new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ProductEntryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
