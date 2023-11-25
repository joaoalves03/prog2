package prog.projeto.models.users;

public class Admin extends User {
  public Admin(int id, String firstName, String lastName, String email, String password, String address, String city, String phone) {
    super(id, UserType.Admin, firstName, lastName, email, password, address, city, phone);
  }
}
