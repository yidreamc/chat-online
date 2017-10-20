package com.example.demo.model;

import java.util.HashSet;
import java.util.Set;

public class OnlineUser {

    public static Set<User> users = new HashSet<>();
    public static void add(User user){
        users.add(user);
    }
}
