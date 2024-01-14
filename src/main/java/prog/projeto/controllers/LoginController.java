package prog.projeto.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import prog.projeto.SceneManager;
import prog.projeto.models.User;
import prog.projeto.repositories.UserRepository;

public class LoginController {
  @FXML
  public TextField email;
  @FXML
  public TextField password;
  @FXML
  private Hyperlink registerLink;

  @FXML
  protected void onRegisterClick() {
    SceneManager.switchScene(registerLink, "pages/register.fxml");
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

      if(user.getPassword().equals(password.getText()) && user.getStatus()) {
        Stage stage = (Stage) email.getScene().getWindow();
        userRepository.setSelectedUser(user);

        switch(user.getType()) {
          case Client -> SceneManager.switchScene(stage, "pages/client/index.fxml");
          case ServiceProvider -> SceneManager.switchScene(stage, "pages/provider/index.fxml");
          case Staff -> SceneManager.switchScene(stage, "pages/staff/index.fxml");
          case Admin -> SceneManager.switchScene(stage, "pages/admin/index.fxml");
        }
      } else {
        throw new Exception();
      }
    } catch (Exception err) {
      System.out.println(err.getMessage());
      SceneManager.openErrorAlert("Erro a iniciar sessão", "Credenciais erradas");
    }
  }
}
