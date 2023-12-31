// HeaderController.java
package prog.projeto.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import prog.projeto.SceneManager;

public class HeaderController {
  @FXML
  MenuButton userDropDown;

  @FXML
  FlowPane closeButton;

  public void initialize() throws RuntimeException {
    try {
      userDropDown.setText("Test User");
    } catch (RuntimeException e) {
      System.out.println("Header: " + e.getMessage());
    }
  }
  @FXML
  protected void openPage(ActionEvent event) {
    MenuItem menuItem = (MenuItem) event.getSource();
    String userData = (String) menuItem.getUserData();
    Stage stage = SceneManager.getStage(userDropDown);

    if (stage != null) {
      try {
        SceneManager.switchScene(stage, userData);
      } catch (Exception e) {
        System.out.println("SceneManager: " + e.getMessage());
      }
    } else {
      System.out.println("Stage is null. Unable to switch scenes.");
    }
  }

  @FXML
  protected void openModal(ActionEvent event) {
    MenuItem menuItem = (MenuItem) event.getSource();
    String userData = (String) menuItem.getUserData();

    try {
      SceneManager.openNewModal(userData, "Settings", true);
    } catch (Exception e) {
      System.out.println("SceneManager: " + e.getMessage());
    }
  }

  @FXML
  protected void logout(){
    Stage stage = SceneManager.getStage(userDropDown);

    if (stage != null) {
      try {
        SceneManager.switchScene(stage, "login.fxml");
      } catch (Exception e) {
        System.out.println("SceneManager: " + e.getMessage());
      }
    } else {
      System.out.println("Stage is null. Unable to switch scenes.");
    }
  }

  @FXML
  protected void closeWindow(){
    Stage stage = SceneManager.getStage(closeButton);
    if (stage != null) {
      stage.close();
    } else {
      System.out.println("Stage is null. Unable to close stage");
    }
  }
}
