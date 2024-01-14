package prog.projeto.controllers.admin;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import prog.projeto.SceneManager;
import prog.projeto.models.AnimalCenter;
import prog.projeto.models.User;
import prog.projeto.models.UserType;
import prog.projeto.repositories.AnimalCenterRepository;
import prog.projeto.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class ManageUsersController {
  private int currentUser = -1;

  @FXML
  ListView<User> usersList;

  @FXML
  ComboBox<UserType> filterUsers;

  @FXML
  public Label name;

  @FXML
  public Label email;

  @FXML
  public Label address;

  @FXML
  public Label phone;


  @FXML
  private void initialize() {

    filterUsers.setItems(FXCollections.observableArrayList(UserType.values()));
    filterUsers.setCellFactory(tc -> new UserTypeListCell());
    filterUsers.setButtonCell(new UserTypeListCell());
    filterUsers.setValue(UserType.Client);
    filterUsers.setOnAction((event -> refreshList(filterUsers.getValue())));

    refreshList(filterUsers.getValue());
    usersList.setCellFactory(param -> new UserListCell());
    usersList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    usersList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        currentUser = newValue.getId();
        name.setText(newValue.getFirstName() + " " + newValue.getLastName());
        email.setText(newValue.getEmail());
        address.setText(newValue.getAddress() + ", " + newValue.getCity());
        phone.setText(newValue.getPhone());
      } else {
        name.setText("");
        email.setText("");
        address.setText("");
        phone.setText("");
      }
    });
  }

  private static class UserTypeListCell extends ListCell<UserType> {
    @Override
    public void updateItem(UserType item, boolean empty) {
      super.updateItem(item, empty);
      if (empty || item == null) {
        setText(null);
      } else {
        setText(item.description);
      }
    }
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
    try {
      SceneManager.openNewModal("pages/admin/userForm.fxml", "Adicionar Utilizador", true);

      refreshList(filterUsers.getValue());
    } catch (Exception ignored) {
      SceneManager.openErrorAlert("Erro", "Não foi possível criar um novo utilizador");
    }
  }

  @FXML
  private void edit() {
    if (currentUser == -1) {
      SceneManager.openErrorAlert("Erro", "Selecione um Utilizador");
      return;
    }

    try {
      SceneManager.openNewModal(
              "pages/admin/userForm.fxml",
              "Editar Utilizador",
              true,
              controller -> {
                UserFormController userFormController = (UserFormController) controller;
                userFormController.enableEdit(usersList.getSelectionModel().getSelectedItem());
              }
      );

      refreshList(filterUsers.getValue());
    } catch (Exception ignored) {
      SceneManager.openErrorAlert("Erro", "Não foi possível editar o utilizador");
    }
  }

  @FXML
  private void remove() {
    if (currentUser == -1) {
      SceneManager.openErrorAlert("Erro", "Selecione um Utilizador");
      return;
    }

    boolean response = SceneManager.openConfirmationAlert("Remover utilizador", "Tem a certeza que quer eliminar este utilizador?");
    if(!response) { return; }

    UserRepository userRepository = UserRepository.getInstance();
    AnimalCenterRepository animalCenterRepository = AnimalCenterRepository.getInstance();
    UserType currentUserType = userRepository.findById(currentUser).getType();
    if(currentUserType == UserType.Staff){
      for (AnimalCenter center : animalCenterRepository.getEntities()) {
        if (center.getEmployees().contains(currentUser)) {
          center.getEmployees().remove((Integer) currentUser);
          animalCenterRepository.update(center);
          break;
        }
      }
    }
    userRepository.delete(currentUser);


    try {
      userRepository.save();
      if(currentUserType == UserType.Staff) { animalCenterRepository.save(); }

      refreshList(filterUsers.getValue());
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  protected void refreshList(UserType type) {
    UserRepository userRepository = UserRepository.getInstance();
    List<User> userList = new ArrayList<>(userRepository.getByType(type));
    userList.remove(userRepository.getSelectedUser());

    usersList.getItems().setAll(userList);
    currentUser = -1;
  }
}
