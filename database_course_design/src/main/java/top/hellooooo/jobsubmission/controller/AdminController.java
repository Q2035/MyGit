package top.hellooooo.jobsubmission.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import top.hellooooo.jobsubmission.service.UserService;

@Controller
@RequestMapping("/job/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @RequestMapping("/userdelete/{id}")
    public String index(Model model,
                        @PathVariable("id")Integer id){

        return "admin/index";
    }
}
