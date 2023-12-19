package prog.projeto;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
  public static void switchScene(Stage stage, String sceneName) throws Exception {
    FXMLLoader fxmlLoader = new FXMLLoader(PetCareApplication.class.getResource(sceneName));
    Scene scene = new Scene(fxmlLoader.load());
    stage.setScene(scene);
    stage.centerOnScreen();
    stage.show();
  }
}
