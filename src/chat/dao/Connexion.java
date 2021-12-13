/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package chat.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Schumann Juda
 */
public class Connexion {
    public Connection con;
    public Statement stm;
    public Connexion(){
        try {   
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Connexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            con = (Connection) DriverManager.getConnection(Parametre.HOST_BD,Parametre.USERNAME_BD,Parametre.PASSWORD_BD);
            stm = con.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(Connexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("connexion avec la base de données reussi avec succès");
    }
    
    public ResultSet executeQuery(String query){
                ResultSet rs = null;
        try {
            rs = stm.executeQuery(query);
            System.out.println("Query executé : "+query);
        } catch (SQLException ex) {
            Logger.getLogger(Connexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (rs);
    }
    
    public int executeUpdate(String query) throws SQLException{
         int rs = 0 ;
            rs = stm.executeUpdate(query);
            System.out.println("Query executé : "+query);
        
          return (rs);
    }
    
    public void CloseConnexion(){
        try{
            stm.close();
        } catch(SQLException ex){
            Logger.getLogger(Connexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
