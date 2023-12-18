package prog.projeto.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

@Getter
public class Appointment implements Serializable {
  private final int companyID;
  @Setter
  private float price;
  private final Set<Extra> extras;

  public Appointment(int companyID, float price) {
    this.companyID = companyID;
    this.price = price;
    this.extras = new HashSet<>();
  }

  public void addExtra(String name, float price, int quantity) {
    Optional<Extra> extra = this.extras.stream().filter(x -> x.getName().equals(name)).findFirst();

    if(extra.isPresent()) {
      Extra _extra = extra.get();

      _extra.setQuantity(_extra.getQuantity() + 1);
    } else {
      this.extras.add(new Extra(name, price, quantity));
    }
  }

  public void removeExtra(String name, int quantity) throws NoSuchElementException {
    Optional<Extra> extra = this.extras.stream().filter(x -> x.getName().equals(name)).findFirst();

    if(extra.isPresent()) {
      Extra _extra = extra.get();
      int _quantity = _extra.getQuantity();

      if (_quantity - quantity <= 0) {
        this.extras.remove(_extra);
      } else {
        _extra.setQuantity(_quantity - quantity);
      }
    } else {
      throw new NoSuchElementException();
    }
  }

  public float getFinalValue() {
    float total = price;

    for(Extra x: extras) {
      total += x.getPrice();
    }

    return total;
  }
}
