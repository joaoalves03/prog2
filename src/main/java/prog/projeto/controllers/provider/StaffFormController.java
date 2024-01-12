package prog.projeto.controllers.provider;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import prog.projeto.SceneManager;
import prog.projeto.controllers.RegisterFormController;
import prog.projeto.models.AnimalCenter;
import prog.projeto.models.User;
import prog.projeto.models.UserType;
import prog.projeto.repositories.AnimalCenterRepository;
import prog.projeto.repositories.UserRepository;


/*

    TODO: select the animal center id
    Add the user, then in the animal center employee list, add the new user's id

 */




public class StaffFormController {
  @FXML
  Button cancelButton;
  @FXML
  RegisterFormController registerFormController;

  boolean edit = false;
  int animalCenterID = -1;

  public void setAnimalCenter(int id){
    animalCenterID = id;
  }

  @FXML
  protected void onRegisterSubmit() {
    if (!registerFormController.isFormCorrect()) {
      SceneManager.openErrorAlert("Erro ao registar", "Por favor preencha todos os campos corretamente");
      return;
    }

    UserRepository userRepository = UserRepository.getInstance();
    AnimalCenterRepository animalCenterRepository = AnimalCenterRepository.getInstance();

    try {
      animalCenterRepository.read();
    } catch (Exception ignored) {
      SceneManager.openErrorAlert("Erro", "Não foi possível obter os centros de recolha");
      closeWindow();
    }

    AnimalCenter animalCenter = animalCenterRepository.findById(animalCenterID);

    try {
      userRepository.findByEmail(registerFormController.email.getText());

      SceneManager.openErrorAlert("Erro ao registar", "Um utilizador com este e-mail já existe");
      return;
    } catch (Exception ignored) {
    }

    User newEmployee = new User(
        userRepository.getNextId(),
        UserType.Staff,
        registerFormController.firstName.getText(),
        registerFormController.lastName.getText(),
        registerFormController.email.getText(),
        registerFormController.password.getText(),
        registerFormController.address.getText(),
        registerFormController.city.getText(),
        registerFormController.phone.getText()
    );

    userRepository.add(newEmployee);
    animalCenter.getEmployees().add(newEmployee.getId());

    try {
      userRepository.save();
      animalCenterRepository.save();
    } catch (Exception exc) {
      SceneManager.openErrorAlert("Erro ao registar", "Não foi possível guardar o registo");
    }

    SceneManager.closeWindow(cancelButton);
  }

  @FXML
  protected void closeWindow() {
    SceneManager.closeWindow(cancelButton);
  }
}
