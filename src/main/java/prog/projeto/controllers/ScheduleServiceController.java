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

public class ScheduleServiceController implements Initializable {
  @FXML
  private ChoiceBox<String> serviceTypeChoiceBox;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    serviceTypeChoiceBox.setItems(
            FXCollections.observableArrayList(
                    AnimalServiceType.Education.description,
                    AnimalServiceType.Walking.description,
                    AnimalServiceType.Grooming.description
            )
    );
  }
}
