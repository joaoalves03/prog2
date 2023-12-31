package prog.projeto.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import prog.projeto.models.AnimalServiceType;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
