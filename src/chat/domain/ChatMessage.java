/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.domain;

/**
 *
 * @author Schumann
 */
public class ChatMessage {
    private int idMess;
    private String destinataire, expediteur, corpsMess, heure;

    public int getIdMess() {
        return idMess;
    }

    public void setIdMess(int idMess) {
        this.idMess = idMess;
    }

    public String getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(String destinataire) {
        this.destinataire = destinataire;
    }

    public String getExpediteur() {
        return expediteur;
    }

    public void setExpediteur(String expediteur) {
        this.expediteur = expediteur;
    }
    
    public String getCorpsMess() {
        return corpsMess;
    }

    public void setCorpsMess(String corpsMess) {
        this.corpsMess = corpsMess;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }
}
