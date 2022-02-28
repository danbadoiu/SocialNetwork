package com.example.dani;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("NatureIT");
        stage.setScene(new Scene(root,520,400));
        stage.show();

    }

    public static void main(String[] args) throws SQLException {
        //Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/project_dec", "postgres", "ilikepussy44");

        launch();
    }

}