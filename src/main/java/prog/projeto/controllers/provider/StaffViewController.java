package prog.projeto.controllers.provider;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;
import prog.projeto.models.User;
import prog.projeto.models.UserType;
import prog.projeto.repositories.UserRepository;

public class StaffViewController {
  @FXML
  ListView<User> staffList;
  @FXML
  VBox infoBox;
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

    staffList.getItems().addAll(userRepository.getByType(UserType.Admin));
    staffList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    staffList.setCellFactory(param -> new ListCell<>(){
      @Override
      protected void updateItem(User user, boolean empty) {
        super.updateItem(user, empty);
        if (empty || user == null) { setText(null); }
        else { setText(user.getFirstName() + " " + user.getLastName()); }
      }
    });

    staffList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        email.setText("Email: " + newValue.getEmail());
        address.setText("Rua: " + newValue.getAddress());
        city.setText("Cidade: " + newValue.getCity());
        phone.setText("Telemovel: " + newValue.getPhone());
      } else {
        email.setText("Email:");
        address.setText("Rua:");
        city.setText("Cidade:");
        phone.setText("Telemovel:");
      }
    });
  }
}
