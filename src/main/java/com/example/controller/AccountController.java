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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AccountController implements Initializable {
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
    ArrayList<String> users = new ArrayList<String>(userList());




    @FXML
    private javafx.scene.text.Text firstName;
    @FXML
    private javafx.scene.text.Text lastName;
    @FXML
    private javafx.scene.text.Text email;
    @FXML
    private javafx.scene.text.Text friendsNumber;
    @FXML
    private Label nameLabel;
    @FXML
    private Button logoutButton;
    @FXML
    private TextField searchBar;
    @FXML
    private ListView<String> listView;
    @FXML
    private Button searchButton;
    @FXML
    private Button friendsButton;

    @FXML
    private javafx.scene.text.Text selectedFirstName;
    @FXML
    private javafx.scene.text.Text selectedLastName;
    @FXML
    private javafx.scene.text.Text selectedEmail;

    @FXML
    private Button addFriendButton;
    @FXML
    private Button messageButton;
    @FXML
    private Button messagesButton;
    @FXML
    private Button removeButton;

    @FXML
    private Button settingsButton;
    @FXML
    private Button friendsListButton;

    









    @Override
    public void initialize(URL location, ResourceBundle resources) {

        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        email.setText(user.getEmail());
        friendsNumber.setText("Friends number: "+service.friendsNumber(user.getId()));

        listView.getItems().addAll(users);






    }

    public void setLabelText(String text){
        nameLabel.setText(text);

    }



    public void logoutButtonOnAction(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 520, 400);
            stage.setTitle("Login");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private List<String> searchList(String searchWords, List<String> listOfStrings){
        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split(","));
        return  listOfStrings.stream().filter(input->{
            return searchWordsArray.stream().allMatch(word->input.toLowerCase().contains(word.toLowerCase()));
        }).collect(Collectors.toList());
    }

    @FXML
    void searchButtonOnAction(ActionEvent event){
        listView.getItems().clear();
        listView.getItems().addAll(searchList(searchBar.getText(),users));
    }

    private List<String> userList(){
        List<String> userList = new ArrayList<>();
        for(User user1:service.printAll()){
            if(user1.getId().equals(user.getId())){

            }
            else{
            userList.add(user1.getEmail());}
        }
        return userList;
    }

    public void friendsButtonOnAction(ActionEvent event) {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("friends.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) friendsButton.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 520, 400);
            stage.setTitle("Friends");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }


    }
    private String selectedUser;

    public void listViewSelectedEmail(){
        selectedUser = listView.getSelectionModel().getSelectedItem();
        selectedUserOnAction();
    }

    public void selectedUser(String email){
        selectedUser = email;
        listView.getItems().addAll(users);
        selectedUserOnAction();

    }
    public void selectedUserOnAction(){
        User user = userRepository.getByEmail(selectedUser);
        selectedFirstName.setText(user.getFirstName());
        selectedLastName.setText(user.getLastName());
        selectedEmail.setText(user.getEmail());
        addFriendButton.setText("Add friend");



    }

    public void addFriendOnAction(ActionEvent event) {
        int ok=1;
        int ok1=1;
        User user1 = user;
        User user2 = userRepository.getByEmail(selectedUser);
        for(Friendship friendship:friendshipRepository.findAll()){
            if((user.getId().equals(friendship.getId1())&&user2.getId().equals(friendship.getId2())||(user.getId().equals(friendship.getId2())&&user2.getId().equals(friendship.getId1())))){
                ok1=0;
                addFriendButton.setText("Friends");
            }
        }
        if(ok1 ==1){
            addFriendButton.setText("Add friend");
        }
        else{
            addFriendButton.setText("Friends");
        }
        for(FriendRequest friendRequest: friendRequestRepository.findAll()){
            if(friendRequest.getId2().equals(user2.getId())){
                addFriendButton.setText("Friends");

                ok = 0;
            }
        }
        if(ok==1&& ok1==1){
            service.sendFriendRequest(user1.getId(), user2.getId());
            addFriendButton.setText("Request sent!");

        }

    }

    public void messageButtonOnAction(ActionEvent event) {
        User user = userRepository.getByEmail(selectedUser);
        Receiver.setUser(user);
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("message.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) messageButton.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 520, 400);
            stage.setTitle("Message");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }


        
    }



    public void messagesButtonOnAction(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("messagesTable.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) messagesButton.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 520, 400);
            stage.setTitle("Messages");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void removeButtonOnAction(ActionEvent event) {
        User user2 = userRepository.getByEmail(selectedUser);
        for(Friendship friendship:friendshipRepository.findAll()){
            if((user.getId().equals(friendship.getId1())&&user2.getId().equals(friendship.getId2())||(user.getId().equals(friendship.getId2())&&user2.getId().equals(friendship.getId1()))))
            {
                service.deleteFriendship(friendship.getId());
                addFriendButton.setText("Add friend");
                
                break;
            }
        }

    }



    public void settingsbuttonOnAction(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("settings.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) settingsButton.getScene().getWindow();
            stage.close();
            scene = new Scene(fxmlLoader.load(), 200, 200);
            stage.setTitle("settings");
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public void friendsListButtonOnAction(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("friends_list.fxml"));
        Scene scene;
        try {
            Stage stage = (Stage) friendsListButton.getScene().getWindow();
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
