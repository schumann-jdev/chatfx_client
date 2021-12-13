/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.dao;

import chat.domain.ChatMessage;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Schumann
 */
public class ChatDao {
    Connexion conn = new Connexion();
    ResultSet rs = null;
    
    public ResultSet getAllMess(String nomExped){
        String query = "SELECT * FROM message WHERE \"expediteur\"='"+nomExped+"' OR \"destinataire\"='"+nomExped+"' order by \"idMess\"";
        rs = conn.executeQuery(query);
        return rs;
    }
    public ResultSet getMessBetween(String nomExped, String nomDest) {
        String query = "SELECT * FROM message WHERE \"expediteur\"='"+nomExped+"' AND \"destinataire\"='"+nomDest+"' OR \"expediteur\"='"+nomDest+"' AND \"destinataire\"='"+nomExped+"' order by \"idMess\"";
        rs = conn.executeQuery(query);
        return rs;
    }
    public void addMess(ChatMessage mess) throws SQLException{
        String query = "INSERT INTO message (\"destinataire\", \"expediteur\", \"corpsMess\", \"heure\") "
                + "VALUES('"+mess.getDestinataire()+"', '"+mess.getExpediteur()+"', '"+mess.getCorpsMess()+"', '"+mess.getHeure()+"')";
        conn.executeUpdate(query);
    }
}
