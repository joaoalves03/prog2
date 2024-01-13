package prog.projeto.controllers.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import prog.projeto.PetCareApplication;
import prog.projeto.SceneManager;
import prog.projeto.models.User;
import prog.projeto.repositories.UserRepository;

public class ManageUsersController {
  private int currentUser = -1;

  @FXML
  ListView<User> usersList;

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

    refreshList();
    usersList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    usersList.setCellFactory(param -> new ListCell<>() {
      @Override
      protected void updateItem(User user, boolean empty) {
        super.updateItem(user, empty);
        if (empty || user == null) {setText(null);}
        else {setText(user.getFirstName() + " " + user.getLastName());}
      }
    });

    usersList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        currentUser = newValue.getId();
        name.setText(newValue.getFirstName() + " " + newValue.getLastName());
        email.setText(newValue.getEmail());
        address.setText(newValue.getAddress() + ", " + newValue.getCity());
        phone.setText(newValue.getPhone());
      }
    });
  }

  @FXML
  void add() {
    try {
      SceneManager.openNewModal("pages/admin/userForm.fxml", "Adicionar Utilizador", true);
    } catch (Exception ignored) {
      SceneManager.openErrorAlert("Erro", "Não foi possível criar um novo utilizador");
    }
    refreshList();
  }

  @FXML
  private void edit() {
    if (currentUser == -1) {
      SceneManager.openErrorAlert("Erro", "Selecione um Utilizador");
      return;
    }

    try {
      FXMLLoader fxmlLoader = new FXMLLoader(PetCareApplication.class.getResource("pages/admin/userForm.fxml"));
      Scene scene = new Scene(fxmlLoader.load());
      Stage stage = new Stage();
      stage.setScene(scene);
      stage.setTitle("Editar Utilizador");
      stage.centerOnScreen();
      UserFormController controller = fxmlLoader.getController();
      controller.enableEdit(usersList.getSelectionModel().getSelectedItem());
      stage.showAndWait();
    } catch (Exception ignored) {
      SceneManager.openErrorAlert("Erro", "Não foi possível editar o utilizador");
    }


    refreshList();
  }

  @FXML
  private void remove() {
    if (currentUser == -1) {
      SceneManager.openErrorAlert("Erro", "Selecione um Utilizador");
      return;
    }

    boolean response = SceneManager.openConfirmationAlert("Remover utilizador", "Têm a certeza que quer eliminareste utilizador?");
    if (!response) {
      return;
    }

    UserRepository userRepository = UserRepository.getInstance();
    userRepository.delete(currentUser);
    try {
      userRepository.save();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  protected void refreshList() {
    UserRepository userRepository = UserRepository.getInstance();

    usersList.getItems().clear();
    usersList.getItems().addAll(userRepository.getAllUsers());
  }
}
