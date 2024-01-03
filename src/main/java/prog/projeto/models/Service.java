package prog.projeto.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class Service implements Serializable {
  private int id;
  @Setter private String name;
  @Setter private float price;

  @Override
  public String toString() {
    return this.name + " (" + String.format("%.02f", this.price) + "€)";
  }
}
