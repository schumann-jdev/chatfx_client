package chat.model;
 
import java.io.Serializable;



public class Message
  implements Serializable
{
  private String from;
  private String to;
  private String data;
    
  public Message(String from, String to, String data) {
    this.from = from;
    this.to = to;
    this.data = data;
  }
 
  public String getData() { return this.data; }
  
  public String getTo() { return this.to; }
  
  public String getFrom() { return this.from; }
  
  public void setFrom(String name) { this.from = name; }
}