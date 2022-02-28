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
import java.util.ResourceBundle;

public class FriendsListController implements Initializable {
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
    ArrayList<String> users = new ArrayList<String>(friendsList());

    private List<String> friendsList() {
        List<String> userList = new ArrayList<>();
        for(Friendship friendship:friendshipRepository.findAll()){
            if(friendship.getId1().equals(user.getId())){

                userList.add(userRepository.findOne(friendship.getId2()).getEmail());
            }
            else if(friendship.getId2().equals(user.getId())){
                userList.add(userRepository.findOne(friendship.getId1()).getEmail());
            }

        }
        return userList;
    }

    @FXML
    private ListView<String> friendsListView;
    @FXML
    private Button backButton;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        friendsListView.getItems().addAll(users);

    }

    public void backButtonOnActionte(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("account.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 520, 400);
            stage.setTitle("Message");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
