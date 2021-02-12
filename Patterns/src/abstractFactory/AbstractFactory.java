package abstractFactory;

public interface AbstractFactory<T> {
  T create(String animalType);
}
