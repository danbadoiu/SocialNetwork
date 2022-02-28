package com.example.controller;

import com.example.dani.HelloApplication;
import com.example.domain.*;
import com.example.domain.validators.FriendRequestValidator;
import com.example.domain.validators.FriendshipValidator;
import com.example.domain.validators.MessageValidator;
import com.example.domain.validators.UserValidator;
import com.example.repository.Repository;
import com.example.repository.db.FriendRequestDbRepository;
import com.example.repository.db.FriendshipDbRepository;
import com.example.repository.db.MessageDbRepository;
import com.example.repository.db.UsersDbRepository;
import com.example.service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class FriendsController implements Initializable {
    User user = LoggedUser.user;
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
    ArrayList<String> users = new ArrayList<String>(requestsList());

    @FXML
    private ListView<String> listView;
    @FXML
    private Button backButton;
    @FXML
    private Button acceptButton;
    @FXML
    private javafx.scene.text.Text selectedUser;
    @FXML
    private Button declineButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listView.getItems().addAll(users);

    }
    private List<String> userList(){
        List<String> userList = new ArrayList<>();
        for(User user:service.getFriends(user)){
            userList.add(user.getEmail());
        }
        return userList;
    }

    public void backButtonOnAction(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("account.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 520, 400);
            stage.setTitle("Login");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private List<String> requestsList(){
        List<String> userList = new ArrayList<>();
        for(FriendRequest friendRequest:friendRequestRepository.findAll()){
            if(friendRequest.getId2().equals(user.getId())){

                userList.add(userRepository.findOne(friendRequest.getId1()).getEmail());
            }

        }
        return userList;

    }
    private String selected;

    public void listViewSelectedUser(){
        selected = listView.getSelectionModel().getSelectedItem();
        selectedUserOnAction();
    }

    public void selectedUser(String email){
        selected = email;
        listView.getItems().addAll(users);
        selectedUserOnAction();


    }
    public void selectedUserOnAction(){
        User user = userRepository.getByEmail(selected);
        selectedUser.setText(user.getFirstName());



    }


    public void acceptButtonOnAction(ActionEvent event) {
        service.saveFriendship(user.getId(),userRepository.getByEmail(selected).getId());
        for(FriendRequest friendRequest1: friendRequestRepository.findAll()){
            if(friendRequest1.getId1().equals(userRepository.getByEmail(selected).getId())){
                service.deleteFriendRequests(friendRequest1.getId());
                break;

            }
        }
        acceptButton.setText("Accepted");




    }

    public void declineButtonOnAction(ActionEvent event) {
        for(FriendRequest friendRequest1: friendRequestRepository.findAll()){
            if(friendRequest1.getId1().equals(userRepository.getByEmail(selected).getId())){
                service.deleteFriendRequests(friendRequest1.getId());
                break;

            }
        }
        declineButton.setText("Declined");
    }
}
