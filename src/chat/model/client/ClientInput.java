package chat.model.client;

import chat.Main;
import chat.domain.ChatClient;

import chat.model.Message;
import chat.model.NameAvailable;
import chat.model.RequestToExit;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashSet;


public class ClientInput
  implements Runnable
{
  private ObjectInputStream input;
  boolean moreDataAvailable;
  private Main main;
  public static String nomExped;
    
  public ClientInput(ObjectInputStream inputFromClient, Main main) {
    this.moreDataAvailable = true;
    this.main = main;
    setInput(inputFromClient);
  }
  
  public void run() {
    while (true) {
      Object obj = null;
     
      try {
        obj = getInput().readObject();
      } catch (ClassNotFoundException e) {
        
        e.printStackTrace();
      } catch (IOException e) {
        
        e.printStackTrace();
      } 
     
      if (obj instanceof Message) {
        
        Message msg = (Message)obj;
        String str = msg.getData();
        
        if (str != null) {
          
          String inputNextLine = str;
          System.out.println(inputNextLine);
          this.main.addMessage(msg);
        }  continue;
      } 
      if (obj instanceof HashSet) {
        
        HashSet<String> names = (HashSet)obj;
        this.main.setClientsList(names); continue;
      } 
      if (obj instanceof String) {
        
        String name = (String)obj;
        this.main.addClientToList(name); continue;
        
      } 
      if (obj instanceof RequestToExit) {
        
        String name = ((RequestToExit)obj).getName();
        this.main.removeFromClientList(name); continue;
      } 
      if (obj instanceof NameAvailable) {
        
        NameAvailable nameAvailable = (NameAvailable)obj;
        System.out.print("nameAvailable: " + nameAvailable.getIsNameAvailable());
        nomExped = nameAvailable.getName();
        if (nameAvailable.getIsNameAvailable()) {
          
          this.main.displayChatLayout(nameAvailable.getName());
          
          continue;
        } 
        this.main.alertNameInNotAvailable();
      } 
    } 
  }
  
  public ObjectInputStream getInput() { return this.input; }



  
  public void setInput(ObjectInputStream inputFromClient) { this.input = inputFromClient; }
}