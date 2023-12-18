package prog.projeto.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Extra {
  private String name;
  private float price;
  private int quantity;
}
