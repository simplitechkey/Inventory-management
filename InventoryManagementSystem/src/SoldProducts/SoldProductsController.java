/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SoldProducts;

import Beans.ProductItem;
import Beans.SoldProductItem;
import DatabaseHelper.DBDAO;
import ProductEntry.ProductEntryController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author omkarkamate
 */
public class SoldProductsController implements Initializable {

    @FXML
    private TableView<SoldProductItem> tableSoldProducts;
    @FXML
    private DatePicker dateField;
    @FXML
    private TextField fieldId;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         try {
            TableColumn<SoldProductItem,String>productId=new TableColumn("productId");
            productId.setCellValueFactory(new PropertyValueFactory<>("productId"));
            
            TableColumn<SoldProductItem,String>productName=new TableColumn("productName");
            productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
            
            TableColumn<SoldProductItem,Double>productPrice=new TableColumn("productPrice");
            productPrice.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
            
             TableColumn<SoldProductItem,String>productSaleDate=new TableColumn("productSaleDate");
            productSaleDate.setCellValueFactory(new PropertyValueFactory<>("productSaleDate"));
            tableSoldProducts.getColumns().addAll(productId,productName,productPrice);
            tableSoldProducts.setItems(DBDAO.getAllSoldProducts());
            
        } catch (Exception ex) {
            Logger.getLogger(ProductEntryController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }    
    
}
