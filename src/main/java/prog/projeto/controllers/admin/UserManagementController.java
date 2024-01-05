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
import prog.projeto.controllers.EditUserController;
import prog.projeto.controllers.RegisterController;
import prog.projeto.models.User;
import prog.projeto.repositories.UserRepository;

public class UserManagementController {
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
    UserRepository userRepository = UserRepository.getInstance();

    // Set the cell factory to display the user's first name
    usersList.setCellFactory(param -> new UserListCell());

    // Populate the ListView with users
    usersList.getItems().addAll(userRepository.getAllUsers());

    // Set the selection mode to single
    usersList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    // Add a listener to detect when an item is selected
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

  private static class UserListCell extends ListCell<User> {
    @Override
    protected void updateItem(User user, boolean empty) {
      super.updateItem(user, empty);
      if (empty || user == null) {
        setText(null);
      } else {
        setText(user.getFirstName());
      }
    }
  }

  @FXML
  void add() throws Exception {
    FXMLLoader fxmlLoader = new FXMLLoader(PetCareApplication.class.getResource("pages/register.fxml"));
    Scene scene = new Scene(fxmlLoader.load());
    Stage stage = new Stage();
    stage.setScene(scene);
    stage.setTitle("Adicionar Utilizador");
    stage.centerOnScreen();
    RegisterController controller = fxmlLoader.getController();
    controller.addAdminType();
    controller.insideModal();

    stage.showAndWait();
  }

  @FXML
  private void edit() throws Exception {
    if (currentUser == -1) {
      SceneManager.openErrorAlert("Erro", "Selecione um Utilizador");
      return;
    }

    FXMLLoader fxmlLoader = new FXMLLoader(PetCareApplication.class.getResource("widgets/edit-user.fxml"));
    Scene scene = new Scene(fxmlLoader.load());
    Stage stage = new Stage();
    stage.setScene(scene);
    stage.setTitle("Editar Utilizador");
    stage.centerOnScreen();
    EditUserController controller = fxmlLoader.getController();
    controller.setUser(currentUser);

    stage.showAndWait();
  }

  @FXML
  private void remove(){
    if (currentUser == -1) {
      SceneManager.openErrorAlert("Erro", "Selecione um Utilizador");
      return;
    }

    boolean response = SceneManager.openConfirmationAlert("Remover utilizador", "Têm a certeza que quer eliminareste utilizador?");
    if(!response) { return; }

    UserRepository userRepository = UserRepository.getInstance();
    userRepository.delete(currentUser);
  }
}
