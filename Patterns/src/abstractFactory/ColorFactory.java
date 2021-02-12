package abstractFactory;

public class ColorFactory implements AbstractFactory<Color> {
  @Override
  public Color create(String colorType) {
      if (colorType.equalsIgnoreCase("White")) {
          return new White();
      } else if (colorType.equalsIgnoreCase("Black")) {
          return new Black();
      }

      return null;
  }
}
