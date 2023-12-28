package prog.projeto.controllers;

import javafx.fxml.FXML;
import prog.projeto.models.users.Admin;
import prog.projeto.repositories.UserRepository;

public class FirstTimeRegisterController {
  @FXML
  RegisterFormController registerFormController;

  @FXML
  protected void onRegisterSubmit() throws Exception {
    // TODO: Add form verification
    UserRepository userRepository = UserRepository.getInstance();

    // TODO: Get id automatically
    userRepository.add(new Admin(
        1,
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
