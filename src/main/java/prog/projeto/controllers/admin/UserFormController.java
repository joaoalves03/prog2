package prog.projeto.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import prog.projeto.SceneManager;
import prog.projeto.models.User;
import prog.projeto.models.UserType;
import prog.projeto.repositories.UserRepository;
import prog.projeto.controllers.RegisterFormController;

public class UserFormController {
  @FXML
  Button cancelButton;
  @FXML
  ToggleGroup userType;
  @FXML
  RegisterFormController registerFormController;

  boolean edit = false;
  int userId = -1;

  public void enableEdit(User user){
    edit = true;
    userId = user.getId();
    registerFormController.setValues(
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getPassword(),
            user.getAddress(),
            user.getCity(),
            user.getPhone()
    );
    registerFormController.hidePassword();

    if (user.getType() == UserType.Client) {
      userType.getToggles().get(0).setSelected(true);
    } else {
      if (user.getType() == UserType.ServiceProvider) {
        userType.getToggles().get(1).setSelected(true);
      } else {
        userType.getToggles().get(2).setSelected(true);
      }
    }
  }

  @FXML
  protected void save() {
    if (!registerFormController.isFormCorrect()) {
      SceneManager.openErrorAlert("Erro ao registar", "Por favor preencha todos os campos corretamente");
      return;
    }

    UserRepository userRepository = UserRepository.getInstance();
    String selectedToggle = ((RadioButton) userType.getSelectedToggle()).getText();
    UserType selectedUserType = selectedToggle.equals("Cliente")
            ? UserType.Client
            : selectedToggle.equals("Prestador")
            ? UserType.ServiceProvider
            : UserType.Admin;

    User newUser = new  User(
            edit ? userId : userRepository.getNextId(),
            selectedUserType,
            registerFormController.firstName.getText(),
            registerFormController.lastName.getText(),
            registerFormController.email.getText(),
            registerFormController.password.getText(),
            registerFormController.address.getText(),
            registerFormController.city.getText(),
            registerFormController.phone.getText()
    );

    // Check if email was changed
    if(!edit || !newUser.getEmail().equals(registerFormController.email.getText())) {
      try {
        userRepository.findByEmail(registerFormController.email.getText());

        SceneManager.openErrorAlert("Erro ao registar", "Um utilizador com este e-mail já existe");
        return;
      } catch (Exception ignored) {
      }
    }

    if(edit) {
      userRepository.update(newUser);
    } else {
      userRepository.add(newUser);
    }

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
