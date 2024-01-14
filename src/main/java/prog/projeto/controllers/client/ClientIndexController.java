package prog.projeto.controllers.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import java.util.List;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import prog.projeto.SceneManager;
import prog.projeto.models.Appointment;
import prog.projeto.models.Service;
import prog.projeto.repositories.AppointmentRepository;
import prog.projeto.repositories.ServiceRepository;
import prog.projeto.repositories.UserRepository;

import java.time.LocalDate;

public class ClientIndexController {
  @FXML
  FlowPane appointmentsToday;
  @FXML
  Label noAppointmentsLabel;


  @FXML
  public void initialize(){
    refresh();
  }

  private VBox createCard(Appointment appointment){
    UserRepository userRepository = UserRepository.getInstance();
    ServiceRepository serviceRepository = ServiceRepository.getInstance();
    Service service = serviceRepository.findById(appointment.getServiceID());

    VBox mainVBox = new VBox();
    mainVBox.getStyleClass().add("card");

    Label serviceLabel = new Label(service.getName() + " " + "(" + service.getPrice() + "€)");
    serviceLabel.getStyleClass().add("service");
    Label descriptionLabel = new Label(appointment.getStatus().description);
    descriptionLabel.getStyleClass().add("description");

    HBox providerBox = new HBox();
    VBox.setMargin(providerBox, new Insets(20,0,0,0));
    providerBox.getStyleClass().add("provider-box");
    Label providerLabel = new Label(userRepository.findById(appointment.getProviderID()).getFirstName());
    Label dashLabel = new Label("-");
    Label staffLabel = new Label(userRepository.findById(appointment.getEmployeeID()).getFirstName());

    providerBox.getChildren().addAll(providerLabel, dashLabel, staffLabel);
    mainVBox.getChildren().addAll(serviceLabel, descriptionLabel, providerBox);


    return mainVBox;
  }

  @FXML
  public void refresh(){
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
    List<Appointment> appointments = AppointmentRepository.getInstance().getAppointmentsForDay(userRepository.getSelectedUser().getId(), today);

    if(appointments.isEmpty()){
      noAppointmentsLabel.setManaged(true);
    }else{
      appointmentsToday.getChildren().clear();
      for (Appointment appointment : appointments){
        appointmentsToday.getChildren().add(createCard(appointment));
      }
    }
  }

  @FXML
  protected void openModal(ActionEvent event){
    Button node = (Button) event.getSource();
    String scenePath = (String) node.getUserData();
    String title = node.getText();

    SceneManager.openNewModal(scenePath, title, true);
  }
}
