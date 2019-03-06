/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SoldProducts;

import Beans.SoldProductItem;
import DatabaseHelper.DBDAO;
import ProductEntry.ProductEntryController;
import SellProduct.SellProductController;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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
    @FXML
    private Label lblSum;

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
            tableSoldProducts.getColumns().addAll(productId,productName,productPrice,productSaleDate);
            tableSoldProducts.setItems(DBDAO.getAllSoldProducts());
            
        } catch (Exception ex) {
            Logger.getLogger(ProductEntryController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }    

    @FXML
    private void sortOnDateAction(ActionEvent event) throws Exception {
       String sum = "0";
       String date=dateField.getValue().toString();
       ObservableList<SoldProductItem> sortedproductList=DBDAO.getSoldProductbyDate(date);
        if(!sortedproductList.isEmpty()){
        tableSoldProducts.setItems(sortedproductList);
        sum=DBDAO.getSumOfSoldProductbyDate(date);
        lblSum.setText("Total Collectioin on this date is :"+sum);
        
    }else{
             Alert alert=new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("No Records Found");
                alert.showAndWait();
        }
    }

    @FXML
    private void sendReportEmailAction(ActionEvent event) {
       final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
  // Get a Properties object
     Properties props = System.getProperties();
     props.setProperty("mail.smtp.host", "smtp.gmail.com");
     props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
     props.setProperty("mail.smtp.socketFactory.fallback", "false");
     props.setProperty("mail.smtp.port", "465");
     props.setProperty("mail.smtp.socketFactory.port", "465");
     props.put("mail.smtp.auth", "true");
     props.put("mail.debug", "true");
     props.put("mail.store.protocol", "pop3");
     props.put("mail.transport.protocol", "smtp");
     final String username = "omskamate@gmail.com";//
     final String password = "omkar#14u";
     try{
     Session session = Session.getDefaultInstance(props, 
                          new Authenticator(){
                             protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(username, password);
                             }});

   // -- Create a new message --
     Message msg = new MimeMessage(session);

  // -- Set the FROM and TO fields --
     msg.setFrom(new InternetAddress("omskamate@gmail.com"));
     msg.setRecipients(Message.RecipientType.TO, 
                      InternetAddress.parse("nikteshriya@gmail.com",false));
     msg.setSubject("Hello");
     msg.setText("How are you");
     msg.setSentDate(new Date());
     Transport.send(msg);
     System.out.println("Message sent.");
     }catch (MessagingException e){ 
         System.out.println("Erreur d'envoi, cause: " + e);
     }
     }

    @FXML
    private void backAction(ActionEvent event) {
        
          try {
           Stage stage2 = (Stage) fieldId.getScene().getWindow();
           stage2.close();
           AnchorPane root = FXMLLoader.load(getClass().getResource("/SellProduct/SellProduct.fxml"));
           Stage stage=new Stage();
           Scene scene = new Scene(root);
           stage.setScene(scene);
           stage.show();
       } catch (IOException ex) {
           Logger.getLogger(SellProductController.class.getName()).log(Level.SEVERE, null, ex);
       }
    }
    }

