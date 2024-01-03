package prog.projeto.controllers.client;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import prog.projeto.models.AnimalServiceType;

public class ScheduleServiceController {
  @FXML
  private ChoiceBox<String> serviceTypeChoiceBox;

  public void initialize() throws RuntimeException {
    try {
      serviceTypeChoiceBox.setItems(
              FXCollections.observableArrayList(
                      AnimalServiceType.Education.description,
                      AnimalServiceType.Walking.description,
                      AnimalServiceType.Grooming.description
              )
      );
    } catch (RuntimeException e) {
      System.out.println(e.getMessage());
    }
  }
}
