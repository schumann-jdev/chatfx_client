package chat.model.client;

import chat.Main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client
{
  private String ip;
  private Main main;
  private boolean isConnectedToServer;
  
  public Client(String ip, Main main) {
    this.isConnectedToServer = false;
    
    this.ip = ip;
    this.main = main;
  }
  
  private ObjectOutputStream outputToClient;
  private Socket clientSocket;
  private ClientInput clientInput;
  private Thread clientInputThread;
  
  public boolean connectClientToServer() {
    try {
      this.clientSocket = new Socket(this.ip, 1016);
      
      this.outputToClient = new ObjectOutputStream(this.clientSocket.getOutputStream());
      ObjectInputStream inputFromClient = new ObjectInputStream(this.clientSocket.getInputStream());
      
      this.clientInput = new ClientInput(inputFromClient, this.main);
      this.clientInputThread = new Thread(this.clientInput);
      this.clientInputThread.start();
      
      this.isConnectedToServer = true;
    }
    catch (IOException ex) {
      
      this.isConnectedToServer = false;
      return false;
    } 
    
    return true;
  }
  
  public void SendToServer(Object obj) {
    if (!this.isConnectedToServer) {
      System.out.print("Non Connecté au Serveur");
      return;
    } 
    
    try {
      getOutput().writeObject(obj);
      getOutput().flush();
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }
  
  public ObjectOutputStream getOutput() { return this.outputToClient; }
  
  public void closeSocket() {
    this.clientInputThread.stop();
    try {
      this.clientSocket.close();
    } catch (IOException e) {
      
      e.printStackTrace();
    } 
  }
}