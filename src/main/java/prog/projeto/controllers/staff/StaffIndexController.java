package prog.projeto.controllers.staff;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import prog.projeto.SceneManager;
import prog.projeto.models.Appointment;
import prog.projeto.models.Service;
import prog.projeto.models.User;
import prog.projeto.repositories.AppointmentRepository;
import prog.projeto.repositories.ServiceRepository;
import prog.projeto.repositories.UserRepository;

import java.time.LocalDate;
import java.util.List;

public class StaffIndexController {
  @FXML
  FlowPane appointmentsToday;
  @FXML
  Label noAppointmentsLabel;


  @FXML
  public void initialize() {
    refresh();
  }

  private VBox createCard(Appointment appointment) {
    UserRepository userRepository = UserRepository.getInstance();
    ServiceRepository serviceRepository = ServiceRepository.getInstance();
    Service service = serviceRepository.findById(appointment.getServiceID());
    User client = userRepository.findById(appointment.getClientID());

    VBox mainVBox = new VBox();
    mainVBox.getStyleClass().add("card");

    Label serviceLabel = new Label(service.getName() + " " + "(" + service.getPrice() + "€)");
    serviceLabel.getStyleClass().add("service");
    Label descriptionLabel = new Label(appointment.getStatus().description);
    descriptionLabel.getStyleClass().add("description");

    HBox clientBox = new HBox();
    VBox.setMargin(clientBox, new Insets(20, 0, 0, 0));
    clientBox.getStyleClass().add("provider-box");
    Label clientLabel = new Label(client.getFirstName() + " " + client.getLastName());

    clientBox.getChildren().addAll(clientLabel);
    mainVBox.getChildren().addAll(serviceLabel, descriptionLabel, clientBox);


    return mainVBox;
  }

  @FXML
  public void refresh() {
    AppointmentRepository appointmentRepository = AppointmentRepository.getInstance();
    UserRepository userRepository = UserRepository.getInstance();
    ServiceRepository serviceRepository = ServiceRepository.getInstance();

    try {
      appointmentRepository.read();
      serviceRepository.read();
    } catch (Exception ignored) {
      SceneManager.openErrorAlert("Erro", "Não foi possível obter as suas consultas");
      noAppointmentsLabel.setManaged(true);
    }

    LocalDate today = LocalDate.now();
    List<Appointment> appointments = AppointmentRepository.getInstance().getAppointmentsForDay(today).stream()
            .filter(appointment -> appointment.getEmployeeID() == userRepository.getSelectedUser().getId())
            .toList();

    if (appointments.isEmpty()) {
      noAppointmentsLabel.setManaged(true);
    } else {
      appointmentsToday.getChildren().clear();
      for (Appointment appointment : appointments) {
        appointmentsToday.getChildren().add(createCard(appointment));
      }
    }
  }
}
