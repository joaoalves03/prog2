package prog.projeto.controllers.provider;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import prog.projeto.SceneManager;
import prog.projeto.models.AnimalCenter;
import prog.projeto.models.User;
import prog.projeto.repositories.AnimalCenterRepository;
import prog.projeto.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ManageStaffController {
  @FXML
  HBox controlButtons;
  @FXML
  ComboBox<AnimalCenter> animalCenterList;
  @FXML
  ListView<User> usersList;

  @FXML
  VBox informationBox;

  @FXML
  Label name;
  @FXML
  Label email;
  @FXML
  Label address;
  @FXML
  Label phone;


  @FXML
  private void initialize() {
    AnimalCenterRepository animalCenterRepository = AnimalCenterRepository.getInstance();
    UserRepository userRepository = UserRepository.getInstance();

    try {
      animalCenterRepository.read();
    } catch (Exception ignored) {
      SceneManager.openErrorAlert("Erro", "Não foi possível obter locais de recolha");
      close();
    }

    animalCenterList.getItems().addAll(
      animalCenterRepository.getByProvider(userRepository.getSelectedUser().getId())
    );

    animalCenterList.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
      refreshList();
      controlButtons.setVisible(true);
    });

    usersList.setCellFactory(param -> new UserListCell());
    usersList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    usersList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        name.setText(newValue.getFirstName() + " " + newValue.getLastName());
        email.setText(newValue.getEmail());
        address.setText(newValue.getAddress() + ", " + newValue.getCity());
        phone.setText(newValue.getPhone());
      }
    });
  }

  private static class UserListCell extends ListCell<User> {
    @Override
    protected void updateItem(User user, boolean empty) {
      super.updateItem(user, empty);
      if (empty || user == null) {
        setText(null);
      } else {
        setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
      }
    }
  }

  @FXML
  void add() {
    try{
      SceneManager.openNewModal(
              "pages/provider/staffForm.fxml",
              "Adicionar Funcionário",
              true,
              controller -> {
                StaffFormController _controller = (StaffFormController) controller;
                _controller.setAnimalCenter(animalCenterList.getSelectionModel().getSelectedItem().getId());
              }
      );

      refreshList();
    } catch (Exception e) {
      System.out.println("add (ManageStaffController):" + e.getCause());
      SceneManager.openErrorAlert("Erro", "Não foi possível adicionar o funcionário");
    }
  }

  @FXML
  private void edit() {
    if(usersList.getSelectionModel().getSelectedItem() == null) {
      return;
    }

    try{
      SceneManager.openNewModal(
              "pages/provider/staffForm.fxml",
              "Editar Funcionário",
              true,
              controller -> {
                StaffFormController _controller = (StaffFormController) controller;
                _controller.setAnimalCenter(animalCenterList.getSelectionModel().getSelectedItem().getId());
                _controller.enableEdit(usersList.getSelectionModel().getSelectedItem());
              }
      );

      refreshList();
    } catch (Exception e) {
      System.out.println("add (ManageStaffController):" + e.getCause());
      SceneManager.openErrorAlert("Erro", "Não foi possível editar o funcionário");
    }
  }

  @FXML
  private void remove(){
    if(usersList.getSelectionModel().getSelectedItem() == null) {
      return;
    }

    UserRepository userRepository = UserRepository.getInstance();
    AnimalCenterRepository animalCenterRepository = AnimalCenterRepository.getInstance();
    User employee = usersList.getSelectionModel().getSelectedItem();
    AnimalCenter animalCenter = animalCenterList.getSelectionModel().getSelectedItem();

    Alert alert = new Alert(
        Alert.AlertType.CONFIRMATION,
        "Deseja mesmo apagar este funcionário?",
        ButtonType.OK,
        ButtonType.CANCEL
    );
    alert.setHeaderText(String.format(
        "Apagar %s %s?",
        employee.getFirstName(),
        employee.getLastName()
    ));
    alert.setTitle("Apagar funcionário");
    Optional<ButtonType> result = alert.showAndWait();

    if(result.isPresent() && result.get() == ButtonType.OK) {
      try {
        userRepository.delete(employee.getId());
        animalCenter.getEmployees().remove((Integer) employee.getId());
        userRepository.save();
        animalCenterRepository.save();
      } catch (Exception e) {
        SceneManager.openErrorAlert("Erro", "Não foi possível apagar o funcionário");
      }
    }

    refreshList();
  }

  protected void refreshList() {
    UserRepository userRepository = UserRepository.getInstance();
    AnimalCenterRepository animalCenterRepository = AnimalCenterRepository.getInstance();
    List<User> employees = new ArrayList<>();

    // This looks repetitive but is needed to get the newest
    // animal center information after adding an employee
    AnimalCenter animalCenter = animalCenterRepository.findById(
        animalCenterList.getSelectionModel().getSelectedItem().getId()
    );

    for(int id: animalCenter.getEmployees()) {
      employees.add(
          userRepository.findById(id)
      );
    }

    usersList.getItems().clear();
    usersList.getItems().addAll(employees);
  }

  public void close() {
    Stage stage = (Stage) animalCenterList.getScene().getWindow();
    stage.close();
  }
}
