package singleton;

public class OnlyOne {
  private static OnlyOne instance = new OnlyOne();
  
  private OnlyOne() {}
  
  public static OnlyOne getInstance() {
    return instance;
  }
}
