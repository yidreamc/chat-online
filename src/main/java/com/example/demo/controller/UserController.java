package com.example.demo.controller;

import com.example.demo.model.Message;
import com.example.demo.model.OnlineUser;
import com.example.demo.model.User;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageRepository messageRepository;

    @RequestMapping(value = {"/","/home"},method = RequestMethod.GET)
    public String home(HttpServletRequest request,Model model){
        if(request.getSession().getAttribute("user") == null){
            //没有登陆过
            return "redirect:/login";
        }else{
            User user = (User) request.getSession().getAttribute("user");
            model.addAttribute("user",user);
            return "index";
        }

    }

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(String name, String passwd, Model model, HttpServletRequest request){
        User user = userRepository.findByNameAndPasswd(name,passwd);
        if(user == null){
            //error
            model.addAttribute("msg","用户名密码错误或账户不存在！");
            return "login";
        }else {
            //success
            OnlineUser.add(user);
            model.addAttribute("user",user);
            model.addAttribute("userlist",OnlineUser.users);
            request.getSession().setAttribute("user",user);
            List<Message> list = messageRepository.findTop10ByOrderById();
            model.addAttribute("top10mes",list);
            messagingTemplate.convertAndSend("/userlist",OnlineUser.users);
            return "index";
        }
    }

    @MessageMapping("/logout")
    public void logout(@Payload Message message){
        String uid = message.getSendId();

        Iterator<User> userIterator = OnlineUser.users.iterator();
        while(userIterator.hasNext()){
            User user = userIterator.next();
            if(String.valueOf(user.getId()).equals(uid)){
                OnlineUser.users.remove(user);
                break;
            }

        }
        messagingTemplate.convertAndSend("/userlist",OnlineUser.users);
    }
}
