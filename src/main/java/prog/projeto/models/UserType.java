package prog.projeto.models;

public enum UserType {
  Client("Cliente"),
  ServiceProvider("Prestador"),
  Staff("Funcionário"),
  Admin("Admin");

  public final String description;
  UserType(String description) { this.description = description; }
}
