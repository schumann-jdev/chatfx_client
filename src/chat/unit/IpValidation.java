package chat.unit;

import java.util.regex.Pattern;

public class IpValidation
{
  private static final Pattern PATTERN = Pattern.compile(
      "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

  
  public static boolean isIp(String ip) { return PATTERN.matcher(ip).matches(); }
}