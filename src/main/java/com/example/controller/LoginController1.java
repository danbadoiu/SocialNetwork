package com.example.controller;

import com.example.dani.HelloApplication;
import com.example.domain.*;
import com.example.domain.validators.FriendRequestValidator;
import com.example.domain.validators.FriendshipValidator;
import com.example.domain.validators.MessageValidator;
import com.example.domain.validators.UserValidator;
import com.example.repository.Repository;
import com.example.repository.db.*;
import com.example.service.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.security.spec.ECField;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController1 implements Initializable {
    Connection connection;
    {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/project_dec","postgres","ilikepussy44");
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
    private Button loginButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private ImageView brandingImageView;
    @FXML
    private ImageView lockImageView;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField enterPasswordField;
    @FXML
    private Button registerButton;


    public void loginButtonOnAction(ActionEvent event){
        loginMessageLabel.setText("You try to login");
        if(!emailTextField.getText().isBlank() && !enterPasswordField.getText().isBlank()){
            validateLogin();

        }
        else{
            loginMessageLabel.setText("Please enter username and password!");
        }

    }

    public void registerButtonOnAction(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("register.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 520, 400);
            stage.setTitle("Register");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void cancelButtonOnAction(ActionEvent event){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();

    }
    private void validateLogin(){

        boolean ok=false;
        for (User user:service.printAll()){
            if (user.getEmail().equals(emailTextField.getText()) && user.getPassword().equals(enterPasswordField.getText())){
                loginMessageLabel.setText("Congratulations!");
                //createAccountFrom();

                {   //User user1 = userRepository.getByEmail(emailTextField.getText());
                    User user2 = userRepository.findOne(user.getId());
                    LoggedUser.setUser(user2);
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("account.fxml"));
                    Scene scene;
                    try {
                        Stage stage = (Stage) loginButton.getScene().getWindow();
                        stage.close();
                        scene = new Scene(fxmlLoader.load(), 520, 400);
                        stage.setTitle(emailTextField.getText());
                        stage.setScene(scene);
                        stage.show();

                        AccountController accountController = fxmlLoader.getController();
                        accountController.setLabelText(emailTextField.getText());
//                        service.saveCurrentUser(emailTextField.getText());


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                ok=true;
                break;
            }
        }
        if (!ok){
            loginMessageLabel.setText("Invalid login! Please try again.");
        }

    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File brandingFile = new File("/Users/danbadoiu/Downloads/dani/src/main/resources/com/example/dani/im1.png");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);

        File lockFile = new File("/Users/danbadoiu/Downloads/dani/src/main/resources/com/example/dani/im2.png");
        Image lockImage = new Image(lockFile.toURI().toString());
        lockImageView.setImage(lockImage);


    }

    public void createAccountFrom(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("register.fxml"));
            Stage registerStage = new Stage();
            registerStage.initStyle(StageStyle.UNDECORATED);
            registerStage.setScene(new Scene(root, 520, 400));
            registerStage.show();
        }catch (Exception e){
            e.printStackTrace();
        }




    }



}

