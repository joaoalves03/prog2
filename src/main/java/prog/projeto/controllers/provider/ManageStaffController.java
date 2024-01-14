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
  Button deactivateButton;


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
        deactivateButton.setText(
                newValue.getStatus() ? "Desativar" : "Ativar"
        );
        deactivateButton.setDisable(false);
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
        setText(String.format("%s %s %s", user.getFirstName(), user.getLastName(), user.getStatus() ? "" : "(Desativado)"));
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
  private void changeStatus() {
    if(usersList.getSelectionModel().getSelectedItem() == null) {
      return;
    }
    UserRepository userRepository = UserRepository.getInstance();
    User employee = usersList.getSelectionModel().getSelectedItem();

    boolean response;
    if(!employee.getStatus()){
      response = SceneManager.openConfirmationAlert("Ativar funcionário", "Tem a certeza que quer ativar este funcionário?");
    }else{
      response = SceneManager.openConfirmationAlert("Destativar funcionário", "Tem a certeza que quer destivar este funcionário?");
    }
    if(!response) { return; }
    employee.setStatus(!employee.getStatus());

    try {
      userRepository.save();

      refreshList();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
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
