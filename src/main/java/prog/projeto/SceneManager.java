package prog.projeto;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

import java.util.Objects;
import java.util.function.Consumer;

public class SceneManager {
  public static void openNewWindow(String sceneName, boolean maximized) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(PetCareApplication.class.getResource(sceneName));
      Scene scene = new Scene(fxmlLoader.load());



      Stage stage = new Stage();
      stage.setScene(scene);
      stage.setTitle("PetCare");
      stage.getIcons().clear();
      stage.getIcons().add(getAppIcon());
      stage.centerOnScreen();
      stage.setMaximized(maximized);
      stage.show();
    } catch (Exception e){
      System.out.println("openNewWindow (SceneManager): " + e.getCause());
    }
  }

  public static void openNewModal(String sceneName, String title, boolean movable, Consumer<Object> handler) {
    try {
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

      Object controller = fxmlLoader.getController();

      if (handler != null) {
        handler.accept(controller);
      }

      stage.showAndWait();
    } catch (Exception e) {
      System.out.println("openNewModal (SceneManager): " + e.getCause());
    }
  }



  public static void openNewModal(String sceneName, String title, boolean movable) {
    try {
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
    } catch (Exception e) {
      System.out.println("openNewModal (SceneManager): " + e.getCause());
    }
  }

  public static void switchScene(Stage stage, Scene scene) {
    if(stage == null) { System.out.println("Stage is null. Unable to switch scenes."); return;}
    stage.setScene(scene);
    stage.getIcons().clear();
    stage.getIcons().add(getAppIcon());
    stage.centerOnScreen();
    stage.show();
  }

  public static void switchScene(Stage stage, String sceneName) {
    try {
      if(stage == null) { throw new Exception("Stage is null. Unable to switch scenes.");}
      FXMLLoader fxmlLoader = new FXMLLoader(PetCareApplication.class.getResource(sceneName));
      Scene scene = new Scene(fxmlLoader.load());
      stage.setScene(scene);
      stage.getIcons().clear();
      stage.getIcons().add(getAppIcon());
      stage.centerOnScreen();
      stage.show();
    } catch (Exception e) {
      System.out.println("switchScene (SceneManager): " + e.getCause());
    }
  }

  public static void switchScene(Node node, String sceneName) {
    switchScene(getStage(node), sceneName);
  }

  public static boolean openConfirmationAlert(String alertHeader, String alertContent){
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Alerta");
    alert.setHeaderText(alertHeader);
    alert.setContentText(alertContent);
    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    stage.getIcons().add(getAppIcon());
    stage.getIcons().clear();
    return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
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
    return null;
  }

  public static void closeWindow(Stage stage){
      if(stage == null) { System.out.println("Stage is null. Unable to switch scenes."); return;}
      stage.close();
  }

  public static void closeWindow(Node node){
    closeWindow(getStage(node));
  }
}
