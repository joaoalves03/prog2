package prog.projeto.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;
import prog.projeto.models.User;
import prog.projeto.repositories.UserRepository;

public class UserManagementController {
  @FXML
  ListView<User> usersList;

  @FXML
  VBox informationBox;

  @FXML
  Label name;
  @FXML
  Label email;
  @FXML
  Label address;
  @FXML
  Label city;
  @FXML
  Label phone;


  @FXML
  private void initialize() {
    UserRepository userRepository = UserRepository.getInstance();

    // Set the cell factory to display the user's first name
    usersList.setCellFactory(param -> new UserListCell());

    // Populate the ListView with users
    usersList.getItems().addAll(userRepository.getAllUsers());

    // Set the selection mode to single
    usersList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    // Add a listener to detect when an item is selected
    usersList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        // If a user is selected, update the label text
        name.setText("Nome: " + newValue.getFirstName() + " " + newValue.getLastName());
        email.setText("Email: " + newValue.getEmail());
        address.setText("Rua: " + newValue.getAddress());
        city.setText("Cidade: " + newValue.getCity());
        phone.setText("Telemovel: " + newValue.getPhone());
      } else {
        // If no user is selected
        name.setText("Nome:");
        email.setText("Email:");
        address.setText("Rua:");
        city.setText("Cidade:");
        phone.setText("Telemovel:");
      }
    });
  }

  private static class UserListCell extends ListCell<User> {
    @Override
    protected void updateItem(User user, boolean empty) {
      super.updateItem(user, empty);
      if (empty || user == null) {
        setText(null);
      } else {
        setText(user.getFirstName());
      }
    }
  }
}
