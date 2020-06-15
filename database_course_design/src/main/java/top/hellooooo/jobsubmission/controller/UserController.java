package top.hellooooo.jobsubmission.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.hellooooo.jobsubmission.pojo.User;
import top.hellooooo.jobsubmission.service.UserService;

import javax.servlet.http.HttpSession;

@RequestMapping("job/user")
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping("/authentication")
    public User auth(@RequestParam("username")String username,
                     @RequestParam("password")String password,
                     HttpSession session){
        User user = userService.getUserByUsername(username);
        if (user.getPassword().equals(password)){
            user.setPassword(null);
            session.setAttribute("user",user);
        }else {

        }


        return user;
    }

}
