package prog.projeto.controllers.provider;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;
import prog.projeto.SceneManager;
import prog.projeto.models.User;
import prog.projeto.repositories.UserRepository;

public class ManageStaffController {
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
  Label city;
  @FXML
  Label phone;


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
        // If a user is selected, update the label text
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
  void add() {
    SceneManager.openNewModal("pages/provider/staffForm.fxml", "Adicionar Empregado", true);
  }

  @FXML
  private void edit() {
    /*if (currentUser == -1) {
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

    stage.showAndWait();*/
  }

  @FXML
  private void remove(){
    /*if (currentUser == -1) {
      SceneManager.openErrorAlert("Erro", "Selecione um Utilizador");
      return;
    }

    boolean response = SceneManager.openConfirmationAlert("Remover utilizador", "Têm a certeza que quer eliminareste utilizador?");
    if(!response) { return; }

    UserRepository userRepository = UserRepository.getInstance();
    userRepository.delete(currentUser);
    try {
      userRepository.save();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }*/
  }
}
