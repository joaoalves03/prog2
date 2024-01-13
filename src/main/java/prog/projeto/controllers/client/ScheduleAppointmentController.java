package prog.projeto.controllers.client;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import prog.projeto.models.AnimalCenter;
import prog.projeto.models.Appointment;
import prog.projeto.models.User;
import prog.projeto.models.UserType;
import prog.projeto.repositories.AnimalCenterRepository;
import prog.projeto.repositories.AppointmentRepository;
import prog.projeto.repositories.UserRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class ScheduleAppointmentController {
  @FXML
  DatePicker datePicker;
  @FXML
  private ComboBox<User> providerComboBox;
  @FXML
  private Label animalCenterLabel;
  @FXML
  private ComboBox<AnimalCenter> animalCenterComboBox;
  @FXML
  private Label employeeLabel;
  @FXML
  private ComboBox<User> employeeComboBox;

  public void initialize() throws RuntimeException {
    try {
      UserRepository userRepository = UserRepository.getInstance();
      AnimalCenterRepository animalCenterRepository = AnimalCenterRepository.getInstance();

      userRepository.read();
      animalCenterRepository.read();

      providerComboBox.setItems(
          FXCollections.observableArrayList(userRepository.getByType(UserType.ServiceProvider))
      );

      providerComboBox.setOnAction((event -> {
        User selectedUser = providerComboBox.getSelectionModel().getSelectedItem();
        if(selectedUser == null) {
          animalCenterLabel.setVisible(false);
        } else {
          animalCenterComboBox.setItems(
            FXCollections.observableList(animalCenterRepository.getByProvider(selectedUser.getId()))
          );
          animalCenterLabel.setVisible(true);
        }
      }));

      animalCenterComboBox.setOnAction((event -> {
        AnimalCenter selectedAnimalCenter = animalCenterComboBox.getSelectionModel().getSelectedItem();
        if(selectedAnimalCenter == null){
          employeeLabel.setVisible(false);
        } else {
          List<User> employees = new ArrayList<>();

          for(int id: animalCenterComboBox.getSelectionModel().getSelectedItem().getEmployees()) {
            employees.add(
                userRepository.findById(id)
            );
          }

          employeeComboBox.setItems(FXCollections.observableList(employees));
          employeeLabel.setVisible(true);
        }
      }));

      employeeComboBox.setOnAction((event -> {
        User selectedEmployee = employeeComboBox.getSelectionModel().getSelectedItem();
        if(selectedEmployee == null) {
          datePicker.setVisible(false);
        } else {
          AppointmentRepository appointmentRepository = AppointmentRepository.getInstance();
          List<Appointment> appointments = appointmentRepository.getByEmployee(selectedEmployee.getId());

          datePicker.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
              super.updateItem(date,empty);
              if(empty) setDisable(true);
              else {
                appointments.forEach(appointment -> {
                  if(date.isEqual(LocalDate.ofInstant(appointment.getDate().toInstant(), ZoneId.systemDefault()))) {
                    setDisable(true);
                  }
                });

                setDisable(false);
              }
            }
          });

          datePicker.setVisible(true);
        }
      }));
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
