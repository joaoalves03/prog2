package prog.projeto.controllers;

import javafx.fxml.FXML;
import prog.projeto.SceneManager;

public class ProviderIndexController {
  @FXML
  protected void onServiceClick() {
    try {
      SceneManager.openNewModal("provider/serviceView.fxml", "Serviços", true);
    } catch (Exception e) {
      System.out.println("SceneManager: " + e.getMessage());
    }
  }

  @FXML
  protected void onAnimalCenterClick() {
    try {
      SceneManager.openNewModal("provider/animalCenterView.fxml", "Locais de recolha", true);
    } catch (Exception e) {
      System.out.println("SceneManager: " + e.getMessage());
    }
  }
}
