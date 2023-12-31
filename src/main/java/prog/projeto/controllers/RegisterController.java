package prog.projeto.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import prog.projeto.SceneManager;
import prog.projeto.models.users.Admin;
import prog.projeto.repositories.UserRepository;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
  @FXML
  RegisterFormController registerFormController;
  @FXML
  boolean firstTime = false;

  @FXML
  Label firstTimeLabel;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    UserRepository userRepository = UserRepository.getInstance();
    if(userRepository.length() == 0) {
      firstTime = true;
      firstTimeLabel.setVisible(true);
      firstTimeLabel.setManaged(true);
    }
  }

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

    returnToLogin();
  }

  @FXML
  protected void returnToLogin() throws Exception {
    Stage stage = (Stage) firstTimeLabel.getScene().getWindow();
    SceneManager.switchScene(stage, "login.fxml");
  }
}
