package prog.projeto.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
  public Label passwordLabel;
  @FXML
  public PasswordField password;
  @FXML
  public TextField address;
  @FXML
  public TextField city;
  @FXML
  public TextField phone;
  @FXML
  public TextField cc;
  @FXML
  public TextField nif;

  boolean passwordHidden = false;

  public boolean isFormCorrect() {
    return
            email.getText().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
                    && phone.getText().matches("\\d{9}")
                    && cc.getText().matches("\\d{8}")
                    && nif.getText().matches("\\d{9}")
                    && !firstName.getText().trim().isEmpty()
                    && !lastName.getText().trim().isEmpty()
                    && (passwordHidden || (!password.getText().trim().isEmpty()))
                    && !address.getText().trim().isEmpty()
                    && !city.getText().trim().isEmpty()
            ;
  }

  public void setValues(String firstName, String lastName, String email, String address, String city, String phone, String cc, String nif){
    this.firstName.setText(firstName);
    this.lastName.setText(lastName);
    this.email.setText(email);
    this.address.setText(address);
    this.city.setText(city);
    this.phone.setText(phone);
    this.cc.setText(cc);
    this.nif.setText(nif);
  }

  public void setValues(String firstName, String lastName, String email, String password, String address, String city, String phone, String cc, String nif){
    this.firstName.setText(firstName);
    this.lastName.setText(lastName);
    this.email.setText(email);
    this.password.setText(password);
    this.address.setText(address);
    this.city.setText(city);
    this.phone.setText(phone);
    this.cc.setText(cc);
    this.nif.setText(nif);
  }

  public void clearValues(){
    this.firstName.clear();
    this.lastName.clear();
    this.email.clear();
    this.password.clear();
    this.address.clear();
    this.city.clear();
    this.phone.clear();
    this.cc.clear();
    this.nif.clear();
  }

  public void hidePassword() {
    passwordHidden = true;
    passwordLabel.setVisible(false);
    passwordLabel.setManaged(false);
    password.setVisible(false);
    password.setManaged(false);
  }
}
