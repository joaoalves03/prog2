// HeaderController.java
package prog.projeto.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import prog.projeto.SceneManager;
import prog.projeto.models.User;
import prog.projeto.repositories.UserRepository;

public class HeaderController {
  @FXML
  MenuButton userDropDown;

  public void initialize() {
    if(userDropDown == null) return;
    UserRepository userRepository = UserRepository.getInstance();
    User selectedUser = userRepository.getSelectedUser();

    userDropDown.setText(
        selectedUser == null
            ? ""
            : String.format("%s %s", selectedUser.getFirstName(), selectedUser.getLastName())
    );
  }
  @FXML
  protected void openPage(ActionEvent event) {
    MenuItem menuItem = (MenuItem) event.getSource();
    String userData = (String) menuItem.getUserData();

    SceneManager.switchScene(userDropDown, userData);
  }

  @FXML
  protected void openModal(ActionEvent event){
    MenuItem node = (MenuItem) event.getSource();
    String scenePath = (String) node.getUserData();
    String title = node.getText();

    SceneManager.openNewModal(scenePath, title, true);
    initialize();
  }

  @FXML
  protected void logout(){
    SceneManager.switchScene(userDropDown, "login.fxml");
  }

  @FXML
  protected void closeWindow(ActionEvent event){
    Node closeButton = (Node) event.getSource();
    SceneManager.closeWindow(closeButton);
  }
}
