package prog.projeto.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterFormController {
  @FXML
  public TextField firstName;
  @FXML
  public TextField lastName;
  @FXML
  public TextField email;
  @FXML
  public PasswordField password;
  @FXML
  public TextField address;
  @FXML
  public TextField city;
  @FXML
  public TextField phone;

  public boolean isFormCorrect() {
    return
        email.getText().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
        && phone.getText().matches("\\d{9}")
    ;
  }
}
