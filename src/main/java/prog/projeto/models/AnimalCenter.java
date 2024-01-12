package prog.projeto.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import prog.projeto.repositories.ServiceRepository;

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

  @Override
  public String toString() {
    ServiceRepository serviceRepository = ServiceRepository.getInstance();

    try {
      serviceRepository.read();
    } catch (Exception ignored) {return "?";}

    Service service = serviceRepository.findById(serviceType);

    return String.format("%s - %s, %s", service.getName(), address, city);
  }
}
