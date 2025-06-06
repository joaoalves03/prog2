package prog.projeto.controllers.staff;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import prog.projeto.PetCareApplication;
import prog.projeto.SceneManager;
import prog.projeto.models.Appointment;
import prog.projeto.models.AppointmentStatus;
import prog.projeto.models.Extra;
import prog.projeto.models.User;
import prog.projeto.repositories.AppointmentRepository;
import prog.projeto.repositories.ServiceRepository;
import prog.projeto.repositories.UserRepository;

import java.util.Optional;

public class StaffAppointmentsController {
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
  Button addExtraProductButton;
  @FXML
  Button removeExtraProductButton;
  @FXML
  Button changeNotesButton;
  @FXML
  Button confirmButton;
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

        provider.setText(String.format("Prestador: %s %s", _provider.getFirstName(), _provider.getLastName()));
        employee.setText(String.format("Funcionário: %s %s", _employee.getFirstName(), _employee.getLastName()));
        serviceType.setText(String.format("Tipo de serviço: %s", serviceRepository.findById(newValue.getServiceID())));
        date.setText(String.format("Data: %s", newValue.getDate()));
        notes.setText(String.format("Notas: %s", newValue.getNotes()));
        totalLabel.setText(String.format("Total: %.2f€", newValue.getFinalValue()));
        extraProductsList.getItems().clear();
        extraProductsList.getItems().addAll(newValue.getExtraProducts());
        changeNotesButton.setDisable(false);
        if (statusComboBox.getValue() != AppointmentStatus.Complete
            && statusComboBox.getValue() != AppointmentStatus.Cancelled) {
          addExtraProductButton.setDisable(false);
        }
        if (statusComboBox.getValue() == AppointmentStatus.Scheduled) {
          confirmButton.setDisable(false);
          cancelButton.setDisable(false);
        } else if (statusComboBox.getValue() == AppointmentStatus.AwaitingPayment) {
          confirmButton.setDisable(false);
        }
      } else {
        provider.setText("Prestador: ");
        employee.setText("Funcionário: ");
        serviceType.setText("Tipo de serviço: ");
        date.setText("Data: ");
        notes.setText("Notas: ");
        totalLabel.setText("Total: ");
        extraProductsList.getItems().clear();
        cancelButton.setDisable(true);
        confirmButton.setDisable(true);
        addExtraProductButton.setDisable(true);
        changeNotesButton.setDisable(true);
      }
    });

    extraProductsList.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> removeExtraProductButton.setDisable(newValue == null)
    );
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

  @FXML
  protected void setNotes() {
    AppointmentRepository appointmentRepository = AppointmentRepository.getInstance();
    Appointment appointment = appointmentsList.getSelectionModel().getSelectedItem();

    TextInputDialog td = new TextInputDialog(
        appointment.getNotes()
    );

    td.setHeaderText("Introduza informação extra sobre esta marcação");

    Optional<String> newNotes = td.showAndWait();

    if (newNotes.isPresent()) {
      try {
        appointment.setNotes(newNotes.get());
        appointmentRepository.save();
        refreshList(statusComboBox.getSelectionModel().getSelectedItem());
      } catch (Exception ignored) {
        SceneManager.openErrorAlert("Erro", "Não foi possível guardar notas");
      }
    }
  }

  @FXML
  protected void addExtraProduct() {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(
          PetCareApplication.class.getResource("pages/staff/extraProductsForm.fxml")
      );
      Scene scene = new Scene(fxmlLoader.load());

      Stage stage = new Stage();
      stage.setScene(scene);
      stage.setTitle("Produto extra");
      stage.centerOnScreen();

      ExtraProductsFormController controller = fxmlLoader.getController();
      controller.setAppointment(appointmentsList.getSelectionModel().getSelectedItem().getId());

      stage.showAndWait();
    } catch (Exception e) {
      SceneManager.openErrorAlert("Erro", "Não foi possível adicionar produto extra");
    }
  }

  @FXML
  protected void confirmAppointment() {
    AppointmentRepository appointmentRepository = AppointmentRepository.getInstance();
    Appointment appointment = appointmentsList.getSelectionModel().getSelectedItem();

    String message = statusComboBox.getValue() == AppointmentStatus.Scheduled
        ? "Deseja definir esta marcação como \"Por pagar\"?"
        : "Deseja completar esta marcação?";

    boolean response = SceneManager.openConfirmationAlert(
        "Confirmação",
        message
    );

    if (response) {
      appointment.setStatus(statusComboBox.getValue() == AppointmentStatus.Scheduled
          ? AppointmentStatus.AwaitingPayment
          : AppointmentStatus.Complete
      );

      try {
        appointmentRepository.save();
      } catch (Exception e) {
        SceneManager.openErrorAlert("Erro", "Não foi possível efetuar esta ação");
      }

      refreshList(statusComboBox.getValue());
    }
  }

  @FXML
  protected void removeExtraProduct() {
    Extra extra = extraProductsList.getSelectionModel().getSelectedItem();
    Appointment appointment = appointmentsList.getSelectionModel().getSelectedItem();

    appointment.getExtraProducts().remove(extra);
    refreshList(statusComboBox.getValue());
  }

  protected void refreshList(AppointmentStatus status) {
    AppointmentRepository appointmentRepository = AppointmentRepository.getInstance();
    UserRepository userRepository = UserRepository.getInstance();

    appointmentsList.getItems().clear();
    appointmentsList.getItems().addAll(
        appointmentRepository
            .getByEmployee(userRepository.getSelectedUser().getId())
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
