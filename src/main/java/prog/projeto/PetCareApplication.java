package prog.projeto;

import javafx.application.Application;
import javafx.stage.Stage;
import prog.projeto.repositories.UserRepository;

public class PetCareApplication extends Application {
  @Override
  public void start(Stage stage) throws Exception {
  String resource = "login.fxml";

    UserRepository userRepository = UserRepository.getInstance();
    try {
      userRepository.read();

      if(userRepository.length() == 0) {
        throw new Exception();
      }
    } catch (Exception exception) {
      resource = "register.fxml";
    }

    SceneManager.openNewWindow(resource);
  }

  public static void main(String[] args) {
    launch();
  }
}