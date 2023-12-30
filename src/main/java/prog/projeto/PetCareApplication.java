package prog.projeto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import prog.projeto.controllers.RegisterController;
import prog.projeto.repositories.UserRepository;

import java.io.IOException;

public class PetCareApplication extends Application {
  @Override
  public void start(Stage stage) throws IOException {
    boolean firstTime = false;

    UserRepository userRepository = UserRepository.getInstance();
    try {
      userRepository.read();

      if(userRepository.length() == 0) {
        throw new Exception();
      }
    } catch (Exception exception) {
      // TODO: Redirect to a one time register screen
      firstTime = true;
    }

    FXMLLoader fxmlLoader = new FXMLLoader(PetCareApplication.class.getResource(
        firstTime ? "register.fxml" : "login.fxml"
    ));
    Parent root = fxmlLoader.load();

    if(firstTime) {
      RegisterController controller = fxmlLoader.getController();
      controller.enableFirstTime();
    }

    Scene scene = new Scene(root);
    stage.setTitle("Pet Care");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}