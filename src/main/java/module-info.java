module prog.projeto {
  requires javafx.controls;
  requires javafx.fxml;

  requires org.controlsfx.controls;
  requires net.synedra.validatorfx;
  requires org.kordamp.bootstrapfx.core;
  requires static lombok;

  opens prog.projeto to javafx.fxml;
  exports prog.projeto;
  exports prog.projeto.controllers;
  opens prog.projeto.controllers to javafx.fxml;
  exports prog.projeto.controllers.client;
  opens prog.projeto.controllers.client to javafx.fxml;
  exports prog.projeto.controllers.admin;
  opens prog.projeto.controllers.admin to javafx.fxml;
  exports prog.projeto.controllers.provider;
  opens prog.projeto.controllers.provider to javafx.fxml;
  exports prog.projeto.models;
  opens prog.projeto.models to javafx.fxml;
}