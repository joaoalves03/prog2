package prog.projeto.models;

import lombok.Getter;
import lombok.Setter;
import prog.projeto.repositories.ServiceRepository;

import java.io.Serializable;
import java.util.*;

@Getter
public class Appointment implements Serializable {
  private final int id;
  private final int providerID;
  private final int serviceID;
  private final int employeeID;
  private final Date date;
  @Setter
  private AppointmentStatus status;
  @Setter
  private String notes;
  private final Set<Extra> extraProducts;

  public Appointment(int id, int providerID, int serviceID, int employeeID, Date date) {
    this.id = id;
    this.providerID = providerID;
    this.serviceID = serviceID;
    this.employeeID = employeeID;
    this.date = date;
    this.status = AppointmentStatus.Scheduled;
    this.notes = "";
    this.extraProducts = new HashSet<>();
  }

  public void addExtra(String name, float price, int quantity) {
    Optional<Extra> extra = this.extraProducts.stream().filter(x -> x.getName().equals(name)).findFirst();

    if (extra.isPresent()) {
      Extra _extra = extra.get();

      _extra.setQuantity(_extra.getQuantity() + 1);
    } else {
      this.extraProducts.add(new Extra(name, price, quantity));
    }
  }

  public void removeExtra(String name, int quantity) throws NoSuchElementException {
    Optional<Extra> extra = this.extraProducts.stream().filter(x -> x.getName().equals(name)).findFirst();

    if (extra.isPresent()) {
      Extra _extra = extra.get();
      int _quantity = _extra.getQuantity();

      if (_quantity - quantity <= 0) {
        this.extraProducts.remove(_extra);
      } else {
        _extra.setQuantity(_quantity - quantity);
      }
    } else {
      throw new NoSuchElementException();
    }
  }

  public float getFinalValue() {
    ServiceRepository serviceRepository = ServiceRepository.getInstance();
    Service service = serviceRepository.findById(serviceID);

    float total = service.getPrice();

    for (Extra x : extraProducts) {
      total += x.getPrice();
    }

    return total;
  }
}
