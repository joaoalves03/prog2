package prog.projeto.controllers.provider;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import prog.projeto.PetCareApplication;
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

    // TODO: Filter by animal center
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
    FXMLLoader fxmlLoader = new FXMLLoader(PetCareApplication.class.getResource("pages/provider/staffForm.fxml"));

    try {
      Scene scene = new Scene(fxmlLoader.load());
      Stage stage = new Stage();
      stage.setScene(scene);
      stage.setTitle("Adicionar Empregado");
      stage.centerOnScreen();
      StaffFormController controller = fxmlLoader.getController();
      controller.setAnimalCenter(usersList.getSelectionModel().getSelectedIndex());
      stage.showAndWait();
    } catch (Exception ignored) {
      SceneManager.openErrorAlert("Erro", "Não foi possível criar um novo empregado");
    }

    refreshList();
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

  protected void refreshList() {
    UserRepository userRepository = UserRepository.getInstance();

    // TODO: Filter users by animal center
    usersList.getItems().clear();
    usersList.getItems().addAll(userRepository.getAllUsers());
  }
}
