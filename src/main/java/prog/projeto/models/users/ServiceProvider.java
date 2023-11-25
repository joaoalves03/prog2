package prog.projeto.models.users;

public class ServiceProvider extends User {
  public ServiceProvider(int id, String firstName, String lastName, String email, String password, String address, String city, String phone) {
    super(id, UserType.ServiceProvider, firstName, lastName, email, password, address, city, phone);
  }
}
