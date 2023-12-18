package prog.projeto.models;

public enum CompanySpecialty {
  // TODO: Needs more specialties
  DogGeneral("Consulta geral para cães"),
  DogProsthesis("Prótese para cães"),
  CatGrooming("Cabeleireiro de gatos");

  public final String description;

  CompanySpecialty(String description) {
    this.description = description;
  }
}
