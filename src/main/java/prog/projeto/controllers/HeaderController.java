// HeaderController.java
package prog.projeto.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import prog.projeto.SceneManager;

public class HeaderController {
  @FXML
  MenuButton userDropDown;

  private Stage getStage() {
    return (Stage) userDropDown.getScene().getWindow();
  }

  @FXML
  protected void openPage(ActionEvent event) {
    MenuItem menuItem = (MenuItem) event.getSource();
    String userData = (String) menuItem.getUserData();

    if (getStage() != null) {
      try {
        SceneManager.switchScene(getStage(), userData);
      } catch (Exception e) {
        System.out.println("SceneManager: " + e.getMessage());
      }
    } else {
      System.out.println("Stage is null. Unable to switch scenes.");
    }
  }

  @FXML
  protected void logout(){
    if (getStage() != null) {
      try {
        SceneManager.switchScene(getStage(), "login.fxml");
      } catch (Exception e) {
        System.out.println("SceneManager: " + e.getMessage());
      }
    } else {
      System.out.println("Stage is null. Unable to switch scenes.");
    }
  }
}
