package prog.projeto.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@AllArgsConstructor
public class AnimalCenter implements Serializable {
  private final int id;
  private final int providerID;
  @Setter
  private String address;
  @Setter
  private String city;
  @Setter
  private String phone;
  private int serviceType;
  private List<User> employees;
}
