package prog.projeto.models;

public enum AnimalServiceType {
  Education("Educação"),
  Walking("Passeio"),
  Grooming("Banho e Tosquia");

  public final String description;

  AnimalServiceType(String description) {
    this.description = description;
  }
}
