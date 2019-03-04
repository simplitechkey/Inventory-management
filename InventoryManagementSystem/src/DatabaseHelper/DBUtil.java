package DatabaseHelper;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.sun.rowset.CachedRowSetImpl;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author omkarkamate
 */
public class DBUtil {
    static Connection dbConnection;
    //This method is for connecting the app to the database.
    public static void dbConnect(){
        try {
            Class.forName("org.sqlite.JDBC");
            dbConnection=DriverManager.getConnection("jdbc:sqlite:InventoryDB.db");  
            if(dbConnection!=null){
            System.out.println("omkarincon");
            }else{
                 System.out.println("omkardics");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
            
        }
}
    //This method is for disconnecting the app from the database.
     public static void dbDisconnect(){
        try {
            if(dbConnection!=null && dbConnection.isClosed()){
           dbConnection.close();
            }
           
        } catch (SQLException ex) {
            Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
           
        }
     }
     //This method is for insert,delete and update operations.
     public static void  dbexcuteQuery(String sqlStmt) throws Exception{
         Statement Stmnt=null;
         try
         {
             dbConnect();
             Stmnt=dbConnection.createStatement();
             Stmnt.executeUpdate(sqlStmt);
             
         }catch(SQLException e){
         }finally{
             if(Stmnt!=null){
                 Stmnt.close();
             }
             dbDisconnect();
         }
         
     }
     //This method is for retrieving the records from the database.
     public static ResultSet dbExecute(String sqlQuery)throws Exception{
         PreparedStatement stmnt=null;
         ResultSet rs=null;
         CachedRowSetImpl crs=null;
         try{
             dbConnect();
             stmnt=dbConnection.prepareStatement(sqlQuery);
             rs=stmnt.executeQuery();
             crs=new CachedRowSetImpl();
             crs.populate(rs);
         }catch(SQLException e){
             
         }finally{
             if(rs!=null){
                 rs.close();
             }
             if(stmnt!=null){
                 stmnt.close();
             }
             dbDisconnect();
         }
         return crs;
     }
     
     
     
}
