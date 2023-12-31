package prog.projeto;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

import java.util.Objects;

public class SceneManager {
  public static void openNewWindow(String sceneName) throws Exception {
    FXMLLoader fxmlLoader = new FXMLLoader(PetCareApplication.class.getResource(sceneName));
    Scene scene = new Scene(fxmlLoader.load());

    Stage stage = new Stage();
    stage.setScene(scene);
    stage.setTitle("PetCare");
    stage.getIcons().clear();
    stage.getIcons().add(getAppIcon());
    stage.centerOnScreen();
    stage.show();
  }

  public static void openNewModal(String sceneName, String title, boolean movable) throws Exception {
    FXMLLoader fxmlLoader = new FXMLLoader(PetCareApplication.class.getResource(sceneName));
    Scene scene = new Scene(fxmlLoader.load());

    Stage stage = new Stage();
    stage.setScene(scene);
    stage.setTitle(title);
    stage.getIcons().clear();
    stage.getIcons().add(getAppIcon());
    if(!movable) {
      stage.initStyle(StageStyle.UNDECORATED);
    }
    stage.initModality(Modality.APPLICATION_MODAL);  // This makes the new window modal
    stage.centerOnScreen();
    stage.showAndWait();
  }

  public static void switchScene(Stage stage, String sceneName) throws Exception {
    FXMLLoader fxmlLoader = new FXMLLoader(PetCareApplication.class.getResource(sceneName));
    Scene scene = new Scene(fxmlLoader.load());
    stage.setScene(scene);
    stage.getIcons().clear();
    stage.getIcons().add(getAppIcon());
    stage.centerOnScreen();
    stage.show();
  }
  
  public static void openErrorAlert(String errorHeader, String errorContent){
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Erro");
    alert.setHeaderText(errorHeader);
    alert.setContentText(errorContent);
    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    stage.getIcons().add(getAppIcon());
    alert.showAndWait();
    stage.getIcons().clear();
  }

  private static Image getAppIcon() {
    return new Image(Objects.requireNonNull(SceneManager.class.getResourceAsStream("assets/icons/petcare.png")));
  }

  public static Stage getStage(Node node) {
    if (node != null) {
      Scene scene = node.getScene();
      if (scene != null) {
        return (Stage) scene.getWindow();
      }
    }
    return null; // Return null if the node or scene is null
  }
}
