package prog.projeto.controllers;

import javafx.fxml.FXML;
import prog.projeto.models.users.Admin;
import prog.projeto.repositories.UserRepository;

public class RegisterController {
  @FXML
  RegisterFormController registerFormController;
  @FXML
  boolean firstTime = true;

  public void enableFirstTime() { firstTime = true; }

  @FXML
  protected void onRegisterSubmit() throws Exception {
    // TODO: Add form verification
    UserRepository userRepository = UserRepository.getInstance();

    // TODO: Register other types of users, obviously
    userRepository.add(new Admin(
        userRepository.getNextId(),
        registerFormController.firstName.getText(),
        registerFormController.lastName.getText(),
        registerFormController.email.getText(),
        registerFormController.password.getText(),
        registerFormController.address.getText(),
        registerFormController.city.getText(),
        registerFormController.phone.getText()
    ));

    userRepository.save();
  }
}
