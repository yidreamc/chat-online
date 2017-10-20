package com.example.demo.controller;

import com.example.demo.model.OnlineUser;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;


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
            return "index";
        }
    }
}
