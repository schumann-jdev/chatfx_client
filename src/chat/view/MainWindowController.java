 package chat.view;
 
import chat.Main;
import chat.dao.ChatDao;
import chat.domain.ChatClient;
import chat.domain.ChatMessage;
 
 import chat.model.Message;
 import chat.model.RequestToExit;
 import chat.model.client.Client;
import chat.model.client.ClientInput;
import java.sql.ResultSet;
import java.sql.SQLException;
 import java.text.SimpleDateFormat;
 import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
 import javafx.application.Platform;
 import javafx.beans.property.DoubleProperty;
 import javafx.beans.property.SimpleDoubleProperty;
 import javafx.beans.property.StringProperty;
 import javafx.beans.value.ChangeListener;
 import javafx.beans.value.ObservableValue;
 import javafx.collections.ObservableList;
 import javafx.fxml.FXML;
 import javafx.geometry.Pos;
 import javafx.scene.Node;
 import javafx.scene.SnapshotParameters;
 import javafx.scene.control.Button;
 import javafx.scene.control.Label;
 import javafx.scene.control.ScrollPane;
 import javafx.scene.control.TableColumn;
 import javafx.scene.control.TableView;
 import javafx.scene.control.TextField;
 import javafx.scene.image.Image;
 import javafx.scene.image.ImageView;
 import javafx.scene.image.WritableImage;
 import javafx.scene.layout.BorderPane;
 import javafx.scene.layout.HBox;
 import javafx.scene.layout.VBox;
 import javafx.scene.paint.Color;
 import javafx.scene.shape.Rectangle;
 import javafx.scene.text.Text;
 import javafx.stage.Stage;
 import javafx.stage.WindowEvent;
  
 public class MainWindowController
 {
   @FXML
   private TableView<StringProperty> table;
   @FXML
   private TableColumn<StringProperty, String> column;
   private Client client;
   @FXML
   private TextField textField;
   @FXML
   private Button button;
   @FXML
   private VBox vbox;
   @FXML
   private ScrollPane scrollPane;
   @FXML
   private Label nomUtilisateur;
   private String name;
   private Stage stage;
   private Main main;
   
   private ResultSet rs = null;
   @FXML
   private void initialize() {
    this.column.setCellValueFactory(cellData -> (ObservableValue)cellData.getValue());
     
    DoubleProperty wProperty = new SimpleDoubleProperty();
    wProperty.bind(this.vbox.heightProperty());
    wProperty.addListener(new ChangeListener()
         {
           public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            MainWindowController.this.scrollPane.setVvalue(MainWindowController.this.scrollPane.getVmax());
            System.out.print("hhhh");
           }
         });
    this.nomUtilisateur.setText(ClientInput.nomExped);
    this.scrollPane.setFitToHeight(true);
    this.scrollPane.setFitToWidth(true);
    this.scrollPane.setStyle("-fx-background: #FFFFFF;");
    System.out.println("tokony we eto zany no manw get\n de ito ny nomN'l exped: "+ClientInput.nomExped);
    this.table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> 
        privateMessage(newValue));
    ChatDao chD = new ChatDao();
    rs = chD.getAllMess(ClientInput.nomExped);
       try {
           while(rs.next()){
               ChatMessage chMess = new ChatMessage();
               chMess.setDestinataire(rs.getString("destinataire"));
               chMess.setExpediteur(rs.getString("expediteur"));
               chMess.setCorpsMess(rs.getString("corpsMess"));
               chMess.setHeure(rs.getString("heure"));
               addChatItem(chMess);
               System.out.println("From: "+rs.getString("expediteur")+" to :"+rs.getString("destinataire")+" message:"+rs.getString("corpsMess"));
           }  } catch (SQLException ex) {
           Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
       }
   }
    
   private void privateMessage(StringProperty newValue) {
    if (newValue != null) {
       
      this.button.setText("Envoyer à " + newValue.getValue());
      System.out.println("mety hety rah manw resultSet eto a");
      this.vbox.getChildren().clear();
      ChatDao chD = new ChatDao();
    rs = chD.getMessBetween(ClientInput.nomExped, newValue.getValue());
       try {
           while(rs.next()){
               ChatMessage chMess = new ChatMessage();
               chMess.setDestinataire(rs.getString("destinataire"));
               chMess.setExpediteur(rs.getString("expediteur"));
               chMess.setCorpsMess(rs.getString("corpsMess"));
               chMess.setHeure(rs.getString("heure"));
               addChatItem(chMess);
           }  } catch (SQLException ex) {
           Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
       }
     }
     else {
       
      this.table.getSelectionModel().clearSelection();
      this.button.setText("Envoyer");
     } 
   }
    
  public Client getClient() { return this.client; }
   
  public void setClient(Client client) { this.client = client; }
   
  public void setClientsNames(ObservableList<StringProperty> clientNames) { this.table.setItems(clientNames); }
   
   @FXML
   private void handleSendMessage() {
    if (!this.textField.getText().trim().isEmpty()) {
       
      int selectedIndex = this.table.getSelectionModel().getSelectedIndex();
       
      if (selectedIndex < 0) {
         
        Message msg = new Message(this.name, null, this.textField.getText());
        this.client.SendToServer(msg);
       }
       else {
        StringProperty from = (StringProperty)this.table.getSelectionModel().getSelectedItem();
        Message msg = new Message(this.name, from.getValue(), this.textField.getText());
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("hh:mm a");
        ChatDao chatD = new ChatDao();
        ChatMessage chatM = new ChatMessage();
        chatM.setDestinataire(from.getValue());
        chatM.setExpediteur(this.name);
        chatM.setCorpsMess(this.textField.getText());
        chatM.setHeure(ft.format(dNow));
          try {
              chatD.addMess(chatM);
          } catch (SQLException ex) {
              Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
          }
        this.client.SendToServer(msg);
        this.table.getSelectionModel().clearSelection();
        this.button.setText("Envoyer");
       } 
       
      this.textField.setText("");
     } 
   }
   
  public void setName(String name) { this.name = name; }
   
   @FXML
   public void handleDisconnect() {
    this.client.SendToServer(new RequestToExit(this.name));
    this.main.cleanClientsList();
    this.client.closeSocket();
    this.main.initRootLayout();
   }
   
   public void addChatItem(Message item) {
    if (item != null) {    
      BorderPane borderPane = new BorderPane();
      ImageView profileImage = new ImageView(new Image(MainWindowController.class.getResourceAsStream("pictures/profile.png")));
      profileImage.setFitHeight(32.0D);
      profileImage.setFitWidth(32.0D);     
      Rectangle clip = new Rectangle(
          profileImage.getFitWidth(), profileImage.getFitHeight());
       
      clip.setArcWidth(30.0D);
      clip.setArcHeight(30.0D);
      profileImage.setClip(clip);
      SnapshotParameters parameters = new SnapshotParameters();
      parameters.setFill(Color.TRANSPARENT);
      WritableImage image = profileImage.snapshot(parameters, null);
      profileImage.setClip(null);
      profileImage.setImage(image);
       
      ImageView arrowImage = new ImageView(new Image(MainWindowController.class.getResourceAsStream("pictures/arrow1.png")));
      ImageView arrowImage2 = new ImageView(new Image(MainWindowController.class.getResourceAsStream("pictures/arrow1.png")));
      Label nameLabel = new Label(item.getFrom());
      nameLabel.setStyle(" -fx-text-alignment: center; -fx-padding: 2;");
       
      HBox messageBox = null;
      ImageView sendImage = null;
       
      Text textLabel = null;
      BorderPane pane = null;
      BorderPane bp = null;
       
      textLabel = new Text();
      String messageText = splitTolines(item.getData());
      textLabel.setText(messageText);
      
      pane = new BorderPane(textLabel);
      pane.setStyle("-fx-background-color: #f1f3f4; -fx-padding: 10;\n-fx-spacing: 5;");
       
      messageBox = new HBox(new Node[] { arrowImage, pane });
       
      VBox imageAndNameBox = new VBox(new Node[] { profileImage, nameLabel });
       
      Date dNow = new Date();
      SimpleDateFormat ft = new SimpleDateFormat("hh:mm a");
      Label timeLabel = new Label(ft.format(dNow));
      timeLabel.setStyle("-fx-font: 9px Tahoma;  -fx-width: 100%");
       
      HBox timeVBox = new HBox(new Node[] { arrowImage2, timeLabel });
      arrowImage2.setVisible(false);
      VBox timaAndMessageVBox = new VBox(new Node[] { messageBox, timeVBox });
      borderPane.setCenter(timaAndMessageVBox);
       
      if (item.getFrom().equals(this.name)) {
         
        borderPane.setLeft(null);
        borderPane.setRight(imageAndNameBox);
        timeVBox.setAlignment(Pos.BOTTOM_RIGHT);
        messageBox.setAlignment(Pos.BOTTOM_RIGHT);
        timaAndMessageVBox.setAlignment(Pos.CENTER_RIGHT);
         
        arrowImage = new ImageView(new Image(MainWindowController.class.getResourceAsStream("pictures/arrow2.png")));
        messageBox.getChildren().clear();
         
        pane = new BorderPane(textLabel);
        pane.setStyle("-fx-background-color: #d2e3fc; -fx-padding: 10;\n-fx-spacing: 5;");
         
        System.out.println("text: " + textLabel.getText());
        messageBox.getChildren().add(pane);
         
        messageBox.getChildren().add(arrowImage);
        arrowImage.setRotate(180.0D);
         
        timeVBox.getChildren().clear();
        timeVBox.getChildren().add(timeLabel);
        timeVBox.getChildren().add(arrowImage2);
        imageAndNameBox.setAlignment(Pos.BOTTOM_RIGHT);
       }
       else {         
        borderPane.setLeft(imageAndNameBox);
       } 
       
      this.vbox.getChildren().add(borderPane);
     } 
   }
   
   public void addChatItem(ChatMessage item) {
    if (item != null) {    
      BorderPane borderPane = new BorderPane();
      ImageView profileImage = new ImageView(new Image(MainWindowController.class.getResourceAsStream("pictures/profile.png")));
      profileImage.setFitHeight(32.0D);
      profileImage.setFitWidth(32.0D);     
      Rectangle clip = new Rectangle(
          profileImage.getFitWidth(), profileImage.getFitHeight());
       
      clip.setArcWidth(30.0D);
      clip.setArcHeight(30.0D);
      profileImage.setClip(clip);
      SnapshotParameters parameters = new SnapshotParameters();
      parameters.setFill(Color.TRANSPARENT);
      WritableImage image = profileImage.snapshot(parameters, null);
      profileImage.setClip(null);
      profileImage.setImage(image);
       
      ImageView arrowImage = new ImageView(new Image(MainWindowController.class.getResourceAsStream("pictures/arrow1.png")));
      ImageView arrowImage2 = new ImageView(new Image(MainWindowController.class.getResourceAsStream("pictures/arrow1.png")));
      Label nameLabel = new Label(item.getExpediteur());
      nameLabel.setStyle(" -fx-text-alignment: center; -fx-padding: 2;");
       
      HBox messageBox = null;
      ImageView sendImage = null;
       
      Text textLabel = null;
      BorderPane pane = null;
      BorderPane bp = null;
       
      textLabel = new Text();
      String messageText = splitTolines(item.getCorpsMess());
      textLabel.setText(messageText);
      
      pane = new BorderPane(textLabel);
      pane.setStyle("-fx-background-color: #f1f3f4; -fx-padding: 10;\n-fx-spacing: 5;");
       
      messageBox = new HBox(new Node[] { arrowImage, pane });
       
      VBox imageAndNameBox = new VBox(new Node[] { profileImage, nameLabel });
       
      Label timeLabel = new Label(item.getHeure());
      timeLabel.setStyle("-fx-font: 9px Tahoma;  -fx-width: 100%");
       
      HBox timeVBox = new HBox(new Node[] { arrowImage2, timeLabel });
      arrowImage2.setVisible(false);
      VBox timaAndMessageVBox = new VBox(new Node[] { messageBox, timeVBox });
      borderPane.setCenter(timaAndMessageVBox);
      if (item.getExpediteur().equals(ClientInput.nomExped)) {
         
        borderPane.setLeft(null);
        borderPane.setRight(imageAndNameBox);
        timeVBox.setAlignment(Pos.BOTTOM_RIGHT);
        messageBox.setAlignment(Pos.BOTTOM_RIGHT);
        timaAndMessageVBox.setAlignment(Pos.CENTER_RIGHT);
         
        arrowImage = new ImageView(new Image(MainWindowController.class.getResourceAsStream("pictures/arrow2.png")));
        messageBox.getChildren().clear();
         
        pane = new BorderPane(textLabel);
        pane.setStyle("-fx-background-color: #d2e3fc; -fx-padding: 10;\n-fx-spacing: 5;");
         
        System.out.println("text: " + textLabel.getText());
        messageBox.getChildren().add(pane);
         
        messageBox.getChildren().add(arrowImage);
        arrowImage.setRotate(180.0D);
         
        timeVBox.getChildren().clear();
        timeVBox.getChildren().add(timeLabel);
        timeVBox.getChildren().add(arrowImage2);
        imageAndNameBox.setAlignment(Pos.BOTTOM_RIGHT);
       }
       else {         
        borderPane.setLeft(imageAndNameBox);
       } 
       
      this.vbox.getChildren().add(borderPane);
     } 
   }
   
   private String splitTolines(String text) {
    String newText = "";
    int charLimit = 50;
    char[] chars = text.toCharArray();
    boolean endOfString = false;
    int start = 0;
    int end = start;
     
    if (text.length() == 1)
     {
      return text;
     }
     
    while (start < chars.length - 1) {       
      int charCount = 0;
      int lastSpace = 0;
      while (charCount < charLimit) {
        if (chars[charCount + start] == ' ')
         {
          lastSpace = charCount;
         }
        charCount++;
        if (charCount + start == text.length()) {
          endOfString = true;
           break;
         } 
       } 
      end = endOfString ? text.length() : (
        (lastSpace > 0) ? (lastSpace + start) : (charCount + start));
      newText = String.valueOf(newText) + text.substring(start, end) + "\n";
      start = end + 1;
     } 
    return newText.substring(0, newText.length() - 1);
   }
   
   public void setStage(Stage primaryStage) {
    this.stage = primaryStage;
    this.stage.setOnCloseRequest(e -> {           
          this.client.SendToServer(new RequestToExit(this.name));
          this.client.closeSocket();
          Platform.exit();
          System.exit(0);
         });
   }
   
  public void setMain(Main main) { this.main = main; }
 }