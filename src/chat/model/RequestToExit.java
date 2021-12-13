package chat.model;

import java.io.Serializable;

public class RequestToExit
  implements Serializable
{
  private String name;
    
  public RequestToExit(String name) { this.name = name; }
  
  public String getName() { return this.name; }
}