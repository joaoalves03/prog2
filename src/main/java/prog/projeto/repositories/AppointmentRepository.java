package prog.projeto.repositories;

import prog.projeto.models.Appointment;

import java.util.List;

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

  @Override
  public int getId(Appointment entity) {
    return entity.getId();
  }
}
