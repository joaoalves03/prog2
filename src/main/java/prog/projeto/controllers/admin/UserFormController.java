package prog.projeto.controllers.admin;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import prog.projeto.SceneManager;
import prog.projeto.models.AnimalCenter;
import prog.projeto.models.User;
import prog.projeto.models.UserType;
import prog.projeto.repositories.AnimalCenterRepository;
import prog.projeto.repositories.UserRepository;
import prog.projeto.controllers.RegisterFormController;

public class UserFormController {
  @FXML
  Button cancelButton;
  @FXML
  ToggleGroup userType;
  @FXML
  RegisterFormController registerFormController;

  @FXML
  private HBox staffExtra;
  @FXML
  private ComboBox<User> providerComboBox;
  @FXML
  private Label animalCenterLabel;
  @FXML
  private ComboBox<AnimalCenter> animalCenterComboBox;

  boolean staff = false;
  boolean edit = false;
  int userId = -1;

  public void initialize() {
    staffExtra.setVisible(false);
  }

  public void enableEdit(User user) {
    edit = true;
    userId = user.getId();
    registerFormController.setValues(
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getPassword(),
            user.getAddress(),
            user.getCity(),
            user.getPhone(),
            user.getCc(),
            user.getNif()
    );
    registerFormController.hidePassword();

    for (Toggle toggle : userType.getToggles()) {
      if (((RadioButton) toggle).getText().equals(user.getType().description)) {
        toggle.setSelected(true);
        break;
      }
    }
    if (user.getType() == UserType.Staff) {
      enableStaff();
    }
  }

  @FXML
  void enableStaff() {
    staffExtra.setVisible(true);
    staff = true;

    UserRepository userRepository = UserRepository.getInstance();
    AnimalCenterRepository animalCenterRepository = AnimalCenterRepository.getInstance();

    try {
      userRepository.read();
      animalCenterRepository.read();
    } catch (Exception e) {
      System.out.println("Staff (userForm): " + e.getCause());
    }


    providerComboBox.setItems(
            FXCollections.observableArrayList(userRepository.getByType(UserType.ServiceProvider))
    );
    if(edit && userRepository.findById(userId).getType() == UserType.ServiceProvider){
      providerComboBox.getItems().remove(userRepository.findById(userId));
    }

    providerComboBox.setOnAction((event -> {
      User selectedUser = providerComboBox.getSelectionModel().getSelectedItem();

      if (selectedUser == null) {
        animalCenterLabel.setVisible(false);
      } else {
        animalCenterComboBox.setItems(
                FXCollections.observableList(animalCenterRepository.getByProvider(selectedUser.getId()))
        );
        animalCenterLabel.setVisible(true);
      }
    }));

    if (edit) {
      animalCenterLabel.setVisible(true);
      for (AnimalCenter center : animalCenterRepository.getEntities()) {
        if (center.getEmployees().contains(userId)) {
          animalCenterComboBox.setValue(center);
          animalCenterComboBox.setItems(
                  FXCollections.observableList(animalCenterRepository.getByProvider(center.getProviderID()))
          );
          providerComboBox.setValue(userRepository.findById(center.getProviderID()));
          break;
        }
      }
    }
  }

  @FXML
  private void disableStaff(){
    staffExtra.setVisible(false);
    staff = false;
  }

  @FXML
  protected void save() {
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

    User newUser = new User(
            edit ? userId : userRepository.getNextId(),
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
            userRepository.findById(userId).getStatus()
    );

    // Check if email was changed
    if (!edit || !newUser.getEmail().equals(registerFormController.email.getText())) {
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

    if (staff && (providerComboBox.getValue() == null || animalCenterComboBox.getValue() == null)) {
      SceneManager.openErrorAlert("Erro ao registar", "Por favor preencha todos os campos corretamente");
      return;
    }

    AnimalCenterRepository animalCenterRepository = AnimalCenterRepository.getInstance();
    AnimalCenter animalCenter = null;
    if (staff) {
      animalCenter = animalCenterRepository.findById(animalCenterComboBox.getValue().getId());

      if (edit && !animalCenter.getEmployees().contains(newUser.getId())) {
        for (AnimalCenter center : animalCenterRepository.getEntities()) {
          if (center.getEmployees().contains(userId)) {
            center.getEmployees().remove((Integer) userId);
            animalCenterRepository.update(center);
            break;
          }
        }
        animalCenter.getEmployees().add(newUser.getId());
        animalCenterRepository.update(animalCenter);
      }
    }
    if (edit && userRepository.findById(userId).getType() == UserType.Staff && !staff) {
      for (AnimalCenter center : animalCenterRepository.getEntities()) {
        if (center.getEmployees().contains(userId)) {
          center.getEmployees().remove((Integer) userId);
          animalCenterRepository.update(center);
          break;
        }
      }
    }
    if(edit && userRepository.findById(userId).getType() == UserType.ServiceProvider && newUser.getType() != UserType.ServiceProvider){
      animalCenterRepository.getByProvider(userId).clear();
    }

    if (edit) {
      userRepository.update(newUser);
    } else {
      userRepository.add(newUser);
      if (staff) {
        assert animalCenter != null;
        animalCenter.getEmployees().add(newUser.getId());
      }
    }

    try {
      userRepository.save();
      animalCenterRepository.save();
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
