package prog.projeto.models;

import lombok.Data;
import prog.projeto.models.users.ServiceProvider;

@Data
public class AnimalCenter {
  ServiceProvider provider;
  String address;
  String city;
  String phone;
  AnimalServiceType serviceType;
  float servicePrice;
}
