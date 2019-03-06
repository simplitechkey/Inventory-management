/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SellProduct;

import Beans.ProductItem;
import DatabaseHelper.DBDAO;
import ProductEntry.ProductEntryController;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
public class SellProductController implements Initializable {

   @FXML
    private TableView<ProductItem> tableProducts;
    @FXML
    private TextField fieldId;

    String dateToday;
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
            
            LocalDate today= LocalDate.now();
            dateToday=today.toString();
            
        } catch (Exception ex) {
            Logger.getLogger(ProductEntryController.class.getName()).log(Level.SEVERE, null, ex);
        }
           
    }      

    @FXML
    private void sellProductfromAvailableProductsAction(ActionEvent event) {
       try {
           ResultSet rs=DBDAO.getProductbyID(fieldId.getText().trim());
           DBDAO.insertSoldProduct(fieldId.getText().trim(),rs.getString("productName") , rs.getDouble("productPrice"), dateToday);
           DBDAO.deleteProductbyID(fieldId.getText().trim());
           tableProducts.setItems(DBDAO.getAllProducts());
                Alert alert=new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Product Sold Successfully");
                alert.showAndWait();
       } catch (Exception ex) {
           Logger.getLogger(SellProductController.class.getName()).log(Level.SEVERE, null, ex);
       }
    }

    @FXML
    private void allSoldProductsAction(ActionEvent event) {
         try {
             Stage stage2 = (Stage) fieldId.getScene().getWindow();
             stage2.close();
            AnchorPane root = FXMLLoader.load(getClass().getResource("/SoldProducts/SoldProducts.fxml"));
            Stage stage=new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ProductEntryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void backAction(ActionEvent event) {
       try {
           Stage stage2 = (Stage) fieldId.getScene().getWindow();
           stage2.close();
           AnchorPane root = FXMLLoader.load(getClass().getResource("/ProductEntry/ProductEntry.fxml"));
           Stage stage=new Stage();
           Scene scene = new Scene(root);
           stage.setScene(scene);
           stage.show();
       } catch (IOException ex) {
           Logger.getLogger(SellProductController.class.getName()).log(Level.SEVERE, null, ex);
       }
    }
    
}
