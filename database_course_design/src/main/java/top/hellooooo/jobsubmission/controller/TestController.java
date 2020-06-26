package top.hellooooo.jobsubmission.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.hellooooo.jobsubmission.mapper.UserMapper;
import top.hellooooo.jobsubmission.pojo.User;
import top.hellooooo.jobsubmission.service.UserService;

import java.util.ArrayList;
import java.util.List;

//@Controller
@ResponseBody
//@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserService userService;

    @RequestMapping("/t2")
    public List<User> t2(){
        Integer begin = 180604102;
        User user;
        List<User> hi = new ArrayList<>();
        while (begin < 180604140) {
            user = new User();
            user.setUsername(String.valueOf(begin));
            String password = DigestUtils.md5DigestAsHex(String.valueOf(begin).getBytes());
            user.setPassword(password);
            begin += 1;
            userService.setUserWithRole(user,2);
            hi.add(user);
        }
        return hi;
    }

    @RequestMapping("/t3")
    public List<User> t3(){
        Integer begin = 180604103;
        User user;
        List<User> hi = new ArrayList<>();
        while (begin < 180604140) {
            user = new User();
            user.setUsername(String.valueOf(begin));
            String password = DigestUtils.md5DigestAsHex(String.valueOf(begin).getBytes());
            user.setPassword(password);
            begin += 1;
            user = userService.getUserByUsername(user.getUsername());
            userService.setClazz(user.getId(),1);
            hi.add(user);
        }
        return hi;
    }
}
