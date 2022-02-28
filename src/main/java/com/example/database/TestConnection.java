package com.example.database;

import com.example.domain.*;
import com.example.domain.validators.FriendRequestValidator;
import com.example.domain.validators.FriendshipValidator;
import com.example.domain.validators.MessageValidator;
import com.example.domain.validators.UserValidator;
import com.example.repository.Repository;
import com.example.repository.db.*;
import com.example.service.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TestConnection {
    Connection connection;
    {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/dani","postgres","ilikepussy44");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Connection connection;
        {
            try {
//                connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/dani","postgres","ilikepussy44");
//                Repo2<User2> userRepository = new UserRepo2(connection);
//                Repo2<Friendship2> friendshipRepository = new FriendRepo2(connection);
//                Service2 service = new Service2(userRepository, friendshipRepository);
//                System.out.println(service.getLargestCommunity());

                connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/project_dec","postgres","");
                final FriendshipDbRepository friendshipRepository1 = new FriendshipDbRepository(connection, new FriendshipValidator());

                final Repository<Long, Friendship> friendshipRepository = new FriendshipDbRepository(connection, new FriendshipValidator());
                final Repository<Long, User> userRepository = new UsersDbRepository(connection, new UserValidator(), friendshipRepository1);
                final Repository<Long, Message> messageRepository = new MessageDbRepository(connection, new MessageValidator());
                final Repository<Long, FriendRequest> friendRequestRepository = new FriendRequestDbRepository(connection, new FriendRequestValidator());

                final Service service=new Service(userRepository,friendshipRepository,messageRepository,friendRequestRepository);
                //currentUserRepository.save(new CurrentUser("d"));
                //currentUserRepository.save(new CurrentUser("d"));
                //System.out.println(service.printAll().toString());
                //service.saveUser("d","j","y","t");

                //System.out.println(service.printAll());
//                System.out.println(friendshipRepository.findAll());
//                Friendship friendship = friendshipRepository.findOne(1L);
//                System.out.println(friendship);

                //service.deleteUser(25L);
                //System.out.println(userRepository.getByEmail("d"));
                //friendRequestRepository.save(new FriendRequest(1L,2L,"approved"));
                //System.out.println(service.printAllFriendRequests());
                //User user2 = userRepository.findOne(user.getId());
                //System.out.println(user2);
                //service.updateUser(user.getId(),"dddd","ddd","llll","ddddd");
//                User user2 = userRepository.getByEmail("llll");
//                System.out.println(user2);
//                User user = userRepository.findOne(3L);
//                service.sendFriendRequest(1L,3L);
//                System.out.println(friendRequestRepository.findAll());
//                for(FriendRequest friendRequest:friendRequestRepository.findAll()){
//                    //if(user.getId().equals(friendRequest.getId1()))
//                    System.out.println(friendRequest.getStatus());
//                    long id1 = friendRequest.getId1();
//                    long id2 = friendRequest.getId2();
//                    //User user1 = userRepository.findOne(id1);
//                    User user2 = userRepository.findOne(id2);
//                    System.out.println(user.getEmail());
//                    System.out.println(id1);
//                    System.out.println(id2);
//
//                }

//                service.updateUser(24L,"dddd","fff","ggg","pp");
                //User user = userRepository.findOne(3L);

  //              System.out.println(user.getId());

                //System.out.println(userList);
//                Receiver.setUser(user);
//                System.out.println(Receiver.user.getEmail());
                //service.saveFriendship(1L,3L);
                //service.sendFriendRequest(1L,3L);
                //service.saveFriendship(1L,3L);
                //service.deleteFriendRequests(friendRequest.getId());
//                for(FriendRequest friendRequest1: friendRequestRepository.findAll()){
//                    if(friendRequest1.getId1().equals(1L)){
//                        service.deleteFriendRequests(friendRequest1.getId());
//
//                    }
//                }
                //System.out.println(friendRequest.getStatus());
                //service.deleteFriendRequests(friendRequest.getId());
//                User user = new User("d","d","d","d");
//                user.setID(1L);
//                userRepository.save(user);
//                User user1 = new User("g","g","g","g");
//                user1.setID(2L);
//                userRepository.save(user1);
                //service.saveFriendship(27L,28L);
//                User user=userRepository.findOne(27L);
//                User user2 = userRepository.findOne(28L);
//                for(Friendship friendship:friendshipRepository.findAll()){
//                    if((user.getId().equals(friendship.getId1())&&user2.getId().equals(friendship.getId2())||(user.getId().equals(friendship.getId2())&&user2.getId().equals(friendship.getId1())))){
//                        System.out.println(friendship.getId());
//                    }
//                }






            } catch (SQLException e) {
                e.printStackTrace();
            }
        }}



    }

