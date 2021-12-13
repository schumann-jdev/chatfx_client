package chat.model;

import java.io.Serializable;

public class NameAvailable implements Serializable{
    private boolean isNameAvailable;
    private String name;

    public NameAvailable(String name, boolean isNameAvailable) {
      this.isNameAvailable = isNameAvailable;
      this.name = name;
    }

    public boolean getIsNameAvailable() { return this.isNameAvailable; }

    public String getName() { return this.name; }
}