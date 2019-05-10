/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.awt.*;
      import java.awt.event.*;
      import java.sql.*;
      import java.util.*;
      import javax.swing.*;
    import javax.swing.table.*;
    
    public class DisplayQueryResults extends JFrame {
       
       // JDBC driver and database URL
         static final String JDBC_DRIVER = 	"com.microsoft.sqlserver.jdbc.SQLServerDriver";
 static final String DATABASE_URL = "jdbc:sqlserver://localhost:1433;databaseName=Database project;user=ha;password=ha";     
       
       // default query selects all rows from authors table
      static final String DEFAULT_QUERY = "SELECT * FROM Customer";
             private ResultSetTableModel tableModel;
       private JTextArea queryArea;
       
       // create ResultSetTableModel and GUI
      public DisplayQueryResults() 
       {   
          super( "Displaying Query Results" );
try {
    
             // specify location of database on filesystem
             System.setProperty( "db2j.system.home", "C:/Cloudscape_5.0" );
    
            // create TableModel for results of query SELECT * FROM authors 
             tableModel = new ResultSetTableModel( JDBC_DRIVER, DATABASE_URL,
                DEFAULT_QUERY );                                             
    
             // set up JTextArea in which user types queries
             queryArea = new JTextArea( DEFAULT_QUERY, 3, 100 );
             queryArea.setWrapStyleWord( true );
             queryArea.setLineWrap( true );
             
            JScrollPane scrollPane = new JScrollPane( queryArea,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
             
             // set up JButton for submitting queries
             JButton submitButton = new JButton( "Submit Query" );

             // create Box to manage placement of queryArea and 
             // submitButton in GUI
             Box box = Box.createHorizontalBox();
             box.add( scrollPane );
             box.add( submitButton );
             JTable resultTable = new JTable( tableModel );
             
             // place GUI components on content pane
             Container c = getContentPane();         
             c.add( box, BorderLayout.NORTH );
             c.add( new JScrollPane( resultTable ), BorderLayout.CENTER );
    
             // create event listener for submitButton
             submitButton.addActionListener( 
             
                new ActionListener() {
             
                   // pass query to table model
                   public void actionPerformed( ActionEvent event )
                   {
                      // perform a new query
                      try {
                         tableModel.setQuery( queryArea.getText() );
                      }
 
                      // catch SQLExceptions when performing a new query
                      catch ( SQLException sqlException ) {
                         JOptionPane.showMessageDialog( null, 
                            sqlException.getMessage(), "Database error", 
                            JOptionPane.ERROR_MESSAGE );
try {
                           tableModel.setQuery( DEFAULT_QUERY );
                            queryArea.setText( DEFAULT_QUERY );
                         }
                         
                         // catch SQLException when performing default query
                         catch ( SQLException sqlException2 ) {
                            JOptionPane.showMessageDialog( null, 
                               sqlException2.getMessage(), "Database error", 
                               JOptionPane.ERROR_MESSAGE );
             
                            // ensure database connection is closed
                            tableModel.disconnectFromDatabase();   
             
                            System.exit( 1 );   // terminate application
                          
                       }  // end inner catch                   
             
                    } // end outer catch
                    
                 }  // end actionPerformed
                 
              }  // end ActionListener inner class          
              
           ); 
setSize( 500, 250 );
           setVisible( true );   
           
        }  // end try
  
        // catch ClassNotFoundException thrown by 
        // ResultSetTableModel if database driver not found
        catch ( ClassNotFoundException classNotFound ) {
           JOptionPane.showMessageDialog( null, 
                   "Cloudscape driver not found", "Driver not found",
              JOptionPane.ERROR_MESSAGE );
           
           System.exit( 1 );   // terminate application
        } // end catch
        
        // catch SQLException thrown by ResultSetTableModel 
        // if problems occur while setting up database
        // connection and querying database
        catch ( SQLException sqlException ) {
           JOptionPane.showMessageDialog( null, sqlException.getMessage(), 
              "Database error", JOptionPane.ERROR_MESSAGE );
   
           // ensure database connection is closed
           tableModel.disconnectFromDatabase();   
           
           System.exit( 1 );   // terminate application
        }
setDefaultCloseOperation( DISPOSE_ON_CLOSE );
        
        // ensure database connection is closed when user quits application
        addWindowListener(
        
           new WindowAdapter() {
              
              // disconnect from database and exit when window has closed
              public void windowClosed( WindowEvent event )              
              {                                                          
                 tableModel.disconnectFromDatabase();                    
                 System.exit( 0 );                                       
              }                                                          
           }
        );      
        
     }  // end DisplayQueryResults constructor
     
     // execute application
     public static void main( String args[] ) 
     {
        new DisplayQueryResults();     
     }
     
  }  // end class DisplayQueryResults
