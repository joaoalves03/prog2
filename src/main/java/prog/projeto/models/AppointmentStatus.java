package prog.projeto.models;

public enum AppointmentStatus {
  Scheduled("Marcado"),
  AwaitingPayment("Por pagar"),
  Complete("Efetuado"),
  Cancelled("Cancelado");

  public final String description;
  AppointmentStatus(String description) { this.description = description; }
}
