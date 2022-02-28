package com.example.controller;

import com.example.dani.HelloApplication;
import com.example.domain.*;
import com.example.domain.validators.*;
import com.example.repository.Repository;
import com.example.repository.db.*;
import com.example.service.Service;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;


public class RegisterController1 implements Initializable {

    Connection connection;

    {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/project_dec", "postgres", "ilikepussy44");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private final FriendshipDbRepository friendshipRepository1 = new FriendshipDbRepository(connection, new FriendshipValidator());

    private final Repository<Long, Friendship> friendshipRepository = new FriendshipDbRepository(connection, new FriendshipValidator());
    private final Repository<Long, User> userRepository = new UsersDbRepository(connection, new UserValidator(), friendshipRepository1);
    private final Repository<Long, Message> messageRepository = new MessageDbRepository(connection, new MessageValidator());
    private final Repository<Long, FriendRequest> friendRequestRepository = new FriendRequestDbRepository(connection, new FriendRequestValidator());

    private final Service service=new Service(userRepository,friendshipRepository,messageRepository,friendRequestRepository);

    @FXML
    private ImageView shieldImageView;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField confirmPasswordTextField;

    @FXML
    private Label labelText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File shieldFile = new File("/Users/danbadoiu/Downloads/dani/src/main/resources/com/example/dani/images.png");
        Image shieldImage = new Image(shieldFile.toURI().toString());
        shieldImageView.setImage(shieldImage);

    }



    public void confirmButtonOnAction(ActionEvent event) {
        labelText.setText("You try to confirm!");
        if(passwordTextField.getText().equals(confirmPasswordTextField.getText())) {
            service.saveUser(firstNameTextField.getText(), lastNameTextField.getText(), emailTextField.getText(), passwordTextField.getText());
        }
        else{
            labelText.setText("Invalid password!");
        }
    }

    public void cancelButtonOnAction(ActionEvent event){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 520, 400);
            stage.setTitle("Login");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }



    }
}
