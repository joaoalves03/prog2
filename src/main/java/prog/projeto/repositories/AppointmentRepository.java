package prog.projeto.repositories;

import prog.projeto.models.Appointment;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentRepository extends Repository<Appointment> {
  private static AppointmentRepository instance;

  private AppointmentRepository(){fileName = "appointments.dat";}

  public static AppointmentRepository getInstance() {
    if(instance == null) instance = new AppointmentRepository();
    return instance;
  }

  public List<Appointment> getByClient(int id) {
    return this.entities.values().stream().filter(
        x -> x.getClientID() == id
    ).toList();
  }

  public List<Appointment> getByEmployee(int id) {
    return this.entities.values().stream().filter(
        x -> x.getEmployeeID() == id
    ).toList();
  }

  public List<Appointment> getAppointmentsForDay(LocalDate day) {
    return this.entities.values().stream()
            .filter(appointment -> appointment.getDate().isEqual(day))
            .collect(Collectors.toList());
  }


  @Override
  public int getId(Appointment entity) {
    return entity.getId();
  }
}
