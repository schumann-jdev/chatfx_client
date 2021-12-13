package chat.view;

import chat.Main;

import chat.model.client.Client;
import chat.unit.IpValidation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
 
 public class LogInController
 {
   @FXML
   Button button;
   @FXML
   TextField ipAdressField;
   @FXML
   TextField userNameField;
   private Main main;
   
   @FXML
   private void initialize() {}
   
   @FXML
   private void handelLogIn() {
     if (this.userNameField.getText().trim().isEmpty()) {
       
       displayWarning("Valeur invalide", "vous devez saisir un nom d'utilisateur");
       
       return;
     } 
     if (!IpValidation.isIp(this.ipAdressField.getText())) {
       
       displayWarning("Valeur invalide", "vous devez saisir un adresse IP valide(ex: 127.0.0.1)");
       
       return;
     } 
     boolean res = this.main.connectToServer(this.ipAdressField.getText(), this.userNameField.getText());
     
     if (!res) {
       
       displayWarning("Connection Erreur", "Serveur introuvable, Essayer de changer l'adresse IP");
       return;
     } 
   }
   
   private void displayWarning(String title, String message) {
     Alert alert = new Alert(Alert.AlertType.WARNING);
     alert.initOwner(this.main.getPrimaryStage());
     alert.setTitle(title);
     alert.setHeaderText(message);
     alert.setContentText("Assayer encore une fois");
     alert.showAndWait();
   }
    
   public void setMain(Main main) { this.main = main; }
    
   public void alertNameIsNotAvailable(Client client) {
     displayWarning("Nom d'utilisateur dejà utilisé", "Veillez choisir un autre nom d'utilisateur");
     
     this.ipAdressField.setVisible(false);
     
     this.button.setOnAction(event -> handleNameChanging(client));
   }
    
   private void handleNameChanging(Client client) {
     if (this.userNameField.getText().trim().isEmpty()) {       
       displayWarning("Valeur invalide", "Vous devez saisir un nom d'utilisateur");
     }
     else {       
       client.SendToServer(this.userNameField.getText());
     } 
   }
 }