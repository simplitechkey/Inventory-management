/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseHelper;

import Beans.ProductItem;
import Beans.SoldProductItem;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author omkarkamate
 */
public class DBDAO {
    
    
    
     public static void insertProduct(String productId, String productName, double productPrice) //public static void insertBook(String bookId, String bookSubject, String bookBranch)
    {
       // String sql = "INSERT INTO `productsTable`(`productId`,`productName`,`productPrice`) VALUES "+(1,NULL,NULL);";
         String sql="insert into productsTable ( productId, productName, productPrice) values ('"+productId+"','"+productName+"',"+productPrice+");";
        try {
            DBUtil.dbexcuteQuery(sql);
        } catch (Exception e) {

        }
    }
     
     public static void insertSoldProduct(String productId, String productName, double productPrice,String date,String MOP) //public static void insertBook(String bookId, String bookSubject, String bookBranch)
    {
       // String sql = "INSERT INTO `productsTable`(`productId`,`productName`,`productPrice`) VALUES "+(1,NULL,NULL);";
         String sql="insert into tableSoldProducts ( productId, productName, productPrice,productSaleDate,MOP) values ('"+productId+"','"+productName+"',"+productPrice+",'"+date+"','"+MOP+"');";
        try {
            DBUtil.dbexcuteQuery(sql);
        } catch (Exception e) {

        }
    }
     
     


   

    public static ResultSet getProductbyID(String productId) {

        String sql = "select * from productsTable where productId = '" + productId + "'";
        ResultSet rs=null;
        try {
          
          rs= DBUtil.dbExecute(sql);

           
             while (rs.next()) {
               return rs;
             }
        } catch (Exception e) {

        }
         return rs;
    }
    
    public static void deleteProductbyID(String productId) {

        String sql = "delete from productsTable where productId = '" + productId + "'";

        try {
           DBUtil.dbexcuteQuery(sql);
        } catch (Exception e) {

        }
    }
    
    

    public static ObservableList<ProductItem> getAllProducts() throws Exception {
        
         String sql = "select * from productsTable";
        try {

            ResultSet rs = DBUtil.dbExecute(sql);

            ObservableList<ProductItem> productList = FXCollections.observableArrayList();
             while (rs.next()) {
                productList.add(new ProductItem(rs.getString("productId"), rs.getString("productName"), rs.getDouble("productPrice")));
                //System.out.println("omkar"+rs.getString("bookBranch"));
            }
            return productList;

           

        } catch (Exception e) {
            throw e;
        }
    }
    
       public static  ObservableList<SoldProductItem> getSoldProductbyDate(String date) throws Exception {

        String sql = "select * from tableSoldProducts where productSaleDate = '" + date + "'";
        ResultSet rs=null;
        try {
          
             rs = DBUtil.dbExecute(sql);

            ObservableList<SoldProductItem> productList = FXCollections.observableArrayList();
           
             while (rs.next()) {
                productList.add(new SoldProductItem(rs.getString("productId"), rs.getString("productName"), rs.getDouble("productPrice"),rs.getString("productSaleDate"),rs.getString("MOP")));
               
            }
            
            return productList;

        } catch (Exception e) {
            throw e;
        }

    }

   
      public static  String getSumOfSoldProductbyDate(String date) throws Exception {

        double sum=0;
       
        try {
            ObservableList<SoldProductItem> productList =DBDAO.getSoldProductbyDate(date);
            for (int i=0;i<productList.size();i++ ){
                sum+=productList.get(i).getProductPrice();
            }
            
            return String.valueOf(sum);
           
        } catch (Exception e) {
            throw e;
        }

    }
     
    
     public static ObservableList<SoldProductItem> getAllSoldProducts() throws Exception {
        
         String sql = "select * from tableSoldProducts";
        try {

            ResultSet rs = DBUtil.dbExecute(sql);

            ObservableList<SoldProductItem> productList = FXCollections.observableArrayList();
             while (rs.next()) {
                productList.add(new SoldProductItem(rs.getString("productId"), rs.getString("productName"), rs.getDouble("productPrice"),rs.getString("productSaleDate"),rs.getString("MOP")));
               
            }
            return productList;

           

        } catch (Exception e) {
            throw e;
        }
    }
     
     
      
    
}
