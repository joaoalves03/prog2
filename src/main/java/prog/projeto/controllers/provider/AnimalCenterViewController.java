package prog.projeto.controllers.provider;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import prog.projeto.PetCareApplication;
import prog.projeto.SceneManager;
import prog.projeto.models.AnimalCenter;
import prog.projeto.models.Service;
import prog.projeto.repositories.AnimalCenterRepository;
import prog.projeto.repositories.ServiceRepository;
import prog.projeto.repositories.UserRepository;

import java.net.URL;
import java.util.ResourceBundle;

public class AnimalCenterViewController implements Initializable {
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
    ObservableList<AnimalCenter> entities = FXCollections.observableArrayList(
        animalCenterRepository.getEntities().stream().toList()
    );

    table.setItems(entities);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
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
  public void newAnimalCenter() throws Exception {
    UserRepository userRepository = UserRepository.getInstance();

    FXMLLoader fxmlLoader = new FXMLLoader(PetCareApplication.class.getResource("provider/animalCenterForm.fxml"));
    Scene scene = new Scene(fxmlLoader.load());
    Stage stage = new Stage();
    stage.setScene(scene);
    stage.setTitle("Novo Local de recolha");
    stage.centerOnScreen();
    AnimalCenterFormController controller = fxmlLoader.getController();
    controller.setProvider(userRepository.getSelectedUser().getId());

    stage.showAndWait();

    refreshTable();
  }

  @FXML
  public void editAnimalCenter() throws Exception {
    UserRepository userRepository = UserRepository.getInstance();
    AnimalCenter selectedAnimalCenter = table.getSelectionModel().getSelectedItem();
    if (selectedAnimalCenter == null) return;

    FXMLLoader fxmlLoader = new FXMLLoader(PetCareApplication.class.getResource("provider/animalCenterForm.fxml"));
    Scene scene = new Scene(fxmlLoader.load());
    Stage stage = new Stage();
    stage.setScene(scene);
    stage.setTitle("Editar Local de recolha");
    stage.centerOnScreen();
    AnimalCenterFormController controller = fxmlLoader.getController();
    controller.setProvider(userRepository.getSelectedUser().getId());
    controller.setAnimalCenterToEdit(selectedAnimalCenter.getId());

    stage.showAndWait();

    refreshTable();
  }

  public void close() {
    Stage stage = (Stage) table.getScene().getWindow();
    stage.close();
  }
}
