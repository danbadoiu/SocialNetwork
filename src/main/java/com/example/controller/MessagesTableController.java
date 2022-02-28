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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MessagesTableController implements Initializable {
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
    ArrayList<String> messages = new ArrayList<String>(messagesList());
    ArrayList<String> coversation = new ArrayList<String>(conversationList());
    @FXML
    private ListView<String> messagesListView;
    @FXML
    private Button backButton;
    @FXML
    private ListView<String> conversationListView;
    @FXML
    private Button selectButton;
    @FXML
    private Button replyButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        messagesListView.getItems().addAll(messages);
        //conversationListView.getItems().addAll(coversation);
        //Receiver.setUser(userRepository.getByEmail(conversationListView.getSelectionModel().getSelectedItem()));

    }
    private String selectedUser;

    public void listViewSelectedMessage(){
        selectedUser = messagesListView.getSelectionModel().getSelectedItem();
        selectedMessageOnAction();
    }

    public void selectedUser(String email){
        selectedUser = email;
        messagesListView.getItems().addAll(messages);
        selectedMessageOnAction();


    }
    public void selectedMessageOnAction(){
        User user = userRepository.getByEmail(selectedUser);
        //selec.setText(user.getFirstName());



    }

    private List<String> messagesList(){
        List<String> messagesList = new ArrayList<>();
        for(Message message:messageRepository.findAll()){
            if(user.getId().equals(message.getTo())){
                //messagesList.add("Message from: "+userRepository.findOne(message.getFrom()).getEmail()+", Subject: "+message.getMessage());
                messagesList.add(userRepository.findOne(message.getFrom()).getEmail());

            }

        }
        return messagesList;

    }
    private List<String> conversationList(){
        List<String> conversationList = new ArrayList<>();
//        selectedUser = messagesListView.getSelectionModel().getSelectedItem();
//        System.out.println(selectedUser);
        for(Message message:messageRepository.findAll()){
            if(user.getId().equals(message.getTo())){
                conversationList.add("["+message.getDate()+"] "+userRepository.findOne(message.getFrom()).getEmail()+": "+message.getMessage());

            }
            else if(user.getId().equals(message.getFrom())){
                conversationList.add("["+message.getDate()+"] "+user.getEmail()+": "+message.getMessage());

            }

        }
        return conversationList;

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

    public void selectButtonOnAction(ActionEvent event) {
    }

    public void selectedMessage(MouseEvent mouseEvent) {

        selectedUser = messagesListView.getSelectionModel().getSelectedItem();
        List<String> conversationList = new ArrayList<>();
        for(Message message:messageRepository.findAll()){
            if(user.getId().equals(message.getTo())&&message.getFrom().equals(userRepository.getByEmail(selectedUser).getId())){
                conversationList.add("["+message.getDate()+"] "+userRepository.findOne(message.getFrom()).getEmail()+": "+message.getMessage());

            }
            else if(user.getId().equals(message.getFrom())&&message.getTo().equals(userRepository.getByEmail(selectedUser).getId())){
                conversationList.add("["+message.getDate()+"] "+user.getEmail()+": "+message.getMessage());

            }

        }
        conversationListView.getItems().addAll(conversationList);





    }

    public String selectedMessage2(){
        return conversationListView.getSelectionModel().getSelectedItem();

    }

    public void replyButtonOnAction(ActionEvent event) {
        User user1 = userRepository.getByEmail(selectedUser);
        Receiver.setUser(user1);
        //System.out.println(Receiver.user);
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("message.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) replyButton.getScene().getWindow();
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
