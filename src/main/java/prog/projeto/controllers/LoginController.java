package prog.projeto.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import prog.projeto.SceneManager;
import prog.projeto.models.users.User;
import prog.projeto.repositories.UserRepository;

public class LoginController {
  @FXML
  public TextField email;
  @FXML
  public TextField password;
  @FXML
  private Hyperlink registerLink;

  @FXML
  protected void onRegisterClick() throws Exception {
    Stage stage = (Stage) registerLink.getScene().getWindow();
    SceneManager.switchScene(stage, "firstTimeRegister.fxml");
  }

  @FXML
  protected void onLoginClick() {
    UserRepository userRepository = UserRepository.getInstance();

    if(email.getText().isEmpty() || password.getText().isEmpty()) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Aviso");
      alert.setHeaderText("Por favor preencha todos os campos");
      alert.showAndWait();
      return;
    }

    try {
      User user = userRepository.findByEmail(email.getText());

      if(user.getPassword().equals(password.getText())) {
        // TODO: Switch with an actual scene
        Stage stage = (Stage) email.getScene().getWindow();
        SceneManager.switchScene(stage, "hello-view.fxml");
      } else {
        throw new Exception();
      }
    } catch (Exception err) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Erro");
      alert.setHeaderText("Erro a iniciar sessão");
      alert.setContentText("Credenciais erradas");
      alert.showAndWait();
    }
  }
}
