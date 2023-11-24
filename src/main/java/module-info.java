module prog.projeto {
  requires javafx.controls;
  requires javafx.fxml;

  requires org.controlsfx.controls;
  requires net.synedra.validatorfx;
  requires org.kordamp.bootstrapfx.core;

  opens prog.projeto to javafx.fxml;
  exports prog.projeto;
}