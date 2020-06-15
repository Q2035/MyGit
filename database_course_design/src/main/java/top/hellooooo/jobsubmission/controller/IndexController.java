package top.hellooooo.jobsubmission.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/job")
@Controller
public class IndexController {

    @RequestMapping({"/user/index","/user/","/user/login"})
    public String userIndex(){
        return "index";
    }
}
