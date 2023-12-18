package prog.projeto.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import prog.projeto.models.users.ServiceProvider;

@Data
@AllArgsConstructor
public class AnimalCenter {
  private ServiceProvider provider;
  private String address;
  private String city;
  private String phone;
  private AnimalServiceType serviceType;
  private float servicePrice;
}
