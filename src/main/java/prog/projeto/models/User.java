package prog.projeto.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class User implements Serializable {
  private int id;
  private UserType type;
  @Setter private String firstName, lastName;
  @Setter private String email;
  @Setter private String password;
  @Setter private String address;
  @Setter private String city;
  @Setter private String phone;

  @Override
  public String toString() {
    return this.firstName + " " + this.lastName;
  }
}