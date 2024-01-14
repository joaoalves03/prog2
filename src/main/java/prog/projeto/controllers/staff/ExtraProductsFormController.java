package prog.projeto.controllers.staff;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import prog.projeto.SceneManager;
import prog.projeto.models.Appointment;
import prog.projeto.models.Extra;
import prog.projeto.repositories.AppointmentRepository;

public class ExtraProductsFormController {
  @FXML
  TextField nameField;
  @FXML
  TextField priceField;
  @FXML
  TextField quantityField;

  int appointmentID = -1;

  public void setAppointment(int id) {
    appointmentID = id;
  }

  @FXML
  protected void submit() {
    AppointmentRepository appointmentRepository = AppointmentRepository.getInstance();
    Appointment appointment = appointmentRepository.findById(appointmentID);
    float price;
    int quantity;

    try {
      if(nameField.getText().isEmpty()) throw new Exception();
      price = Float.parseFloat(priceField.getText());
      quantity = Integer.parseInt(quantityField.getText());
    } catch (Exception ignored) {
      SceneManager.openErrorAlert("Erro", "Por favor verifique todos os campos");
      return;
    }

    appointment.getExtraProducts().add(new Extra(
        nameField.getText(),
        price,
        quantity
    ));

    try {
      appointmentRepository.save();
      close();
    } catch (Exception e) {
      SceneManager.openErrorAlert("Erro", "Não foi possível adicionar produto extra\n" + e.getMessage());
    }
  }

  @FXML
  protected void close() {
    Stage stage = (Stage) nameField.getScene().getWindow();
    stage.close();
  }
}
