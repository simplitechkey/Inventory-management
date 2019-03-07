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
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import jxl.Range;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

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
    String to = "omkar.kamate@gorillaexpense.com";

      // Sender's email ID needs to be mentioned
      String from = "omskamate@gmail.com";

      final String username = "omskamate@gmail.com";//change accordingly
      final String password = "omkar#14u";//change accordingly

      // Assuming you are sending email through relay.jangosmtp.net
      

      Properties props = new Properties();
    props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
		props.put("mail.smtp.port", 587); //TLS Port
		props.put("mail.smtp.auth", true); //enable authentication
		props.put("mail.smtp.starttls.enable", true); //enable 

      // Get the Session object.
      Session session = Session.getInstance(props,
         new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication(username, password);
            }
         });

      try {
         // Create a default MimeMessage object.
         Message message = new MimeMessage(session);

         // Set From: header field of the header.
         message.setFrom(new InternetAddress(from));

         // Set To: header field of the header.
         message.setRecipients(Message.RecipientType.TO,
            InternetAddress.parse(to));

         // Set Subject: header field
         message.setSubject("Testing Subject");

         // Create the message part
         BodyPart messageBodyPart = new MimeBodyPart();

         // Now set the actual message
         messageBodyPart.setText("This is message body");

         // Create a multipar message
         Multipart multipart = new MimeMultipart();

         // Set text message part
         multipart.addBodyPart(messageBodyPart);

         // Part two is attachment
         messageBodyPart = new MimeBodyPart();
         String filename = "abc.txt";
         DataSource source = new FileDataSource(filename);
         messageBodyPart.setDataHandler(new DataHandler(source));
         messageBodyPart.setFileName(filename);
         multipart.addBodyPart(messageBodyPart);

         // Send the complete message parts
         message.setContent(multipart);

         // Send message
         Transport.send(message);

         System.out.println("Sent message successfully....");
     }  catch (Exception ex) {
            Logger.getLogger(SoldProductsController.class.getName()).log(Level.SEVERE, null, ex);
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

    @FXML
    private void generateAction(ActionEvent event) {
        try {
            LocalDate today=LocalDate.now();
            File file=new File("dailyreport.xlsx");
            WritableWorkbook myexcel=Workbook.createWorkbook(file);
            WritableSheet mysheet=myexcel.createSheet("Report for "+today.toString(),0);
          jxl.write.Label l=new  jxl.write.Label(0,0,"data1");
           mysheet.addCell(l);
           myexcel.write();
           myexcel.close();
           
        } catch (Exception ex) {
            Logger.getLogger(SoldProductsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    }

