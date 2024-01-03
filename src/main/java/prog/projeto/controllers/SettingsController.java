package prog.projeto.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import prog.projeto.SceneManager;
import prog.projeto.models.User;
import prog.projeto.repositories.UserRepository;

public class SettingsController {
  @FXML
  RegisterFormController registerFormController;

  @FXML
  Button cancelButton;

  public void initialize() {
    UserRepository userRepository = UserRepository.getInstance();
    User selectedUser = userRepository.getSelectedUser();
    if (selectedUser == null) { return; }

    registerFormController.firstName.setText(selectedUser.getFirstName());
    registerFormController.lastName.setText(selectedUser.getLastName());
    registerFormController.email.setText(selectedUser.getEmail());
    registerFormController.password.setText(selectedUser.getPassword());
    registerFormController.address.setText(selectedUser.getAddress());
    registerFormController.city.setText(selectedUser.getCity());
    registerFormController.phone.setText(selectedUser.getPhone());
  }

  @FXML
  protected void saveSettings(){
    if(!registerFormController.isFormCorrect()) {
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
    }

    selectedUser.setFirstName(registerFormController.firstName.getText());
    selectedUser.setLastName(registerFormController.lastName.getText());
    selectedUser.setEmail(registerFormController.email.getText());
    selectedUser.setPassword(registerFormController.password.getText());
    selectedUser.setAddress(registerFormController.address.getText());
    selectedUser.setCity(registerFormController.city.getText());
    selectedUser.setPhone(registerFormController.phone.getText());

    try {
      userRepository.save();

      closeWindow();
    } catch (Exception exc) {
      SceneManager.openErrorAlert("Erro ao registar", "Não foi possível guardar o registo");
    }
  }

  @FXML
  protected void closeWindow(){
    Stage stage = SceneManager.getStage(cancelButton);
    if (stage != null) {
      stage.close();
    } else {
      System.out.println("Stage is null. Unable to close stage");
    }
  }
}
