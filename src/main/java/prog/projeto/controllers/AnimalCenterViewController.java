package prog.projeto.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.AllArgsConstructor;
import prog.projeto.models.AnimalCenter;
import prog.projeto.models.Service;
import prog.projeto.repositories.AnimalCenterRepository;

import java.net.URL;
import java.util.ResourceBundle;

@AllArgsConstructor
class AnimalCenterTableElement {
  String provider;
  String address;
  String city;
  String phone;
  String serviceType;
  Float price;
}

public class AnimalCenterViewController implements Initializable {
  @FXML
  TableView<AnimalCenterTableElement> table;
  @FXML
  TableColumn<Service, String> providerColumn;
  @FXML
  TableColumn<Service, String> addressColumn;
  @FXML
  TableColumn<Service, String> cityColumn;
  @FXML
  TableColumn<Service, String> phoneColumn;
  @FXML
  TableColumn<Service, String> serviceTypeColumn;
  @FXML
  TableColumn<Service, Float> priceColumn;

  protected void refreshTable() {
    AnimalCenterRepository animalCenterRepository = AnimalCenterRepository.getInstance();
    ObservableList<AnimalCenterTableElement> entities = FXCollections.observableArrayList();

    for(AnimalCenter animalCenter: animalCenterRepository.getEntities()){
      entities.add(new AnimalCenterTableElement(
          "lol",
          animalCenter.getAddress(),
          animalCenter.getCity(),
          animalCenter.getPhone(),
          "animalCenter.getServiceType()",
          animalCenter.getServicePrice()
      ));
    }

    table.setItems(entities);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    AnimalCenterRepository animalCenterRepository = AnimalCenterRepository.getInstance();

    // TODO: Probably shouldn't ignore this, will check later
    try {animalCenterRepository.read();} catch (Exception ignored) {}

    providerColumn.setCellValueFactory(new PropertyValueFactory<>("provider"));
    addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
    cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
    phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
    serviceTypeColumn.setCellValueFactory(new PropertyValueFactory<>("serviceType"));
    priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

    refreshTable();
  }
}
