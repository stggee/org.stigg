package org.stigg.chat.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.stigg.chat.pojo.Chat;
import org.stigg.chat.pojo.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class UserRepository {
    Set<User> userList = new HashSet<>();

    final
    PasswordEncoder encoder;

    public UserRepository(PasswordEncoder encoder) {
        this.encoder = encoder;
        //add users
        User user1 = new User(12345,"stigg",encoder.encode("280211"),
                "Yevhe", "Katushev","my@email.com", null,
                List.of(new SimpleGrantedAuthority[]{new SimpleGrantedAuthority("ROLE_USER"),
                        new SimpleGrantedAuthority("ROLE_ADMIN")}), new HashSet<>());

        User user2 = new User(143324,"alex",encoder.encode("0000"),
                "Alex", "Kokolov","my@email.com", null, List.of(new SimpleGrantedAuthority("ROLE_USER")), new HashSet<>());

        User user3 = new User(54531,"test",encoder.encode("0000"),
                "test", "test","test@email.com", null, List.of(new SimpleGrantedAuthority("ROLE_USER")), new HashSet<>());


        Set<User> stigg_friends = new HashSet<>();
        stigg_friends.add(user2);
        stigg_friends.add(user3);
        user1.setFriends(stigg_friends);
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);

    }

    public User findUserByUsername(String username){
        return userList.stream().filter(u -> u.getUsername().equals(username)).findAny().orElse(null);
    }


}
