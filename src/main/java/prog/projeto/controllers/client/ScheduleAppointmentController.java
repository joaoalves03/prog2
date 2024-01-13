package prog.projeto.controllers.client;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import prog.projeto.SceneManager;
import prog.projeto.models.*;
import prog.projeto.repositories.AnimalCenterRepository;
import prog.projeto.repositories.AppointmentRepository;
import prog.projeto.repositories.ServiceRepository;
import prog.projeto.repositories.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ScheduleAppointmentController {
  @FXML
  private DatePicker datePicker;
  @FXML
  private ComboBox<User> providerComboBox;
  @FXML
  private Label animalCenterLabel;
  @FXML
  private ComboBox<AnimalCenter> animalCenterComboBox;
  @FXML
  private Label serviceLabel;
  @FXML
  private ComboBox<Service> serviceComboBox;
  @FXML
  private Label employeeLabel;
  @FXML
  private ComboBox<User> employeeComboBox;
  @FXML
  private Button submitButton;

  public void initialize() throws RuntimeException {
    try {
      UserRepository userRepository = UserRepository.getInstance();
      AnimalCenterRepository animalCenterRepository = AnimalCenterRepository.getInstance();
      ServiceRepository serviceRepository = ServiceRepository.getInstance();

      userRepository.read();
      animalCenterRepository.read();
      serviceRepository.read();

      providerComboBox.setItems(
          FXCollections.observableArrayList(userRepository.getByType(UserType.ServiceProvider))
      );

      providerComboBox.setOnAction((event -> {
        User selectedUser = providerComboBox.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
          animalCenterLabel.setVisible(false);
        } else {
          animalCenterComboBox.setItems(
              FXCollections.observableList(animalCenterRepository.getByProvider(selectedUser.getId()))
          );
          animalCenterLabel.setVisible(true);
        }

        employeeLabel.setVisible(false);
        serviceLabel.setVisible(false);
        datePicker.setVisible(false);
        submitButton.setDisable(true);
      }));

      animalCenterComboBox.setOnAction((event -> {
        AnimalCenter selectedAnimalCenter = animalCenterComboBox.getSelectionModel().getSelectedItem();
        if (selectedAnimalCenter == null) {
          employeeLabel.setVisible(false);
        } else {
          List<User> employees = new ArrayList<>();

          for (int id : animalCenterComboBox.getSelectionModel().getSelectedItem().getEmployees()) {
            employees.add(
                userRepository.findById(id)
            );
          }

          employeeComboBox.setItems(FXCollections.observableList(employees));
          employeeLabel.setVisible(true);
        }

        serviceLabel.setVisible(false);
        datePicker.setVisible(false);
        submitButton.setDisable(true);
      }));

      employeeComboBox.setOnAction((event -> {
        serviceComboBox.getItems().clear();
        serviceComboBox.getItems().addAll(serviceRepository.getEntities());
        serviceLabel.setVisible(true);
        submitButton.setDisable(true);
      }));

      serviceComboBox.setOnAction((event -> {
        User selectedEmployee = employeeComboBox.getSelectionModel().getSelectedItem();
        datePicker.valueProperty().set(null);
        if (selectedEmployee == null) {
          datePicker.setVisible(false);
        } else {
          AppointmentRepository appointmentRepository = AppointmentRepository.getInstance();
          List<Appointment> appointments = appointmentRepository.getByEmployee(selectedEmployee.getId());

          datePicker.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
              super.updateItem(date, empty);
              if (empty) setDisable(true);
              else {
                appointments.forEach(appointment -> {
                  if (date.isEqual(appointment.getDate())) {
                    setDisable(true);
                  }
                });

                setDisable(false);
              }
            }
          });

          datePicker.setVisible(true);
          submitButton.setDisable(true);
        }
      }));

      datePicker.setOnAction((event -> {
        if(datePicker.valueProperty().isNotNull().get()){
          submitButton.setDisable(false);
        }
      }));
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  @FXML
  protected void submit() {
    UserRepository userRepository = UserRepository.getInstance();
    AppointmentRepository appointmentRepository = AppointmentRepository.getInstance();

    appointmentRepository.add(new Appointment(
        appointmentRepository.getNextId(),
        providerComboBox.getSelectionModel().getSelectedItem().getId(),
        serviceComboBox.getSelectionModel().getSelectedItem().getId(),
        employeeComboBox.getSelectionModel().getSelectedItem().getId(),
        userRepository.getSelectedUser().getId(),
        datePicker.getValue()
    ));

    try {
      appointmentRepository.save();
      close();
    } catch (Exception ignored) {
      SceneManager.openErrorAlert("Erro", "Não foi possível guardar a consulta");
    }
  }

  @FXML
  public void close() {
    Stage stage = (Stage) datePicker.getScene().getWindow();
    stage.close();
  }
}
