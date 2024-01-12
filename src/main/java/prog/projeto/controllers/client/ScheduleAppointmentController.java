package prog.projeto.controllers.client;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import prog.projeto.models.AnimalCenter;
import prog.projeto.models.User;
import prog.projeto.models.UserType;
import prog.projeto.repositories.AnimalCenterRepository;
import prog.projeto.repositories.UserRepository;

public class ScheduleAppointmentController {
  @FXML
  private ComboBox<User> providerComboBox;
  @FXML
  private Label animalCenterLabel;
  @FXML
  private ComboBox<AnimalCenter> animalCenterComboBox;
  @FXML
  private Label employeeLabel;
  @FXML
  private ComboBox<User> employeeComboBox;

  public void initialize() throws RuntimeException {
    try {
      UserRepository userRepository = UserRepository.getInstance();
      AnimalCenterRepository animalCenterRepository = AnimalCenterRepository.getInstance();

      userRepository.read();
      animalCenterRepository.read();

      providerComboBox.setItems(
          FXCollections.observableArrayList(userRepository.getByType(UserType.ServiceProvider))
      );

      providerComboBox.setOnAction((event -> {
        User selectedUser = providerComboBox.getSelectionModel().getSelectedItem();
        if(selectedUser == null) {
          animalCenterLabel.setVisible(false);
        } else {
          animalCenterComboBox.setItems(
            FXCollections.observableList(animalCenterRepository.getByProvider(selectedUser.getId()))
          );
          animalCenterLabel.setVisible(true);
        }
      }));
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
