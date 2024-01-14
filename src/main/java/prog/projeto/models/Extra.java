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
}
