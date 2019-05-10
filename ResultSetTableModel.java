/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.sql.*;
      import java.util.*;
     import javax.swing.table.*;

    public class ResultSetTableModel extends AbstractTableModel {
       private Connection connection;
       private Statement statement;
       private ResultSet resultSet;
       private ResultSetMetaData metaData;
      private int numberOfRows;
    
       // keep track of database connection status 
       private boolean connectedToDatabase = false;
public ResultSetTableModel( String driver, String url,String query ) throws SQLException, ClassNotFoundException
       {
          // load database driver class
         Class.forName( driver );
   
         // connect to database
         connection = DriverManager.getConnection( url );
  
         // create Statement to query database   
         statement = connection.createStatement( 
           ResultSet.TYPE_SCROLL_INSENSITIVE,   
            ResultSet.CONCUR_READ_ONLY );        
   
        // update database connection status
         connectedToDatabase = true;         
   
         // set query and execute it
        setQuery( query );
       
      }

public Class getColumnClass( int column ) throws IllegalStateException
       {
         // ensure database connection is available                        
         if ( !connectedToDatabase )                                       
           throw new IllegalStateException( "Not Connected to Database" ); 
         // determine Java class of column
          try {
             String className = metaData.getColumnClassName( column + 1 );
            
            // return Class object that represents className
           return Class.forName( className );              
          }
          
         // catch SQLExceptions and ClassNotFoundExceptions
         catch ( Exception exception ) {
            exception.printStackTrace();
         }
        
         // if problems occur above, assume type Object 
         return Object.class;
       }
public int getColumnCount() throws IllegalStateException
      {   
          // ensure database connection is available
          if ( !connectedToDatabase ) 
             throw new IllegalStateException( "Not Connected to Database" );
              // determine number of columns
         try {
             return metaData.getColumnCount(); 
          }
          
          // catch SQLExceptions and print error message
          catch ( SQLException sqlException ) {
             sqlException.printStackTrace();
          }
          
          // if problems occur above, return 0 for number of columns
          return 0;
       }
    
       // get name of a particular column in ResultSet
       public String getColumnName( int column ) throws IllegalStateException
       {    
          // ensure database connection is available
          if ( !connectedToDatabase ) 
             throw new IllegalStateException( "Not Connected to Database" );
   
try {
           return metaData.getColumnName( column + 1 );  
        }
        
        // catch SQLExceptions and print error message
        catch ( SQLException sqlException ) {
           sqlException.printStackTrace();
        }
       
       // if problems, return empty string for column name
       return "";
     }
  
     // return number of rows in ResultSet
    public int getRowCount() throws IllegalStateException
     {      
        // ensure database connection is available
        if ( !connectedToDatabase ) 
           throw new IllegalStateException( "Not Connected to Database" );
   
        return numberOfRows;
     }
    public Object getValueAt( int row, int column ) 
        throws IllegalStateException
     {
        // ensure database connection is available
        if ( !connectedToDatabase ) 
           throw new IllegalStateException( "Not Connected to Database" );
  
        // obtain a value at specified ResultSet row and column
        try {
           resultSet.absolute( row + 1 );
           
           return resultSet.getObject( column + 1 );
     }
       
      // catch SQLExceptions and print error message
        catch ( SQLException sqlException ) {
           sqlException.printStackTrace();
        }
       
      // if problems, return empty string object
       return "";
     }
public void setQuery( String query ) 
        throws SQLException, IllegalStateException 
     {       // ensure database connection is available
      if ( !connectedToDatabase ) 
           throw new IllegalStateException( "Not Connected to Database" );
 
        // specify query and execute it
       resultSet = statement.executeQuery( query );
 
      // obtain meta data for ResultSet
       metaData = resultSet.getMetaData();
 
        // determine number of rows in ResultSet
        resultSet.last();                   // move to last row
        numberOfRows = resultSet.getRow();  // get row number      
        
        // notify JTable that model has changed
        fireTableStructureChanged();           
     }

public void disconnectFromDatabase()             
     {                                                
        // close Statement and Connection             
       try {                                         
           statement.close();                         
          connection.close();                        
      }                                             
                                                     
        // catch SQLExceptions and print error message
        catch ( SQLException sqlException ) {         
           sqlException.printStackTrace();            
        }                                             
                                                    
        // update database connection status          
        finally {                                     
           connectedToDatabase = false;               
       }                                             
     }                                                
  
  }  
  
     
