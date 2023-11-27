package prog.projeto.models.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public abstract class User implements Serializable {
  int id;
  UserType type;
  @Setter String firstName, lastName;
  @Setter String email;
  @Setter String password;
  @Setter String address;
  @Setter String city;
  @Setter String phone;
}