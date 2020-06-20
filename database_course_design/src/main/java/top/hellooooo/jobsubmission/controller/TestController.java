package top.hellooooo.jobsubmission.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.hellooooo.jobsubmission.mapper.UserMapper;
import top.hellooooo.jobsubmission.pojo.User;

@Controller
@ResponseBody
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/t1")
    public User t1(){
        return userMapper.getUserWithClazzAndRoleByUsername("180604101");
    }
}
