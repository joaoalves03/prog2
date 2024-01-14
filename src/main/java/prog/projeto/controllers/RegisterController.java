package prog.projeto.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import prog.projeto.SceneManager;
import prog.projeto.models.User;
import prog.projeto.models.UserType;
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
  @FXML
  ToggleGroup userType;
  @FXML
  HBox userTypeSelection;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    UserRepository userRepository = UserRepository.getInstance();
    if (userRepository.length() == 0) {
      firstTime = true;
      firstTimeLabel.setVisible(true);
      firstTimeLabel.setManaged(true);
      userTypeSelection.setVisible(false);
      userTypeSelection.setManaged(false);
    }
  }
  @FXML
  protected void onRegisterSubmit() {
    if (!registerFormController.isFormCorrect()) {
      SceneManager.openErrorAlert("Erro ao registar", "Por favor preencha todos os campos corretamente");
      return;
    }

    UserRepository userRepository = UserRepository.getInstance();
    UserType selectedUserType = null;
    for (UserType type : UserType.values()) {
      if (type.description.equals(((RadioButton) userType.getSelectedToggle()).getText())) {
        selectedUserType = type;
        break;
      }
    }


    if (firstTime) selectedUserType = UserType.Admin;

    try {
      userRepository.findByEmail(registerFormController.email.getText());
      SceneManager.openErrorAlert("Erro ao registar", "Um utilizador com este e-mail já existe");
      return;
    } catch (Exception ignored) {}
    try {
      userRepository.findByCC(registerFormController.cc.getText());
      SceneManager.openErrorAlert("Erro ao registar", "Um utilizador com este cc já existe");
      return;
    } catch (Exception ignored) {}
    try {
      userRepository.findByNIF(registerFormController.nif.getText());
      SceneManager.openErrorAlert("Erro ao registar", "Um utilizador com este nif já existe");
      return;
    } catch (Exception ignored) {}

    userRepository.add(new User(
            userRepository.getNextId(),
            selectedUserType,
            registerFormController.firstName.getText(),
            registerFormController.lastName.getText(),
            registerFormController.email.getText(),
            registerFormController.password.getText(),
            registerFormController.address.getText(),
            registerFormController.city.getText(),
            registerFormController.phone.getText(),
            registerFormController.cc.getText(),
            registerFormController.nif.getText(),
            true
    ));

    try {
      userRepository.save();

      returnToLogin();
    } catch (Exception exc) {
      SceneManager.openErrorAlert("Erro ao registar", "Não foi possível guardar o registo");
    }
  }

  @FXML
  protected void returnToLogin() {
    try {
      SceneManager.switchScene(firstTimeLabel, "pages/login.fxml");
    } catch (Exception e) {
      System.out.println("SceneManager");
    }
  }
}
