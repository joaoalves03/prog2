package prog.projeto.models.users.employees;

import prog.projeto.models.users.User;
import prog.projeto.models.users.UserType;

public class Secretary extends User {
  public Secretary(int id, String firstName, String lastName, String email, String password, String address, String city, String phone) {
    super(id, UserType.Secretary, firstName, lastName, email, password, address, city, phone);
  }
}
