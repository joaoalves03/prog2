package prog.projeto.controllers;

import javafx.fxml.FXML;
import prog.projeto.SceneManager;

public class ProviderIndexController {
  @FXML
  protected void onServiceClick() {
    SceneManager.openNewModal("provider/serviceView.fxml", "Serviços", true);
  }
}
