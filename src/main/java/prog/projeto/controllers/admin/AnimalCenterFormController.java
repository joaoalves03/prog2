package prog.projeto.controllers.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import prog.projeto.SceneManager;
import prog.projeto.models.AnimalCenter;
import prog.projeto.models.Service;
import prog.projeto.repositories.AnimalCenterRepository;
import prog.projeto.repositories.ServiceRepository;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AnimalCenterFormController implements Initializable {
  @FXML
  ComboBox<Service> serviceType;
  @FXML
  TextField address;
  @FXML
  TextField city;
  @FXML
  TextField phone;

  boolean edit = false;
  int providerId = -1;
  boolean animalCenterStatus;
  int newID;
  List<Integer> employees;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    ServiceRepository serviceRepository = ServiceRepository.getInstance();
    AnimalCenterRepository animalCenterRepository = AnimalCenterRepository.getInstance();
    newID = animalCenterRepository.getNextId();

    try {
      serviceRepository.read();
    } catch (Exception ignored) {
      SceneManager.openErrorAlert("Erro ao abrir formulário", "Não foi possível obter serviços");
      close();
    }

    ObservableList<Service> services =
        FXCollections.observableList(serviceRepository.getEntities().stream().toList());

    serviceType.setItems(services);
  }

  public void setAnimalCenterToEdit(int id) {
    AnimalCenterRepository animalCenterRepository = AnimalCenterRepository.getInstance();
    ServiceRepository serviceRepository = ServiceRepository.getInstance();
    edit = true;

    AnimalCenter animalCenter = animalCenterRepository.findById(id);
    newID = id;
    serviceType.setValue(serviceRepository.findById(animalCenter.getServiceType()));
    address.setText(animalCenter.getAddress());
    city.setText(animalCenter.getCity());
    phone.setText(animalCenter.getPhone());
    employees = animalCenter.getEmployees();
    animalCenterStatus = animalCenter.getStatus();
  }

  public void setProvider(int id) {
    providerId = id;
  }

  @FXML
  public void save() {
    AnimalCenterRepository animalCenterRepository = AnimalCenterRepository.getInstance();

    if (
        serviceType.getValue() == null
            || address.getText().isEmpty()
            || city.getText().isEmpty()
            || phone.getText().isEmpty()
    ) {
      SceneManager.openErrorAlert("Erro ao guardar", "Por favor preencha todos os campos");
      return;
    }

    if (edit) {
      animalCenterRepository.update(new AnimalCenter(
          newID,
          providerId,
          address.getText(),
          city.getText(),
          phone.getText(),
          serviceType.getValue().getId(),
          employees,
          animalCenterStatus
      ));
    } else {
      animalCenterRepository.add(new AnimalCenter(
          newID,
          providerId,
          address.getText(),
          city.getText(),
          phone.getText(),
          serviceType.getValue().getId(),
          new ArrayList<>(),
          true
      ));
    }

    try {
      animalCenterRepository.save();
    } catch (Exception e) {
      SceneManager
          .openErrorAlert("Erro a guardar", "Ocorreu um erro a guardar o Local de Recolha\n"
              + e.getCause());
      return;
    }

    close();
  }

  public void close() {
    Stage stage = (Stage) serviceType.getScene().getWindow();
    stage.close();
  }
}
