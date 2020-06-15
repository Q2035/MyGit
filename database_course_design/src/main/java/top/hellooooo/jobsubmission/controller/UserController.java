package top.hellooooo.jobsubmission.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import top.hellooooo.jobsubmission.pojo.Role;
import top.hellooooo.jobsubmission.pojo.User;
import top.hellooooo.jobsubmission.service.UserService;

import javax.servlet.http.HttpSession;

@RequestMapping("job/user")
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/authentication")
    public String auth(@RequestParam("username")String username,
                     @RequestParam("password")String password,
                     RedirectAttributes attributes,
                     HttpSession session){
        Object sessionAttribute = session.getAttribute("user");
        if (sessionAttribute != null) {

        }
        User user = userService.getUserByUsername(username);
        String redirectAddress = "";
//        将用户信息存入Session
        if (user != null
            && user.getPassword().equals(password)){
            user.setPassword(null);
            switch (user.getRole().getRoleName()) {
                case Role.ADMIN:
                    redirectAddress = "admin/index";
                    break;
                case Role.MANAGER:
                    redirectAddress =  "manager/index";
                    break;
                case Role.STUDENT:
                    redirectAddress = "user/home";
                    break;
            }
            session.setAttribute("user",user);
            return redirectAddress;
//            提示密码错误
        }else {
            String message = "";
            if (user == null) {
                message = "cann't find the user in db, please check the username";
            }else {
                message = "fail to login, please check your username or password.";
            }
            attributes.addFlashAttribute("message",message);
        }
        return "redirect:index";
    }



}
