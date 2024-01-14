package prog.projeto.controllers.admin;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import prog.projeto.SceneManager;
import prog.projeto.models.User;
import prog.projeto.models.UserType;
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
  public Button deactivateButton;
  @FXML
  public Button editButton;


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

        deactivateButton.setText(
                newValue.getStatus() ? "Desativar" : "Ativar"
        );
        deactivateButton.setDisable(false);
        editButton.setDisable(false);
      } else {
        name.setText("");
        email.setText("");
        address.setText("");
        phone.setText("");

        deactivateButton.setDisable(true);
        editButton.setDisable(true);
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
        setText(String.format("%s %s %s", user.getFirstName(), user.getLastName(), user.getStatus() ? "" : "(Desativado)"));
      }
    }
  }

  @FXML
  void add() {
    try {
      SceneManager.openNewModal("pages/admin/userForm.fxml", "Adicionar Utilizador", true);

      refreshList(filterUsers.getValue());
    } catch (Exception e) {
      System.out.println("add (ManageUsersController):" + e.getCause());
      SceneManager.openErrorAlert("Erro", "Não foi possível criar o utilizador");
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
                UserFormController _controller = (UserFormController) controller;
                _controller.enableEdit(usersList.getSelectionModel().getSelectedItem());
              }
      );

      refreshList(filterUsers.getValue());
    } catch (Exception e) {
      System.out.println("edit (ManageUsersController):" + e.getCause());
      SceneManager.openErrorAlert("Erro", "Não foi possível editar o utilizador");
    }
  }

  @FXML
  private void changeStatus() {
    if (currentUser == -1) {
      SceneManager.openErrorAlert("Erro", "Selecione um Utilizador");
      return;
    }
    UserRepository userRepository = UserRepository.getInstance();

    boolean response;
    if(!userRepository.findById(currentUser).getStatus()){
      response = SceneManager.openConfirmationAlert("Ativar utilizador", "Tem a certeza que quer ativar este utilizador?");
    }else{
      response = SceneManager.openConfirmationAlert("Destativar utilizador", "Tem a certeza que quer destivar este utilizador?");
    }
    if(!response) { return; }
    userRepository.findById(currentUser).setStatus(!userRepository.findById(currentUser).getStatus());

    try {
      userRepository.save();

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
