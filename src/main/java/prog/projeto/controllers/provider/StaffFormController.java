package prog.projeto.controllers.provider;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import prog.projeto.SceneManager;


/*

    TODO: select the animal center id
    Add the user, then in the animal center employee list, add the new user's id

 */




public class StaffFormController {
  @FXML
  Button cancelButton;

  boolean edit = false;

  @FXML
  protected void onRegisterSubmit() {
    SceneManager.closeWindow(cancelButton);
  }

  @FXML
  protected void closeWindow() {
    SceneManager.closeWindow(cancelButton);
  }
}
