package prog.projeto.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
@Data
public class Extra implements Serializable {
  private String name;
  private float price;
  private int quantity;

  @Override
  public String toString() {
    return String.format("%s: %d un. %.2f€", name, quantity, price);
  }
}
