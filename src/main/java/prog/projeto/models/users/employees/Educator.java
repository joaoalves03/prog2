package prog.projeto.models.users.employees;

import prog.projeto.models.users.User;
import prog.projeto.models.users.UserType;

public class Educator extends User {
  public Educator(int id, String firstName, String lastName, String email, String password, String address, String city, String phone) {
    super(id, UserType.Educator, firstName, lastName, email, password, address, city, phone);
  }
}
