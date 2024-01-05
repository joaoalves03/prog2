package prog.projeto.repositories;

import prog.projeto.models.Appointment;

public class AppointmentRepository extends Repository<Appointment> {
  private static AppointmentRepository instance;

  private AppointmentRepository(){fileName = "appointments.dat";}

  public static AppointmentRepository getInstance() {
    if(instance == null) instance = new AppointmentRepository();
    return instance;
  }

  @Override
  public int getId(Appointment entity) {
    return entity.getId();
  }
}
