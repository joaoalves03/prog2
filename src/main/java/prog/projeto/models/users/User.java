package prog.projeto.models.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public abstract class User {
  int id;
  UserType type;
  @Setter String firstName, lastName;
  @Setter String email;
  @Setter String password;
  @Setter String address;
  @Setter String city;
  @Setter String phone;
}