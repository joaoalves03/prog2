package prog.projeto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import prog.projeto.repositories.UserRepository;

import java.io.IOException;
import java.net.URL;

public class PetCareApplication extends Application {
  @Override
  public void start(Stage stage) throws IOException {
    URL url = PetCareApplication.class.getResource("client/scheduleService.fxml");

    UserRepository userRepository = UserRepository.getInstance();
    try {
      userRepository.read();

      if(userRepository.length() == 0) {
        throw new Exception();
      }
    } catch (Exception exception) {
      // TODO: Redirect to a one time register screen
      url = PetCareApplication.class.getResource("register.fxml");
    }

    FXMLLoader fxmlLoader = new FXMLLoader(url);
    Scene scene = new Scene(fxmlLoader.load());
    stage.setTitle("Pet Care");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}