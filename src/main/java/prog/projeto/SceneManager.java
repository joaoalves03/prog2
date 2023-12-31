package prog.projeto;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

public class SceneManager {
  public static void switchScene(Stage stage, String sceneName) throws Exception {
    FXMLLoader fxmlLoader = new FXMLLoader(PetCareApplication.class.getResource(sceneName));
    Scene scene = new Scene(fxmlLoader.load());
    stage.setScene(scene);
    stage.centerOnScreen();
    stage.show();
  }

  public static void openNewWindow(String sceneName) throws Exception {
    FXMLLoader fxmlLoader = new FXMLLoader(PetCareApplication.class.getResource(sceneName));
    Scene scene = new Scene(fxmlLoader.load());

    Stage stage = new Stage();
    stage.setScene(scene);
    stage.centerOnScreen();
    stage.showAndWait();
  }

  public static void openNewModal(String sceneName) throws Exception {
    FXMLLoader fxmlLoader = new FXMLLoader(PetCareApplication.class.getResource(sceneName));
    Scene scene = new Scene(fxmlLoader.load());

    Stage stage = new Stage();
    stage.setScene(scene);
    stage.initStyle(StageStyle.UNDECORATED);
    stage.initModality(Modality.APPLICATION_MODAL);  // This makes the new window modal
    stage.showAndWait();
  }
}
