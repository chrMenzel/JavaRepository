package abstractFactory;

public class AnimalFactory implements AbstractFactory<Animal> {
  @Override
  public Animal create(String animalType) {
      if (animalType.equalsIgnoreCase("Dog")) {
          return new Dog();
      } else if (animalType.equalsIgnoreCase("Frog")) {
          return new Frog();
      }

      return null;
  }
}
