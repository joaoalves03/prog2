package prog.projeto.controllers.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import prog.projeto.SceneManager;
import prog.projeto.models.AnimalCenter;
import prog.projeto.models.Service;
import prog.projeto.models.User;
import prog.projeto.models.UserType;
import prog.projeto.repositories.AnimalCenterRepository;
import prog.projeto.repositories.ServiceRepository;
import prog.projeto.repositories.UserRepository;

public class AnimalCenterViewController {
  @FXML
  TableView<AnimalCenter> table;
  @FXML
  TableColumn<AnimalCenter, String> addressColumn;
  @FXML
  TableColumn<AnimalCenter, String> cityColumn;
  @FXML
  TableColumn<AnimalCenter, String> phoneColumn;
  @FXML
  TableColumn<AnimalCenter, Integer> serviceTypeColumn;
  @FXML
  private ComboBox<User> providerComboBox;
  @FXML
  private Button add;
  @FXML
  private Button edit;

  private int providerId = -1;

  protected void refreshTable() {
    if (providerId == -1) {
      return;
    }
    AnimalCenterRepository animalCenterRepository = AnimalCenterRepository.getInstance();

    ObservableList<AnimalCenter> entities = FXCollections.observableArrayList(
        animalCenterRepository.getByProvider(providerId)
    );

    table.setItems(entities);
  }

  public void initialize() {
    UserRepository userRepository = UserRepository.getInstance();
    AnimalCenterRepository animalCenterRepository = AnimalCenterRepository.getInstance();
    ServiceRepository serviceRepository = ServiceRepository.getInstance();

    try {
      animalCenterRepository.read();
      serviceRepository.read();
    } catch (Exception ignored) {
      SceneManager.openErrorAlert("Erro", "Não foi possível obter locais de recolha");
      close();
    }

    addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
    cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
    phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
    serviceTypeColumn.setCellValueFactory(new PropertyValueFactory<>("serviceType"));

    serviceTypeColumn.setCellFactory(tc -> new TableCell<>() {
      @Override
      protected void updateItem(Integer item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
          setText(null);
        } else {
          Service service = serviceRepository.findById(item);
          setText(
              service.getName() + " (" + service.getPrice() + "€)"
          );
        }
      }
    });

    providerComboBox.setItems(
            FXCollections.observableArrayList(userRepository.getByType(UserType.ServiceProvider))
    );

    providerComboBox.setOnAction((event -> {
      providerId = providerComboBox.getSelectionModel().getSelectedItem().getId();
      add.setDisable(false);
      edit.setDisable(false);
      refreshTable();
    }));

    add.setDisable(true);
    edit.setDisable(true);
    refreshTable();
  }

  @FXML
  public void newAnimalCenter() {
    try {
      SceneManager.openNewModal(
              "pages/admin/animalCenterForm.fxml",
              "Editar Local de recolha",
              true,
              controller -> {
                AnimalCenterFormController _controller = (AnimalCenterFormController) controller;
                _controller.setProvider(providerId);
              }
      );

      refreshTable();
    } catch (Exception e){
      System.out.println("newAnimalCenter (AnimalCenterViewController):" + e.getCause());
      SceneManager.openErrorAlert("Erro", "Não foi possível adicionar o Local de recolha");
    }
  }

  @FXML
  public void editAnimalCenter() {
    try {
      AnimalCenter selectedAnimalCenter = table.getSelectionModel().getSelectedItem();
      if (selectedAnimalCenter == null) return;

      SceneManager.openNewModal(
              "pages/admin/animalCenterForm.fxml",
              "Editar Local de recolha",
              true,
              controller -> {
                AnimalCenterFormController _controller = (AnimalCenterFormController) controller;
                _controller.setProvider(providerId);
                _controller.setAnimalCenterToEdit(selectedAnimalCenter.getId());
              }
      );

      refreshTable();
    } catch (Exception e){
      System.out.println("editAnimalCenter (AnimalCenterViewController):" + e.getCause());
      SceneManager.openErrorAlert("Erro", "Não foi possível editar o Local de recolha");
    }
  }

  public void close() {
    Stage stage = (Stage) table.getScene().getWindow();
    stage.close();
  }
}
