package prog.projeto.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import prog.projeto.SceneManager;
import prog.projeto.models.User;
import prog.projeto.models.UserType;
import prog.projeto.repositories.UserRepository;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
  private boolean insideModal = false;
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
  @FXML
  Label title;
  @FXML
  ImageView logo;
  @FXML
  VBox container;
  @FXML
  Button cancelButton;
  @FXML
  Button backButton;

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

    cancelButton.setVisible(false);
    cancelButton.setManaged(false);

    //rbClient.setSelected(true);
  }

  public void addAdminType() {
    RadioButton adminRadioButton = new RadioButton("Admin");
    adminRadioButton.setToggleGroup(userType);
    userTypeSelection.getChildren().add(adminRadioButton);
  }

  public void insideModal() {
    title.setVisible(false);
    title.setManaged(false);
    logo.setVisible(false);
    logo.setManaged(false);
    backButton.setVisible(false);
    backButton.setManaged(false);
    cancelButton.setVisible(true);
    cancelButton.setManaged(true);
    container.getStyleClass().add("modal");
    insideModal = true;
  }

  @FXML
  protected void onRegisterSubmit() {
    if (!registerFormController.isFormCorrect()) {
      SceneManager.openErrorAlert("Erro ao registar", "Por favor preencha todos os campos corretamente");
      return;
    }

    UserRepository userRepository = UserRepository.getInstance();
    UserType selectedUserType = ((RadioButton) userType.getSelectedToggle()).getText().equals("Cliente")
            ? UserType.Client
            : UserType.ServiceProvider;

    if (firstTime) selectedUserType = UserType.Admin;

    try {
      userRepository.findByEmail(registerFormController.email.getText());

      SceneManager.openErrorAlert("Erro ao registar", "Um utilizador com este e-mail já existe");
      return;
    } catch (Exception ignored) {
    }

    userRepository.add(new User(
            userRepository.getNextId(),
            selectedUserType,
            registerFormController.firstName.getText(),
            registerFormController.lastName.getText(),
            registerFormController.email.getText(),
            registerFormController.password.getText(),
            registerFormController.address.getText(),
            registerFormController.city.getText(),
            registerFormController.phone.getText()
    ));

    try {
      userRepository.save();

      if (insideModal) {
        closeWindow();
      } else {
        returnToLogin();
      }
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

  @FXML
  protected void closeWindow() {
    SceneManager.closeWindow(cancelButton);
  }
}
