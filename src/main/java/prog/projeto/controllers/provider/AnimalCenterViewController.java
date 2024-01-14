package prog.projeto.controllers.provider;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import prog.projeto.SceneManager;
import prog.projeto.models.AnimalCenter;
import prog.projeto.models.Service;
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

  protected void refreshTable() {
    AnimalCenterRepository animalCenterRepository = AnimalCenterRepository.getInstance();
    UserRepository userRepository = UserRepository.getInstance();

    ObservableList<AnimalCenter> entities = FXCollections.observableArrayList(
        animalCenterRepository.getByProvider(userRepository.getSelectedUser().getId())
    );

    table.setItems(entities);
  }

  public void initialize() {
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

    refreshTable();
  }

  @FXML
  public void newAnimalCenter() {
    UserRepository userRepository = UserRepository.getInstance();

    try{
      SceneManager.openNewModal(
              "pages/provider/animalCenterForm.fxml",
              "Novo Local de recolha",
              true,
              controller -> {
                AnimalCenterFormController _controller = (AnimalCenterFormController) controller;
                _controller.setProvider(userRepository.getSelectedUser().getId());
              }
      );

      refreshTable();
    } catch (Exception e) {
      System.out.println("newAnimalCenter (AnimalCenterViewController):" + e.getCause());
      SceneManager.openErrorAlert("Erro", "Não foi possível adicionar o local de recolha");
    }
  }

  @FXML
  public void editAnimalCenter() {
    UserRepository userRepository = UserRepository.getInstance();
    AnimalCenter selectedAnimalCenter = table.getSelectionModel().getSelectedItem();
    if (selectedAnimalCenter == null) return;

    try{
      SceneManager.openNewModal(
              "pages/provider/animalCenterForm.fxml",
              "Editar Local de recolha",
              true,
              controller -> {
                AnimalCenterFormController _controller = (AnimalCenterFormController) controller;
                _controller.setProvider(userRepository.getSelectedUser().getId());
                _controller.setAnimalCenterToEdit(selectedAnimalCenter.getId());
              }
      );

      refreshTable();
    } catch (Exception e) {
      System.out.println("editAnimalCenter (AnimalCenterViewController):" + e.getCause());
      SceneManager.openErrorAlert("Erro", "Não foi possível editar o local de recolha");
    }
  }

  public void close() {
    Stage stage = (Stage) table.getScene().getWindow();
    stage.close();
  }
}
