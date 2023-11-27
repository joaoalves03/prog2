package prog.projeto.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;
import prog.projeto.SceneManager;

public class LoginController {
  @FXML
  private Hyperlink registerLink;

  @FXML
  protected void onRegisterClick() throws Exception {
    Stage stage = (Stage) registerLink.getScene().getWindow();
    SceneManager.switchScene(stage, "register.fxml");
  }
}
