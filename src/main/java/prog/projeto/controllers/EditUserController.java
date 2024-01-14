package prog.projeto.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import prog.projeto.SceneManager;
import prog.projeto.models.User;
import prog.projeto.repositories.UserRepository;

public class EditUserController {
  @FXML
  RegisterFormController registerFormController;

  @FXML
  Button cancelButton;

  public void initialize() {
    UserRepository userRepository = UserRepository.getInstance();
    User selectedUser = userRepository.getSelectedUser();
    if (selectedUser == null) {
      return;
    }

    registerFormController.setValues(
            selectedUser.getFirstName(),
            selectedUser.getLastName(),
            selectedUser.getEmail(),
            selectedUser.getPassword(),
            selectedUser.getAddress(),
            selectedUser.getCity(),
            selectedUser.getPhone(),
            selectedUser.getCc(),
            selectedUser.getNif()
    );
  }

  @FXML
  protected void save() {
    if (!registerFormController.isFormCorrect()) {
      SceneManager.openErrorAlert("Erro ao registar", "Por favor preencha todos os campos corretamente");
      return;
    }

    UserRepository userRepository = UserRepository.getInstance();
    User selectedUser = userRepository.getSelectedUser();

    // Check if email was changed
    if (!selectedUser.getEmail().equals(registerFormController.email.getText())) {
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
    }

    selectedUser.setFirstName(registerFormController.firstName.getText());
    selectedUser.setLastName(registerFormController.lastName.getText());
    selectedUser.setEmail(registerFormController.email.getText());
    selectedUser.setPassword(registerFormController.password.getText());
    selectedUser.setAddress(registerFormController.address.getText());
    selectedUser.setCity(registerFormController.city.getText());
    selectedUser.setPhone(registerFormController.phone.getText());
    selectedUser.setCc(registerFormController.cc.getText());
    selectedUser.setNif(registerFormController.nif.getText());

    try {
      userRepository.save();

      closeWindow();
    } catch (Exception exc) {
      SceneManager.openErrorAlert("Erro ao registar", "Não foi possível guardar o registo");
    }
  }

  @FXML
  protected void closeWindow() {
    SceneManager.closeWindow(cancelButton);
  }
}
