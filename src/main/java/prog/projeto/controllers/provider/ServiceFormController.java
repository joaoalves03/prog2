package prog.projeto.controllers.provider;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import prog.projeto.SceneManager;
import prog.projeto.models.Service;
import prog.projeto.repositories.ServiceRepository;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ServiceFormController implements Initializable {
  int newID;
  @FXML
  TextField nameField;
  @FXML
  TextField priceField;
  boolean edit = false;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    ServiceRepository serviceRepository = ServiceRepository.getInstance();
    newID = serviceRepository.getNextId();
  }

  public void setServiceToEdit(int id) {
    ServiceRepository serviceRepository = ServiceRepository.getInstance();

    newID = id;
    Service service = serviceRepository.findById(id);

    nameField.setText(service.getName());
    priceField.setText(String.valueOf(service.getPrice()));

    edit = true;
  }

  @FXML
  protected void save() throws IOException {
    ServiceRepository serviceRepository = ServiceRepository.getInstance();
    float price;

    try {
      if(nameField.getText().isEmpty() || priceField.getText().isEmpty()) throw new Exception();
      price = Float.parseFloat(priceField.getText());
    } catch (Exception ignored) {
      SceneManager.openErrorAlert("Erro a guardar serviço", "Dados incorretos");
      return;
    }

    if(edit) {
      serviceRepository.update(new Service(
          newID,
          nameField.getText(),
          price
      ));
    } else {
      serviceRepository.add(new Service(
          newID,
          nameField.getText(),
          Float.parseFloat(priceField.getText())
      ));
    }

    serviceRepository.save();

    Stage stage = (Stage) nameField.getScene().getWindow();
    stage.close();
  }
}
