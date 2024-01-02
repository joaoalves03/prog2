package prog.projeto.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class AnimalCenter {
  private final int id;
  private final int providerID;
  @Setter
  private String address;
  @Setter
  private String city;
  @Setter
  private String phone;
  private final AnimalServiceType serviceType;
  @Setter
  private float servicePrice;
}
