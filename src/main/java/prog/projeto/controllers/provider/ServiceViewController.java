package prog.projeto.controllers.provider;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import prog.projeto.PetCareApplication;
import prog.projeto.SceneManager;
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

  protected void refreshTable() {
    ServiceRepository serviceRepository = ServiceRepository.getInstance();
    ObservableList<Service> entities = FXCollections.observableList(serviceRepository.getEntities().stream().toList());
    table.setItems(entities);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    ServiceRepository serviceRepository = ServiceRepository.getInstance();

    try {
      serviceRepository.read();
    } catch (Exception ignored) {
      SceneManager.openErrorAlert("Erro ao abrir formulário", "Não foi possível obter serviços");
      close();
    }

    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

    refreshTable();
  }

  @FXML
  public void newService() {
    SceneManager.openNewModal("pages/provider/serviceForm.fxml", "Serviço", true);
    refreshTable();
  }

  @FXML
  public void editService() throws Exception {
    Service selectedService = table.getSelectionModel().getSelectedItem();
    if(selectedService == null) return;

    FXMLLoader fxmlLoader = new FXMLLoader(PetCareApplication.class.getResource("pages/provider/serviceForm.fxml"));
    Scene scene = new Scene(fxmlLoader.load());
    Stage stage = new Stage();
    stage.setScene(scene);
    stage.setTitle("Serviço");
    stage.centerOnScreen();
    ServiceFormController controller = fxmlLoader.getController();
    controller.setServiceToEdit(selectedService.getId());

    stage.showAndWait();

    refreshTable();
  }

  public void close() {
    Stage stage = (Stage) table.getScene().getWindow();
    stage.close();
  }
}
