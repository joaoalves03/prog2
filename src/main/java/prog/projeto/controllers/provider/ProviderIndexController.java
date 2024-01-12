package prog.projeto.controllers.provider;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import prog.projeto.SceneManager;

public class ProviderIndexController {
  @FXML
  protected void openModal(ActionEvent event) {
    Button node = (Button) event.getSource();
    String scenePath = (String) node.getUserData();
    String title = node.getText();

    SceneManager.openNewModal(scenePath, title, true);
  }
}
