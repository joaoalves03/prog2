package prog.projeto.controllers.client;


import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import prog.projeto.SceneManager;
import prog.projeto.models.Appointment;
import prog.projeto.models.User;
import prog.projeto.repositories.AppointmentRepository;
import prog.projeto.repositories.ServiceRepository;
import prog.projeto.repositories.UserRepository;

public class ViewAppointmentsController {

  @FXML
  ListView<Appointment> appointmentsList;

  @FXML
  VBox informationBox;

  @FXML
  Label provider;
  @FXML
  Label employee;
  @FXML
  Label serviceType;
  @FXML
  Label date;
  @FXML
  Label status;


  @FXML
  private void initialize() {
    AppointmentRepository appointmentRepository = AppointmentRepository.getInstance();
    UserRepository userRepository = UserRepository.getInstance();
    ServiceRepository serviceRepository = ServiceRepository.getInstance();

    appointmentsList.setCellFactory(param -> new AppointmentListCell());

    // Populate the ListView with users
    appointmentsList.getItems().addAll(
        appointmentRepository.getByClient(userRepository.getSelectedUser().getId())
    );

    // Set the selection mode to single
    appointmentsList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    // Add a listener to detect when an item is selected
    appointmentsList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        User _provider = userRepository.findById(newValue.getProviderID());
        User _employee = userRepository.findById(newValue.getEmployeeID());

        provider.setText("Prestador: " + _provider.getFirstName() + _provider.getLastName());
        employee.setText("Funcionário: " + _employee.getFirstName() + _employee.getLastName());
        serviceType.setText("Tipo de serviço: [TODO]");
        date.setText("Data: " + newValue.getDate());
        status.setText("Estado: " + newValue.getStatus().description);
      } else {
        provider.setText("Prestador: ");
        employee.setText("Funcionário: ");
        serviceType.setText("Tipo de serviço: ");
        date.setText("Data: ");
        status.setText("Estado: ");
      }
    });
  }

  @FXML
  protected void newAppointment() {
    SceneManager.openNewModal("client/scheduleAppointment.fxml", "Nova marcação", true);

    // TODO: Refresh list
  }

  private static class AppointmentListCell extends ListCell<Appointment> {
    @Override
    protected void updateItem(Appointment appointment, boolean empty) {
      super.updateItem(appointment, empty);
      if (empty || appointment == null) {
        setText(null);
      } else {
        UserRepository userRepository = UserRepository.getInstance();
        User provider = userRepository.findById(appointment.getProviderID());
        User employee = userRepository.findById(appointment.getEmployeeID());

        setText(
            provider.getFirstName() + " " + provider.getLastName()
            + " - " + employee.getFirstName() + " " + employee.getLastName()
            + " (" + appointment.getDate() + ")"
        );
      }
    }
  }
}
