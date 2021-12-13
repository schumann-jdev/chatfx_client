package chat;


 
 import chat.model.Message;
 import chat.model.client.Client;
 import chat.view.LogInController;
 import chat.view.MainWindowController;
 import java.io.IOException;
 import java.util.Set;
 import javafx.application.Application;
 import javafx.application.Platform;
 import javafx.beans.property.SimpleStringProperty;
 import javafx.beans.property.StringProperty;
 import javafx.collections.FXCollections;
 import javafx.collections.ObservableList;
 import javafx.fxml.FXMLLoader;
 import javafx.scene.Scene;
 import javafx.scene.layout.AnchorPane;
 import javafx.scene.layout.BorderPane;
 import javafx.stage.Stage;
  
 public class Main
   extends Application
 {
   private Stage primaryStage;
   private Scene logInScene;
   private Scene chatScene;
   private MainWindowController MainWindowController;
   private LogInController logInController;
   protected Client client;
   protected String name;
   private ObservableList<StringProperty> clientNames = FXCollections.observableArrayList();
  
   public void start(Stage primaryStage) {
     this.primaryStage = primaryStage;
     this.primaryStage.setTitle("Chat");
     initRootLayout();
   }
   
   public void initRootLayout() {
     try {
       FXMLLoader loader = new FXMLLoader();
       loader.setLocation(Main.class.getResource("view/LogIn.fxml"));
       AnchorPane logInLayout = (AnchorPane)loader.load();
 
       
       this.logInController = (LogInController)loader.getController();
       this.logInController.setMain(this);
 
       
       this.logInScene = new Scene(logInLayout);
       this.primaryStage.setScene(this.logInScene);
       this.primaryStage.show();
     } catch (IOException e) {
       e.printStackTrace();
     } 
   }
    
   public Stage getPrimaryStage() { return this.primaryStage; }
    
   public static void main(String[] args) { launch(args); }
   
   public boolean connectToServer(String ip, String name) {
     this.name = name;
     this.client = new Client(ip, this);
     
     if (!this.client.connectClientToServer())
     {
       return false;
     }
     
     this.client.SendToServer(name);
     return true;
   }
   
   public void setClientsList(Set<String> names) {
     for (String name : names)
     {
       addClientToList(name);
     }
   }
   
   public void cleanClientsList() { this.clientNames.clear(); }
   
   public void addClientToList(String name) { this.clientNames.add(new SimpleStringProperty(name)); }
   
   public void addMessage(final Message msg) {
     Platform.runLater(new Runnable()
         {
           
           public void run()
           {
             Main.this.MainWindowController.addChatItem(msg);
           }
         });
   }
   
   public void removeFromClientList(String name) {
     for (int i = 0; i < this.clientNames.size(); i++) {
       
       if (((StringProperty)this.clientNames.get(i)).getValue().equals(name)) {
         
         this.clientNames.remove(i);
         break;
       } 
     } 
   } 
   
   public void displayChatLayout(final String name) {
     final Main main = this;
     
     Platform.runLater(new Runnable()
         {
 
 
           
           public void run()
           {
             try {
               FXMLLoader loader = new FXMLLoader();
               loader.setLocation(Main.class.getResource("view/MainWindow.fxml"));
               BorderPane chatLayout = (BorderPane)loader.load();
 
               
               Main.this.MainWindowController = (MainWindowController)loader.getController();
               Main.this.MainWindowController.setClient(Main.this.client);
               Main.this.MainWindowController.setName(name);
               Main.this.MainWindowController.setMain(main);
               Main.this.MainWindowController.setStage(Main.this.primaryStage);
               Main.this.MainWindowController.setClientsNames(Main.this.clientNames);
 
               
               Main.this.chatScene = new Scene(chatLayout);
               Main.this.primaryStage.setScene(Main.this.chatScene);
               Main.this.primaryStage.show();
             }
             catch (IOException e) {
               e.printStackTrace();
             } 
           }
         });
   }
    
   public void alertNameInNotAvailable() {
     Platform.runLater(new Runnable()
         {
           
           public void run()
           {
             Main.this.logInController.alertNameIsNotAvailable(Main.this.client);
           }
         });
   }
 }