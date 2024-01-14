package prog.projeto.controllers.provider;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import prog.projeto.SceneManager;
import prog.projeto.models.Service;
import prog.projeto.repositories.ServiceRepository;

public class ServiceViewController {
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

  public void initialize() {
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
    try {
      SceneManager.openNewModal("pages/provider/serviceForm.fxml", "Serviço", true);
      refreshTable();
    } catch (Exception e){
    System.out.println("newService (ServiceViewController):" + e.getCause());
    SceneManager.openErrorAlert("Erro", "Não foi possível adicionar o serviço");
  }
  }

  @FXML
  public void editService() {
    Service selectedService = table.getSelectionModel().getSelectedItem();
    if(selectedService == null) return;

    try {
      SceneManager.openNewModal(
              "pages/provider/serviceForm.fxml",
              "Editar Serviço",
              true,
              controller -> {
                ServiceFormController _controller = (ServiceFormController) controller;
                _controller.setServiceToEdit(selectedService.getId());
              }
      );

      refreshTable();
    } catch (Exception e){
      System.out.println("editService (ServiceViewController):" + e.getCause());
      SceneManager.openErrorAlert("Erro", "Não foi possível editar o serviço");
    }
  }

  public void close() {
    Stage stage = (Stage) table.getScene().getWindow();
    stage.close();
  }
}
