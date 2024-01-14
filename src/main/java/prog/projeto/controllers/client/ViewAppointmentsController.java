package prog.projeto.controllers.client;


import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import prog.projeto.SceneManager;
import prog.projeto.models.Appointment;
import prog.projeto.models.AppointmentStatus;
import prog.projeto.models.Extra;
import prog.projeto.models.User;
import prog.projeto.repositories.AppointmentRepository;
import prog.projeto.repositories.ServiceRepository;
import prog.projeto.repositories.UserRepository;

public class ViewAppointmentsController {
  @FXML
  ComboBox<AppointmentStatus> statusComboBox;
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
  Button cancelButton;
  @FXML
  Label notes;
  @FXML
  ListView<Extra> extraProductsList;
  @FXML
  Label totalLabel;

  @FXML
  private void initialize() {
    AppointmentRepository appointmentRepository = AppointmentRepository.getInstance();
    UserRepository userRepository = UserRepository.getInstance();
    ServiceRepository serviceRepository = ServiceRepository.getInstance();

    try {
      appointmentRepository.read();
      serviceRepository.read();
    } catch (Exception ignored) {
      SceneManager.openErrorAlert("Erro", "Não foi possível obter as suas consultas");
      close();
    }

    statusComboBox.setItems(FXCollections.observableArrayList(AppointmentStatus.values()));
    statusComboBox.setCellFactory(tc -> new AppointmentStatusListCell());
    statusComboBox.setButtonCell(new AppointmentStatusListCell());
    statusComboBox.setValue(AppointmentStatus.Scheduled);
    statusComboBox.setOnAction((event -> {
      refreshList(statusComboBox.getValue());
      cancelButton.setDisable(true);
    }));

    appointmentsList.setCellFactory(param -> new AppointmentListCell());

    refreshList(AppointmentStatus.Scheduled);

    appointmentsList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    appointmentsList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        User _provider = userRepository.findById(newValue.getProviderID());
        User _employee = userRepository.findById(newValue.getEmployeeID());

        provider.setText(String.format("%s %s", _provider.getFirstName(), _provider.getLastName()));
        employee.setText(String.format("%s %s", _employee.getFirstName(), _employee.getLastName()));
        serviceType.setText(String.format("%s", serviceRepository.findById(newValue.getServiceID())));
        date.setText(String.format("%s", newValue.getDate()));
        notes.setText(String.format("%s", newValue.getNotes()));
        totalLabel.setText(String.format("%.2f€", newValue.getFinalValue()));
        extraProductsList.getItems().clear();
        extraProductsList.getItems().addAll(newValue.getExtraProducts());
        if (statusComboBox.getValue() == AppointmentStatus.Scheduled)
          cancelButton.setDisable(false);
      } else {
        provider.setText("");
        employee.setText("");
        serviceType.setText("");
        date.setText("");
        notes.setText("");
        totalLabel.setText("");
        extraProductsList.getItems().clear();
        cancelButton.setDisable(true);
      }
    });
  }

  @FXML
  protected void newAppointment() {
    SceneManager.openNewModal("pages/client/scheduleAppointment.fxml", "Nova marcação", true);

    refreshList(statusComboBox.getValue());
  }

  @FXML
  protected void cancelAppointment() {
    boolean response = SceneManager.openConfirmationAlert(
        "Cancelar marcação",
        "Tem a certeza que deseja cancelar esta marcação?"
    );
    if (response) {
      AppointmentRepository appointmentRepository = AppointmentRepository.getInstance();

      Appointment appointment = appointmentRepository
          .findById(appointmentsList.getSelectionModel().getSelectedItem().getId());

      appointment.setStatus(AppointmentStatus.Cancelled);

      try {
        appointmentRepository.save();
      } catch (Exception ignored) {
        SceneManager.openErrorAlert("Erro", "Não foi possível cancelar a sua consulta");
      }

      refreshList(statusComboBox.getValue());
    }
  }

  protected void refreshList(AppointmentStatus status) {
    AppointmentRepository appointmentRepository = AppointmentRepository.getInstance();
    UserRepository userRepository = UserRepository.getInstance();

    appointmentsList.getItems().clear();
    appointmentsList.getItems().addAll(
        appointmentRepository
            .getByClient(userRepository.getSelectedUser().getId())
            .stream().filter(x -> x.getStatus() == status)
            .toList()
    );
  }

  protected void close() {
    Stage stage = (Stage) appointmentsList.getScene().getWindow();
    stage.close();
  }

  private static class AppointmentStatusListCell extends ListCell<AppointmentStatus> {
    @Override
    public void updateItem(AppointmentStatus item, boolean empty) {
      super.updateItem(item, empty);
      if (empty || item == null) {
        setText(null);
      } else {
        setText(item.description);
      }
    }
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
