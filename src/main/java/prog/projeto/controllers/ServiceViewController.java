package prog.projeto.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import prog.projeto.models.Service;
import prog.projeto.repositories.ServiceRepository;

import java.net.URL;
import java.util.ResourceBundle;

public class ServiceViewController implements Initializable {
  @FXML
  TableView<Service> table;
  @FXML
  TableColumn<Service, String> nameColumn;
  @FXML
  TableColumn<Service, Float> priceColumn;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    ServiceRepository serviceRepository = ServiceRepository.getInstance();

    try {serviceRepository.read();} catch (Exception ignored) {}

    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

    ObservableList<Service> entities = FXCollections.observableList(serviceRepository.getEntities().stream().toList());

    table.setItems(entities);
  }
}
