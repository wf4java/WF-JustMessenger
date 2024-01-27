package wf.spring.justmessenger_front.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ChatController {


    @GetMapping
    public String index() {
        return  "forward:/index.html";
    }

    @GetMapping("/login")
    public String login() {
        return  "forward:/login.html";
    }

    @GetMapping("/register")
    public String singup() {
        return  "forward:/signup.html";
    }


}
