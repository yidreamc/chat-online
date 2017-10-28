package com.example.demo.controller;

import com.example.demo.model.Message;
import com.example.demo.model.User;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class MessageController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @MessageMapping("/all")
    public void toAll(@Payload Message message){
        String name = message.getSendName();
        User user = userRepository.findByName(name);
        message.setSendAvatar(user.getAvatar());
        messageRepository.save(message);
        messagingTemplate.convertAndSend("/topic/all",message);
    }
}
