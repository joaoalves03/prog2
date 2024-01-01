package prog.projeto.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class Service {
  private int id;
  @Setter private String name;
  @Setter private float price;
}
