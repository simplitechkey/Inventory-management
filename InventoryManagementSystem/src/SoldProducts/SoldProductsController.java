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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
import jxl.format.Font;
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
    @FXML
    private ComboBox<String> monthList;
    
     ArrayList<String> columnNamesList=new ArrayList<>(Arrays.asList("Product Id","Product Name","Product Sale Date","Product Price","Mode of Payment"));
    ObservableList<SoldProductItem> displayList=FXCollections.observableArrayList();
     String [] months={"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    ArrayList<String> monthNamesList =new ArrayList<>(Arrays.asList(months));
    
    
    @FXML
    private Label lblSumMonth;
    @FXML
    private TextField fieldYear;

    /*
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         try {
            monthList.getItems().addAll(monthNamesList);
             TableColumn<SoldProductItem,String>productId=new TableColumn("Product Id");
            productId.setCellValueFactory(new PropertyValueFactory<>("productId"));
            
            TableColumn<SoldProductItem,String>productName=new TableColumn("Product Name");
            productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
            
            TableColumn<SoldProductItem,Double>productPrice=new TableColumn("Product Price");
            productPrice.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
            
            
            TableColumn<SoldProductItem,String>productSaleDate=new TableColumn("Produte Sale Date");
            productSaleDate.setCellValueFactory(new PropertyValueFactory<>("productSaleDate"));
            
             TableColumn<SoldProductItem,String>MOP=new TableColumn("Mode Of Payment");
            MOP.setCellValueFactory(new PropertyValueFactory<>("MOP"));
            tableSoldProducts.getColumns().addAll(productId,productName,productPrice,productSaleDate,MOP);
            tableSoldProducts.setItems(DBDAO.getAllSoldProducts());
            
        } catch (Exception ex) {
            Logger.getLogger(ProductEntryController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }    
    
        private boolean netIsAvailable() {
    try {
        final URL url = new URL("http://www.google.com");
        final URLConnection conn = url.openConnection();
        conn.connect();
        conn.getInputStream().close();
        return true;
    } catch (MalformedURLException e) {
        throw new RuntimeException(e);
    } catch (IOException e) {
        return false;
    }
}

    @FXML
    private void sortOnDateAction(ActionEvent event) throws Exception {
       String sum = "0";
       String date=dateField.getValue().toString();
       if(!date.isEmpty()){
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
    }else{
            Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please Select Some Date");
                alert.showAndWait();
       }
    }
    
    

    @FXML
    private void sendReportEmailAction(ActionEvent event) {
        
         try {
            ObservableList<SoldProductItem> todayIdsList = FXCollections.observableArrayList();
            LocalDate today=LocalDate.now();
            
            File file=new File("dailyreport.xls");
            
            WritableWorkbook myexcel=Workbook.createWorkbook(file);
            WritableSheet mysheet=myexcel.createSheet("Report for "+today.toString(),0);
          
            todayIdsList=DBDAO.getSoldProductbyDate(today.toString());
            for(int i=0;i<columnNamesList.size();i++){
              jxl.write.Label l=new  jxl.write.Label(i,0,columnNamesList.get(i));
               mysheet.addCell(l);
                         
               for(int j=0;j<todayIdsList.size();j++){
                      jxl.write.Label l2=new  jxl.write.Label(0,j+1,todayIdsList.get(j).getProductId());
                      
                      mysheet.addCell(l2);
               }
                for(int j=0;j<todayIdsList.size();j++){
                      jxl.write.Label l2=new  jxl.write.Label(1,j+1,todayIdsList.get(j).getProductName());
                      mysheet.addCell(l2);
               }
                 for(int j=0;j<todayIdsList.size();j++){
                      jxl.write.Label l2=new  jxl.write.Label(2,j+1,todayIdsList.get(j).getProductSaleDate());
                      mysheet.addCell(l2);
               }
                  for(int j=0;j<todayIdsList.size();j++){
                      jxl.write.Label l2=new  jxl.write.Label(3,j+1,String.valueOf(todayIdsList.get(j).getProductPrice()));
                      mysheet.addCell(l2);
                      
                      if(j==todayIdsList.size()-1){
                     jxl.write.Label l3=new  jxl.write.Label(3,j+2,"Total Collection :"+DBDAO.getSumOfSoldProductbyDate(today.toString()));
                     mysheet.addCell(l3);
                      }
                      
               }
                  
                  for(int j=0;j<todayIdsList.size();j++){
                      jxl.write.Label l2=new  jxl.write.Label(4,j+1,todayIdsList.get(j).getMOP());
                      mysheet.addCell(l2);
               }
            }
               
           myexcel.write();
           myexcel.close();
           
        } catch (Exception ex) {
            Logger.getLogger(SoldProductsController.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        
        if (netIsAvailable()){
            
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
                        @Override
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
                String filename ="dailyreport.xls";
                DataSource source = new FileDataSource(filename);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(filename);
                multipart.addBodyPart(messageBodyPart);
                
                // Send the complete message parts
                message.setContent(multipart);
                
                // Send message
                Transport.send(message);
                
                 Alert alert=new Alert(Alert.AlertType.INFORMATION);   
                 alert.setContentText("Mail Report Sent Successfully");   
                 alert.showAndWait();
            }  catch (Exception ex) {
                Logger.getLogger(SoldProductsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setContentText("No Internet Connection. Please check your network and try again");
            alert.showAndWait();
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
           stage.setResizable(false);
           stage.setScene(scene);
           stage.show();
       } catch (IOException ex) {
           Logger.getLogger(SellProductController.class.getName()).log(Level.SEVERE, null, ex);
       }
    }

    private void generateAction() {
        try {
            ObservableList<SoldProductItem> todayIdsList = FXCollections.observableArrayList();
            LocalDate today=LocalDate.now();
            
            File file=new File("monthlyreport.xls");
            
            WritableWorkbook myexcel=Workbook.createWorkbook(file);
            WritableSheet mysheet=myexcel.createSheet("Report for "+today.getMonth().toString(),0);
         
            todayIdsList=displayList;
            double sum=0.0;
                    for(int i=0;i<columnNamesList.size();i++){
              jxl.write.Label l=new  jxl.write.Label(i,0,columnNamesList.get(i));
               mysheet.addCell(l);
                         
               for(int j=0;j<todayIdsList.size();j++){
                      jxl.write.Label l2=new  jxl.write.Label(0,j+1,todayIdsList.get(j).getProductId());
                      
                      mysheet.addCell(l2);
               }
                for(int j=0;j<todayIdsList.size();j++){
                      jxl.write.Label l2=new  jxl.write.Label(1,j+1,todayIdsList.get(j).getProductName());
                      mysheet.addCell(l2);
               }
                 for(int j=0;j<todayIdsList.size();j++){
                      jxl.write.Label l2=new  jxl.write.Label(2,j+1,todayIdsList.get(j).getProductSaleDate());
                      mysheet.addCell(l2);
               }
                  for(int j=0;j<todayIdsList.size();j++){
                      jxl.write.Label l2=new  jxl.write.Label(3,j+1,String.valueOf(todayIdsList.get(j).getProductPrice()));
                      mysheet.addCell(l2);
                      sum+=todayIdsList.get(j).getProductPrice();
                      if(j==todayIdsList.size()-1){
                     jxl.write.Label l3=new  jxl.write.Label(3,j+2,"Total Collection :"+String.valueOf(sum));
                     mysheet.addCell(l3);
                      }
                      
               }
                  
                   
                  for(int j=0;j<todayIdsList.size();j++){
                      jxl.write.Label l2=new  jxl.write.Label(4,j+1,todayIdsList.get(j).getMOP());
                      mysheet.addCell(l2);
               }
            }
               
           myexcel.write();
           myexcel.close();
           
        } catch (Exception ex) {
            Logger.getLogger(SoldProductsController.class.getName()).log(Level.SEVERE, null, ex);
        }
                
    }

    @FXML
    private void sendMonthlyReportAction(ActionEvent event) {
        
         if (netIsAvailable()){
            generateAction();
            String to = "nikteshriya@gmail.com";
            
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
                        @Override
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
                String filename ="monthlyreport.xls";
                DataSource source = new FileDataSource(filename);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(filename);
                multipart.addBodyPart(messageBodyPart);
                
                // Send the complete message parts
                message.setContent(multipart);
                
                // Send message
                Transport.send(message);
                
                 Alert alert=new Alert(Alert.AlertType.INFORMATION);  
                 alert.setContentText("Mail Report Sent Successfully");    
                 alert.showAndWait();
            }  catch (Exception ex) {
                Logger.getLogger(SoldProductsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setContentText("No Internet Connection. Please check your network and try again");
            alert.showAndWait();
        }
        
    }
    
    
    

    @FXML
    private void viewMonthlySaleAction(ActionEvent event) {
        try {
         if(!monthList.getSelectionModel().isEmpty()){
            if(!fieldYear.getText().trim().isEmpty()){
            double sum = 0.0;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                           
            String selectedMonth=monthList.getValue();
            
            ObservableList<SoldProductItem> sortedproductList=DBDAO.getAllSoldProducts();
            
            if(!sortedproductList.isEmpty()){
                                
                for(int i=0;i<sortedproductList.size();i++)
                {
                     LocalDate date = LocalDate.parse(sortedproductList.get(i).getProductSaleDate(), formatter);
                
                if((selectedMonth.toUpperCase()).equals(date.getMonth().toString()) && (fieldYear.getText().trim()).equals(String.valueOf(date.getYear()))){
                    displayList.add(sortedproductList.get(i));
                    sum+=sortedproductList.get(i).getProductPrice();
                    
                    
                }
                }
                tableSoldProducts.setItems(displayList);
                lblSumMonth.setText("Total Collectioin on this Month is :"+String.valueOf(sum));
                     displayList.clear();
                
            }else{
                Alert alert=new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("No Records Found");
                alert.showAndWait();
            }
        
        }else{
                 Alert alert=new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Please Enter Year");
                alert.showAndWait();
                }
        }
        else{
                
                 Alert alert=new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Please Select Month");
                alert.showAndWait();
                }
        }
         catch (Exception ex) {
            Logger.getLogger(SoldProductsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @FXML
    private void sendReportDateEmailAction(ActionEvent event) {
         try {
            ObservableList<SoldProductItem> todayIdsList = FXCollections.observableArrayList();
            String today=dateField.getValue().toString();
           
            if(!today.isEmpty()){
            File file=new File("dailyreport.xls");
            
            WritableWorkbook myexcel=Workbook.createWorkbook(file);
            WritableSheet mysheet=myexcel.createSheet("Report for "+today.toString(),0);
           
            todayIdsList=DBDAO.getSoldProductbyDate(today.toString());
            for(int i=0;i<columnNamesList.size();i++){
              jxl.write.Label l=new  jxl.write.Label(i,0,columnNamesList.get(i));
               mysheet.addCell(l);
                         
               for(int j=0;j<todayIdsList.size();j++){
                      jxl.write.Label l2=new  jxl.write.Label(0,j+1,todayIdsList.get(j).getProductId());
                      
                      mysheet.addCell(l2);
               }
                for(int j=0;j<todayIdsList.size();j++){
                      jxl.write.Label l2=new  jxl.write.Label(1,j+1,todayIdsList.get(j).getProductName());
                      mysheet.addCell(l2);
               }
                 for(int j=0;j<todayIdsList.size();j++){
                      jxl.write.Label l2=new  jxl.write.Label(2,j+1,todayIdsList.get(j).getProductSaleDate());
                      mysheet.addCell(l2);
               }
                  for(int j=0;j<todayIdsList.size();j++){
                      jxl.write.Label l2=new  jxl.write.Label(3,j+1,String.valueOf(todayIdsList.get(j).getProductPrice()));
                      mysheet.addCell(l2);
                      
                      if(j==todayIdsList.size()-1){
                     jxl.write.Label l3=new  jxl.write.Label(3,j+2,"Total Collection :"+DBDAO.getSumOfSoldProductbyDate(today.toString()));
                     mysheet.addCell(l3);
                      }
                      
               }
            }
               
           myexcel.write();
           myexcel.close();
           
            
                     
        
        if (netIsAvailable()){
            
            String to = "nikteshriya@gmail.com";
            
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
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    });
            
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
                String filename ="dailyreport.xls";
                DataSource source = new FileDataSource(filename);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(filename);
                multipart.addBodyPart(messageBodyPart);
                
                // Send the complete message parts
                message.setContent(multipart);
                
                // Send message
                Transport.send(message);
                
                 Alert alert=new Alert(Alert.AlertType.INFORMATION); 
                 alert.setContentText("Mail Report Sent Successfully");
                 alert.showAndWait();
            }else{
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setContentText("No Internet Connection. Please check your network and try again");
            alert.showAndWait();
        }
    } else{
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please Select Some Date");
                alert.showAndWait();
                
            }
    }
            
            catch (Exception ex) {
                Logger.getLogger(SoldProductsController.class.getName()).log(Level.SEVERE, null, ex);
            }
}
}

